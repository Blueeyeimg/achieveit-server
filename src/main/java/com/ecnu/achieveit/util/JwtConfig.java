package com.ecnu.achieveit.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
public class JwtConfig {

    private final String secret = "iwqjhda8232bjgh432[cicada-smile]";
    private final long expire = 3600;
    private final String header = "token";


    /**
     * @description: 根据身份信息生成token
     */
    public String getToken (String identityId){
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(identityId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    /**
     * @description: 获取token中的信息
     */
    public Claims getTokenClaim (String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @description: 检验token是否过期
     */
    public boolean isTokenExpired (Date expirationTime) {
        return expirationTime.before(new Date());
    }

}
