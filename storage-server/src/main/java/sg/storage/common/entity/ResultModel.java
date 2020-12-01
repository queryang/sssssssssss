package sg.storage.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 统一返回
 *
 * @param <T>
 */
@Getter
@Setter
@Builder
@ApiModel(value = "统一返回")
public class ResultModel<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private Integer code;
    /**
     * 是否成功
     */
    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    /**
     * 信息
     */
    @ApiModelProperty(value = "信息")
    private String message;
    /**
     * 数据
     */
    @ApiModelProperty(value = "数据")
    private T data;
}