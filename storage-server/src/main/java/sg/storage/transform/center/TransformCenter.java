package sg.storage.transform.center;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.storage.common.util.IDGenerator;
import sg.storage.service.IFileService;
import sg.storage.transform.service.ITransformService;
import sg.storage.model.vo.file.TransformRequest;
import sg.storage.model.po.FileInfo;

import java.io.File;

/**
 * 转化中心
 */
@Slf4j
@Component
public class TransformCenter {

    private static IFileService fileManageService;

    @Autowired
    public TransformCenter(IFileService fileManageService) {
        TransformCenter.fileManageService = fileManageService;
    }

    public static boolean transform(TransformRequest transformRequest) {
        boolean flag = false;
        try {
            ITransformService ITransformService = null;
            try {
                ITransformService = (ITransformService) Class.forName(transformRequest.getTransformClass()).newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                log.error("未找到转化类");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                log.error("未获取转化类访问权限");
            } catch (InstantiationException e) {
                e.printStackTrace();
                log.error("实例化转化类异常");
            }
            if (null == ITransformService) {
                log.error("获取转化类异常");
                return false;
            }
            File file = new File(transformRequest.getTransformFilePath());
            if (!file.exists()) {
                createDir(transformRequest.getTransformFilePath());
                ITransformService.transform(transformRequest);
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("转化异常:{}", e.getMessage());
        }
        if (flag) {
            /**
             * 将转化文件信息入库
             */
            FileInfo fileInfo = fileManageService.findById(transformRequest.getOriginFileId());
            fileInfo.setId(IDGenerator.getSnowflakeId());
            fileInfo.setBeOrigin(0);
            fileInfo.setFilePath(transformRequest.getTransformFilePath());
            fileInfo.setFileType(transformRequest.getOriginFileType());
            fileInfo.setFileSuffix(transformRequest.getTransformFileSuffix());
            fileInfo.setFileSizeType(transformRequest.getTransformFileSizeType());
            fileManageService.insert(fileInfo);
        }
        return flag;
    }

    /**
     * 创建文件夹
     *
     * @param filePath 文件路径
     */
    public static void createDir(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}