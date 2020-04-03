package com.ecnu.achieveit.service;

import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.model.AssetItem;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.model.ProjectId;

import java.util.List;

public interface ProjectService {
    boolean addProject(ProjectBasicInfo projectBasicInfo);

    boolean updateProject(ProjectBasicInfo projectBasicInfo);

    boolean updateProjectState(ProjectBasicInfo projectBasicInfo,String projectState);

    boolean updateProjectStateById(String projectId, String projectState);

    ProjectBasicInfo querryProjectByPrimaryKey(String projectId);

    List<ProjectBasicInfo> querryProjectByEmployeeId(String employeeId);

    List<ProjectBasicInfo> querryProjectByClientId(String clientId);

    List<ProjectBasicInfo> querryProjectByState(String state);

    List<ProjectBasicInfo> querryProjectByBossId(String bossId);

    List<ProjectBasicInfo> querryProjectByKeyWord(String keyWord,String employeeId);

    List<AssetItem> querryAssetItem();

}
