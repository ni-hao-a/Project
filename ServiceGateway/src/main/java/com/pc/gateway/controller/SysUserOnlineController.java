package com.pc.gateway.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.core.constants.Constants;
import com.pc.core.model.ResponseBean;
import com.pc.core.redis.RedisCache;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.StringUtils;
import com.pc.gateway.service.ISysUserOnlineService;
import com.pc.model.rlzy.entity.SysUserOnline;
import com.pc.model.rlzy.login.LoginUser;
import com.pc.model.rlzy.req.OnlineReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/pc/base/gateway/v1/online")
public class SysUserOnlineController {
    @Autowired
    private ISysUserOnlineService userOnlineService;

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @RequestMapping(value = "/onlinePeo", method = RequestMethod.POST)
    public ResponseBean list(@RequestBody OnlineReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        String ipAddr = req.getIpAddr();
        String userName = req.getUserName();
        Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
        List<SysUserOnline> userOnlineList = new ArrayList<SysUserOnline>();
        for (String key : keys) {
            LoginUser user = redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty(ipAddr) && StringUtils.isNotEmpty(userName)) {
                if (StringUtils.equals(ipAddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername())) {
                    userOnlineList.add(userOnlineService.selectOnlineByInfo(ipAddr, userName, user));
                }
            } else if (StringUtils.isNotEmpty(ipAddr)) {
                if (StringUtils.equals(ipAddr, user.getIpaddr())) {
                    userOnlineList.add(userOnlineService.selectOnlineByIpaddr(ipAddr, user));
                }
            } else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser())) {
                if (StringUtils.equals(userName, user.getUsername())) {
                    userOnlineList.add(userOnlineService.selectOnlineByUserName(userName, user));
                }
            } else {
                userOnlineList.add(userOnlineService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return ResponseUtil.success(new PageInfo<>(userOnlineList));
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @RequestMapping(value = "/forceLogout", method = RequestMethod.POST)
    public ResponseBean forceLogout(@PathVariable String tokenId) {
        redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + tokenId);
        return ResponseUtil.success();
    }
}
