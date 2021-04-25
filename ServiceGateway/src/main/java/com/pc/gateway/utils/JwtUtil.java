package com.pc.gateway.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pc.gateway.bean.UserInfoBean;
import com.pc.gateway.config.SystemConfig;
import com.pc.gateway.mapper.LoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtUtil {

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private LoginMapper loginMapper;

    /**
     * 生成Token
     *
     * @param userId id
     * @return token
     * @throws Exception e
     */
    public String createToken(String userId) throws Exception {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, systemConfig.getExpireTime());
        Date expireDate = nowTime.getTime();

        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        String token = JWT.create()
                .withHeader(map)//头
                .withClaim("userId", userId)
                .withSubject("测试")//
                .withIssuedAt(new Date())//签名时间
                .withExpiresAt(expireDate)//过期时间
                .sign(Algorithm.HMAC256(systemConfig.getSecret()));//签名
        return token;
    }

    /**
     * 验证Token
     *
     * @param token 凭证
     * @return map or null
     */
    public Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(systemConfig.getSecret())).build();
            jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
            log.error("凭证已过期，请重新登录", e);
        }
        return null;
    }

    /**
     * 解析Token
     *
     * @param token 凭证
     * @return map
     */
    public Map<String, Claim> parseToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims();
    }

    public UserInfoBean getUserInfo(String token) {
        if (StringUtils.isNotEmpty(token)) {
            Map<String, Claim> map = parseToken(token);
            String userId = map.get("userId").asString();
            if (StringUtils.isNotEmpty(userId)) {
                return loginMapper.getUserInfo(userId);
            }
        }
        return null;

    }


}
