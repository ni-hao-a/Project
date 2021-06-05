package com.pc.gateway.controller;

import com.google.code.kaptcha.Producer;
import com.pc.core.constants.Constants;
import com.pc.core.exception.ResponseCodeEnum;
import com.pc.core.model.ResponseBean;
import com.pc.core.redis.RedisCache;
import com.pc.core.utils.Base64.Base64s;
import com.pc.core.utils.MessageUtils;
import com.pc.core.utils.ResponseUtil;
import com.pc.core.utils.ServletUtils;
import com.pc.core.utils.uuid.EntryUtil;
import com.pc.gateway.contants.RedisContants;
import com.pc.gateway.manager.AsyncManager;
import com.pc.gateway.manager.factory.AsyncFactory;
import com.pc.gateway.mapper.LoginMapper;
import com.pc.gateway.utils.GenerateVerificationCodeUtil;
import com.pc.gateway.utils.JwtUtil;
import com.pc.gateway.utils.feignService.RSysMenuFeignService;
import com.pc.gateway.utils.service.SysPermissionService;
import com.pc.gateway.utils.service.TokenService;
import com.pc.model.rlzy.User;
import com.pc.model.rlzy.entity.SysMenu;
import com.pc.model.rlzy.entity.SysUser;
import com.pc.model.rlzy.entity.VerificationCodeBean;
import com.pc.model.rlzy.login.LoginUser;
import com.pc.model.rlzy.user.UserInfoBean;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Service
@RequestMapping(value = "/pc/base/gateway/v1/rlzy", produces = "application/json")
public class LoginController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private LoginMapper loginMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TokenService service;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private RSysMenuFeignService menuFeignService;
    @Resource
    private AuthenticationManager authenticationManager;

    // 验证码类型
    @Value("${system.captchaType}")
    private String captchaType;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public String checkLogin() {
        String token = request.getHeader("token");
        return "hello,welcome" + token;
    }

    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseBean login(@Valid @RequestBody UserInfoBean req) throws Exception {
        // 验证码验证
        String verifyKey = RedisContants.CACHE_ID + req.getUuid();
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey); // 删除已经使用过的验证码
        if (null == req.getCode() || "".equals(req.getCode().trim())) {
            return ResponseUtil.error(ResponseCodeEnum.VERIFICATION_CODE_IS_EMPTY);
        }
        if (null == captcha) {
            return ResponseUtil.error(ResponseCodeEnum.VERIFICATION_CODE_IS_EXPIRED);
        }
        if (!req.getCode().toLowerCase().equals(captcha.toLowerCase())) { // 设置验证码忽略大小写
            return ResponseUtil.error(ResponseCodeEnum.VERIFICATION_CODE_IS_ERROR);
        }

        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(req.getUserName(), req.getPassword()));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                return ResponseUtil.error(ResponseCodeEnum.USERNAME_OR_PWD_IS_ERROR);
            } else {
                return ResponseUtil.error(ResponseCodeEnum.USERNAME_OR_PWD_IS_ERROR);
            }
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        String token = service.createToken(loginUser);
        loginUser.setToken(token);
        User user = new User();
        user.setToken(token);
        user.setUserId(loginUser.getUser().getUserId());
        user.setUserName(loginUser.getUser().getUserName());
        return ResponseUtil.success(user);
    }

    @ApiOperation("用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseBean logout() throws Exception {
        // 验证码验证
        return ResponseUtil.success();
    }

    /**
     * 生成验证码
     */
    @ApiOperation("生成验证码")
    @RequestMapping(value = "/captchaImage", method = RequestMethod.GET)
    public ResponseBean getCode(HttpServletResponse response) throws IOException {
        // 保存验证码信息
        String uuid = EntryUtil.simpleUUID();
        String verifyKey = RedisContants.CACHE_ID + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisCache.setCacheObject(verifyKey, code, RedisContants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            log.error("IOException", e);
        }

        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("uuid", uuid);
        codeMap.put("img", Base64s.encode(os.toByteArray()));
        return ResponseUtil.success(codeMap);
    }

    /**
     * 生成字符验证码
     *
     * @param request  request
     * @param response response
     */
    @RequestMapping(value = "/getCode", method = RequestMethod.GET)
    public ResponseBean getVerifyCode(HttpServletRequest request,
                                      HttpServletResponse response) {
        // 设置响应头通知浏览器以图片的形式打开
        response.setContentType("image/jpeg");// 等同于response.setHeader("Content-Type",
        // "image/jpeg");
        // 设置响应头控制浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        // 获取验证码和图片
        VerificationCodeBean verCode = GenerateVerificationCodeUtil.verCode();

        // 7.将随机数存在redis中,设置过期时间为2分钟
        String uuid = EntryUtil.simpleUUID();// 保存验证码信息生成唯一标识
        String key = RedisContants.CACHE_ID + uuid;
        redisCache.setCacheObject(key, verCode.getCode(), RedisContants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);

        // 将图片写给浏览器，转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(verCode.getImage(), "jpg", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回给浏览器一个uuid，作为登录时获取验证码使用
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("uuid", uuid);
        codeMap.put("img", Base64s.encode(os.toByteArray()));
        return ResponseUtil.success(codeMap);
    }

    /**
     * 获取用户基本信息
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public ResponseBean getInfomation() {
        LoginUser loginUser = service.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("user", user);
        codeMap.put("roles", roles);
        codeMap.put("permissions", permissions);
        return ResponseUtil.success(codeMap);
    }

    /**
     * 获取路由
     *
     * @return ResponseBean
     */
    @RequestMapping(value = "/getRoute", method = RequestMethod.POST)
    public ResponseBean getRoute() {
        LoginUser loginUser = service.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuFeignService.selectMenuTreeByUserId(user.getUserId());
        return ResponseUtil.success(menus);
    }

}
