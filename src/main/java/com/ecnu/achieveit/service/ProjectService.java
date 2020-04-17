package com.ecnu.achieveit.service;

import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.model.AssetItem;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.model.ProjectId;
import com.ecnu.achieveit.model.ProjectMemberKey;

import java.util.List;

public interface ProjectService {
    boolean addProject(ProjectBasicInfo projectBasicInfo);

    boolean updateProject(ProjectBasicInfo projectBasicInfo);

    boolean updateProjectState(ProjectBasicInfo projectBasicInfo,String projectState);

    boolean updateProjectStateById(String projectId, String projectState);

    boolean updateOutputLinkOfProjectInfo(String projectId,String outputLink);

    boolean deleteProjectMemberByKey(ProjectMemberKey projectMemberKey);

    boolean deleteProjectInfoByKey(String projectId);

    ProjectBasicInfo querryProjectByPrimaryKey(String projectId);

    List<ProjectBasicInfo> queryProjectByEmployeeId(String employeeId);

    List<ProjectBasicInfo> queryProjectByClientId(String clientId);

    List<ProjectBasicInfo> queryProjectByState(String state);

    List<ProjectBasicInfo> queryProjectByBossId(String bossId);

    List<ProjectBasicInfo> queryProjectByKeyWord(String keyWord,String employeeId);

    List<AssetItem> queryAssetItem();

}
