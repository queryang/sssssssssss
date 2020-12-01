package sg.storage.model.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 文件
 */
@Data
@Entity
@Table(name = "file")
@ApiModel(value = "文件")
public class FileInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id
    @ApiModelProperty(value = "id", example = "1")
    @Column(name = "id", columnDefinition = "bigint comment 'id'")
    private Long id;
    /**
     * appKey（默认1）
     */
    @ApiModelProperty(value = "appKey", example = "1")
    @Column(name = "appKey", columnDefinition = "varchar(50) comment 'appKey（默认1）'")
    private String appKey = "1";
    /**
     * ownId（默认1）--接收人
     */
    @ApiModelProperty(value = "ownId", example = "1")
    @Column(name = "ownId", columnDefinition = "varchar(50) comment 'ownId（默认1）'")
    private String ownId = "1";
    /**
     * 文件唯一标识
     */
    @ApiModelProperty(value = "文件唯一标识", example = "ss22lewrsaq281sa12")
    @Column(name = "etag", columnDefinition = "varchar(200) comment '文件唯一标识'")
    private String etag;
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径", example = "D://file.txt")
    @Column(name = "filePath", columnDefinition = "varchar(200) comment '文件路径'")
    private String filePath;
    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小", example = "14252")
    @Column(name = "fileSize", columnDefinition = "bigint comment '文件大小'")
    private Long fileSize;
    /**
     * 文件类型（image/video/audio/word/excel/other）（默认other）
     */
    @ApiModelProperty(value = "文件类型", example = "image")
    @Column(name = "fileType", columnDefinition = "varchar(20) comment '文件类型（image/video/audio/word/excel/other）（默认other）'")
    private String fileType = "other";
    /**
     * 文件后缀
     */
    @ApiModelProperty(value = "文件后缀", example = "png")
    @Column(name = "fileSuffix", columnDefinition = "varchar(20) comment '文件后缀'")
    private String fileSuffix;
    /**
     * 文件尺寸类型（o原尺寸t缩略图s小尺寸m中尺寸l大尺寸）（默认o）
     */
    @ApiModelProperty(value = "文件尺寸类型", example = "o")
    @Column(name = "fileSizeType", columnDefinition = "varchar(20) comment '文件尺寸类型（o原尺寸t缩略图s小尺寸m中尺寸l大尺寸）（默认o）'")
    private String fileSizeType = "o";
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "该文件为测试文件")
    @Column(name = "remark", columnDefinition = "varchar(200) comment '备注'")
    private String remark;
    /**
     * 文件夹id
     */
    @ApiModelProperty(value = "文件夹id", example = "1")
    @Column(name = "folderId", columnDefinition = "bigint comment '文件夹id'")
    private Long folderId;
    /**
     * 是否源文件（0否1是）（默认0）
     */
    @ApiModelProperty(value = "是否源文件", example = "0")
    @Column(name = "beOrigin", columnDefinition = "int(2) comment '是否源文件（0否1是）（默认0）'")
    private Integer beOrigin = 0;
    /**
     * 源文件id
     */
    @ApiModelProperty(value = "源文件id", example = "1")
    @Column(name = "originFileId", columnDefinition = "bigint comment '源文件id'")
    private Long originFileId;
    /**
     * 源文件名称
     */
    @ApiModelProperty(value = "源文件名称", example = "源文件")
    @Column(name = "originalFileName", columnDefinition = "varchar(200) comment '源文件名称'")
    private String originalFileName;
    /**
     * 是否删除（0否1是）（默认0）
     */
    @ApiModelProperty(value = "是否删除", example = "0")
    @Column(name = "del", columnDefinition = "int(2) comment '是否删除（0否1是）（默认0）'")
    private Integer del = 0;
    /**
     * 上传时间（毫秒）
     */
    @ApiModelProperty(value = "上传时间", example = "1595474649000")
    @Column(name = "uploadTime", columnDefinition = "bigint comment '上传时间（毫秒）'")
    private Long uploadTime;
    /**
     * 上传人-用户
     */
    @ApiModelProperty(value = "上传人", example = "管理员")
    @Column(name = "uploadUser", columnDefinition = "varchar(20) comment '上传人'")
    private String uploadUser;



}