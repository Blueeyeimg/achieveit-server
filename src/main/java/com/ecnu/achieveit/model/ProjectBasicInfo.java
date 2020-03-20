package com.ecnu.achieveit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table project_basic_info
 */
@Getter
@Setter
@ToString
public class ProjectBasicInfo implements Serializable {

    private String projectId;

    private String projectName;

    private String clientId;

    private String state;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date expStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date expEndDate;

    private String projectBossId;

    private String milestone;

    private String technology;

    private String businessDomain;

    private String mainFunctions;

    private String outputLink;

    private String gitAddress;

    private String fileSystemAddress;

    private static final long serialVersionUID = 1L;

}