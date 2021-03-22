package com.pc.business.service;

import com.github.pagehelper.PageInfo;
import com.pc.business.model.permodel.User;
import com.pc.business.model.permodel.UserReq;

public interface PersonnelInfoService {

    PageInfo<User> getPersonnelInfo(String token, UserReq req);
}
