package com.pc.business.mapper;

import com.pc.model.rlzy.user.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonnelInfoMapper {

    List<UserInfo> getPersonnelInfo();
}
