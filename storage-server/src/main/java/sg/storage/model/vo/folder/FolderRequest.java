package sg.storage.model.vo.folder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "文件夹request")
public class FolderRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id", example = "1")
    private String id;
    @ApiModelProperty(value = "appKey（默认1）", example = "1")
    private String appKey = "1";
    @ApiModelProperty(value = "ownId（默认1）", example = "1")
    private String ownId = "1";
    @ApiModelProperty(value = "父文件夹id（默认-1）", example = "-1")
    private String pid = "-1";
    @ApiModelProperty(value = "文件夹名称", example = "新建文件夹")
    private String name;
    @ApiModelProperty(value = "文件夹类型（默认1）", example = "1")
    private Integer type = 1;
    @ApiModelProperty(value = "备注", example = "该文件夹为测试文件夹")
    private String remark;
    @ApiModelProperty(value = "是否共享（0否1是）（默认0）", example = "0")
    private Integer beShare = 0;
    @ApiModelProperty(value = "是否只读（0否1是）（默认0）", example = "0")
    private Integer beReadOnly = 0;
    @ApiModelProperty(value = "创建人id", example = "1")
    private String createUserId;
    @ApiModelProperty(value = "创建人名称", example = "管理员")
    private String createUserName;
}