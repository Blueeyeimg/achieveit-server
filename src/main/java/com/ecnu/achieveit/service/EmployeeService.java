package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.Employee;

import java.util.List;

public interface EmployeeService {
    /**
     * @return true: success, false: delete failed
     */
    boolean deleteEmployeeById(String id);

    boolean addEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    List<Employee> queryEmployees();

    Employee queryEmployeeById(String id);

    List<Employee> queryBasicEmployees();

    Employee queryBasicEmployeeById(String id);

    Employee queryBasicEmployeeByEmail(String email);

    boolean checkTitle(String userId,String title);

    Employee queryBasicEmployeeByIdOrEmail(String idOrEmail);

    List<Employee> queryBasicEmployeeGroup(String group);

}
