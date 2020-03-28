package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ProjectIdMapper;
import com.ecnu.achieveit.model.ProjectId;
import com.ecnu.achieveit.service.ProjectIdService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectIdServiceImpl implements ProjectIdService {
    @Autowired(required = false)
    private ProjectIdMapper projectIdMapper;

    @Override
    public List<ProjectId> querryProjectIds() {
        return projectIdMapper.selectAll();
    }

    @Override
    public boolean addProjectId(String projectId) {
        if(projectIdMapper.insert(new ProjectId(projectId)) > 0) return true;
        return false;
    }

    @Override
    public boolean deleteProjectId(String projectId) {
        if(projectIdMapper.deleteByPrimaryKey(projectId)>0) return true;
        return false;
    }

    @Override
    public boolean updateProjectId(String oldProjectId,
                                    String newProjectId) {
        if(projectIdMapper.updateProjectId(oldProjectId,newProjectId)>0)return true;
        return false;
    }
}
