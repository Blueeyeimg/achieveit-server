package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectMember;

public interface ProjectMemberService {

    boolean addProjectMember(ProjectMember projectMember);

    boolean addProjectMember(String projectId, String userId, String role);

}
