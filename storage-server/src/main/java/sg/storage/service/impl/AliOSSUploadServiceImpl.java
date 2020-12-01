package sg.storage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.properties.AliOSSProperties;
import sg.storage.model.vo.file.FileUploadResponse;
import sg.storage.service.IFileUploadService;
import sg.storage.model.dto.FileByte;

import java.util.List;

@Slf4j
@Service(value = "ossUploadService")
public class AliOSSUploadServiceImpl implements IFileUploadService {

    @Autowired
    private AliOSSProperties aliOSSProperties;

    @Override
    public ResultModel<FileUploadResponse> receiveAndAdd(List<FileByte> fileBytes) {
        return null;
    }

    @Override
    public ResultModel<FileUploadResponse> receiveAndUpdate(FileByte fileByte) {
        return null;
    }
}