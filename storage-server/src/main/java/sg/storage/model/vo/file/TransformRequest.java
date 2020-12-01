package sg.storage.model.vo.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 文件转化request
 */
@Getter
@Setter
@ApiModel(value = "文件转化request")
public class TransformRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 源文件id
     */
    @ApiModelProperty(value = "源文件id", example = "1")
    private String originFileId;
    /**
     * 源文件类型（image/video/audio/word/excel）
     */
    @ApiModelProperty(value = "源文件类型", example = "image")
    private String originFileType;
    /**
     * 源文件路径
     */
    @ApiModelProperty(value = "源文件路径", example = "D://file.txt")
    private String originFilePath;
    /**
     * 转化文件后缀
     */
    @ApiModelProperty(value = "转化文件后缀", example = "png")
    private String transformFileSuffix;
    /**
     * 转化文件尺寸类型（o原尺寸t缩略图s小尺寸m中尺寸l大尺寸）
     */
    @ApiModelProperty(value = "转化文件尺寸类型", example = "o")
    private String transformFileSizeType;
    /**
     * 转化文件路径
     */
    @ApiModelProperty(value = "转化文件路径", example = "D://file.png")
    private String transformFilePath;
    /**
     * 转化实现类
     */
    @ApiModelProperty(value = "转化实现类", example = "sg.storage.transform.service.impl.image.ImageTransform")
    private String transformClass;
}