package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ProjectBasicInfoMapper;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public ProjectBasicInfo querryProjectByPrimaryKey(String projectId) {
        ProjectBasicInfo result = projectBasicInfoMapper.selectByPrimaryKey(projectId);
        return result;
    }

    @Override
    public List<ProjectBasicInfo> querryProjectByEmployeeId(String employeeId) {
        List<ProjectBasicInfo> result = projectBasicInfoMapper.selectByEmployeeId(employeeId);

        return result;
    }

    @Override
    public List<ProjectBasicInfo> querryProjectByClientId(String clientId) {
        List<ProjectBasicInfo> result = projectBasicInfoMapper.selectByClientId(clientId);

        return result;
    }

    @Override
    public List<ProjectBasicInfo> querryProjectByState(String state) {
        List<ProjectBasicInfo> result = projectBasicInfoMapper.selectByState(state);

        return result;
    }

    @Override
    public List<ProjectBasicInfo> querryProjectByBossId(String bossId) {
        List<ProjectBasicInfo> result = projectBasicInfoMapper.selectByBossId(bossId);

        return  result;
    }

    @Override
    public List<ProjectBasicInfo> querryProjectByKeyWord(String keyWord,String employeeId){
        List<ProjectBasicInfo> result = projectBasicInfoMapper.selectByKeyWord(keyWord,employeeId);

        return  result;
    }
}
