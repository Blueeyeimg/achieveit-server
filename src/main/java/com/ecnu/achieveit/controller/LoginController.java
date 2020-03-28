package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.RestCode;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.modelview.LoginView;
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

        Employee userObject = loginService.login(user, password);
        if(userObject == null){
            return RestResponse.fail("user name or password is not correct.");
        }

        String token = jwtConfig.getToken(userObject.getEmployeeId());
        if (StringUtils.isEmpty(token)) {
            return RestResponse.fail("Due to unknow reason, can not create token for this user.");
        }

        userObject.setPassword("");
        return RestResponse.success(new LoginView(token,userObject));
    }


    @PostMapping("/info")
    public Object info (){
        return RestResponse.success("Pass the authrization.");
    }
}
