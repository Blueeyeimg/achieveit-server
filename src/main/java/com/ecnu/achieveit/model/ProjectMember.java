package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * This class corresponds to the database table project_member
 */
@Getter
@Setter
@ToString
public class ProjectMember extends ProjectMemberKey implements Serializable {

    private String bossInProjectId;

    /**
     * Database Column Remarks:
     *   可选择多个role，用；分割
     */
    private String role;

    private String accessGit;

    private String accessFileSystem;

    private Integer inEmailList;

    private Integer inTimesheetModule;

    private Integer faultTrack;

    private static final long serialVersionUID = 1L;

}