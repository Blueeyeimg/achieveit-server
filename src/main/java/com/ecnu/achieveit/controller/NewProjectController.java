package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.achieveit.constant.ConstantUtil;
import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.modelview.ProjectBasicInfoView;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.NewProjectFlowService;
import com.ecnu.achieveit.util.RestResponse;
import com.sun.javafx.tk.Toolkit;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Task;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NewProjectController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private NewProjectFlowService newProjectFlowService;

    @GetMapping("/mail")
    public Object sendEmail(){

        runtimeService.startProcessInstanceByKey("mail_test");
        return RestResponse.success();
    }

    @PostMapping("/newproject")
    public Object createNewProject(ProjectBasicInfo projectBasicInfo, @RequestAttribute("userId") String userId){

        if(!employeeService.checkTitle(userId,"项目经理")){
            return RestResponse.fail("该用户不是项目经理!");
        }
        projectBasicInfo.setState(ProjectState.APPLIED.getState());
        String instanceId = newProjectFlowService.startProcess();
        newProjectFlowService.createPrject(userId,projectBasicInfo,instanceId);
        return RestResponse.success(projectBasicInfo.getProjectId());
    }

    @GetMapping("/newproject")
    public Object getNewProjects(@RequestAttribute("userId") String userId){
        if(!employeeService.checkTitle(userId,"项目上级")){
                return RestResponse.fail("该用户不是项目上级!");
        }
        List<ProjectBasicInfoView> projectBasicInfoViews =  newProjectFlowService.getAppliedProjects(userId);

        return RestResponse.success(projectBasicInfoViews);
    }

    @PutMapping("/newproject")
    public Object projectApproval(@RequestParam("taskId") String taskId, @RequestParam("approval") int approval,@RequestAttribute("userId") String userId){
        boolean result;
        if(approval == 1){
            result = newProjectFlowService.approveProject(taskId,userId);
        }else{
            result = newProjectFlowService.disapproveProject(taskId,userId);
        }
        if(result){
            return RestResponse.success("审核项目成功，已发邮件给相关人员！");
        }
        return RestResponse.fail("无待审核的该项目");
    }

    @GetMapping("/newproject/ids")
    public Object getNewProjectIds(){

        JSONObject newProjectIds = new JSONObject();
        newProjectIds.put("ids", ConstantUtil.newProjectIds);
        return RestResponse.success(newProjectIds);
    }


}
