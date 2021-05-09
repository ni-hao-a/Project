package com.pc.business.service;

import com.pc.model.rlzy.req.UserReq;
import com.pc.core.model.ResponseBean;

public interface PersonnelInfoService {

    ResponseBean getPersonnelInfo(String token, UserReq req);
}
