package sg.storage.transform.service.impl.openOffice;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * OpenOffice
 */
@Component
public class OpenOfficeUtil {

    public static String convertFile(String host, Integer port, String originalFilePath, String convertFilePath) throws Exception {
        File originalFile = new File(originalFilePath);
        if (originalFile.exists()) {
            File newFile = new File(convertFilePath);
            File parentFolder = newFile.getParentFile();
            if (!parentFolder.exists()) {
                parentFolder.mkdirs();
            }
            newFile.createNewFile();
            /**
             * OpenOffice转化文件
             */
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(host, port);
            connection.connect();
            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
            converter.convert(originalFile, newFile);
            connection.disconnect();
        }
        return convertFilePath;
    }
}