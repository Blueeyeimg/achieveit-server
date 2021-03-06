package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.dao.ProjectMemberMapper;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.service.ProjectService;
import com.ecnu.achieveit.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectMemberServiceImpl implements ProjectMemberService {

    @Autowired(required = false)
    private ProjectMemberMapper projectMemberMapper;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public boolean addProjectMember(ProjectMember projectMember) {

        if(!ProjectRole.isImportant(projectMember.getRole())
                && !canManageMember(projectMember.getProjectId())){
            return false;
        }

        if(projectMember.getBossInProjectId() == null){
            List<ProjectMember> managers = queryMemberByRole(projectMember.getProjectId(),ProjectRole.MANAGER.getRole());
            if(ObjectUtils.isEmpty(managers)){
                return false;
            }
            projectMember.setBossInProjectId(managers.get(0).getEmployeeId());
        }

        /*应当先插入再调用权限修改，若权限修改失败->再次尝试 OR 抛出异常，数据回滚。下几个函数也有类似问题。
        * 在刪除時特別要注意，成員刪除後是不能查詢到相關信息的 獲取的project是空的
        * */
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

        ProjectMember projectMember = new ProjectMember(projectId, userId, bossInProjectId, role, inTimSheetModule, false);


        return addProjectMember(projectMember);
    }

    @Override
    public boolean modifyMemberPermission(ProjectMember projectMember) {

        if(!canManageMember(projectMember.getProjectId())){
            return false;
        }

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

        if(!canManageMember(projectMemberKey.getProjectId())){
            return false;
        }

        if(removePermissionToServer(projectMemberKey)){
            return projectMemberMapper.deleteByPrimaryKey(projectMemberKey) == 1;
        }

        return false;
    }

    @Override
    public List<ProjectMember> queryMemberByRole(String projectId, String role) {
        return projectMemberMapper.selectListByRole(projectId, role);
    }

    @Override
    public List<Employee> queryAddibleEmployees(String projectId) {
        List<Employee> employees = employeeService.queryEmployees();
        List<String> projectMemberIds = queryMemberList(projectId).stream()
                .map(ProjectMember::getEmployeeId)
                .collect(Collectors.toList());

        employees = employees.stream()
                    .filter(e ->
                            e.getTitle().equals(EmployeeTitle.DEVELOPER.getTitleName())
                                    || e.getTitle().equals(EmployeeTitle.TEST.getTitleName()))
                    .filter(e -> !projectMemberIds.contains(e.getEmployeeId()))
                    .collect(Collectors.toList());
        return employees;
    }

    private boolean removePermissionToServer(ProjectMemberKey projectMemberKey){
        ProjectMember projectMember = projectMemberMapper.selectByPrimaryKey(projectMemberKey);
        ProjectBasicInfo projectBasicInfo = projectService.querryProjectByPrimaryKey(projectMember.getProjectId());
        callGitShell(projectBasicInfo.getGitAddress(),
                projectMember.getProjectId(),
                projectMember.getEmployeeId(),
                projectMember.getRole(),
                new Date(),
                new Date(),
                0);
        callFileSystemShell(projectBasicInfo.getFileSystemAddress() + "\\" + projectBasicInfo.getProjectId(),
                projectMember.getProjectId(),
                projectMember.getEmployeeId(),
                projectMember.getRole(),
                new Date(),
                new Date(),
                0);
        callEmailShell(projectBasicInfo.getProjectId() + "-List",
                projectMember.getProjectId(),
                projectMember.getEmployeeId(),
                projectMember.getRole(),
                new Date(),
                new Date(),
                false);

        return true;
    }

    private boolean updatePermissionToServer(ProjectMember projectMember){
        ProjectMember originalMember = projectMemberMapper.selectByPrimaryKey(projectMember);
        ProjectBasicInfo projectBasicInfo = projectService.querryProjectByPrimaryKey(projectMember.getProjectId());
        if(originalMember != null){
            if(projectMember.getAccessGit() != null
                && !projectMember.getAccessGit().equals(originalMember.getAccessGit())){
                callGitShell(projectBasicInfo.getGitAddress(),
                        projectMember.getProjectId(),
                        projectMember.getEmployeeId(),
                        projectMember.getRole(),
                        new Date(),
                        new Date(),
                        "W".equals(projectMember.getAccessGit()) ? 2 : 1);
            }
            if(projectMember.getAccessFileSystem() != null
                && !projectMember.getAccessFileSystem().equals(originalMember.getAccessFileSystem())){
                callFileSystemShell(projectBasicInfo.getFileSystemAddress() + "\\" + projectBasicInfo.getProjectId(),
                        projectMember.getProjectId(),
                        projectMember.getEmployeeId(),
                        projectMember.getRole(),
                        new Date(),
                        new Date(),
                        "W".equals(projectMember.getAccessFileSystem()) ? 2 : 1);
            }
            if(projectMember.getInEmailList() != null
                && !projectMember.getInEmailList().equals(originalMember.getInEmailList())){
                callEmailShell(projectBasicInfo.getProjectId() + "-List",
                        projectMember.getProjectId(),
                        projectMember.getEmployeeId(),
                        projectMember.getRole(),
                        new Date(),
                        new Date(),
                        new Integer(1).equals(projectMember.getInEmailList()));
            }
        }else{
            callGitShell(projectBasicInfo.getGitAddress(),
                    projectMember.getProjectId(),
                    projectMember.getEmployeeId(),
                    projectMember.getRole(),
                    new Date(),
                    new Date(),
                    "W".equals(projectMember.getAccessGit()) ? 2 : 1);
            callFileSystemShell(projectBasicInfo.getFileSystemAddress() + "\\" + projectBasicInfo.getProjectId(),
                    projectMember.getProjectId(),
                    projectMember.getEmployeeId(),
                    projectMember.getRole(),
                    new Date(),
                    new Date(),
                    "W".equals(projectMember.getAccessFileSystem()) ? 2 : 1);
            callEmailShell(projectBasicInfo.getProjectId() + "-List",
                    projectMember.getProjectId(),
                    projectMember.getEmployeeId(),
                    projectMember.getRole(),
                    new Date(),
                    new Date(),
                    new Integer(1).equals(projectMember.getInEmailList()));
        }

        return true;
    }

    private boolean callGitShell(String gitAddress, String projectId,
                                 String userId, String role, Date joinDate,
                                 Date quitDate, int permit){
        //获取git地址，根据permit调用相应的shell 0:删除成员无权限、1：读权限、2：写权限
        LogUtil.i("Git权限变更，已在Git更新权限");
        return true;
    }

    private boolean callFileSystemShell(String fileAddress, String projectId,
                                        String userId, String role, Date joinDate,
                                        Date quitDate, int permit){
        //获取file system地址，根据permit调用相应的shell 0:删除成员无权限、1：读权限、2：写权限
        LogUtil.i("文件系统权限变更，已在文件系统服务器更新权限");
        return true;
    }

    private boolean callEmailShell(String emailList, String projectId,
                                   String userId, String role, Date joinDate,
                                   Date quitDate, boolean permit){
        //获取项目邮件列表服务器地址，根据permit和user email调用相应的shell
        LogUtil.i("邮件列表权限变更，已在邮件服务器更新权限");
        return true;
    }

    private boolean canManageMember(String projectId){
        ProjectBasicInfo projectBasicInfo = projectService.querryProjectByPrimaryKey(projectId);

        if(ProjectState.inProcess(projectBasicInfo.getState())
                && !ObjectUtils.isEmpty(projectBasicInfo.getGitAddress())
                && !ObjectUtils.isEmpty(projectBasicInfo.getFileSystemAddress())){
            return true;
        }

        return false;
    }
}
