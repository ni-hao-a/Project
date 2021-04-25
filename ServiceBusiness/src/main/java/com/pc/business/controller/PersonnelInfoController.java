package com.pc.business.controller;

import com.pc.business.model.permodel.UserReq;
import com.pc.business.model.pub.ResponseBean;
import com.pc.business.service.PersonnelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 人员信息
 */
@RestController
@RequestMapping(value = "/pc/base/business/v1/rlzy", produces = {"application/json;charset=UTF-8"})
public class PersonnelInfoController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private PersonnelInfoService infoService;

    /**
     * 获取用户信息
     *
     * @return 用户信息列表
     */
    @RequestMapping(value = "/getPersonnelInfo", method = RequestMethod.POST)
    public ResponseBean getPersonnelInfo(@Valid @RequestBody UserReq req) {
        String token = request.getHeader("token");
        return infoService.getPersonnelInfo(token, req);
    }

}
