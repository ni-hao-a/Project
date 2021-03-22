package com.pc.gateway.mapper;

import com.pc.gateway.bean.UserInfoBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface LoginMapper {

    int getUserCount(@Param("userName") String userName, @Param("password") String password);

    UserInfoBean getUserInfo(@Param("userName") String userName);
}
