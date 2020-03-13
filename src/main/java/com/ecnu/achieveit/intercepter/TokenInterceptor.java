package com.ecnu.achieveit.intercepter;

import com.ecnu.achieveit.util.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private JwtConfig jwtConfig ;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 地址过滤
        String uri = request.getRequestURI() ;
        if (uri.contains("/login")){
            return true ;
        }
        // Token 验证
        String token = request.getHeader("Authorization");

        if(StringUtils.isEmpty(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }else if(token.startsWith("Bearer")){
            token = token.substring(7);
        }else{
            throw new Exception("Authorization" + "不以Bearer开头。");
        }
        if(StringUtils.isEmpty(token)){
            throw new Exception(jwtConfig.getHeader()+ "不能为空。");
        }
        Claims claims = jwtConfig.getTokenClaim(token);
        if(claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
            throw new Exception(jwtConfig.getHeader() + "失效，请重新登录。");
        }
        //设置 identityId 用户身份ID
        request.setAttribute("identityId", claims.getSubject());
        return true;
    }
}
