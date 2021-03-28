package com.pc.business.service;

import com.pc.business.model.permodel.UserReq;
import com.pc.business.model.pub.ResponseBean;

public interface PersonnelInfoService {

    ResponseBean getPersonnelInfo(String token, UserReq req);
}
