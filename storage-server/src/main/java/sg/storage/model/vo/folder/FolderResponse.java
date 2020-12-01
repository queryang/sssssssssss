package sg.storage.model.vo.folder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹response
 */
@Getter
@Setter
@ApiModel(value = "文件夹response")
public class FolderResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * appKey
     */
    @ApiModelProperty(value = "appKey")
    private String appKey;
    /**
     * ownId
     */
    @ApiModelProperty(value = "ownId")
    private String ownId;
    /**
     * 父文件夹id
     */
    @ApiModelProperty(value = "父文件夹id")
    private String pid;
    /**
     * 文件夹名称
     */
    @ApiModelProperty(value = "文件夹名称")
    private String name;
    /**
     * 文件夹类型
     */
    @ApiModelProperty(value = "文件夹类型")
    private Integer type;
    /**
     * 文件夹类型（中文）
     */
    @ApiModelProperty(value = "文件夹类型（中文）")
    private String typeCN;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 是否共享（0否1是）
     */
    @ApiModelProperty(value = "是否共享")
    private Integer beShare;
    /**
     * 是否只读（0否1是）
     */
    @ApiModelProperty(value = "是否只读")
    private Integer beReadOnly;
    /**
     * 创建时间（毫秒）
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期")
    private String createDate;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createUser;
    /**
     * 子文件夹
     */
    @ApiModelProperty(value = "子文件夹")
    private List<FolderResponse> childFolders = new ArrayList<>(1);
}