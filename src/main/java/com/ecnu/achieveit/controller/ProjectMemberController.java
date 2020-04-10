package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.util.LogUtil;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/test/{role}")
    public Object sendEmail(@PathVariable("role") String role ){
        LogUtil.i("验证角色" + role);
        if(ProjectRole.QA.in(role)
                || ProjectRole.EPG.in(role)
                || ProjectRole.MANAGER.in(role)){
            return RestResponse.noPermission("不能新增QA、EPG或者项目经理");
        }
        LogUtil.i("成功");
        return RestResponse.success();
    }


    @GetMapping("/member/{projectId}")
    public Object list(@PathVariable("projectId") String projectId){
        return RestResponse.success(projectMemberService.queryMemberList(projectId));
    }

    @PostMapping("/member")
    public Object add(@RequestAttribute("userId") String userId, ProjectMember projectMember){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectMember.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法导入项目成员");
        }

        if(ProjectRole.isImportant(projectMember.getRole())){

            return RestResponse.noPermission("不能导入QA、EPG或者项目经理");
        }

        if(projectMemberService.addProjectMember(projectMember)){
            return RestResponse.success();
        }
        return RestResponse.fail();
    }

    @PutMapping("/member")
    public Object update(@RequestAttribute("userId") String userId, ProjectMember projectMember){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectMember.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法修改项目成员权限");
        }

        ProjectMember oldMember = projectMemberService.queryMemberByKey(projectMember);

        if(ProjectRole.isImportant(projectMember.getRole())
                ^ ProjectRole.isImportant(oldMember.getRole())){

            return  RestResponse.noPermission("不能将重要角色转化（项目经理、EPG、QA）为普通角色或者将普通角色转化为重要角色");
        }

        if(projectMemberService.modifyMemberPermission(projectMember)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    @DeleteMapping("/member")
    public Object delete(@RequestAttribute("userId") String userId, ProjectMember projectMember){
        LogUtil.i("userId: " + userId);
        LogUtil.i("ProjectMember: " + projectMember.toString());
        LogUtil.i("projectId: " + projectMember.getProjectId());
        LogUtil.i("employeeId: " + projectMember.getEmployeeId());

        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectMember.getProjectId(),userId));

        if(ObjectUtils.isEmpty(user)){
            LogUtil.i("项目用户为空");
        }
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            LogUtil.i("用户在项目中的角色为" + user.getRole());
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法删除项目成员");
        }

        ProjectMember oldMember = projectMemberService.queryMemberByKey(projectMember);

        if(ProjectRole.isImportant(oldMember.getRole())){
            return RestResponse.noPermission("不能删除QA、EPG或者项目经理");
        }

        if(projectMemberService.deleteMemberByKey(projectMember)){
            return RestResponse.success();
        }
        return RestResponse.fail("删除失败");
    }

    @GetMapping("/role")
    public Object getRole(){
        return RestResponse.success(Arrays.stream(ProjectRole.values()).map(ProjectRole::getRole).collect(Collectors.toList()));
    }

    @GetMapping("/memberinfo/{projectId}")
    public Object getProjectMemberByKey(@PathVariable("projectId")String projectId,
                                      @RequestAttribute("userId")String employeeId){
        ProjectMemberKey projectMemberKey = new ProjectMemberKey(projectId,employeeId);
        ProjectMember result = projectMemberService.queryMemberByKey(projectMemberKey);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

}
