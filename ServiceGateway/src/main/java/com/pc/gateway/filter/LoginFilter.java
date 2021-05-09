package com.pc.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.pc.core.constants.Constants;
import com.pc.core.utils.StringUtils;
import com.pc.gateway.config.SystemConfig;
import com.pc.gateway.contants.FilterContants;
import com.pc.gateway.utils.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class LoginFilter extends ZuulFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private SystemConfig config;

    @Override
    public String filterType() {
        return FilterContants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        // 添加网关转发认证参数
        String token = request.getHeader(Constants.TOKEN);
        if (StringUtils.isNotEmpty(token)) {
            String auth = tokenService.getAuthKeyByToken(token);
            ctx.addZuulRequestHeader(Constants.AUTHORIZATION, auth);
        }
        // MDC日志记录zuul转发转发的请求
        MDC.put("zuulPath", request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/") + 1));
        //ctx.addZuulRequestHeader("authKey", AesUtil.encrypt(auth, config.getAuthKey()));
        log.info("zuul转发：" + request.getMethod() + "请求：" + request.getRequestURL().toString());
        MDC.remove("zuulPath");
        return null;
    }
}
