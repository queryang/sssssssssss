package sg.storage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.storage.common.util.FileZip;
import sg.storage.common.creator.FilePathCreator;
import sg.storage.common.properties.FileProperties;
import sg.storage.service.IFileDownloadService;
import sg.storage.model.po.FileInfo;
import sg.storage.repository.FileRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;

/**
 * 本地文件下载
 * Created by asus on 2018/8/9.
 */
@Service("localFileDownloadService")
@Slf4j
public class LocalDownloadServiceImpl implements IFileDownloadService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileProperties fileConfiguration;

    private final static String ZIP_SUFFIX = "zip";

    @Override
    public List<FileInfo> findByETagAndFileSuffixAndFileSizeType(String eTag, String fileSuffix, String fileSizeType) {
        return fileRepository.findByEtagAndFileSuffixAndFileSizeType(eTag, fileSuffix, fileSizeType);
    }

    @Override
    public void wirte(OutputStream out, String targetFilePath) throws IOException {
        zeroCopyWirte(out, targetFilePath);
    }

    @Override
    public void zipWirte(OutputStream out, List<String> filePath) throws IOException {
        //创建临时压缩文件
        StringBuilder fileNameSB = new StringBuilder(String.valueOf(System.currentTimeMillis()))
                .append(".")
                .append(ZIP_SUFFIX);
        String zipTmpFilePath = FilePathCreator.getTmpDirPath(fileConfiguration.getBasePath(), fileNameSB.toString());

        //待优化为零拷贝将需要下载的文件写入压缩文件中
        try {
            FileZip.toZipFile(zipTmpFilePath, null, filePath.toArray(new String[]{}));
        } catch (IOException e) {
            log.error("文件写入失败：{}", e.getMessage());
        }
        //再通过零拷贝将文件写入ServletOutputStream传递到前台，并删除临时压缩文件
        zeroCopyWirteAndRemove(out, zipTmpFilePath);
    }

    /**
     * 拷贝写入流中
     *
     * @param out
     * @param targetFilePath
     * @param removeTragetFile 是否移除文件
     * @throws IOException
     */
    private void zeroCopyWirte(OutputStream out, String targetFilePath, boolean removeTragetFile) throws IOException {
        try (FileInputStream in = new FileInputStream(targetFilePath);
             WritableByteChannel outChannel = Channels.newChannel(out);
             //零拷贝下载
             FileChannel fileChannel = in.getChannel();) {
            long fileSize = fileChannel.size();
            //TODO
            //此处计算文件写入大小可优化，需要根据文件大小决定
            long writeLen = fileSize < 100 ? fileSize : fileSize / 100;
            long position = 0;
            while (true) {
                //每次写入writeLen长度的字节
                position += fileChannel.transferTo(position, writeLen, outChannel);
                if (position >= fileSize) {
                    //如果当前position大于文件字节长度，则停止循环
                    break;
                }
            }
            if (removeTragetFile) {
                //删除文件
                new java.io.File(targetFilePath).deleteOnExit();
            }
        }
    }

    /**
     * 拷贝写入流中
     *
     * @param out
     * @param targetFilePath
     * @throws IOException
     */
    public void zeroCopyWirte(OutputStream out, String targetFilePath) throws IOException {
        zeroCopyWirte(out, targetFilePath, false);
    }


    /**
     * 拷贝写入流中，并移除文件
     *
     * @param out
     * @param targetFilePath
     * @throws IOException
     */
    public void zeroCopyWirteAndRemove(OutputStream out, String targetFilePath) throws IOException {
        zeroCopyWirte(out, targetFilePath, true);
    }
}