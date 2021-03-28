package com.pc.business.aspect;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
@Slf4j
public class AspectLog {

    /**
     * 声明切入点
     * 切入点表达式常用格式举例如下：
     * -public * com.pc.business.controller..*(..)：表示 public * com.pc.business.controller包中所有公共方法
     * -com.pc.business.*.*(..))：表示 com.pc.business 包(不含子包)下任意类中的任意方法
     * -com.pc.business..*.*(..))：表示 com.pc.business 包及其子包下任意类中的任意方法
     */
    @Pointcut(value = "execution(public * com.pc.business.controller..*(..)))")
    public void webLog() {

    }

    /**
     * 前置通知：目标方法执行之前执行以下方法体的内容。
     * value：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     */
    @Before(value = "webLog()")
    public void aspectBefore(JoinPoint joinPoint) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String method = request.getMethod();
        String reqBody;
        if (method.equals("GET")) {
            reqBody = request.getQueryString();
        } else {
            Object[] args = joinPoint.getArgs();
            Object[] params = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse ||
                        args[i] instanceof MultipartFile) {
                    continue;
                }
                params[i] = args[i];
            }
            reqBody = JSONObject.toJSONString(params);
        }
        if (log.isDebugEnabled())
            log.debug("请求入参INPUT: " + reqBody);
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = proceedingJoinPoint.proceed();
        if (log.isDebugEnabled())
            log.debug("耗时:{}毫秒 ", Long.valueOf(System.currentTimeMillis() - startTime));
        return ob;
    }

    /**
     * 后置通知：目标方法执行之后执行以下方法体的内容，不管目标方法是否发生异常。
     * value：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     */
    @AfterReturning(returning = "resp", value = "webLog()")
    public void aspectAfter(Object resp) {
        if (log.isDebugEnabled())
            log.debug("响应出参OUTPUT: " + JSONObject.toJSONString(resp));
    }

    /**
     * 异常通知：目标方法发生异常的时候执行以下代码，此时返回通知不会再触发
     * value 属性：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     * pointcut 属性：绑定通知的切入点表达式，优先级高于 value，默认为 ""
     * throwing 属性：与方法中的异常参数名称一致，
     *
     * @param ex：捕获的异常对象，名称与 throwing 属性值一致
     */
    @AfterThrowing(pointcut = "webLog()", throwing = "ex")
    public void aspectAfterThrowing(JoinPoint jp, Exception ex) {
        String methodName = jp.getSignature().getName();
        log.error("【" + methodName + "】方法异常：", ex.getMessage());
    }

}

