package com.pc.gateway.mapper;

import com.pc.gateway.model.route.SysAnonRoute;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysAnonRouteMapper {
    List<SysAnonRoute> getAnonRouteById(String sysId);

    List<SysAnonRoute> getAllAnonRoute();
}
