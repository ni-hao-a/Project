package com.wx.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.pc.service.domain.DtsBrand;
import com.pc.service.service.DtsBrandService;
import com.wx.core.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌供应商
 */
@RestController
@RequestMapping("/wx/brand")
@Validated
@Slf4j
public class WxBrandController {

    @Autowired
    private DtsBrandService brandService;

    /**
     * 品牌列表
     *
     * @param page 分页页数
     * @param size 分页大小
     * @return 品牌列表
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("【请求开始】品牌列表,请求参数,page:{},size:{}", page, size);
        List<DtsBrand> brandList = brandService.queryVO(page, size);
        int total = brandService.queryTotalCount();
        int totalPages = (int) Math.ceil((double) total / size);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("brandList", brandList);
        data.put("totalPages", totalPages);

        log.info("【请求结束】品牌列表,响应结果：{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }

    /**
     * 品牌详情
     *
     * @param id 品牌ID
     * @return 品牌详情
     */
    @GetMapping("detail")
    public Object detail(@NotNull Integer id) {
        log.info("【请求开始】品牌详情,请求参数,id:{}", id);
        DtsBrand entity = brandService.findById(id);
        if (entity == null) {
            log.error("品牌商获取失败,id:{}", id);
            return ResponseUtil.badArgumentValue();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("brand", entity);

        log.info("【请求结束】品牌详情,响应结果：{}", JSONObject.toJSONString(data));
        return ResponseUtil.ok(data);
    }
}