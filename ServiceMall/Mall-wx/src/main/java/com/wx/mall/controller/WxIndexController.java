package com.wx.mall.controller;

import com.wx.core.util.ResponseUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试服务
 */
@RestController
@RequestMapping("/wx/index")
public class WxIndexController {

    /**
     * 测试数据
     *
     * @return 测试数据
     */
    @RequestMapping("/index")
    public Object index() {
        return ResponseUtil.ok("hello dts");
    }

}