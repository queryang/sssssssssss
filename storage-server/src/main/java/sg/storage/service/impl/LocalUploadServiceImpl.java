package sg.storage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.creator.FileInfoCreator;
import sg.storage.common.util.FileReceiver;
import sg.storage.common.util.KafkaUtil;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.util.ResultModelUtil;
import sg.storage.common.properties.FileProperties;
import sg.storage.common.properties.FileConstant;
import sg.storage.common.properties.KafkaConstant;
import sg.storage.service.IFileService;
import sg.storage.service.IFileUploadService;
import sg.storage.common.ModelCover;
import sg.storage.model.dto.FileByte;
import sg.storage.model.vo.file.FileResponse;
import sg.storage.model.vo.file.FileUploadResponse;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.model.po.FileInfo;
import sg.storage.repository.FileRepository;
import sg.storage.transform.creator.TransformRequestCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service(value = "localUploadService")
public class LocalUploadServiceImpl implements IFileUploadService {

    @Autowired
    private FileProperties fileProperties;
    @Autowired
    private FileInfoCreator fileInfoCreator;
    @Autowired
    private IFileService fileService;
    @Autowired
    private FileRepository fileRepository;

    /**
     * 本地上传文件
     * @param fileBytes
     * @return
     */
    @Override
    public ResultModel<FileUploadResponse> receiveAndAdd(List<FileByte> fileBytes) {
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        //需接收存储文件信息
        List<FileInfo> receiveFileInfos = new ArrayList<>(1);
        //重复文件赋值信息
        List<FileInfo> repeatFileInfos = new ArrayList<>(1);
        //需入库文件信息
        List<FileInfo> saveFileInfos = new ArrayList<>(1);
        fileBytes.stream().forEach(fileByte -> {

            /**
             * 查询重复源文件-etag文件唯一标识
             */
            List<FileInfo> repeatFiles = fileRepository.findByEtagAndBeOrigin(fileByte.getEtag(), 1);
            //WuYang修改--通过判断指定路径是否包含去判断存储的文件夹中是否包含重复文件
            List<String> paths = new ArrayList<>();
            boolean flg = false;
            //记录下标
            int i = 0;
            int j = 0;
            for (FileInfo repeatFile : repeatFiles) {
                String filePath = repeatFile.getFilePath();
                if (filePath.contains(fileByte.getPath())){
                    flg =true;
                    i = j;
                }
                j++;
            }
            /**
             * 若无重复源文件，则接收存储文件，并入库文件信息
             * 若有重复源文件，则赋值第一条源文件路径，并入库文件信息
             */
            FileInfo fileInfo = null;//fileInfo才有文件路径
            //若没有重复文件-则存储到本地-重复符文剑不同路径
            if (CollectionUtils.isEmpty(repeatFiles)||!flg) {
                //保存到本地
                fileInfo = receive(fileUploadResponse, fileByte);
            } else {
                //获取重复图片的信息，不用存储重复文件到本地，直接读取文件信息存储起来即可
                fileInfo = getFileInfo(fileByte, repeatFiles.get(i).getFilePath());

            }
            //存储文件的信息不为空则存储起来
            if (fileInfo != null) {
                receiveFileInfos.add(fileInfo);
            }
        });
        /**
         * 入库文件信息
         */
        saveFileInfos.addAll(receiveFileInfos);
        saveFileInfos.addAll(repeatFileInfos);
        fileInDB(saveFileInfos);
        /**
         * 将新接收存储的文件转化信息发送至Kafka
         */
        transformToKafka(receiveFileInfos);
        List<FileResponse> fileResponses = new ArrayList<>(1);
        ModelCover.filePOsToResponses(saveFileInfos, fileResponses);
        fileUploadResponse.addSuccessFiles(fileResponses);
        return ResultModelUtil.getSuccessInstance(fileUploadResponse);
    }

    @Override
    @Transactional
    public ResultModel<FileUploadResponse> receiveAndUpdate(FileByte fileByte) {

        /**
         * 按id查询旧文件信息
         */
        FileInfo oldFile = fileService.findById(fileByte.getId());
        if (null == oldFile) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "原文件id无效");
        }
        FileUploadResponse fileUploadResponse = new FileUploadResponse();
        FileInfo fileInfo;
        boolean isNewFile = false;
        /**
         * 查询相同源文件
         */
        List<FileInfo> repeatFiles = fileRepository.findByEtagAndBeOrigin(fileByte.getEtag(), 1);
        /**
         * 若无重复源文件，则接收存储文件，并入库文件信息，删除旧文件及信息
         * 若有重复源文件，则赋值第一条源文件路径，并入库文件信息
         */
        if (CollectionUtils.isEmpty(repeatFiles)) {
            fileInfo = receive(fileUploadResponse, fileByte);
            /**
             * 删除旧文件信息
             */
            JSONArray ids = new JSONArray(1);
            ids.add(oldFile.getId());
            fileService.deleteReal(ids);
            isNewFile = true;
        } else {
            fileInfo = getFileInfo(fileByte, repeatFiles.get(0).getFilePath());
        }
        /**
         * 将旧文件信息转化为新文件信息并入库
         */
        coverOldToNew(oldFile, fileInfo);
        fileInDB(Arrays.asList(fileInfo));
        FileResponse fileResponse = new FileResponse();
        ModelCover.filePOToResponse(fileInfo, fileResponse);
        fileUploadResponse.addSuccessFiles(fileResponse);
        /**
         * 若为新文件，则发送文件转化信息至Kafka
         */
        if (isNewFile) {
            transformToKafka(Arrays.asList(fileInfo));
        }
        return ResultModelUtil.getSuccessInstance(fileUploadResponse);
    }

    /**
     * 将旧文件信息赋值给新文件信息
     *
     * @param oldFile
     * @param newFile
     */
    private void coverOldToNew(FileInfo oldFile, FileInfo newFile) {
        newFile.setId(oldFile.getId());
        newFile.setAppKey(oldFile.getAppKey());
        newFile.setOwnId(oldFile.getOwnId());
        newFile.setFolderId(oldFile.getFolderId());
        newFile.setBeOrigin(oldFile.getBeOrigin());
        newFile.setOriginFileId(oldFile.getOriginFileId());
        newFile.setDel(oldFile.getDel());
    }

    /**
     * 接收保存文件并获取文件信息
     *
     * @param fileUploadResponse
     * @param fileByte
     * @return
     */
    private FileInfo receive(FileUploadResponse fileUploadResponse, FileByte fileByte) {
        /**
         * 获取文件信息并存储文件至本地路径
         * 若失败则获取文件名称并追加进返回响应中
         */
        //todo-文件路径需要配置
        String etag1 = fileByte.getEtag();
        String fileName = fileByte.getFileName();
        //获得暂存的路径
        String path = fileByte.getPath()+"\\";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        FileInfo fileInfo = getFileInfo(fileByte,path+etag1+suffix);

        boolean flag = FileReceiver.writeFile(fileInfo, fileByte.getFileByte());
        if (!flag) {
            fileUploadResponse.addFailFileNames(fileByte.getFileName());
            return null;
        }
        return fileInfo;
    }

    /**
     * 文件信息入库
     *
     * @param fileInfos
     */
    private void fileInDB(List<FileInfo> fileInfos) {
        if (!CollectionUtils.isEmpty(fileInfos)) {
            fileRepository.saveAll(fileInfos);
        }
    }

    /**
     * 获取转化信息并发布至kafka
     *
     * @param fileInfos
     */
    private void transformToKafka(List<FileInfo> fileInfos) {
        for (FileInfo fileInfo : fileInfos) {
            /**
             * 依据文件类型获取是否自动转化
             */
            boolean autoTransform = fileProperties.getAutoTransform(fileInfo.getFileType());
            if (!autoTransform) {
                continue;
            }
            /**
             * 依据文件类型获取需转化类型
             */
            List<FileProperties.TransformInfo.TransformType> transformTypes = fileProperties.getTransformTypes(fileInfo.getFileType());
            for (FileProperties.TransformInfo.TransformType transformType : transformTypes) {
                /**
                 * 获取转化请求
                 */
                List<TransformRequest> transformRequests = getTransformRequests(fileInfo
                        , transformType.getTransformSuffix()
                        , transformType.getTransformClass()
                        , transformType.getTransformRequestClass());
                if (CollectionUtils.isEmpty(transformRequests)) {
                    continue;
                }
                /**
                 * 发布转化请求至kafka
                 */
                sendTransformRequest(KafkaConstant.TRANSFORM_TOPIC, transformRequests);
            }
        }
    }

    /**
     * 发送转化请求
     *
     * @param transformRequests
     */
    public void sendTransformRequest(String kafkaTopic, List<TransformRequest> transformRequests) {
        if (CollectionUtils.isEmpty(transformRequests)) {
            return;
        }
        for (int i = 0; i < transformRequests.size(); i++) {
            String message = JSON.toJSONString(transformRequests.get(i));
            KafkaUtil.sendMessage(kafkaTopic, message);
            log.info("发布kafka文件标准化:{}", message);
        }
    }

    /**
     * 获取转化请求
     *
     * @param fileInfo          源文件信息
     * @param transType         转化类型
     * @param transClass        转化映射类
     * @param transRequestClass 转化请求获取类
     * @return
     */
    private List<TransformRequest> getTransformRequests(FileInfo fileInfo
            , String transType, String transClass, String transRequestClass) {
        List<TransformRequest> transformRequests = new ArrayList<>(1);
        TransformRequestCreator transformCreator;
        try {
            transformCreator = (TransformRequestCreator) Class.forName(transRequestClass).newInstance();
            transformRequests = transformCreator.getTransformRequests(fileInfo
                    , fileProperties.getBasePath(), transType, transClass);
        } catch (InstantiationException e) {
            e.printStackTrace();
            log.error("实例化转化类异常");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            log.error("未获取转化类访问权限");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.error("未找到转化类");
        }
        return transformRequests;
    }

    private FileInfo getFileInfo(FileByte fileReceiveRequest) {
        return getFileInfo(fileReceiveRequest, null);
    }

    private FileInfo getFileInfo(FileByte fileByte, String filePath) {
        FileInfo fileInfo = fileInfoCreator.create(fileByte.getId()
                , fileByte.getAppKey()
                , fileByte.getOwnId()
                , fileByte.getEtag()
                , fileByte.getFileName()
                , fileByte.getFileSize()
                , FileConstant.ORIGINAL
                , fileByte.getRemark()
                , fileByte.getFolderId()
                , fileByte.getUploadUser()
                , fileByte.getContentType());
        if (StringUtils.isNotEmpty(filePath)) {
            fileInfo.setFilePath(filePath);
        }
        return fileInfo;
    }
}