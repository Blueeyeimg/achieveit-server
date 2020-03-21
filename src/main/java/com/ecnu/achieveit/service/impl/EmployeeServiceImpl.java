package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.EmployeeMapper;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.util.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import javax.annotation.Resource;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired(required = false)
    private EmployeeMapper employeeMapper;

    @Override
    public boolean deleteEmployeeById(String id) {

        Integer result = employeeMapper.deleteByPrimaryKey(id);

        return !result.equals(0);
    }

    @Override
    public boolean addEmployee(Employee employee) {

        Integer result = employeeMapper.insertSelective(employee);

        return !result.equals(0);
    }

    @Override
    public boolean updateEmployee(Employee employee) {

        Integer result = employeeMapper.updateByPrimaryKey(employee);

        return !result.equals(0);
    }

    @Override
    public List<Employee> queryEmployees() {

        return employeeMapper.selectEmployeeList();
    }

    @Override
    public Employee queryEmployeeById(String id) {

        return employeeMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Employee> queryBasicEmployees() {
        return employeeMapper.selectBasicEmployeeList();
    }

    @Override
    public Employee queryBasicEmployeeById(String id) {
        return employeeMapper.selectBasicByPrimaryKey(id);
    }

    @Override
    public Employee queryBasicEmployeeByEmail(String email) {
        return employeeMapper.selectBasicByEmail(email);
    }

    @Override
    public boolean checkTitle(String userId, String title) {
        Employee employee = queryBasicEmployeeById(userId);
        if(employee == null){
            return false;
        }
        return title.equals(employee.getTitle());
    }

    @Override
    public Employee queryBasicEmployeeByIdOrEmail(String idOrEmail) {
        Employee employee;
        if(Validate.isEmail(idOrEmail)){
            employee = queryBasicEmployeeByEmail(idOrEmail);
        }else{
            employee = queryBasicEmployeeById(idOrEmail);
        }
        return employee;
    }

    @Override
    public List<Employee> queryBasicEmployeeGroup(String group) {
        return null;
    }
}
