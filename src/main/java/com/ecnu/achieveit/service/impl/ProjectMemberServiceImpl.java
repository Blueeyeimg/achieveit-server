package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.dao.ProjectMemberMapper;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired(required = false)
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public boolean addProjectMember(ProjectMember projectMember) {

        if(updatePermissionToServer(projectMember)){
            return projectMemberMapper.insertSelective(projectMember) == 1;
        }

        return false;
    }

    @Override
    public boolean addProjectMember(String projectId, String userId, String bossInProjectId, String role) {
        Integer inTimSheetModule = 1;

        if(role.equals(ProjectRole.EPG.getRole())){
            inTimSheetModule = 0;
        }
        LogUtil.i("member create start");
        ProjectMember projectMember = new ProjectMember(projectId, userId, bossInProjectId, role, inTimSheetModule, false);

        LogUtil.i("member create end");
        return addProjectMember(projectMember);
    }

    @Override
    public boolean modifyMemberPermission(ProjectMember projectMember) {
        if(updatePermissionToServer(projectMember)){
            return projectMemberMapper.updateByPrimaryKeySelective(projectMember) == 1;
        }

        return false;
    }

    @Override
    public List<ProjectMember> queryMemberList(String projectId) {
        return projectMemberMapper.queryListByProjectId(projectId);
    }

    @Override
    public ProjectMember queryMemberByKey(ProjectMemberKey projectMemberKey) {
        return projectMemberMapper.selectByPrimaryKey(projectMemberKey);
    }

    @Override
    public boolean deleteMemberByKey(ProjectMemberKey projectMemberKey) {
        return projectMemberMapper.deleteByPrimaryKey(projectMemberKey) == 1;
    }

    private boolean updatePermissionToServer(ProjectMember projectMember){
        ProjectMember originalMember = projectMemberMapper.selectByPrimaryKey(projectMember);
        if(originalMember != null){
            if(!projectMember.getAccessGit().equals(originalMember.getAccessGit())){
                callGitShell(projectMember.getProjectId(),projectMember.getEmployeeId(),"W".equals(projectMember.getAccessGit()));
            }
            if(!projectMember.getAccessFileSystem().equals(originalMember.getAccessFileSystem())){
                callFileSystemShell(projectMember.getProjectId(),projectMember.getEmployeeId(),"W".equals(projectMember.getAccessFileSystem()));
            }
            if(!projectMember.getInEmailList().equals(originalMember.getInEmailList())){
                callEmailShell(projectMember.getProjectId(),projectMember.getEmployeeId(),projectMember.getInEmailList().equals(1));
            }
        }

        return true;
    }

    private boolean callGitShell(String projectId, String userId, boolean permit){
        //获取git地址，根据permit调用相应的shell
        return true;
    }

    private boolean callFileSystemShell(String projectId, String userId, boolean permit){
        //获取file system地址，根据permit调用相应的shell
        return true;
    }

    private boolean callEmailShell(String projectId, String userId, boolean permit){
        //获取项目邮件列表服务器地址，根据permit和user email调用相应的shell
        return true;
    }
}
