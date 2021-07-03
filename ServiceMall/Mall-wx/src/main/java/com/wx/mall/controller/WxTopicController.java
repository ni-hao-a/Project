package com.wx.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.pc.service.domain.DtsGoods;
import com.pc.service.domain.DtsTopic;
import com.pc.service.service.DtsGoodsService;
import com.pc.service.service.DtsTopicService;
import com.wx.core.util.ResponseUtil;
import com.wx.core.validator.Order;
import com.wx.core.validator.Sort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专题服务
 */
@RestController
@RequestMapping("/wx/topic")
@Validated
@Slf4j
public class WxTopicController {

    @Autowired
    private DtsTopicService topicService;
    @Autowired
    private DtsGoodsService goodsService;

    /**
     * 专题列表
     *
     * @param page 分页页数
     * @param size 分页大小
     * @return 专题列表
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        log.info("【请求开始】获取专题列表,请求参数,page:{},size:{}", page, size);

        List<DtsTopic> topicList = topicService.queryList(page, size, sort, order);
        int total = topicService.queryTotal();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", topicList);
        data.put("count", total);

        log.info("【请求结束】获取专题列表,响应结果:{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }

    /**
     * 专题详情
     *
     * @param id 专题ID
     * @return 专题详情
     */
    @GetMapping("detail")
    public Object detail(@NotNull Integer id) {
        log.info("【请求开始】获取专题详情,请求参数,id:{}", id);

        Map<String, Object> data = new HashMap<>();
        DtsTopic topic = topicService.findById(id);
        data.put("topic", topic);
        List<DtsGoods> goods = new ArrayList<>();
        for (String idOrSn : topic.getGoods()) {
            try {
                Long sn = Long.parseLong(idOrSn);
                DtsGoods good = null;
                if (sn.intValue() < Integer.MAX_VALUE) {
                    good = goodsService.findByIdVO(sn.intValue());
                }
                if (good == null) {//如果配置的不是id,则可能是SN
                    good = goodsService.findBySnVO(idOrSn);
                }
                if (null != good) goods.add(good);
            } catch (Exception e) {
                log.info("获取专题详情,根据配置的商品id或sn获取商品详情出错:{}", e.getMessage());
                e.printStackTrace();
            }
        }
        data.put("goods", goods);

        log.info("【请求结束】获取专题详情,响应结果:{}", "成功");
        return ResponseUtil.ok(data);
    }

    /**
     * 相关专题
     *
     * @param id 专题ID
     * @return 相关专题
     */
    @GetMapping("related")
    public Object related(@NotNull Integer id) {
        log.info("【请求开始】相关专题列表,请求参数,id:{}", id);

        List<DtsTopic> topicRelatedList = topicService.queryRelatedList(id, 0, 6);

        log.info("【请求结束】相关专题列表,响应结果:相关专题数{}", topicRelatedList == null ? 0 : topicRelatedList.size());
        return ResponseUtil.ok(topicRelatedList);
    }
}