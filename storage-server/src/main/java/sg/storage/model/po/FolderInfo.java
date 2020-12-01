package sg.storage.model.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 文件夹
 */
@Getter
@Setter
@Entity
@Table(name = "folder")
@ApiModel(value = "文件夹")
public class FolderInfo implements Serializable {

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
     * ownId（默认1）
     */
    @ApiModelProperty(value = "ownId", example = "1")
    @Column(name = "ownId", columnDefinition = "varchar(50) comment 'ownId（默认1）'")
    private String ownId = "1";
    /**
     * 父文件夹id（默认-1）
     */
    @ApiModelProperty(value = "父文件夹id", required = true, example = "-1")
    @Column(name = "pid", columnDefinition = "bigint comment '父文件夹id（默认-1）'")
    private Long pid = -1L;
    /**
     * 文件夹名称
     */
    @ApiModelProperty(value = "文件夹名称", required = true, example = "新建文件夹")
    @Column(name = "name", columnDefinition = "varchar(50) comment '文件夹名称'")
    private String name;
    /**
     * 文件夹类型
     */
    @ApiModelProperty(value = "文件夹类型", example = "1")
    @Column(name = "type", columnDefinition = "int(2) comment '文件夹类型'")
    private Integer type;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "该文件夹为测试文件夹")
    @Column(name = "remark", columnDefinition = "varchar(200) comment '备注'")
    private String remark;
    /**
     * 是否共享（0否1是）（默认0）
     */
    @ApiModelProperty(value = "是否共享", example = "0")
    @Column(name = "beShare", columnDefinition = "int(2) comment '是否共享（0否1是）（默认0）'")
    private Integer beShare = 0;
    /**
     * 是否只读（0否1是）（默认0）
     */
    @ApiModelProperty(value = "是否只读", example = "0")
    @Column(name = "beReadOnly", columnDefinition = "int(2) comment '是否只读（0否1是）（默认0）'")
    private Integer beReadOnly = 0;
    /**
     * 是否删除（0否1是）（默认0）
     */
    @ApiModelProperty(value = "是否删除", example = "0")
    @Column(name = "del", columnDefinition = "int(2) comment '是否删除（0否1是）（默认0）'")
    private Integer del = 0;
    /**
     * 创建时间（毫秒）
     */
    @ApiModelProperty(value = "创建时间", example = "1595474649000")
    @Column(name = "createTime", columnDefinition = "bigint comment '创建时间（毫秒）'")
    private Long createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", example = "管理员")
    @Column(name = "createUser", columnDefinition = "varchar(20) comment '创建人'")
    private String createUser;
}