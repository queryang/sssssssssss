package sg.storage.common.creator;

import sg.storage.model.po.FileInfo;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * 文件存储路径生成
 */
public class FilePathCreator {

    private final static String DATE_FILE_DIR_MONTH = "yyyyMM";
    private final static String TMP_DIR_NAME = "tmp";

    /**
     * 组装文件存储路径 todo
     * 基础路径\appKey\ownId\上传时间年月\文件类型\文件尺寸类型\唯一标识.后缀
     *
     * @param fileInfo
     * @param basePath
     * @param fileSizeType
     * @return
     */
    public static String getFilePath(FileInfo fileInfo, String basePath, String fileSizeType) {
        StringBuilder stringBuilder = new StringBuilder(basePath);
        stringBuilder.append(File.separator).append(fileInfo.getAppKey())
                .append(File.separator).append(fileInfo.getOwnId())
                .append(File.separator).append(new SimpleDateFormat(DATE_FILE_DIR_MONTH).format(fileInfo.getUploadTime()))
                .append(File.separator).append(fileInfo.getFileType())
                .append(File.separator).append(fileSizeType)
                .append(File.separator).append(fileInfo.getEtag())
                .append(".").append(fileInfo.getFileSuffix());
        return stringBuilder.toString();
    }

    /**
     * 组装文件存储路径
     * 基础路径\appKey\ownId\上传时间年月\文件类型\文件尺寸类型\唯一标识.后缀
     *
     * @param fileInfo
     * @param basePath
     * @param fileSuffix
     * @param fileSizeType
     * @return
     */
    public static String getFilePath(FileInfo fileInfo, String basePath, String fileSuffix, String fileSizeType) {
        StringBuilder stringBuilder = new StringBuilder(basePath);
        stringBuilder.append(File.separator).append(fileInfo.getAppKey())
                .append(File.separator).append(fileInfo.getOwnId())
                .append(File.separator).append(new SimpleDateFormat(DATE_FILE_DIR_MONTH).format(fileInfo.getUploadTime()))
                .append(File.separator).append(fileInfo.getFileType())
                .append(File.separator).append(fileSizeType)
                .append(File.separator).append(fileInfo.getEtag())
                .append(".").append(fileSuffix);
        return stringBuilder.toString();
    }

    /**
     * 组装文件临时存储路径
     * 基础路径\临时文件夹\文件
     *
     * @param basePath
     * @param tmpFileName
     * @return
     */
    public static String getTmpDirPath(String basePath, String tmpFileName) {
        StringBuilder stringBuilder = new StringBuilder(basePath);
        stringBuilder.append(File.separator).append(TMP_DIR_NAME)
                .append(File.separator).append(tmpFileName);
        return stringBuilder.toString();
    }
}