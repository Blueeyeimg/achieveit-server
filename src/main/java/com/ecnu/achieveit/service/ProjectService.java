package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectBasicInfo;

public interface ProjectService {
    boolean addProject(ProjectBasicInfo projectBasicInfo);

    boolean updateProject(ProjectBasicInfo projectBasicInfo);

}
