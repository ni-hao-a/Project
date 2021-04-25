package com.pc.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.pc.core.model.ResponseBean;
import com.pc.gateway.contants.FilterContants;
import com.pc.gateway.utils.JwtUtil;
import com.pc.gateway.utils.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class LoginFilter extends ZuulFilter {

    @Autowired
    private TokenService tokenService;

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
        log.info(request.getMethod()+"请求："+request.getRequestURL().toString());
        String accessToken = request.getHeader("token");
        if (null == accessToken) {
            log.error("token is empty");
            ctx.setSendZuulResponse(false);
            errorResponse(ctx.getResponse(), "token is empty");
            return null;
        }
        if (null == tokenService.parseToken(accessToken)) {
            log.error("invalid token");
            ctx.setSendZuulResponse(false);
            errorResponse(ctx.getResponse(), "invalid token");
            return null;
        }
        log.info("ok");
        return null;
    }

    private void errorResponse(HttpServletResponse response, String message) {
        ResponseBean resp = new ResponseBean();
        resp.setCode(401);
        resp.setMessage(message);
        response.setStatus(401);
        String result = JSONObject.toJSONString(resp);
        try {
            StreamUtils.copy(result, StandardCharsets.UTF_8, response.getOutputStream());
        } catch (IOException e) {
            log.error("IOException", e);
        }
    }
}
