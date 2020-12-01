package sg.storage.common.creator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sg.storage.common.util.IDGenerator;
import sg.storage.common.properties.FileProperties;
import sg.storage.common.properties.FileConstant;
import sg.storage.model.po.FileInfo;

import java.io.File;
import java.util.Arrays;

/**
 * 文件信息生成
 */
@Component
public class FileInfoCreator {

    @Autowired
    private FileProperties fileProperties;

    private final static String[] WORD = new String[]{"doc", "docx", "DOC", "DOCX"};
    private final static String[] EXCEL = new String[]{"xls", "xlsx", "XLS", "XLSX"};

    /**
     * 生成文件信息
     *
     * @param id              文件Id
     * @param appKey          appKey
     * @param ownId           ownId
     * @param etag            唯一标识
     * @param fileName        文件名称
     * @param fileSize        文件大小
     * @param fileSizeType    文件尺寸类型
     * @param remark          备注
     * @param folderId        文件夹id
     * @param uploadUser      上传用户
     * @param fileContentType 文件内容类型
     * @return
     */
    public FileInfo create(String id, String appKey, String ownId, String etag
            , String fileName, Long fileSize, String fileSizeType, String remark
            , String folderId, String uploadUser, String fileContentType) {
        FileInfo fileInfo = getFileInfo(id, appKey, ownId, etag
                , fileName, fileSize, fileSizeType, remark
                , folderId, uploadUser, fileContentType);
        File parentDir = new File(fileInfo.getFilePath()).getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        return fileInfo;
    }

    /**
     * 获取文件信息
     *
     * @param id              文件Id
     * @param appKey          appKey
     * @param ownId           ownId
     * @param etag            唯一标识
     * @param fileName        文件名称
     * @param fileSize        文件大小
     * @param fileSizeType    文件尺寸类型
     * @param remark          备注
     * @param folderId        文件夹id
     * @param uploadUser      上传用户
     * @param fileContentType 文件内容类型
     * @return
     */
    public FileInfo getFileInfo(String id, String appKey, String ownId, String etag
            , String fileName, Long fileSize, String fileSizeType, String remark
            , String folderId, String uploadUser, String fileContentType) {
        /**
         * 截取文件名称与后缀
         */
        String[] fileNameArr = getFileNameArr(fileName);
        /**
         * Word与Excel需依据后缀判断
         */
        if (Arrays.asList(WORD).contains(fileNameArr[1])) {
            fileContentType = "word";
        } else if (Arrays.asList(EXCEL).contains(fileNameArr[1])) {
            fileContentType = "excel";
        }
        /**
         * 依据文件类型获取文件转换信息
         */
        FileProperties.TransformInfo transformInfo = fileProperties.getTransformInfo(fileContentType);
        if (StringUtils.isBlank(id)) {
            id = String.valueOf(IDGenerator.getSnowflakeId());
        }
        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(Long.parseLong(id));
        fileInfo.setAppKey(appKey);
        fileInfo.setOwnId(ownId);
        fileInfo.setEtag(etag);
        fileInfo.setFileSize(fileSize);
        fileInfo.setFileType(transformInfo.getFileType());
        fileInfo.setFileSuffix(fileNameArr[1]);
        fileInfo.setFileSizeType(FileConstant.ORIGINAL);
        fileInfo.setRemark(remark);
        if (folderId != null) {
            fileInfo.setFolderId(Long.parseLong(folderId));
        }
        fileInfo.setBeOrigin(1);
        fileInfo.setOriginFileId(Long.parseLong(id));
        fileInfo.setOriginalFileName(fileNameArr[0]);
        fileInfo.setDel(0);
        fileInfo.setUploadTime(System.currentTimeMillis());
        fileInfo.setUploadUser(uploadUser);
        fileInfo.setFilePath(FilePathCreator.getFilePath(fileInfo, fileProperties.getBasePath(), fileSizeType));
        return fileInfo;
    }

    /**
     * 截取文件名称与后缀
     *
     * @param fileName
     * @return
     */
    private String[] getFileNameArr(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return null;
        }
        String originalFileName = fileName.substring(0, index);
        String fileType = fileName.substring(index + 1);
        if (StringUtils.isBlank(originalFileName) || StringUtils.isBlank(fileType)) {
            return null;
        }
        String[] fileNameArr = new String[]{originalFileName, fileType};
        return fileNameArr;
    }
}