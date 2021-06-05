package com.pc.business.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.service.ISysMenuService;
import com.pc.business.service.ISysPostService;
import com.pc.business.service.ISysRoleService;
import com.pc.business.service.ISysUserService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.SecurityUtils;
import com.pc.core.utils.StringUtils;
import com.pc.model.rlzy.contants.UserConstants;
import com.pc.model.rlzy.entity.SysMenu;
import com.pc.model.rlzy.entity.SysRole;
import com.pc.model.rlzy.entity.SysUser;
import com.pc.model.rlzy.login.LoginUser;
import com.pc.model.rlzy.req.UserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 用户信息
 *
 * @author qhl
 */
@RestController
@RequestMapping(value = "/pc/base/business/v1/rlzy/user", produces = {"application/json;charset=UTF-8"})
public class SysUserController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysPostService postService;
    @Autowired
    private RSysUserFeignService userFeignService;
    @Autowired
    private ISysMenuService menuService;


    /**
     * 获取路由
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/getRoute", method = RequestMethod.POST)
    public ResponseBean getRoute() {
        SysUser user = userFeignService.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return ResponseUtil.success(menuService.buildMenus(menus));
    }

    /**
     * 获取用户列表
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    public ResponseBean getUserList(@RequestBody SysUser req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysUser> list = userService.selectUserList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 根据用户编号获取详细信息
     */
    @RequestMapping(value = "/getUserInfoById", method = RequestMethod.POST)
    public ResponseBean getUserInfoById(@RequestBody UserReq req) {
        Long userId = req.getUserId();
        Map<String, Object> resp = new HashMap<>();
        List<SysRole> roles = roleService.selectRoleAll();
        resp.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        resp.put("posts", postService.selectPostAll());
        if (StringUtils.isNotNull(userId)) {
            resp.put("data", userService.selectUserById(userId));
            resp.put("postIds", postService.selectPostListByUserId(userId));
            resp.put("roleIds", roleService.selectRoleListByUserId(userId));
        }
        return ResponseUtil.success(resp);
    }

    /**
     * 新增用户
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public ResponseBean add(@Valid @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return ResponseUtil.error(6001, "新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return ResponseUtil.error(6002, "新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return ResponseUtil.error(6003, "新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(userFeignService.getUser().getUserName());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return ResponseUtil.success(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public ResponseBean updateUser(@Valid @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StringUtils.isNotEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return ResponseUtil.error(6004, "修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (StringUtils.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return ResponseUtil.error(6005, "修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    public ResponseBean delUser(@RequestBody UserReq req) {
        return ResponseUtil.success(userService.deleteUserByIds(req.getUserIds()));
    }

    /**
     * 重置密码
     */
    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    public ResponseBean resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(userService.resetPwd(user), "重置成功");
    }

    /**
     * 状态修改
     */
    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public ResponseBean changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(userService.updateUserStatus(user));
    }

    /**
     * 导入数据
     */
    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    public ResponseBean importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        LoginUser loginUser = userFeignService.getLoginUser();
        String operName = loginUser.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return ResponseUtil.success(message);
    }

    /**
     * 下载用户导入模板
     */
    @RequestMapping(value = "/importTemplate", method = RequestMethod.POST)
    public ResponseBean importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 导出用户
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public ResponseBean export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }
}
