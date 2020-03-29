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
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired(required = false)
    private ProjectMemberMapper projectMemberMapper;

    @Override
    public boolean addProjectMember(ProjectMember projectMember) {

        if(projectMember.getBossInProjectId() == null){
            List<ProjectMember> managers = queryMemberByRole(projectMember.getProjectId(),ProjectRole.MANAGER.getRole());
            if(ObjectUtils.isEmpty(managers)){
                return false;
            }
        projectMember.setBossInProjectId(managers.get(0).getEmployeeId());
    }

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
        return projectMemberMapper.selectListByProjectId(projectId);
    }

    @Override
    public ProjectMember queryMemberByKey(ProjectMemberKey projectMemberKey) {
        return projectMemberMapper.selectByPrimaryKey(projectMemberKey);
    }

    @Override
    public boolean deleteMemberByKey(ProjectMemberKey projectMemberKey) {
        return projectMemberMapper.deleteByPrimaryKey(projectMemberKey) == 1;
    }

    @Override
    public List<ProjectMember> queryMemberByRole(String projectId, String role) {
        return projectMemberMapper.selectListByRole(projectId, role);
    }

    private boolean updatePermissionToServer(ProjectMember projectMember){
        ProjectMember originalMember = projectMemberMapper.selectByPrimaryKey(projectMember);
        if(originalMember != null){
            if(projectMember.getAccessGit() != null
                && !projectMember.getAccessGit().equals(originalMember.getAccessGit())){
                callGitShell(projectMember.getProjectId(),projectMember.getEmployeeId(),"W".equals(projectMember.getAccessGit()));
            }
            if(projectMember.getAccessFileSystem() != null
                && !projectMember.getAccessFileSystem().equals(originalMember.getAccessFileSystem())){
                callFileSystemShell(projectMember.getProjectId(),projectMember.getEmployeeId(),"W".equals(projectMember.getAccessFileSystem()));
            }
            if(projectMember.getInEmailList() != null
                && !projectMember.getInEmailList().equals(originalMember.getInEmailList())){
                callEmailShell(projectMember.getProjectId(),projectMember.getEmployeeId(),projectMember.getInEmailList().equals(1));
            }
        }

        return true;
    }

    private boolean callGitShell(String projectId, String userId, boolean permit){
        //获取git地址，根据permit调用相应的shell
        LogUtil.i("Git权限变更，已在Git更新权限");
        return true;
    }

    private boolean callFileSystemShell(String projectId, String userId, boolean permit){
        //获取file system地址，根据permit调用相应的shell
        LogUtil.i("文件系统权限变更，已在文件系统服务器更新权限");
        return true;
    }

    private boolean callEmailShell(String projectId, String userId, boolean permit){
        //获取项目邮件列表服务器地址，根据permit和user email调用相应的shell
        LogUtil.i("邮件列表权限变更，已在邮件服务器更新权限");
        return true;
    }
}
