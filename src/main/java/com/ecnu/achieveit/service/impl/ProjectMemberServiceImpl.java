package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ProjectMemberMapper;
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
}
