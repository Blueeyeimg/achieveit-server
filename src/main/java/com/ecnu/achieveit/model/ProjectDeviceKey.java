package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * This class corresponds to the database table project_device
 */
@Getter
@Setter
@ToString
public class ProjectDeviceKey implements Serializable {

    private String projectId;

    private String deviceId;

    private static final long serialVersionUID = 1L;

}