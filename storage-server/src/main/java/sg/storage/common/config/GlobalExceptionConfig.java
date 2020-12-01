package sg.storage.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sg.storage.common.entity.CodeEnum;
import sg.storage.common.entity.ResultModel;
import sg.storage.common.util.ResultModelUtil;

/**
 * 全局异常配置
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(value = {Exception.class})
    public ResultModel globalException(Exception e) {
        e.printStackTrace();
        log.error("全局异常", e);
        return ResultModelUtil.getFailInstance(CodeEnum.ERROR, e.getMessage());
    }
}