package com.wx.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.pc.service.domain.DtsFeedback;
import com.pc.service.domain.DtsUser;
import com.pc.service.service.DtsFeedbackService;
import com.pc.service.service.DtsUserService;
import com.wx.core.util.RegexUtil;
import com.wx.core.util.ResponseUtil;
import com.wx.mall.annotation.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 意见反馈服务
 *
 * @author CHENBO
 * @QQ 623659388
 * @since 1.0.0
 */
@RestController
@RequestMapping("/wx/feedback")
@Validated
@Slf4j
public class WxFeedbackController {

    @Autowired
    private DtsFeedbackService feedbackService;
    @Autowired
    private DtsUserService userService;

    private Object validate(DtsFeedback feedback) {
        String content = feedback.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseUtil.badArgument();
        }

        String type = feedback.getFeedType();
        if (StringUtils.isEmpty(type)) {
            return ResponseUtil.badArgument();
        }

        Boolean hasPicture = feedback.getHasPicture();
        if (hasPicture == null || !hasPicture) {
            feedback.setPicUrls(new String[0]);
        }

        // 测试手机号码是否正确
        String mobile = feedback.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return ResponseUtil.badArgument();
        }
        if (!RegexUtil.isMobileExact(mobile)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    /**
     * 添加意见反馈
     *
     * @param userId   用户ID
     * @param feedback 意见反馈
     * @return 操作结果
     */
    @PostMapping("submit")
    public Object submit(@LoginUser Integer userId, @RequestBody DtsFeedback feedback) {
        log.info("【请求开始】添加意见反馈,请求参数,userId:{},size:{}", userId, JSONObject.toJSONString(feedback));

        if (userId == null) {
            log.error("添加意见反馈失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }
        Object error = validate(feedback);
        if (error != null) {
            return error;
        }

        DtsUser user = userService.findById(userId);
        String username = user.getUsername();
        feedback.setId(null);
        feedback.setUserId(userId);
        feedback.setUsername(username);
        // 状态默认是0，1表示状态已发生变化
        feedback.setStatus(1);
        feedbackService.add(feedback);

        log.info("【请求结束】添加意见反馈,响应结果:{}", JSONObject.toJSONString(feedback));
        return ResponseUtil.ok();
    }

}
