package sg.storage.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import sg.storage.model.po.FileInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件存储
 */
@Slf4j
@Component
public class FileReceiver {

    /**
     * 写入文件
     *
     * @param fileInfo
     * @param fileByteContent
     * @return
     */
    public static boolean writeFile(FileInfo fileInfo, byte[] fileByteContent) {
        byte[] fileByte = fileByteContent;
        boolean flag = false;
        if (null == fileInfo || StringUtils.isEmpty(fileInfo.getFilePath())) {
            return flag;
        }
        /**
         * 判断文件是否存在,不存在则创建
         */
        File reciveFile = new File(fileInfo.getFilePath());
        if (!reciveFile.exists()) {
            /**
             * 查看父类文件夹是否存在，不存在创建
             */
            File parentDirFile = reciveFile.getParentFile();
            if (!parentDirFile.exists()) {
                parentDirFile.mkdirs();
            }
            try {
                reciveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(reciveFile)) {
            outputStream.write(fileByte, 0, fileByte.length);
            outputStream.flush();
            flag = true;
        } catch (FileNotFoundException e) {
            log.error("文件或文件路径未找到");
        } catch (IOException e) {
            log.error("文件写入失败");
        } finally {
            return flag;
        }
    }
}