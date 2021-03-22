package com.pc.business.mapper;

import com.pc.business.model.permodel.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonnelInfoMapper {

    List<User> getPersonnelInfo();
}
