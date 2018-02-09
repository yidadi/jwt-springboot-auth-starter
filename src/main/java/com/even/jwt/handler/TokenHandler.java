package com.even.jwt.handler;

import com.even.jwt.jwt.UserService;
import com.even.jwt.vo.UserVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class TokenHandler {

    @Value("${com.even.secret}")
    private String secret;

    @Value("${com.even.expire.second}")
    private String expireTime;

    @Autowired
    private UserService userService;

    /**
     * 通过token 解析出用户电话 并查询用户信息
     *
     * @param token
     * @return
     */
    public UserVo parseToken(String token) {
        String mobile = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        if (StringUtils.isEmpty(mobile)) {
            return null;
        }
        return userService.getUserVoByMobile(mobile);
    }

    /**
     * 通过传入的用户信息生成token
     *
     * @param userVo
     * @return
     */
    public String createToken(UserVo userVo) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TimeUnit.SECONDS.toMillis(Long.parseLong(expireTime)));
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userVo.getMobile())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
