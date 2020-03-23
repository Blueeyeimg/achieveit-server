package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectBasicInfo;

import java.util.List;

public interface ProjectService {
    boolean addProject(ProjectBasicInfo projectBasicInfo);

    boolean updateProject(ProjectBasicInfo projectBasicInfo);

    ProjectBasicInfo querryProjectByPrimaryKey(String projectId);

    List<ProjectBasicInfo> querryProjectByEmployeeId(String employeeId);

    List<ProjectBasicInfo> querryProjectByClientId(String clientId);

    List<ProjectBasicInfo> querryProjectByState(String state);

    List<ProjectBasicInfo> querryProjectByBossId(String BossId);

}
