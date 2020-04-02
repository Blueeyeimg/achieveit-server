package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ProjectFunctionMapper;
import com.ecnu.achieveit.model.ProjectFunctionKey;
import com.ecnu.achieveit.modelview.FunctionItem;
import com.ecnu.achieveit.modelview.FunctionView;
import com.ecnu.achieveit.service.ProjectFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectFunctionServiceImpl implements ProjectFunctionService {

    @Autowired(required = false)
    private ProjectFunctionMapper projectFunctionMapper;

    @Override
    public boolean addProjectFunction(FunctionView functionView) {

        FunctionView oldFunctionView = queryProjectFunctionView(functionView.getProjectId());
        if(!ObjectUtils.isEmpty(oldFunctionView.getFunctions())){
            return false;
        }

        List<ProjectFunctionKey> functionKeys = FunctionView.functionViewToList(functionView);

        if(ObjectUtils.isEmpty(functionKeys)){
            return false;
        }

        for(ProjectFunctionKey functionKey : functionKeys){
            projectFunctionMapper.insert(functionKey);
        }

        return true;
    }

    @Override
    public boolean updateProjectFunction(FunctionView functionView) {
        List<ProjectFunctionKey> functionKeys = FunctionView.functionViewToList(functionView);

        if(ObjectUtils.isEmpty(functionKeys)){
            return false;
        }

        List<ProjectFunctionKey> oldFunctionKeys = queryProjectFunction(functionKeys.get(0).getProjectId());

        //删除old中多余的Function
        for(ProjectFunctionKey oldFunctionKey :oldFunctionKeys){
            if(!functionKeys.contains(oldFunctionKey)){
                projectFunctionMapper.deleteByPrimaryKey(oldFunctionKey);
            }
        }

        //添加新的Function
        for(ProjectFunctionKey functionKey :functionKeys){
            if(!oldFunctionKeys.contains(functionKey)){
                projectFunctionMapper.insert(functionKey);
            }
        }
        return true;
    }

    @Override
    public FunctionView queryProjectFunctionView(String projectId) {
        return FunctionView.functionListToView(
                projectFunctionMapper.selectListByProjectId(projectId));
    }

    @Override
    public List<ProjectFunctionKey> queryProjectFunction(String projectId) {
        return projectFunctionMapper.selectListByProjectId(projectId);
    }
}
