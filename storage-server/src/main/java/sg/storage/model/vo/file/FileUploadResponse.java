package sg.storage.model.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文件上传response
 */
@Getter
@Setter
@ApiModel(value = "文件上传response")
public class FileUploadResponse implements Serializable {

    private static final long serialVersionUID = -2499305429944684448L;

    /**
     * 上传成功的文件
     */
    @ApiModelProperty(value = "上传成功的文件")
    private List<FileResponse> successFiles = new ArrayList<>(1);
    /**
     * 上传失败的文件名
     */
    @ApiModelProperty(value = "上传失败的文件名")
    private List<String> failFileNames = new ArrayList<>(1);

    public void addSuccessFiles(FileResponse... fileResponse) {
        this.successFiles.addAll(Arrays.asList(fileResponse));
    }

    public void addSuccessFiles(List<FileResponse> fileResponses) {
        this.successFiles.addAll(fileResponses);
    }

    public void addFailFileNames(String... fileName) {
        this.failFileNames.addAll(Arrays.asList(fileName));
    }

    public void addFailFileNames(List<String> fileNames) {
        this.failFileNames.addAll(fileNames);
    }
}