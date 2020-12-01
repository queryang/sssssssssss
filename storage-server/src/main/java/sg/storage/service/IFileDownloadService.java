package sg.storage.service;

import sg.storage.model.po.FileInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件下载接口
 * Created by asus on 2018/8/9.
 */
public interface IFileDownloadService {

    List<FileInfo> findByETagAndFileSuffixAndFileSizeType(String eTag, String fileSuffix, String fileSizeType);

    /**
     * 将需要下载的文件中从磁盘中读取，使用流的方式传递到前台
     *
     * @param outputStream
     * @param targetFilePath
     * @throws IOException
     */
    void wirte(OutputStream outputStream, String targetFilePath) throws IOException;

    /**
     * 文件打包下载
     *
     * @param outputStream
     * @param filePath
     */
    void zipWirte(OutputStream outputStream, List<String> filePath) throws IOException;
}