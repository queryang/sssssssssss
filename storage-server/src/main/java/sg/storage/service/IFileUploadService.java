package sg.storage.service;

import sg.storage.common.entity.ResultModel;
import sg.storage.model.dto.FileByte;
import sg.storage.model.vo.file.FileUploadResponse;

import java.util.List;

public interface IFileUploadService {

    /**
     * 接收并新增文件
     *
     * @param fileBytes
     * @return
     */
    ResultModel<FileUploadResponse> receiveAndAdd(List<FileByte> fileBytes);

    /**
     * 接收并更新文件
     *
     * @param fileByte
     * @return
     */
    ResultModel<FileUploadResponse> receiveAndUpdate(FileByte fileByte);
}