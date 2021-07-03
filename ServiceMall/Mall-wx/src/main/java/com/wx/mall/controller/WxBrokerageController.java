package com.wx.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.pc.service.domain.DtsAccountTrace;
import com.pc.service.domain.DtsUserAccount;
import com.pc.service.service.DtsAccountService;
import com.wx.core.consts.CommConsts;
import com.wx.core.type.BrokerageTypeEnum;
import com.wx.core.util.DateTimeUtil;
import com.wx.core.util.JacksonUtil;
import com.wx.core.util.ResponseUtil;
import com.wx.mall.annotation.LoginUser;
import com.wx.mall.service.WxOrderService;
import com.wx.mall.util.WxResponseCode;
import com.wx.mall.util.WxResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 佣金业务接口
 *
 * @author CHENBO
 * @QQ 623659388
 * @since 1.0.0
 */
@RestController
@RequestMapping("/wx/brokerage")
@Validated
@Slf4j
public class WxBrokerageController {

    @Autowired
    private DtsAccountService accountService;

    @Autowired
    private WxOrderService wxOrderService;

    /**
     * 用户结算页面数据
     * <p>
     * 目前是用户结算统计信息
     *
     * @param userId 用户ID
     * @return 用户个人页面数据
     */
    @GetMapping("main")
    public Object main(@LoginUser Integer userId) {
        log.info("【请求开始】获取用户结算页面数据,请求参数,userId:{}", userId);
        if (userId == null) {
            log.error("获取结算信息数据失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }
        Map<Object, Object> data = new HashMap<Object, Object>();

        // 查询用户账号
        DtsUserAccount userAccount = accountService.findShareUserAccountByUserId(userId);
        BigDecimal totalAmount = new BigDecimal(0.00);
        BigDecimal remainAmount = new BigDecimal(0.00);
        if (userAccount != null) {
            totalAmount = userAccount.getTotalAmount();
            remainAmount = userAccount.getRemainAmount();
        }

        //可提现金额 = 已结算未提现 remainAmount + 未结算 unSettleAmount
        BigDecimal unSettleAmount = accountService.getUnSettleAmount(userId);
        data.put("totalAmount", totalAmount);
        data.put("remainAmount", remainAmount.add(unSettleAmount));

        // 统计数据信息 本月和上月的结算
        String lastMonthEndTime = DateTimeUtil.getPrevMonthEndDay() + " 23:59:59";
        String lastMonthStartTime = DateTimeUtil.getPrevMonthEndDay().substring(0, 7) + "-01 00:00:00";
        BigDecimal lastMonthSettleMoney = accountService.getMonthSettleMoney(userId, lastMonthStartTime,
                lastMonthEndTime);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        String currMonthEndTime = today + " 23:59:59";
        String currMonthStartTime = today.substring(0, 7) + "-01 00:00:00";
        BigDecimal currMonthSettleMoney = accountService.getMonthSettleMoney(userId, currMonthStartTime,
                currMonthEndTime);
        data.put("lastMonthSettleMoney", lastMonthSettleMoney);
        data.put("currMonthSettleMoney", currMonthSettleMoney);

        Map<String, Object> todayData = accountService.getStatistics(userId, 1);
        Map<String, Object> yestodayData = accountService.getStatistics(userId, 2);
        Map<String, Object> weekData = accountService.getStatistics(userId, 7);
        Map<String, Object> monthData = accountService.getStatistics(userId, 30);

        data.put("todayData", todayData);
        data.put("yestodayData", yestodayData);
        data.put("weekData", weekData);
        data.put("monthData", monthData);

        log.info("【请求结束】获取用户结算页面数据,响应结果：{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }

    /**
     * 推广订单列表
     *
     * @param userId   用户代理用户ID
     * @param showType 订单信息： 0，全部订单； 1，有效订单； 2，失效订单； 3，结算订单； 4，待结算订单。
     * @param page     分页页数
     * @param size     分页大小
     * @return 推广订单列表
     */
    @GetMapping("settleOrderList")
    public Object settleOrderList(@LoginUser Integer userId, @RequestParam(defaultValue = "0") Integer showType,
                                  @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        return wxOrderService.settleOrderList(userId, showType, page, size);
    }

    @GetMapping("extractList")
    public Object extractList(@LoginUser Integer userId, @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer size) {
        return wxOrderService.extractList(userId, page, size);
    }

    /**
     * 提现申请
     *
     * @param userId
     * @return
     */
    @PostMapping("applyWithdrawal")
    public Object applyWithdrawal(@LoginUser Integer userId, @RequestBody String body) {
        log.info("【请求开始】提现申请,请求参数,body:{}", body);
        if (userId == null) {
            log.error("提现申请失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }

        String mobile = JacksonUtil.parseString(body, "mobile");
        //String code = JacksonUtil.parseString(body, "code");
        String amt = JacksonUtil.parseString(body, "amt");

        if (StringUtils.isEmpty(amt) || StringUtils.isEmpty(mobile)) {
            log.error("提现申请失败:{}", CommConsts.MISS_PARAMS);
            return ResponseUtil.badArgument();
        }

        // 判断验证码是否正确
		/*String cacheCode = CaptchaCodeManager.getCachedCaptcha(mobile);
		if (cacheCode == null || cacheCode.isEmpty() || !cacheCode.equals(code)) {
			log.error("提现申请失败:{}", AUTH_CAPTCHA_UNMATCH.desc());
			return WxResponseUtil.fail(AUTH_CAPTCHA_UNMATCH);
		}*/

        //验证是否存在未审批通过的申请单，需完成上一个申请才能继续申请下一个提现
        List<DtsAccountTrace> traceList = accountService.getAccountTraceList(userId, (byte) 0);
        if (traceList.size() > 0) {
            log.error("提现申请失败:{}", WxResponseCode.APPLY_WITHDRAWAL_EXIST.desc());
            return WxResponseUtil.fail(WxResponseCode.APPLY_WITHDRAWAL_EXIST);
        }

        LocalDateTime startTime = LocalDateTime.now().minusDays(DtsAccountService.TWO_MONTH_DAYS);
        LocalDateTime endTime = LocalDateTime.now().minusDays(DtsAccountService.ONE_WEEK_DAYS);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //获取未结算的金额
        BigDecimal unSettleAmount = accountService.getUnSettleAmount(userId, startTime.format(df), endTime.format(df));
        if (unSettleAmount != null && unSettleAmount.compareTo(new BigDecimal(0)) > 0) {
            accountService.settleApplyTrace(userId, startTime.format(df), endTime.format(df), BrokerageTypeEnum.USER_APPLY.getType().intValue(), unSettleAmount, mobile);
        }

        log.info("【请求结束】提现申请成功！");
        return ResponseUtil.ok();
    }
}
