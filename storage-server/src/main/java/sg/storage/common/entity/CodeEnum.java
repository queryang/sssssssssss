package sg.storage.common.entity;

import lombok.Getter;

/**
 * 状态码
 */
@Getter
public enum CodeEnum {

    SUCCESS(200, "成功"),
    ERROR(500, "错误"),
    NO_PATH(404, "访问路径缺失"),
    NO_PARAM(102, "参数缺失"),
    PARAM_ERROR(405, "参数错误"),
    NO_DATA(50001, "数据缺失");

    private Integer code;
    private String message;

    CodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getNameByCode(Integer code) {
        for (CodeEnum codeEnum : CodeEnum.values()) {
            if (code.equals(codeEnum.getCode())) {
                return codeEnum.name();
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        for (CodeEnum codeEnum : CodeEnum.values()) {
            if (code.equals(String.valueOf(codeEnum.getCode()))) {
                return codeEnum.name();
            }
        }
        return null;
    }

    public static String getMessageByCode(Integer code) {
        for (CodeEnum codeEnum : CodeEnum.values()) {
            if (code.equals(codeEnum.getCode())) {
                return codeEnum.getMessage();
            }
        }
        return null;
    }

    public static String getMessageByCode(String code) {
        for (CodeEnum codeEnum : CodeEnum.values()) {
            if (code.equals(String.valueOf(codeEnum.getCode()))) {
                return codeEnum.getMessage();
            }
        }
        return null;
    }
}