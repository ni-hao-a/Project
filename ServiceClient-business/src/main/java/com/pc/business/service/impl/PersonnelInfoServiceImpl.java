package com.pc.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.business.mapper.PersonnelInfoMapper;
import com.pc.business.model.permodel.User;
import com.pc.business.model.permodel.UserReq;
import com.pc.business.service.PersonnelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnelInfoServiceImpl implements PersonnelInfoService {

    @Autowired
    private PersonnelInfoMapper infoMapper;

    @Override
    public PageInfo<User> getPersonnelInfo(String token, UserReq req) {
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<User> users = infoMapper.getPersonnelInfo();
        return new PageInfo<>(users);
    }
}
