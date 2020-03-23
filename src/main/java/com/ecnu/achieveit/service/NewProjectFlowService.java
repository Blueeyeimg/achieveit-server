package com.ecnu.achieveit.service;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.modelview.ProjectBasicInfoView;

import java.util.List;

public interface NewProjectFlowService {

    String startProcess();

    String createProject(String userId, ProjectBasicInfo projectBasicInfo, String instanceId);

    List<ProjectBasicInfoView> getAppliedProjects(String userId);

    boolean approveProject(String taskId, String userId);

    boolean disapproveProject(String taskId, String userId);

    boolean sendApprovedEmail(String projectManagerId, ProjectBasicInfo projectBasicInfo);

    boolean sendDisapprovedEmail(String projectManagerId, ProjectBasicInfo projectBasicInfo);

    boolean setProjectConfig(String taskId, String userId, ProjectBasicInfo projectBasicInfo);

    boolean setProjectQaOrEpg(String taskId, String userId, String role, String... ids);

    boolean sendConfigEmail(String projectManagerId, String projectName, String configId);

    boolean sendSetMemberEmail(String projectManagerId, String projectName, String leaderId, String employeeTitle);

}
