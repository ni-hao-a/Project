package com.pc.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.mapper.PersonnelInfoMapper;
import com.pc.business.service.PersonnelInfoService;
import com.pc.core.exception.ResponseCodeEnum;
import com.pc.core.model.ResponseBean;
import com.pc.core.utils.ResponseUtil;
import com.pc.model.rlzy.req.UserReq;
import com.pc.model.rlzy.user.UserInfo;
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
        List<UserInfo> userInfos = infoMapper.getPersonnelInfo();
        return ResponseUtil.success(new PageInfo<>(userInfos));
    }
}
