package com.pc.business.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.service.ISysNoticeService;
import com.pc.business.utils.feignService.RSysUserFeignService;
import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.model.rlzy.entity.SysNotice;
import com.pc.model.rlzy.req.NoticeReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author qhl
 */
@RestController
@RequestMapping("/pc/base/business/v1/rlzy/notice")
public class SysNoticeController {
    @Autowired
    private ISysNoticeService noticeService;
    @Autowired
    private RSysUserFeignService userFeignService;

    /**
     * 获取通知公告列表
     */
    @RequestMapping(value = "/getNoticeList", method = RequestMethod.POST)
    public ResponseBean getNoticeList(@RequestBody SysNotice req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<SysNotice> list = noticeService.selectNoticeList(req);
        return ResponseUtil.success(new PageInfo<>(list));
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @RequestMapping(value = "/getNoticeInfoById", method = RequestMethod.POST)
    public ResponseBean getNoticeInfoById(@RequestBody NoticeReq req) {
        return ResponseUtil.success(noticeService.selectNoticeById(req.getNoticeId()));
    }

    /**
     * 新增通知公告
     */
    @RequestMapping(value = "/addNotice", method = RequestMethod.POST)
    public ResponseBean addNotice(@Valid @RequestBody SysNotice req) {
        req.setCreateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(noticeService.insertNotice(req));
    }

    /**
     * 修改通知公告
     */
    @RequestMapping(value = "/updateNotice", method = RequestMethod.POST)
    public ResponseBean updateNotice(@Valid @RequestBody SysNotice req) {
        req.setUpdateBy(userFeignService.getUser().getUserName());
        return ResponseUtil.success(noticeService.updateNotice(req));
    }

    /**
     * 删除通知公告
     */
    @RequestMapping(value = "/delNotice", method = RequestMethod.POST)
    public ResponseBean delNotice(@Valid @RequestBody NoticeReq req) {
        return ResponseUtil.success(noticeService.deleteNoticeByIds(req.getNoticeIds()));
    }
}
