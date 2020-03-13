package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.EmployeeMapper;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
}
