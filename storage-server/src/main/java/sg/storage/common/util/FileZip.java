package sg.storage.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZIP
 */
@Slf4j
public class FileZip {

    /**
     * ZIP压缩文件
     *
     * @param targetFilePath 需压缩文件路径
     * @param zipFilePath    ZIP文件路径
     * @throws IOException
     */
    public static void zip(String targetFilePath, String zipFilePath) throws IOException {
        File zipFile = new File(zipFilePath);
        File targetFile = new File(targetFilePath);
        /**
         * 若压缩文件不存在则新建
         */
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));) {
            /**
             * 若为非目录文件则直接压缩
             */
            if (!targetFile.isDirectory()) {
                toZipFile(zipOutputStream, null, targetFile);
                return;
            }
            try {
                /**
                 * 若为目录则压缩目录内所有文件
                 */
                toZipDirectory(zipOutputStream, targetFile.getName(), targetFile);
            } catch (NotDirectoryException e) {
                log.error("当前为非目录文件");
            }
        }
    }

    /**
     * ZIP压缩文件目录
     *
     * @param zipOutputStream ZIP文件流
     * @param directoryName   ZIP文件目录名
     * @param fileDirectory   需压缩文件目录
     * @throws NotDirectoryException
     */
    public static void toZipDirectory(ZipOutputStream zipOutputStream, String directoryName, File fileDirectory) throws NotDirectoryException {
        if (!fileDirectory.isDirectory()) {
            throw new NotDirectoryException("当前为非目录文件");
        }
        /**
         * 获取文件夹中所有文件并进行压缩
         */
        File[] files = fileDirectory.listFiles();
        Arrays.asList(files).stream().forEach(file -> {
            StringBuilder fileNameSB = new StringBuilder(StringUtils.isBlank(directoryName) ? "" : directoryName)
                    .append(File.separator)
                    .append(file.getName());
            /**
             * 若为非目录文件则直接压缩
             */
            if (!file.isDirectory()) {
                toZipFile(zipOutputStream, directoryName, file);
                return;
            }
            /**
             * 若依旧为目录则递归压缩其中文件
             */
            try {
                toZipDirectory(zipOutputStream, fileNameSB.toString(), file);
            } catch (NotDirectoryException e) {
                log.error("当前为非目录文件");
            }
        });
    }

    /**
     * ZIP压缩文件
     *
     * @param zipOutputStream ZIP文件流
     * @param directoryName   ZIP文件目录名
     * @param fileArrays      需压缩文件集合
     */
    public static void toZipFile(ZipOutputStream zipOutputStream, String directoryName, File... fileArrays) {
        Arrays.asList(fileArrays).stream().forEach(file -> {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                StringBuilder fileNameSB = new StringBuilder(directoryName == null ? "" : directoryName)
                        .append(File.separator)
                        .append(file.getName());
                zipOutputStream.putNextEntry(new ZipEntry(fileNameSB.toString()));
                byte[] bytes = new byte[1024];
                int len;
                while ((len = fileInputStream.read(bytes)) != -1) {
                    zipOutputStream.write(bytes, 0, len);
                }
            } catch (FileNotFoundException e) {
                log.error("文件未找到: {}", file.getPath());
            } catch (IOException e) {
                log.error("IO异常: {}", e.getMessage());
            }
        });
    }

    /**
     * ZIP压缩文件
     *
     * @param zipFilePath    ZIP文件路径
     * @param directoryName  ZIP文件目录名
     * @param filePathArrays 需压缩文件路径集合
     * @throws IOException
     */
    public static void toZipFile(String zipFilePath, String directoryName, String... filePathArrays) throws IOException {
        File zipFile = new File(zipFilePath);
        /**
         * 判断父文件夹路径是否存在
         * 若不存在则新建文件夹
         */
        File parentFileDir = zipFile.getParentFile();
        if (!parentFileDir.exists()) {
            parentFileDir.mkdirs();
        }
        /**
         * 判断ZIP文件是否存在
         * 若不存在则新建ZIP文件
         */
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }
        toZipFile(zipFile, directoryName, filePathArrays);
    }

    /**
     * ZIP压缩文件
     *
     * @param zipFile        ZIP文件
     * @param directoryName  ZIP文件目录名
     * @param filePathArrays 需压缩文件路径集合
     * @throws IOException
     */
    public static void toZipFile(File zipFile, String directoryName, String... filePathArrays) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
            toZipFile(zipOutputStream, directoryName, filePathArrays);
        }
    }

    /**
     * ZIP压缩文件
     *
     * @param zipOutputStream ZIP文件流
     * @param directoryName   ZIP文件目录名
     * @param filePathArrays  需压缩文件路径集合
     */
    public static void toZipFile(ZipOutputStream zipOutputStream, String directoryName, String... filePathArrays) {
        List<File> fileList = new ArrayList<>(1);
        for (String filePath : filePathArrays) {
            File file = new File(filePath);
            fileList.add(file);
        }
        toZipFile(zipOutputStream, directoryName, fileList.toArray(new File[]{}));
    }
}