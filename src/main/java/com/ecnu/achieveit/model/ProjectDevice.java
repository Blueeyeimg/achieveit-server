package com.ecnu.achieveit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date checkinDate;

    /**
     * Database Column Remarks:
     *   以月为单位
     */
    private Integer totalUseTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date lastVerifyDate;

    /**
     * Database Column Remarks:
     *   当state为1时使用该变量
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
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

    public boolean valid(){
        return getProjectId() != null
                && getDeviceId() != null
                && type != null
                && deviceManagerId != null
                && totalUseTime != null;
    }

}