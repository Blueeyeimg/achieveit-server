package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.EmployeeMapper;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.LoginService;
import com.ecnu.achieveit.util.LogUtil;
import com.ecnu.achieveit.util.Validate;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired(required = false)
    private EmployeeMapper employeeMapper;

    @Override
    public boolean login(String userIdOrEmail, String password) {

        Employee user = employeeMapper.selectByPrimaryKey(userIdOrEmail);
        if(user == null){
            LogUtil.i("user is not user id.");
            if(Validate.isEmail(userIdOrEmail)){
                user = employeeMapper.selectByEmail(userIdOrEmail);
            }else{
                LogUtil.i("user is not email.");
            }
        }
        if(user == null){
            LogUtil.i("user not found..");
            return false;
        }
        if(user.getPassword().equals(password)){
            LogUtil.i("login success.");
            return true;
        }
        LogUtil.i("password is not correct.");
        return false;
    }

    @Override
    public String signup(Employee employee) {

        Integer result = employeeMapper.insert(employee);
        if(result.equals(0)){
            return employee.getEmployeeId();
        }else{
            return null;
        }
    }
}
