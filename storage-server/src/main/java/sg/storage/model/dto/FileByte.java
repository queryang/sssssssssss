package sg.storage.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文件流
 */
@Getter
@Setter
@Builder
public class FileByte implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * appKey
     */
    private String appKey;
    /**
     * ownId
     */
    private String ownId;
    /**
     * 唯一标识
     */
    private String etag;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 备注
     */
    private String remark;
    /**
     * 文件夹id
     */
    private String folderId;
    /**
     * 上传人
     */
    private String uploadUser;
    /**
     * 文件内容类型
     */
    private String contentType;
    /**
     * 文件字节
     */
    private byte[] fileByte;
    /**
     * 预存文件夹-新添加的属性
     */
    private String path;
}