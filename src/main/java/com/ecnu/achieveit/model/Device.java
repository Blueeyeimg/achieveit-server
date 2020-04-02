package com.ecnu.achieveit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device implements Serializable {
    private String deviceId;

    private String type;

    private static final long serialVersionUID = 1L;

}