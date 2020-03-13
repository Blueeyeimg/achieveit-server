package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.Employee;

/**
 * @author 倪事通
 */
public interface LoginService {

    boolean login(String userIdOrEmail, String password);

    /**
     *
     * @param employee
     * @return the user id.
     */
    String signup(Employee employee);
}
