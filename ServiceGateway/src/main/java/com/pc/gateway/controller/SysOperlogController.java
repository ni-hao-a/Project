package com.pc.gateway.controller;

import com.github.pagehelper.PageHelper;
import com.pc.core.model.ResponseBean;
import com.pc.core.poi.ExcelUtil;
import com.pc.core.utils.ResponseUtil;
import com.pc.gateway.service.ISysOperLogService;
import com.pc.model.rlzy.entity.SysOperLog;
import com.pc.model.rlzy.req.OpeLogReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/pc/base/gateway/v1/operlog")
public class SysOperlogController {
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @RequestMapping(value = "/opeLogList", method = RequestMethod.POST)
    public ResponseBean list(@RequestBody SysOperLog req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysOperLog> list = operLogService.selectOperLogList(req);
        return ResponseUtil.success(list);
    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @RequestMapping(value = "/delLog", method = RequestMethod.POST)
    public ResponseBean remove(@PathVariable OpeLogReq req) {
        return ResponseUtil.success(operLogService.deleteOperLogByIds(req.getOperIds()));
    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @RequestMapping(value = "/clean", method = RequestMethod.POST)
    public ResponseBean clean() {
        operLogService.cleanOperLog();
        return ResponseUtil.success();
    }

    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public ResponseBean export(SysOperLog operLog) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }
}
