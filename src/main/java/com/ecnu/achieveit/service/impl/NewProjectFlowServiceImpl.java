package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectState;
import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.modelview.ProjectBasicInfoView;
import com.ecnu.achieveit.service.*;
import com.ecnu.achieveit.util.LogUtil;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewProjectFlowServiceImpl implements NewProjectFlowService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private IMailService mailService;

    @Qualifier("templateEngine")
    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String startProcess() {
        LogUtil.i("=============start process begining=========");
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("create_project");
        LogUtil.i("=============start process ended=========");
        return instance.getId();
    }

    @Override
    public String createPrject(String userId, ProjectBasicInfo projectBasicInfo, String instanceId) {
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        task.setAssignee(userId);
        LogUtil.i("task assigned to " + userId);

        projectBasicInfo.setState(ProjectState.APPROVED.getState());
        projectService.addProject(projectBasicInfo);
        projectMemberService.addProjectMember(new ProjectMember(projectBasicInfo.getProjectId(),
                userId,userId,"项目经理","W","W",1,1,1));

        Map<String, Object> variables= new HashMap<>();
        variables.put("managerId", userId);
        variables.put("project", projectBasicInfo);
        taskService.complete(task.getId(),variables);

        LogUtil.i("录入信息任务完成");
        return projectBasicInfo.getProjectId();
    }

    @Override
    public List<ProjectBasicInfoView> getAppliedProjects(String userId) {

        LogUtil.i("开始查询当前用户的tasks");
        List<ProjectBasicInfoView> result = new ArrayList<>();
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("create_project")
                .taskCandidateOrAssigned(userId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        if(tasks != null && tasks.size() > 0){
            for (Task task : tasks) {
                ProjectBasicInfo projectBasicInfo = (ProjectBasicInfo)taskService.getVariables(task.getId()).get("project");
                result.add(new ProjectBasicInfoView(task.getId(),projectBasicInfo));
                LogUtil.i("任务Id: " + task.getId());
                LogUtil.i("项目信息: " + projectBasicInfo);
            }
        }
        LogUtil.i("任务查询完成");

        return result;
    }

    @Override
    public boolean approveProject(String taskId, String userId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null){
            return false;
        }
        task.setAssignee(userId);

        Map<String, Object> preVariables = taskService.getVariables(taskId);
        ProjectBasicInfo projectBasicInfo = (ProjectBasicInfo)preVariables.get("project");
        String managerId = (String)preVariables.get("managerId");
        projectBasicInfo.setProjectBossId(userId);
        projectBasicInfo.setState(ProjectState.APPROVED.getState());
        projectService.updateProject(projectBasicInfo);

        Map<String, Object> variables= new HashMap<>();
        variables.put("pass",1);
        taskService.complete(taskId,variables);

        sendApprovedEmail(managerId,projectBasicInfo);

        return true;
    }

    @Override
    public boolean disapproveProject(String taskId, String userId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null){
            return false;
        }
        task.setAssignee(userId);

        Map<String, Object> preVariables = taskService.getVariables(taskId);
        ProjectBasicInfo projectBasicInfo = (ProjectBasicInfo)preVariables.get("project");
        String managerId = (String)preVariables.get("managerId");
        projectBasicInfo.setProjectBossId(userId);
        projectBasicInfo.setState(ProjectState.DISAPPROVED.getState());
        projectService.updateProject(projectBasicInfo);

        Map<String, Object> variables= new HashMap<>();
        variables.put("pass",0);
        taskService.complete(taskId,variables);

        sendDisapprovedEmail(managerId,projectBasicInfo);

        return true;
    }

    @Override
    public boolean sendApprovedEmail(String projectManagerId, ProjectBasicInfo projectBasicInfo) {

        Employee manager = employeeService.queryBasicEmployeeById(projectManagerId);
        Employee boss = employeeService.queryBasicEmployeeById(projectBasicInfo.getProjectBossId());

        /*邮件通知配置管理员*/
        List<Employee> orgCongigs = employeeService.queryBasicEmployeeGroup(EmployeeTitle.ORG_CONFIG.getTitle());
        if(!ObjectUtils.isEmpty(orgCongigs)){
            String to = orgCongigs.stream().map(Employee::getEmail).reduce((s1, s2) -> s1 + "," + s2).get();
            sendApprovedEmailToGroup(to, projectBasicInfo.getProjectName(),
                    EmployeeTitle.ORG_CONFIG.getTitleName(), boss.getEmployeeName(),
                    "请登录AchieveIt系统为其建立配置库。");
        }
        /*邮件通知QA Leader*/
        List<Employee> qaLeaders = employeeService.queryBasicEmployeeGroup(EmployeeTitle.QA_LEADER.getTitle());
        if(!ObjectUtils.isEmpty(qaLeaders)){
            String to = qaLeaders.stream().map(Employee::getEmail).reduce((s1, s2) -> s1 + "," + s2).get();
            sendApprovedEmailToGroup(to, projectBasicInfo.getProjectName(),
                    EmployeeTitle.QA_LEADER.getTitleName(), boss.getEmployeeName(),
                    "请登录AchieveIt系统为其分配QA。");
        }
        /*邮件通知EPG Leader*/
        List<Employee> epgLeaders = employeeService.queryBasicEmployeeGroup(EmployeeTitle.EPG_LEADER.getTitle());
        if(!ObjectUtils.isEmpty(epgLeaders)){
            String to = epgLeaders.stream().map(Employee::getEmail).reduce((s1, s2) -> s1 + "," + s2).get();
            sendApprovedEmailToGroup(to, projectBasicInfo.getProjectName(),
                    EmployeeTitle.EPG_LEADER.getTitleName(), boss.getEmployeeName(),
                    "请登录AchieveIt系统为其分配EPG。");
        }

        sendApprovedEmailToGroup(manager.getEmail(),projectBasicInfo.getProjectName(),
                manager.getEmployeeName(),boss.getEmployeeName(),
                "请登录AchieveIt系统查看详情。");

        return true;
    }

    @Override
    public boolean sendDisapprovedEmail(String projectManagerId, ProjectBasicInfo projectBasicInfo) {
        Employee manager = employeeService.queryBasicEmployeeById(projectManagerId);
        Employee boss = employeeService.queryBasicEmployeeById(projectBasicInfo.getProjectBossId());
        try {
            Context context = new Context();
            context.setVariable("project_name", projectBasicInfo.getProjectName());
            context.setVariable("user", manager.getEmployeeName());
            context.setVariable("boss", boss.getEmployeeName());
            String emailContent = templateEngine.process("disapprove_project_notify", context);

            mailService.sendHtmlMail(manager.getEmail(), "项目被驳回", emailContent);

        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.i("发送邮件给项目经理失败");
            return false;
        }
        return true;
    }

    public boolean sendApprovedEmailToGroup(String to, String projectName, String userGroup, String bossName, String message){
        try {
            Context context = new Context();
            context.setVariable("project_name", projectName);
            context.setVariable("user", userGroup);
            context.setVariable("boss", bossName);
            context.setVariable("message", message);
            String emailContent = templateEngine.process("approve_project_notify", context);

            mailService.sendHtmlMail(to, "项目审核通过", emailContent);

        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.i("发送邮件失败");
            return false;
        }
        return true;
    }


}
