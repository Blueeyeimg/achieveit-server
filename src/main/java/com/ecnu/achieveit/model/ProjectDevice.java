package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table project_device
 */
@Getter
@Setter
@ToString
public class ProjectDevice extends ProjectDeviceKey implements Serializable {

    private String type;

    /**
     * Database Column Remarks:
     *   0：未归还，1：已归还
     */
    private Integer state;

    private String deviceManagerId;

    private Date checkinDate;

    /**
     * Database Column Remarks:
     *   以月为单位
     */
    private Integer totalUseTime;

    private Date lastVerifyDate;

    /**
     * Database Column Remarks:
     *   当state为1时使用该变量
     */
    private Date returnDate;

    private static final long serialVersionUID = 1L;

}