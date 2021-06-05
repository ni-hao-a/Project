package com.pc.core.utils.web.exception;

import com.pc.core.constants.HttpStatus;
import com.pc.core.exception.BaseException;
import com.pc.core.exception.CustomException;
import com.pc.core.exception.DemoModeException;
import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 基础异常
     */
    @ExceptionHandler(BaseException.class)
    public ResponseBean baseException(BaseException e) {
        return ResponseUtil.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public ResponseBean businessException(CustomException e) {
        if (StringUtils.isNull(e.getCode())) {
            return ResponseUtil.error(e.getMessage());
        }
        return ResponseUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseBean handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error(HttpStatus.NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseBean handleAuthorizationException(AccessDeniedException e) {
        log.error(e.getMessage());
        return ResponseUtil.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseBean handleAccountExpiredException(AccountExpiredException e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseBean handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseBean handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseUtil.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseBean validatedBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResponseUtil.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object validExceptionHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseUtil.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public ResponseBean demoModeException(DemoModeException e) {
        return ResponseUtil.error("演示模式，不允许操作");
    }
}
