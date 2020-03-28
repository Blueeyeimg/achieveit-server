package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProjectMemberController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/member/{projectId}")
    public Object list(@PathVariable("projectId") String projectId){
        return RestResponse.success(projectMemberService.queryMemberList(projectId));
    }

    @PostMapping("/member")
    public Object add(@RequestAttribute("userId") String userId, ProjectMember projectMember){
        ProjectMember manager = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectMember.getBossInProjectId(),userId));
        if(ObjectUtils.isEmpty(manager) || !ProjectRole.MANAGER.in(manager.getRole())){
            return RestResponse.fail("当前用户不是该项目的项目经理，无法导入项目成员");
        }
        projectMemberService.addProjectMember(projectMember);
        return RestResponse.success();
    }

    @PutMapping("/member")
    public Object update(@RequestAttribute("userId") String userId, ProjectMember projectMember){
        ProjectMember manager = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectMember.getBossInProjectId(),userId));
        if(ObjectUtils.isEmpty(manager) || !ProjectRole.MANAGER.in(manager.getRole())){
            return RestResponse.fail("当前用户不是该项目的项目经理，无法修改项目成员权限");
        }
        projectMemberService.modifyMemberPermission(projectMember);

        return RestResponse.success();
    }

    @DeleteMapping("/member")
    public Object delete(@RequestAttribute("userId") String userId, ProjectMember projectMember){
        ProjectMember manager = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectMember.getBossInProjectId(),userId));
        if(ObjectUtils.isEmpty(manager) || !ProjectRole.MANAGER.in(manager.getRole())){
            return RestResponse.fail("当前用户不是该项目的项目经理，无法删除项目成员");
        }

        if(ProjectRole.QA.getRole().equals(projectMember.getRole()) || ProjectRole.EPG.getRole().equals(projectMember.getRole())){
            return RestResponse.fail("不能删除QA或者EPG");
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

}
