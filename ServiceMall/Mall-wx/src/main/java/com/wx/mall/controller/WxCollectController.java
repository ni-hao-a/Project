package com.wx.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.pc.service.domain.DtsCollect;
import com.pc.service.domain.DtsGoods;
import com.pc.service.service.DtsCollectService;
import com.pc.service.service.DtsGoodsService;
import com.wx.core.util.JacksonUtil;
import com.wx.core.util.ResponseUtil;
import com.wx.mall.annotation.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户收藏服务
 */
@RestController
@RequestMapping("/wx/collect")
@Validated
@Slf4j
public class WxCollectController {

    @Autowired
    private DtsCollectService collectService;
    @Autowired
    private DtsGoodsService goodsService;

    /**
     * 用户收藏列表
     *
     * @param userId 用户ID
     * @param type   类型，如果是0则是商品收藏，如果是1则是专题收藏
     * @param page   分页页数
     * @param size   分页大小
     * @return 用户收藏列表
     */
    @GetMapping("list")
    public Object list(@LoginUser Integer userId, @NotNull Byte type, @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("【请求开始】用户收藏列表查询,请求参数,userId:{}", userId);
        if (userId == null) {
            log.error("获取用户收藏列表查询失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }

        List<DtsCollect> collectList = collectService.queryByType(userId, type, page, size);
        int count = collectService.countByType(userId, type);
        int totalPages = (int) Math.ceil((double) count / size);

        List<Object> collects = new ArrayList<>(collectList.size());
        for (DtsCollect collect : collectList) {
            Map<String, Object> c = new HashMap<String, Object>();
            c.put("id", collect.getId());
            c.put("type", collect.getType());
            c.put("valueId", collect.getValueId());

            DtsGoods goods = goodsService.findById(collect.getValueId());
            c.put("name", goods.getName());
            c.put("brief", goods.getBrief());
            c.put("picUrl", goods.getPicUrl());
            c.put("retailPrice", goods.getRetailPrice());

            collects.add(c);
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("collectList", collects);
        result.put("totalPages", totalPages);

        log.info("【请求结束】用户收藏列表查询,响应结果：{}", JSONObject.toJSONString(result));
        return ResponseUtil.ok(result);
    }

    /**
     * 用户收藏添加或删除
     * <p>
     * 如果商品没有收藏，则添加收藏；如果商品已经收藏，则删除收藏状态。
     *
     * @param userId 用户ID
     * @param body   请求内容，{ type: xxx, valueId: xxx }
     * @return 操作结果
     */
    @PostMapping("addordelete")
    public Object addordelete(@LoginUser Integer userId, @RequestBody String body) {
        log.info("【请求开始】用户收藏添加或删除,请求参数,userId:{},body:{}", userId, body);

        if (userId == null) {
            log.error("用户收藏添加或删除失败:用户未登录！！！");
            return ResponseUtil.unlogin();
        }

        Byte type = JacksonUtil.parseByte(body, "type");
        Integer valueId = JacksonUtil.parseInteger(body, "valueId");
        if (!ObjectUtils.allNotNull(type, valueId)) {
            return ResponseUtil.badArgument();
        }

        DtsCollect collect = collectService.queryByTypeAndValue(userId, type, valueId);

        String handleType = null;
        if (collect != null) {
            handleType = "delete";
            collectService.deleteById(collect.getId());
        } else {
            handleType = "add";
            collect = new DtsCollect();
            collect.setUserId(userId);
            collect.setValueId(valueId);
            collect.setType(type);
            collectService.add(collect);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("type", handleType);

        log.info("【请求结束】用户收藏添加或删除,响应结果：{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }
}