package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * This class corresponds to the database table client
 */
@Getter
@Setter
@ToString
public class Client implements Serializable {

    private String clientId;

    private String contacter;

    private String companyName;

    private Integer clientLevel;

    private String email;

    private String telephone;

    private String address;

    private static final long serialVersionUID = 1L;

}