package sg.storage.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.DataTableModel;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.util.ResultModelUtil;
import sg.storage.service.IFileService;
import sg.storage.common.ModelCover;
import sg.storage.model.vo.file.FileResponse;
import sg.storage.model.po.FileInfo;
import sg.storage.repository.FileRepository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private FileRepository fileRepository;

    @Override
    public ResultModel insert(FileInfo fileInfo) {
        fileRepository.save(fileInfo);
        return ResultModelUtil.getSuccessInstance();
    }

    @Override
    public ResultModel deleteReal(JSONArray ids) {
        /**
         * 按id查询文件信息
         */
        List<FileInfo> fileInfos = new ArrayList<>(1);
        for (Object id : ids) {
            /**
             * 按id查询文件信息
             */
            FileInfo fileInfo = findById(id.toString());
            if (null == fileInfo) {
                return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "文件id无效");
            }
            fileInfos.add(fileInfo);
        }
        for (FileInfo fileInfo : fileInfos) {
            /**
             * 按eTag查询重复源文件数据并移除当前文件信息
             */
            List<FileInfo> repeatFileInfos = fileRepository.findByEtagAndBeOrigin(fileInfo.getEtag(), 1);
            repeatFileInfos.removeIf(o -> o.getId().equals(fileInfo.getId()));
            /**
             * 若含有其他eTag重复源文件，则仅能删除当前文件数据，不可删除源文件实体及转化文件
             * 否则删除源文件与转化文件实体及数据
             */
            boolean isDel = false;
            if (CollectionUtils.isEmpty(repeatFileInfos)) {
                isDel = true;
            }
            if (isDel) {
                /**
                 * 删除文件及其转化文件
                 */
                deleteSourceAndTrans(fileInfo);
                return ResultModelUtil.getSuccessInstance();
            }
            /**
             * 删除数据库文件信息
             */
            fileRepository.deleteById(fileInfo.getId());
        }
        return ResultModelUtil.getSuccessInstance();
    }

    @Override
    public ResultModel deleteLogic(JSONArray ids) {
        List<FileInfo> fileInfos = new ArrayList<>(1);
        for (Object id : ids) {
            /**
             * 按id查询文件信息
             */
            FileInfo fileInfo = findById(id.toString());
            if (null == fileInfo) {
                return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "文件id无效");
            }
            fileInfo.setDel(1);
            fileInfos.add(fileInfo);
        }
        /**
         * 保存文件信息
         */
        fileRepository.saveAll(fileInfos);
        return ResultModelUtil.getSuccessInstance();
    }

    @Override
    public ResultModel copyFile(String id, String folderId) {
        /**
         * 按id查询文件
         */
        Optional<FileInfo> fileInfo = fileRepository.findById(Long.parseLong(id));
        if (!fileInfo.isPresent()) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "文件id无效");
        }
        /**
         * 修改文件信息并新增信息数据
         */
        fileInfo.get().setOriginFileId(fileInfo.get().getId());
        fileInfo.get().setFolderId(Long.parseLong(folderId));
        fileInfo.get().setDel(0);
        fileRepository.save(fileInfo.get());
        return ResultModelUtil.getSuccessInstance();
    }

    @Override
    public ResultModel updateFileFolder(String id, String folderId) {
        int count = fileRepository.updateFileFolder(Long.parseLong(id), Long.parseLong(folderId));
        if (0 == count) {
            return ResultModelUtil.getFailInstance(CodeEnum.ERROR, "修改文件夹id失败");
        }
        return ResultModelUtil.getSuccessInstance();
    }

    @Override
    public FileInfo findById(String id) {
        Optional<FileInfo> optionalFileInfo = fileRepository.findById(Long.parseLong(id));
        if (optionalFileInfo.isPresent()) {
            return optionalFileInfo.get();
        }
        return null;
    }

    @Override
    public ResultModel findTrans(String id) {
        /**
         * 按id查询文件信息
         */
        FileInfo fileInfo = findById(id);
        if (null == fileInfo) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "文件id无效");
        }
        FileResponse fileResponse = new FileResponse();
        ModelCover.filePOToResponse(fileInfo, fileResponse);
        /**
         * 按eTag查询转化文件信息
         */
        List<FileInfo> transFileInfos = fileRepository.findByEtagAndBeOrigin(fileInfo.getEtag(), 0);
        if (!CollectionUtils.isEmpty(transFileInfos)) {
            List<FileResponse> fileRespons = new ArrayList<>(1);
            ModelCover.filePOsToResponses(transFileInfos, fileRespons);
            fileResponse.setTransformFiles(fileRespons);
        }
        return ResultModelUtil.getSuccessInstance(fileResponse);
    }

    @Override
    public ResultModel findByFolderId(String folderId) {
        List<FileInfo> fileInfos = fileRepository.findByFolderIdAndBeOriginAndDel(Long.parseLong(folderId), 1, 0);
        List<FileResponse> fileRespons = new ArrayList<>(1);
        ModelCover.filePOsToResponses(fileInfos, fileRespons);
        return ResultModelUtil.getSuccessInstance(fileRespons);
    }

    @Override
    public ResultModel findPageByFolderId(String folderId, Integer pageNum, Integer pageSize) {
        Page<FileInfo> page = fileRepository.findAll((Specification<FileInfo>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>(1);
            predicates.add(criteriaBuilder.equal(root.get("folderId").as(Long.class), folderId));
            return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, new QPageRequest(pageNum, pageSize));
        DataTableModel dataTableModel = new DataTableModel();
        dataTableModel.setRecordsTotal(page.getTotalElements());
        List<FileInfo> fileInfos = page.getContent();
        List<FileResponse> fileRespons = new ArrayList<>(1);
        ModelCover.filePOsToResponses(fileInfos, fileRespons);
        dataTableModel.setData(fileRespons);
        return dataTableModel;
    }

    @Override
    public ResultModel deleteFolderFile(String folderId) {
        /**
         * 若文件id为空则查询文件夹下全部文件
         * 否则按文件夹id及文件id查询
         */
        List<FileInfo> fileInfos = fileRepository.findByFolderIdAndBeOrigin(Long.parseLong(folderId), 1);
        if (CollectionUtils.isEmpty(fileInfos)) {
            return ResultModelUtil.getFailInstance(CodeEnum.PARAM_ERROR, "该文件夹下暂无文件");
        }
        /**
         * 按文件id删除文件
         */
        JSONArray ids = new JSONArray(fileInfos.size());
        fileInfos.stream().forEach(fileInfo -> {
            ids.add(fileInfo.getId());
        });
        deleteReal(ids);
        return ResultModelUtil.getSuccessInstance();
    }

    /**
     * 删除文件及其转化文件
     *
     * @param fileInfo
     */
    private void deleteSourceAndTrans(FileInfo fileInfo) {
        /**
         * 按eTag查询转化文件
         */
        List<FileInfo> transFileInfos = fileRepository.findByEtagAndBeOrigin(fileInfo.getEtag(), 0);
        if (null == transFileInfos) {
            transFileInfos = new ArrayList<>(1);
        }
        transFileInfos.add(fileInfo);
        transFileInfos.stream().forEach(file -> {
            /**
             * 删除物理文件
             */
            delPhysicalFile(file);
            /**
             * 删除数据库文件信息
             */
            fileRepository.deleteById(file.getId());
        });
    }

    /**
     * 删除物理文件
     *
     * @param fileInfo
     */
    private boolean delPhysicalFile(FileInfo fileInfo) {
        String filePath = fileInfo.getFilePath();
        java.io.File file = new java.io.File(filePath);
        if (file.exists()) {
            file.delete();
            file = new java.io.File(filePath);
            if (!file.exists()) {
                return false;
            }
            return true;
        }
        return true;
    }
}