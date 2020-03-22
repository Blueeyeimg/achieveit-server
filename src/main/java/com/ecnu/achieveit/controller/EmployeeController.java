package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public Object list(){

        Object result = employeeService.queryEmployees();
        if(result == null){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @GetMapping("/employee/{id}")
    public Object show(@PathVariable("id") String id){

        Object result = employeeService.queryEmployeeById(id);
        if(result == null){
            return RestResponse.fail("");
        }
        return RestResponse.success(result);
    }

    @PostMapping("/employee")
    public Object add(Employee employee){

        Object result = employeeService.addEmployee(employee);
        if(result.equals(0)){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @PutMapping("/employee")
    public Object modify(Employee employee){
        Object result = employeeService.updateEmployee(employee);
        if(result.equals(0)){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }

    @DeleteMapping("/employee/{id}")
    public Object remove(@PathVariable("id") String id){
        Object result = employeeService.deleteEmployeeById(id);
        if(result.equals(0)){
            return RestResponse.fail();
        }
        return RestResponse.success(result);
    }
}
