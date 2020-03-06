package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * This class corresponds to the database table activity
 */
@Getter
@Setter
@ToString
public class Activity implements Serializable {

    private Integer activityId;

    private String primaryActivity;

    private String secondaryActivity;

    private static final long serialVersionUID = 1L;

}