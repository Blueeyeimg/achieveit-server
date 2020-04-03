package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectFunctionKey;
import com.ecnu.achieveit.modelview.FunctionView;

import java.util.List;

public interface ProjectFunctionService {

    boolean addProjectFunction(FunctionView functionView);

    boolean updateProjectFunction(FunctionView functionView);

    FunctionView queryProjectFunctionView(String projectId);

    List<ProjectFunctionKey> queryProjectFunction(String projectId);

}
