package com.pc.gateway.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.gateway.service.ISysLoginInfoService;
import com.pc.model.rlzy.entity.SysLogininfor;
import com.pc.model.rlzy.req.LoginOpeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/gateway/v1/rlzy/loginrecord")
public class SysLoginInfoController {
    @Autowired
    private ISysLoginInfoService loginInfoService;

    /**
     * 获取登录记录
     */
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @RequestMapping(value = "/getRecordList", method = RequestMethod.POST)
    public ResponseBean list(@RequestBody SysLogininfor req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysLogininfor> list = loginInfoService.selectLogininforList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 删除登录记录
     */
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @RequestMapping(value = "/delRecord", method = RequestMethod.POST)
    public ResponseBean remove(@RequestBody LoginOpeReq req) {
        return ResponseUtil.success(loginInfoService.deleteLogininforByIds(req.getInfoIds()));
    }

    /**
     * 清空登录日志
     */
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @RequestMapping(value = "/clean", method = RequestMethod.POST)
    public ResponseBean clean() {
        loginInfoService.cleanLogininfor();
        return ResponseUtil.success();
    }

    /**
     * 导出用户登录记录
     */
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public ResponseBean export(SysLogininfor logininfor) {
        List<SysLogininfor> list = loginInfoService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        return util.exportExcel(list, "登录日志");
    }
}
