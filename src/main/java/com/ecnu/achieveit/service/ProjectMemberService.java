package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;

import java.util.List;

public interface ProjectMemberService {

    boolean addProjectMember(ProjectMember projectMember);

    boolean addProjectMember(String projectId, String userId, String bossInProjectId, String role);

    boolean modifyMemberPermission(ProjectMember projectMember);

    List<ProjectMember> queryMemberList(String projectId);

    ProjectMember queryMemberByKey(ProjectMemberKey projectMemberKey);

    boolean deleteMemberByKey(ProjectMemberKey projectMemberKey);

    List<ProjectMember> queryMemberByRole(String projectId, String role);

    List<Employee> queryAddibleEmployees(String projectId);
}
