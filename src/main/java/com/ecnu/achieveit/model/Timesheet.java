package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table timesheet
 */
@Getter
@Setter
@ToString
public class Timesheet implements Serializable {

    private Integer timesheetId;

    private String projectId;

    private String employeeId;

    private String primaryFunction;

    private String secondaryFuntion;

    private String primaryActivity;

    private String secondaryActivity;

    private Date date;

    private Date startTime;

    private Date endTime;

    /**
     * Database Column Remarks:
     *   草稿、已提交、已确认、打回
     */
    private String state;

    private static final long serialVersionUID = 1L;

}