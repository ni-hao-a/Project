package com.pc.business.filter;

import com.alibaba.fastjson.JSONObject;
import com.pc.business.config.HttpConfig;
import com.pc.core.constants.Constants;
import com.pc.core.exception.ResponseCodeEnum;
import com.pc.core.model.ResponseBean;
import com.pc.core.redis.RedisCache;
import com.pc.core.utils.StringUtils;
import com.pc.core.utils.encrypt.AesUtil;
import com.pc.model.rlzy.login.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.pc.core.constants.Constants.IS_FEIGN;


/**
 * 拦截非网关请求
 */
@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private HttpConfig httpConfig;
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 验证免校验路由直接放行
        if (httpConfig.getNoCheckUrl().contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(Constants.AUTHORIZATION);
        String isFeign = request.getHeader(IS_FEIGN);
        // 验证内部转发路由直接放行
        if (StringUtils.isNotEmpty(isFeign)) {
            if (isInternal(isFeign)) {
                chain.doFilter(request, response);
                return;
            }
        }
        // 验证凭证是否有效
        if (StringUtils.isNotEmpty(token)) {
            // 从rides中获取凭证
            String auth = redisCache.getCacheObject(token);
            if (StringUtils.isNotEmpty(auth)) {
                chain.doFilter(request, response);
            } else {
                errorResponse(response, ResponseCodeEnum.TOKEN_IS_EXPIRED.getMessage());
                return;
            }
        } else {
            errorResponse(response, ResponseCodeEnum.TOKEN_IS_EMPTY.getMessage());
            return;
        }
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

    private boolean isInternal(String text) {
        // 解密
        String context = AesUtil.decrypt(text, httpConfig.getKey());
        if (IS_FEIGN.equals(context)) {
            return true;
        }
        return false;
    }
}
