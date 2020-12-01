package sg.storage.model.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文件request
 */
@Getter
@Setter
@ApiModel(value = "文件request")
public class FileRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private String id;
    /**
     * appKey（默认1）
     */
    @ApiModelProperty(value = "appKey", example = "1")
    private String appKey = "1";
    /**
     * ownId（默认1）
     */
    @ApiModelProperty(value = "ownId", example = "1")
    private String ownId = "1";
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "该文件为测试文件")
    private String remark;
    /**
     * 文件夹id
     */
    @ApiModelProperty(value = "文件夹id", example = "1")
    private String folderId;
    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人", example = "管理员")
    private String uploadUser;

    /**
     * 预存文件夹
     */
    @ApiModelProperty(value = "预存路径", example = "管理员")
    private String path;
}