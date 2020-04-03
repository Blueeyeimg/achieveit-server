package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectId;

import java.util.List;

public interface ProjectIdService {
    boolean deleteProjectId(String projectId);

    boolean addProjectId(String projectId);

    boolean updateProjectId(String oldProjectId,String newProjectId);

    List<ProjectId> queryProjectIds();
}
