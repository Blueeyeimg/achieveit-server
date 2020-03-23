package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.dao.ProjectMemberMapper;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired(required = false)
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public boolean addProjectMember(ProjectMember projectMember) {
        Integer result = projectMemberMapper.insertSelective(projectMember);

        return result.equals(1);
    }

    @Override
    public boolean addProjectMember(String projectId, String userId, String role) {
        Integer inTimSheetModule = 1;
        boolean level = false;
        if(role.equals(ProjectRole.MANAGER.getRole())){
            level = true;
        }
        if(role.equals(ProjectRole.EPG.getRole())){
            inTimSheetModule = 0;
        }
        ProjectMember projectMember = new ProjectMember(projectId, userId, userId, role, inTimSheetModule, level);

        return addProjectMember(projectMember);
    }
}
