package sg.storage.model.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件response
 */
@Getter
@Setter
@ApiModel(value = "文件response")
public class FileResponse implements Serializable {

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
     * 文件唯一标识
     */
    @ApiModelProperty(value = "文件唯一标识")
    private String etag;
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    private String filePath;
    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private Long fileSize;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    /**
     * 文件后缀
     */
    @ApiModelProperty(value = "文件后缀")
    private String fileSuffix;
    /**
     * 文件尺寸类型
     */
    @ApiModelProperty(value = "文件尺寸类型")
    private String fileSizeType;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 文件夹id
     */
    @ApiModelProperty(value = "文件夹id")
    private String folderId;
    /**
     * 是否源文件（0否1是）
     */
    @ApiModelProperty(value = "是否源文件")
    private Integer beOrigin;
    /**
     * 源文件id
     */
    @ApiModelProperty(value = "源文件id")
    private String originFileId;
    /**
     * 源文件名称
     */
    @ApiModelProperty(value = "源文件名称")
    private String originalFileName;
    /**
     * 上传时间（毫秒）
     */
    @ApiModelProperty(value = "上传时间")
    private Long uploadTime;
    /**
     * 上传日期
     */
    @ApiModelProperty(value = "上传日期")
    private String uploadDate;
    /**
     * 上传人
     */
    @ApiModelProperty(value = "上传人")
    private String uploadUser;
    /**
     * 转化文件
     */
    @ApiModelProperty(value = "转化文件")
    private List<FileResponse> transformFiles = new ArrayList<>(1);
}