package sg.storage.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "分页")
public class DataTableModel extends ResultModel {

    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long recordsTotal = 0L;

    public DataTableModel() {
        this(CodeEnum.SUCCESS.getCode(), true, CodeEnum.SUCCESS.getMessage(), null);
    }

    public DataTableModel(int code, boolean success, String message, Object data) {
        super(code, success, message, data);
    }
}