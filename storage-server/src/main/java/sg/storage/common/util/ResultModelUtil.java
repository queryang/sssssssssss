package sg.storage.common.util;

import org.apache.commons.lang3.StringUtils;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.ResultModel;

public class ResultModelUtil {

    public static ResultModel getInstance() {
        return getInstance(true);
    }

    public static ResultModel getInstance(Boolean success) {
        if (success) {
            return getSuccessInstance();
        }
        return getFailInstance();
    }

    public static ResultModel getSuccessInstance() {
        return getInstance(CodeEnum.SUCCESS, true, CodeEnum.SUCCESS.getMessage(), null);
    }

    public static ResultModel getFailInstance() {
        return getInstance(CodeEnum.ERROR, false, CodeEnum.ERROR.getMessage(), null);
    }

    public static ResultModel getSuccessInstance(String message) {
        return getInstance(CodeEnum.SUCCESS, true, message, null);
    }

    public static ResultModel getSuccessInstance(Object data) {
        return getInstance(CodeEnum.SUCCESS, true, CodeEnum.SUCCESS.getMessage(), data);
    }

    public static ResultModel getSuccessInstance(String message, Object data) {
        return getInstance(CodeEnum.SUCCESS, true, message, data);
    }

    public static ResultModel getFailInstance(CodeEnum codeEnum) {
        return getInstance(codeEnum, false, codeEnum.getMessage(), null);
    }

    public static ResultModel getFailInstance(String message) {
        return getInstance(CodeEnum.ERROR, false, message, null);
    }

    public static ResultModel getFailInstance(CodeEnum codeEnum, String message) {
        if (StringUtils.isBlank(message)) {
            message = codeEnum.getMessage();
        }
        return getInstance(codeEnum, false, message, null);
    }

    public static ResultModel getInstance(CodeEnum codeEnum, Boolean success, String message, Object data) {
        if (!success) {
            return ResultModel.builder()
                    .code(codeEnum.getCode())
                    .success(success)
                    .message(message)
                    .build();
        }
        return ResultModel.builder()
                .code(codeEnum.getCode())
                .success(success)
                .message(message)
                .data(data)
                .build();
    }
}