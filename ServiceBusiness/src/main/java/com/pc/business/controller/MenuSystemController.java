package com.pc.business.controller;

import com.pc.business.model.pub.ResponseBean;
import com.pc.business.utils.ResponseUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 菜单系统
 */
@RestController
@RequestMapping(value = "/pc/base/business/v1/rlzy/menu", produces = {"application/json;charset=UTF-8"})
public class MenuSystemController {

    /**
     * 获取用户信息
     *
     * @return 用户信息列表
     */
    @RequestMapping(value = "/getInfo", method = RequestMethod.POST)
    public ResponseBean getPersonnelInfo() {
        Map map = new HashMap();
        map.put("permissions",null);
        map.put("roles",null);
        map.put("user",null);
        ResponseBean resp = new ResponseBean();
        resp.setData(map);
        return ResponseUtil.success(resp);
    }
}
