package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectService;
import com.ecnu.achieveit.util.RestResponse;
import com.sun.org.apache.xpath.internal.objects.XObject;
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
        Object result = projectService.querryProjectByPrimaryKey(projectId);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/project_infos/{item}/{item_value}")
    public Object list1(@PathVariable("item") String item,
                        @PathVariable("item_value")String itemValue){
        if(item.equals("employee_id")){
            Object result = projectService.querryProjectByEmployeeId(itemValue);
            if(result == null) return RestResponse.fail(); //"未查到"+itemValue+"有关信息"
            else return RestResponse.success(result);
        }
        else if(item.equals("client_id")){
            Object result = projectService.querryProjectByClientId(itemValue);
            if(result == null) return RestResponse.fail();//"未查到"+itemValue+"有关信息"
            else return RestResponse.success(result);
        }
        else if(item.equals("state")) {
            Object result = projectService.querryProjectByState(itemValue);
            if(result.equals(0)) return RestResponse.fail();//"未查到"+itemValue+"有关信息"
            else return RestResponse.success(result);
        }
        else if(item.equals("boss_id")){
            Object result = projectService.querryProjectByBossId(itemValue);
            if(result.equals(0)) return RestResponse.fail();//"未查到"+itemValue+"有关信息"
            else return RestResponse.success(result);
        }
        return RestResponse.fail();
    }

/*    @GetMapping("/projectbasicinfo")
    public Object list2(@RequestParam("clientid")String clientId){
        Object result = projectService.querryProjectByClientId(clientId);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/projectbasicinfo")
    public Object list3(@RequestParam("state")String state){
        Object result = projectService.querryProjectByState(state);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/projectbasicinfo")
    public Object list4(@RequestParam("bossid")String bossId){
        Object result = projectService.querryProjectByBossId(bossId);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }*/
}

