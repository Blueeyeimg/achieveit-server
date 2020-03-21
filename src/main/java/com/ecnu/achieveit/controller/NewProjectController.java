package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.achieveit.constant.ConstantUtil;
import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.modelview.ProjectBasicInfoView;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.NewProjectFlowService;
import com.ecnu.achieveit.service.impl.IMailServiceImpl;
import com.ecnu.achieveit.util.RestResponse;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

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

    @Autowired
    private IMailServiceImpl mailService;

    @Qualifier("templateEngine")
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/mail/{to}")
    public Object sendEmail(@PathVariable("to") String to){
        try {
            Context context = new Context();
            context.setVariable("project_name", "《我与世界》");
            context.setVariable("user", "ocean");
            context.setVariable("boss", "robert");
            String emailContent = templateEngine.process("new_project_notify", context);

            mailService.sendHtmlMail(to, "这是模板邮件", emailContent);

            /*mailService.sendSimpleMail("achieveitgroup09@163.com","测试邮件","这次不会乱码了吧！");*/
        }catch (Exception ex){
            ex.printStackTrace();
            return RestResponse.fail("邮件发送失败");
        }
        return RestResponse.success("邮件发送成功");
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
