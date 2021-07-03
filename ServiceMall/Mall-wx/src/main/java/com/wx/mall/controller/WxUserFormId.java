package com.wx.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.pc.service.domain.DtsUser;
import com.pc.service.domain.DtsUserFormid;
import com.pc.service.service.DtsUserFormIdService;
import com.pc.service.service.DtsUserService;
import com.wx.core.util.ResponseUtil;
import com.wx.mall.annotation.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/wx/formid")
@Validated
@Slf4j
public class WxUserFormId {

    @Autowired
    private DtsUserService userService;

    @Autowired
    private DtsUserFormIdService formIdService;

    /**
     * 创建微信访问fromID
     *
     * @param userId
     * @param formId
     * @return
     */
    @GetMapping("create")
    public Object create(@LoginUser Integer userId, @NotNull String formId) {
        log.info("【请求开始】创建微信访问fromID,请求参数,userId:{}", userId);

        if (userId == null) {
            log.error("创建微信访问fromID失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }

        DtsUser user = userService.findById(userId);
        DtsUserFormid userFormid = new DtsUserFormid();
        userFormid.setOpenid(user.getWeixinOpenid());
        userFormid.setFormid(formId);
        userFormid.setIsprepay(false);
        userFormid.setUseamount(1);
        userFormid.setExpireTime(LocalDateTime.now().plusDays(7));
        formIdService.addUserFormid(userFormid);

        log.info("【请求结束】创建微信访问fromID,响应结果:{}", JSONObject.toJSONString(userFormid));
        return ResponseUtil.ok();
    }
}
