package com.ecnu.achieveit.model;

import lombok.*;

import java.io.Serializable;

/**
 * This class corresponds to the database table project_member
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    public ProjectMember(String projectId, String employeeId, String bossInProjectId, String role, String accessGit, String accessFileSystem, Integer inEmailList, Integer inTimesheetModule, Integer faultTrack) {
        super(projectId, employeeId);
        this.bossInProjectId = bossInProjectId;
        this.role = role;
        this.accessGit = accessGit;
        this.accessFileSystem = accessFileSystem;
        this.inEmailList = inEmailList;
        this.inTimesheetModule = inTimesheetModule;
        this.faultTrack = faultTrack;
    }

    public ProjectMember(String projectId, String employeeId, String bossInProjectId, String role, Integer inTimesheetModule, boolean level){
        super(projectId, employeeId);
        this.bossInProjectId = bossInProjectId;
        this.role = role;
        this.inTimesheetModule = inTimesheetModule;
        this.accessGit = level ? "W" : "R";
        this.accessFileSystem = level ? "W" : "R";
        this.inEmailList = level ? 1 : 0;
        this.faultTrack = level ? 1 : 0;
    }

}