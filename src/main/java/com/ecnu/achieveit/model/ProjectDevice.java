package com.ecnu.achieveit.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table project_device
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    public ProjectDevice(String projectId, String deviceId, String type, Integer state, String deviceManagerId, Date checkinDate, Integer totalUseTime, Date lastVerifyDate, Date returnDate) {
        super(projectId, deviceId);
        this.type = type;
        this.state = state;
        this.deviceManagerId = deviceManagerId;
        this.checkinDate = checkinDate;
        this.totalUseTime = totalUseTime;
        this.lastVerifyDate = lastVerifyDate;
        this.returnDate = returnDate;
    }

}