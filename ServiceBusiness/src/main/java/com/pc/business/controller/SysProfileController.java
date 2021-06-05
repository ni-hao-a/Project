package com.pc.business.controller;

import com.pc.business.config.SystemConfig;
import com.pc.business.service.ISysUserService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.file.FileUploadUtils;
import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.SecurityUtils;
import com.pc.core.utils.StringUtils;
import com.pc.model.rlzy.contants.UserConstants;
import com.pc.model.rlzy.entity.SysUser;
import com.pc.model.rlzy.login.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/profile")
public class SysProfileController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private RSysUserFeignService userFeignService;
    @Autowired
    private SystemConfig systemConfig;

    /**
     * 个人信息
     */
    @RequestMapping(value = "/getProfile", method = RequestMethod.POST)
    public ResponseBean getProfile() {
        SysUser user = userFeignService.getUser();
        Map<String, Object> resp = new HashMap<>();
        resp.put("user",user);
        resp.put("roleGroup", userService.selectUserRoleGroup(user.getUserName()));
        resp.put("postGroup", userService.selectUserPostGroup(user.getUserName()));
        return ResponseUtil.success(resp);
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public ResponseBean updateProfile(@RequestBody SysUser user) {
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return ResponseUtil.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return ResponseUtil.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        if (userService.updateUserProfile(user) > 0) {
            LoginUser loginUser = userFeignService.getLoginUser();
            // 更新缓存用户信息
            loginUser.getUser().setNickName(user.getNickName());
            loginUser.getUser().setPhonenumber(user.getPhonenumber());
            loginUser.getUser().setEmail(user.getEmail());
            loginUser.getUser().setSex(user.getSex());
            userFeignService.setLoginUser(loginUser);
            return ResponseUtil.success();
        }
        return ResponseUtil.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public ResponseBean updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = userFeignService.getLoginUser();
        String userName = loginUser.getUsername();
        String password = loginUser.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            return ResponseUtil.error("修改密码失败，旧密码错误");
        }
        if (SecurityUtils.matchesPassword(newPassword, password)) {
            return ResponseUtil.error("新密码不能与旧密码相同");
        }
        if (userService.resetUserPwd(userName, SecurityUtils.encryptPassword(newPassword)) > 0) {
            // 更新缓存用户密码
            loginUser.getUser().setPassword(SecurityUtils.encryptPassword(newPassword));
            userFeignService.setLoginUser(loginUser);
            return ResponseUtil.success();
        }
        return ResponseUtil.error("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    public ResponseBean avatar(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = userFeignService.getLoginUser();
            String avatar = FileUploadUtils.upload(systemConfig.getAvatarPath(), file);
            if (userService.updateUserAvatar(loginUser.getUsername(), avatar)) {
                Map<String, Object> resp = new HashMap<>();
                resp.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                userFeignService.setLoginUser(loginUser);
                return ResponseUtil.success(resp);
            }
        }
        return ResponseUtil.error("上传图片异常，请联系管理员");
    }
}
