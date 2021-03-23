package com.pc.business.exception;

import com.pc.business.model.ResponseBean;
import com.pc.business.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
@Slf4j
public class PubExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    public ResponseBean systemException(Exception e){
        log.error("未定义异常",e);
        return ResponseUtil.setError(ErrorCodeEnum.SERVER_INTERNAL_ERROR);
    }
}
