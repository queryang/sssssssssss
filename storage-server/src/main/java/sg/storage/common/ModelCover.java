package sg.storage.common;

import sg.storage.common.util.IDGenerator;
import sg.storage.common.util.TimeUtil;
import sg.storage.model.vo.file.FileResponse;
import sg.storage.model.vo.folder.FolderRequest;
import sg.storage.model.vo.folder.FolderResponse;
import sg.storage.model.po.FileInfo;
import sg.storage.model.po.FolderInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 模型转换
 */
public class ModelCover {

    /**
     * FolderRequest转Folder
     *
     * @param folderRequest
     * @param folderInfo
     */
    public static void folderRequestToPO(FolderRequest folderRequest, FolderInfo folderInfo) {
        if (StringUtils.isBlank(folderRequest.getId())) {
            folderRequest.setId(String.valueOf(IDGenerator.getSnowflakeId()));
        }
        folderInfo.setId(Long.parseLong(folderRequest.getId()));
        folderInfo.setAppKey(folderRequest.getAppKey());
        folderInfo.setOwnId(folderRequest.getOwnId());
        folderInfo.setPid(Long.parseLong(folderRequest.getPid()));
        folderInfo.setName(folderRequest.getName());
        folderInfo.setType(folderRequest.getType());
        folderInfo.setRemark(folderRequest.getRemark());
        folderInfo.setBeShare(folderRequest.getBeShare());
        folderInfo.setBeReadOnly(folderRequest.getBeReadOnly());
        folderInfo.setCreateUser(folderRequest.getCreateUserName());
        folderInfo.setDel(0);
        folderInfo.setCreateTime(TimeUtil.getCurrentTime());
    }

    /**
     * Folder转FolderResponse
     *
     * @param folderInfo
     * @param folderResponse
     */
    public static void folderPOToResponse(FolderInfo folderInfo, FolderResponse folderResponse) {
        folderResponse.setId(String.valueOf(folderInfo.getId()));
        folderResponse.setAppKey(folderInfo.getAppKey());
        folderResponse.setOwnId(folderInfo.getOwnId());
        folderResponse.setPid(String.valueOf(folderInfo.getPid()));
        folderResponse.setName(folderInfo.getName());
        folderResponse.setType(folderInfo.getType());
        folderResponse.setRemark(folderInfo.getRemark());
        folderResponse.setBeShare(folderInfo.getBeShare());
        folderResponse.setBeReadOnly(folderInfo.getBeReadOnly());
        folderResponse.setCreateTime(folderInfo.getCreateTime());
        folderResponse.setCreateDate(TimeUtil.timeToDate(folderInfo.getCreateTime()));
        folderResponse.setCreateUser(folderInfo.getCreateUser());
    }

    /**
     * List<Folder>转List<FolderResponse>
     *
     * @param folderInfos
     * @param folderResponses
     */
    public static void folderPOsToResponses(List<FolderInfo> folderInfos, List<FolderResponse> folderResponses) {
        folderInfos.stream().forEach(folderInfo -> {
            FolderResponse folderResponse = new FolderResponse();
            folderPOToResponse(folderInfo, folderResponse);
            folderResponses.add(folderResponse);
        });
    }

    /**
     * File转FileResponse
     *
     * @param fileInfo
     * @param fileResponse
     */
    public static void filePOToResponse(FileInfo fileInfo, FileResponse fileResponse) {
        fileResponse.setId(String.valueOf(fileInfo.getId()));
        fileResponse.setAppKey(fileInfo.getAppKey());
        fileResponse.setOwnId(fileInfo.getOwnId());
        fileResponse.setEtag(fileInfo.getEtag());
        fileResponse.setFilePath(fileInfo.getFilePath());
        fileResponse.setFileSize(fileInfo.getFileSize());
        fileResponse.setFileType(fileInfo.getFileType());
        fileResponse.setFileSuffix(fileInfo.getFileSuffix());
        fileResponse.setFileSizeType(fileInfo.getFileSizeType());
        fileResponse.setRemark(fileInfo.getRemark());
        fileResponse.setFolderId(String.valueOf(fileInfo.getFolderId()));
        fileResponse.setBeOrigin(fileInfo.getBeOrigin());
        fileResponse.setOriginFileId(String.valueOf(fileInfo.getOriginFileId()));
        fileResponse.setOriginalFileName(fileInfo.getOriginalFileName());
        fileResponse.setUploadTime(fileInfo.getUploadTime());
        fileResponse.setUploadDate(TimeUtil.timeToDate(fileInfo.getUploadTime()));
        fileResponse.setUploadUser(fileInfo.getUploadUser());
    }

    /**
     * List<File>转List<FileResponse>
     *
     * @param fileInfos
     * @param fileResponses
     */
    public static void filePOsToResponses(List<FileInfo> fileInfos, List<FileResponse> fileResponses) {
        fileInfos.stream().forEach(fileInfo -> {
            FileResponse fileResponse = new FileResponse();
            filePOToResponse(fileInfo, fileResponse);
            fileResponses.add(fileResponse);
        });
    }
}