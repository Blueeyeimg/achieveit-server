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
}
