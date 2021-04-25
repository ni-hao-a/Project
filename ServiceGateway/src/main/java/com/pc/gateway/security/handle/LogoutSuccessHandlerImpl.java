package com.pc.gateway.security.handle;

import com.alibaba.fastjson.JSON;
import com.pc.core.constants.Constants;
import com.pc.core.constants.HttpStatus;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.ServletUtils;
import com.pc.core.utils.StringUtils;
import com.pc.gateway.bean.LoginUser;
import com.pc.gateway.manager.AsyncManager;
import com.pc.gateway.manager.factory.AsyncFactory;
import com.pc.gateway.utils.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author qhl
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(ResponseUtil.error(HttpStatus.SUCCESS, "退出成功")));
    }
}
