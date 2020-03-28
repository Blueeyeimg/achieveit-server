package com.ecnu.achieveit.modelview;

import com.ecnu.achieveit.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginView implements Serializable {

    private String token;

    private Employee user;

    private static final long serialVersionUID = 1L;
}
