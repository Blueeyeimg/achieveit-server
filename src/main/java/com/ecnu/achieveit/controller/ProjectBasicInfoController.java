package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.dao.AssetItemMapper;
import com.ecnu.achieveit.model.*;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectIdService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.service.ProjectService;
import com.ecnu.achieveit.util.LogUtil;
import com.ecnu.achieveit.util.RestResponse;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.ecnu.achieveit.constant.ProjectState.ARCHIVED;

@RestController
public class ProjectBasicInfoController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProjectMemberService projectMemberService;
    @Autowired
    private ProjectIdService projectIdService;

    @GetMapping("/project_info/{project_id}")
    public Object show(@PathVariable("project_id")String projectId){
        ProjectBasicInfo result = projectService.querryProjectByPrimaryKey(projectId);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    /**
     *
     * @param userId(Token里携带的)
     * @return List<ProjectBasicInfo>
     */
    @GetMapping("/project_infos")
    public Object list2(@RequestAttribute("userId")String userId){
        List<ProjectBasicInfo> result = projectService.queryProjectByEmployeeId(userId);

        if(result == null) return RestResponse.fail("");
        else return RestResponse.success(result);
    }

    @GetMapping("/project_infos/key_word/{key_word}")
    public Object list3(@PathVariable("key_word")String keyWord,
                        @RequestAttribute("userId")String employeeId){
        List<ProjectBasicInfo> result = projectService.queryProjectByKeyWord(keyWord,employeeId);

        if(result== null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    /**
     * 该方法用于更改项目的状态，例如改为已结束、申请归档、已归档等等
     * @param projectId
     * @param state
     * @param userId
     * @return
     */
    @PutMapping("/state")
    public  Object updateState(@RequestParam("projectId")String projectId,
                               @RequestParam("state")String state,
                               @RequestAttribute("userId")String userId){
        if(!ProjectState.checkState(state))
            return RestResponse.fail("请检查目标状态是否合法");
        if(state.equals(ARCHIVED.getState())){
            if(!employeeService.queryBasicEmployeeById(userId).getTitle().equals(EmployeeTitle.ORG_CONFIG.getTitleName()))
                return RestResponse.fail("该成员不是" + EmployeeTitle.ORG_CONFIG.getTitleName() + "，不能修改项目状态为已归档。");
        }
        else if(!employeeService.queryBasicEmployeeById(userId).getTitle().equals(EmployeeTitle.MANAGER.getTitleName()))
            return RestResponse.fail("该成员不是" + EmployeeTitle.MANAGER.getTitleName() + "，不能修改项目状态。");
        projectService.updateProjectStateById(projectId,state);
        return RestResponse.success();
    }

    @GetMapping("/state")
    public Object list4(@RequestParam("state")String state,
                        @RequestAttribute("userId")String userId){
        List<ProjectBasicInfo> result = projectService.queryProjectByState(state);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/check_assets")
    public  Object checkAssets(@RequestAttribute("userId") String userId){
        if(!employeeService.queryBasicEmployeeById(userId).getTitle().equals(EmployeeTitle.ORG_CONFIG.getTitleName())){
            return RestResponse.fail("该成员不是" + EmployeeTitle.ORG_CONFIG.getTitleName() + "，没有权限");
        }

        List<AssetItem> result = projectService.queryAssetItem();
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @PutMapping("/projectinfo/outputlink")
    public Object updateOutputLinkOfProjectInfo(@RequestParam("projectId")String projectId,
                                                @RequestParam("outputLink")String outputLink,
                                                @RequestAttribute("userId")String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectId,userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            LogUtil.i("用户在项目中的角色为" + user.getRole());
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法修改outputLink");
        }
        boolean result = projectService.updateOutputLinkOfProjectInfo(projectId,outputLink);
        if(!result)return RestResponse.fail();
        return RestResponse.success();
    }


}
