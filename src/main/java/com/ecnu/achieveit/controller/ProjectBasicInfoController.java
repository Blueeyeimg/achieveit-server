package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectService;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProjectBasicInfoController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/project_info/{project_id}")
    public Object show(@PathVariable("project_id")String projectId){
        ProjectBasicInfo result = projectService.querryProjectByPrimaryKey(projectId);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/project_infos/{item}/{item_value}")
    public Object list1(@PathVariable("item") String item,
                        @PathVariable("item_value")String itemValue){
        if(item.equals("employee_id")){
            List<ProjectBasicInfo> result = projectService.querryProjectByEmployeeId(itemValue);
            if(result.size() == 0) return RestResponse.fail("null");
            else return RestResponse.success(result);
        }
        else if(item.equals("client_id")){
            List<ProjectBasicInfo> result = projectService.querryProjectByClientId(itemValue);
            if(result.size() == 0) return RestResponse.fail("null");
            else return RestResponse.success(result);
        }
        else if(item.equals("state")) {
            List<ProjectBasicInfo> result = projectService.querryProjectByState(itemValue);
            if(result.size() == 0) return RestResponse.fail("null");
            else return RestResponse.success(result);
        }
        else if(item.equals("boss_id")){
            List<ProjectBasicInfo> result = projectService.querryProjectByBossId(itemValue);
            if(result.size() == 0) return RestResponse.fail("null");
            else return RestResponse.success(result);
        }
        return RestResponse.fail();
    }

    /**
     *
     * @param userId(Token里携带的)
     * @return List<ProjectBasicInfo>
     */
    @GetMapping("/project_infos")
    public Object list2(@RequestAttribute("userId")String userId){
        List<ProjectBasicInfo> result = projectService.querryProjectByEmployeeId(userId);

        if(result.size() == 0) return RestResponse.fail("null");
        else return RestResponse.success(result);
    }

    @GetMapping("/project_infos/key_word/{key_word}")
    public Object list3(@PathVariable("key_word")String keyWord,
                        @RequestAttribute("userId")String employeeId){
        List<ProjectBasicInfo> result = projectService.querryProjectByKeyWord(keyWord,employeeId);

        if(result.size() == 0) return RestResponse.fail("null");
        return RestResponse.success(result);
    }


}

