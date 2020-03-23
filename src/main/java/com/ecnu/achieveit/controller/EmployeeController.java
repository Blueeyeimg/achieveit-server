package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public Object list(){

        List<Employee> result = employeeService.queryEmployees();
        if(result == null){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @GetMapping("/employee/{id}")
    public Object show(@PathVariable("id") String id){

        Employee result = employeeService.queryEmployeeById(id);
        if(result == null){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @PostMapping("/employee")
    public Object add(Employee employee){

        boolean result = employeeService.addEmployee(employee);
        if(!result){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @PutMapping("/employee")
    public Object modify(Employee employee){
        boolean result = employeeService.updateEmployee(employee);
        if(!result){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @DeleteMapping("/employee/{id}")
    public Object remove(@PathVariable("id") String id){
        boolean result = employeeService.deleteEmployeeById(id);
        if(!result){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @GetMapping("/employees/{group}")
    public Object listGroup(@PathVariable("group") String group){
        List<Employee> result = employeeService.queryBasicEmployeeGroup(group);
        if(result == null){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }
}
