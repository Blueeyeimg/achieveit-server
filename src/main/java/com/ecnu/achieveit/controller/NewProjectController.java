package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.achieveit.constant.ConstantUtil;
import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.modelview.ProjectBasicInfoView;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.NewProjectFlowService;
import com.ecnu.achieveit.service.impl.IMailServiceImpl;
import com.ecnu.achieveit.util.LogUtil;
import com.ecnu.achieveit.util.RestResponse;
import com.sun.org.apache.regexp.internal.RE;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@RestController
public class NewProjectController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private NewProjectFlowService newProjectFlowService;

    @Autowired
    private IMailServiceImpl mailService;

    @Qualifier("templateEngine")
    @Autowired
    private TemplateEngine templateEngine;

    @PutMapping("test")
    public Object test(@RequestParam("role")String[] role){
        for(String s :role){
            LogUtil.i(s);
        }
        return RestResponse.success(role.length);
    }

    @GetMapping("/mail/{to}")
    public Object sendEmail(@PathVariable("to") String to){
        try {
            Context context = new Context();
            context.setVariable("project_name", "《我与世界》");
            context.setVariable("user", "ocean");
            context.setVariable("boss", "robert");
            context.setVariable("message", "");
            String emailContent = templateEngine.process("approve_project_notify", context);

            mailService.sendHtmlMail(to, "这是模板邮件的测试", emailContent);

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

        if(newProjectFlowService.createProject(userId,projectBasicInfo)){
            return RestResponse.success(projectBasicInfo.getProjectId());
        }
        return RestResponse.fail();

    }

    @GetMapping("/newproject")
    public Object getNewProjects(@RequestAttribute("userId") String userId){

        List<ProjectBasicInfoView> projectBasicInfoViews =  newProjectFlowService.getAppliedProjects(userId);

        return RestResponse.success(projectBasicInfoViews);
    }

    @PutMapping("/newproject")
    public Object projectApproval(@RequestParam("taskId") String taskId, @RequestParam("approval") int approval,@RequestAttribute("userId") String userId){
        if(!employeeService.queryBasicEmployeeById(userId).getTitle().equals(EmployeeTitle.BOSS.getTitleName())){
            return RestResponse.fail("该成员不是" + EmployeeTitle.BOSS.getTitleName() + "，不能审批项目。");
        }

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

    @PutMapping("/newproject/config")
    public Object config(@RequestParam("taskId") String taskId,
                                @RequestAttribute("userId") String userId, ProjectBasicInfo projectBasicInfo){
        if(ObjectUtils.isEmpty(projectBasicInfo.getGitAddress()) || ObjectUtils.isEmpty(projectBasicInfo.getFileSystemAddress())){
            return RestResponse.fail("请提供配置库地址");
        }

        if(!employeeService.queryBasicEmployeeById(userId).getTitle().equals(EmployeeTitle.ORG_CONFIG.getTitleName())){
            return RestResponse.fail("该成员不是" + EmployeeTitle.ORG_CONFIG.getTitleName() + "，无法为项目建立配置库。");
        }

        boolean result = newProjectFlowService.setProjectConfig(taskId, userId, projectBasicInfo);
        if(!result){
            return RestResponse.fail();
        }
        return RestResponse.success();
    }

    @PutMapping("/newproject/member")
    public Object member(@RequestParam("taskId") String taskId, @RequestParam("role") String role ,
                         @RequestParam("ids") String[] ids,
                         @RequestAttribute("userId") String userId){
        if(!ProjectRole.valid(role)){
            return RestResponse.fail("不能为项目设置该类型的成员：" + role);
        }
        if(!employeeService.queryBasicEmployeeById(userId).getTitle().contains(role)){
            return RestResponse.fail("当前用户不是" + role + " Leader，不能为项目添加" + role);
        }
        boolean result = newProjectFlowService.setProjectQaOrEpg(taskId,userId,role,ids);
        if(!result){
            return RestResponse.fail();
        }
        return RestResponse.success();
    }

    @GetMapping("/newproject/ids")
    public Object getNewProjectIds(){

        JSONObject newProjectIds = new JSONObject();
        newProjectIds.put("ids", ConstantUtil.newProjectIds);
        return RestResponse.success(newProjectIds);
    }


}
