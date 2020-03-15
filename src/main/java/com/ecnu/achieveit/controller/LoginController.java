package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.RestCode;
import com.ecnu.achieveit.service.LoginService;
import com.ecnu.achieveit.util.JwtConfig;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private JwtConfig jwtConfig ;
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Object login (@RequestParam("user") String user,
                                     @RequestParam("password") String password){
        Map<String,String> result = new HashMap<>();

        boolean loginValid = loginService.login(user, password);
        if(!loginValid){
            return RestResponse.fail("user name or password is not correct.");
        }
        String token = jwtConfig.getToken(user+password);
        if (StringUtils.isEmpty(token)) {
            return RestResponse.fail("Due to unknow reason, can not create token for this user.");
        }
        result.put("token",token);
        result.put("userName",user);

        return RestResponse.success(result);
    }


    @PostMapping("/info")
    public Object info (){
        return RestResponse.success("Pass the authrization.");
    }
}
