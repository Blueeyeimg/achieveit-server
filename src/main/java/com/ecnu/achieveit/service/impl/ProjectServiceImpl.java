package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ProjectBasicInfoMapper;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired(required = false)
    ProjectBasicInfoMapper projectBasicInfoMapper;

    @Override
    public boolean addProject(ProjectBasicInfo projectBasicInfo) {
        Integer result = projectBasicInfoMapper.insertSelective(projectBasicInfo);

        return !result.equals(0);
    }

    @Override
    public boolean updateProject(ProjectBasicInfo projectBasicInfo) {
        Integer result = projectBasicInfoMapper.updateByPrimaryKeySelective(projectBasicInfo);

        return !result.equals(0);
    }
}
