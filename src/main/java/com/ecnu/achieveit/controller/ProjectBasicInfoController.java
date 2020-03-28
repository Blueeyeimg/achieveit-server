package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.model.ProjectId;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectIdService;
import com.ecnu.achieveit.service.ProjectService;
import com.ecnu.achieveit.util.RestResponse;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
        List<ProjectBasicInfo> result = projectService.querryProjectByEmployeeId(userId);

        if(result.size() == 0) return RestResponse.fail("null");
        else return RestResponse.success(result);
    }

    @GetMapping("/project_infos/key_word/{key_word}")
    public Object list3(@PathVariable("key_word")String keyWord,
                        @RequestAttribute("userId")String employeeId){
        List<ProjectBasicInfo> result = projectService.querryProjectByKeyWord(keyWord,employeeId);

        if(result== null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    /*@GetMapping("project_id")
    public Object list4(){
        //这里要先检查title
        List<ProjectId> result = projectIdService.querryProjectIds();
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @DeleteMapping("project_id/{id}")
    public Object delete1(@PathVariable("id") String projectId){
        Boolean result = projectIdService.deleteProjectId(projectId);
        if(!result){
            return RestResponse.fail();
        }
        return RestResponse.success();
    }*/

}
