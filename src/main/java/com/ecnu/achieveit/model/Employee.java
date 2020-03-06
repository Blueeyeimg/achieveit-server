package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * This class corresponds to the database table employee
 * @author 倪事通
 */
@Getter
@Setter
@ToString
public class Employee implements Serializable {

    private String employeeId;

    private String employeeName;

    private String password;

    private String email;

    private String department;

    private String telephone;

    private String title;

    private static final long serialVersionUID = 1L;

}