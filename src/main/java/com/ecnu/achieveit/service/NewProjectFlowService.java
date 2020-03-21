package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.modelview.ProjectBasicInfoView;

import java.util.List;

public interface NewProjectFlowService {

    String startProcess();

    String createPrject(String userId, ProjectBasicInfo projectBasicInfo, String instanceId);

    List<ProjectBasicInfoView> getAppliedProjects(String userId);

    boolean approveProject(String taskId, String userId);

    boolean disapproveProject(String taskId, String userId);

    boolean sendApprovedEmail(String projectManagerId, ProjectBasicInfo projectBasicInfo);

    boolean sendDisapprovedEmail(String projectManagerId, ProjectBasicInfo projectBasicInfo);
}
