package com.pc.gateway.exception;

import com.pc.core.exception.ExceptionResponseBean;
import com.pc.core.exception.ResponseCodeEnum;
import com.pc.core.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
@Slf4j
public class PubExceptionHandle {

    /**
     * 未定义异常
     *
     * @param e Exception
     * @return 公共异常响应体
     */
    @ExceptionHandler(value = Exception.class)
    public ExceptionResponseBean systemException(Exception e) {
        log.error("未定义异常", e);
        return ResponseUtil.setError(ResponseCodeEnum.SERVER_INTERNAL_ERROR);
    }

    /**
     * 业务异常
     *
     * @param e Exception
     * @return 公共异常响应体
     */
    @ExceptionHandler(value = BusinessException.class)
    public ExceptionResponseBean businessException(BusinessException e) {
        log.error("业务异常", e);
        return ResponseUtil.setError(ResponseCodeEnum.SERVER_INTERNAL_ERROR);
    }
}
