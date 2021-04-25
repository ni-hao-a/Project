package com.pc.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.exception.ResponseCodeEnum;
import com.pc.business.mapper.PersonnelInfoMapper;
import com.pc.business.model.permodel.User;
import com.pc.business.model.permodel.UserReq;
import com.pc.business.model.pub.ResponseBean;
import com.pc.business.service.PersonnelInfoService;
import com.pc.business.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PersonnelInfoServiceImpl implements PersonnelInfoService {

    @Autowired
    private PersonnelInfoMapper infoMapper;

    @Override
    public ResponseBean getPersonnelInfo(String token, UserReq req) {
        if (StringUtils.isEmpty(req.getName())) {
            log.error(ResponseCodeEnum.USER_NAME_IS_EMPTY.getMessage());
            return ResponseUtil.error(ResponseCodeEnum.USER_NAME_IS_EMPTY);
        }
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<User> users = infoMapper.getPersonnelInfo();
        return ResponseUtil.success(new PageInfo<>(users));
    }
}
