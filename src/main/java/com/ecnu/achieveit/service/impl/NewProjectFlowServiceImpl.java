package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
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
import java.util.stream.Collectors;

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

    @Autowired
    private ProjectIdService projectIdService;

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
    public boolean createProject(String userId, ProjectBasicInfo projectBasicInfo) {

        boolean insertResult;
        projectBasicInfo.setState(ProjectState.APPLIED.getState());
        insertResult = projectService.addProject(projectBasicInfo);
        projectIdService.deleteProjectId(projectBasicInfo.getProjectId());
        insertResult = insertResult && projectMemberService.addProjectMember(projectBasicInfo.getProjectId(),userId, userId, ProjectRole.MANAGER.getRole());

        if(!insertResult){
            return false;
        }

        String instanceId = startProcess();
        Task task = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
        task.setAssignee(userId);
        LogUtil.i("task assigned to " + userId);


        Map<String, Object> variables= new HashMap<>();
        variables.put("managerId", userId);
        variables.put("project", projectBasicInfo);
        taskService.complete(task.getId(),variables);

        LogUtil.i("录入信息任务完成");

        sendApplieddEmailToBoss(projectBasicInfo.getProjectName(),userId);

        return true;
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

        LogUtil.i("完成任务，开始发邮件");
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
        List<Employee> orgConfigs = employeeService.queryBasicEmployeeGroup(EmployeeTitle.ORG_CONFIG.getTitle());
        if(!ObjectUtils.isEmpty(orgConfigs)){
            String to = orgConfigs.stream().map(Employee::getEmail).collect(Collectors.joining(","));
            LogUtil.i("即将发送邮件给" + to);
            sendApprovedEmailToGroup(to, projectBasicInfo.getProjectName(),
                    EmployeeTitle.ORG_CONFIG.getTitleName(), boss.getEmployeeName(),
                    "请登录AchieveIt系统为其建立配置库。");
            LogUtil.i("已经向组织级配置管理员" + to + "发送邮件");
        }
        /*邮件通知QA Leader*/
        List<Employee> qaLeaders = employeeService.queryBasicEmployeeGroup(EmployeeTitle.QA_LEADER.getTitle());
        if(!ObjectUtils.isEmpty(qaLeaders)){
            String to = qaLeaders.stream().map(Employee::getEmail).collect(Collectors.joining(","));
            LogUtil.i("即将发送邮件给" + to);
            sendApprovedEmailToGroup(to, projectBasicInfo.getProjectName(),
                    EmployeeTitle.QA_LEADER.getTitleName(), boss.getEmployeeName(),
                    "请登录AchieveIt系统为其分配QA。");
            LogUtil.i("已经向组QA经理" + to + "发送邮件");
        }
        /*邮件通知EPG Leader*/
        List<Employee> epgLeaders = employeeService.queryBasicEmployeeGroup(EmployeeTitle.EPG_LEADER.getTitle());
        if(!ObjectUtils.isEmpty(epgLeaders)){
            String to = epgLeaders.stream().map(Employee::getEmail).collect(Collectors.joining(","));
            LogUtil.i("即将发送邮件给" + to);
            sendApprovedEmailToGroup(to, projectBasicInfo.getProjectName(),
                    EmployeeTitle.EPG_LEADER.getTitleName(), boss.getEmployeeName(),
                    "请登录AchieveIt系统为其分配EPG。");
            LogUtil.i("已经向EPG Leader" + to + "发送邮件");
        }
        LogUtil.i("即将发送邮件给" + manager.getEmail());

        sendApprovedEmailToGroup(manager.getEmail(),projectBasicInfo.getProjectName(),
                manager.getEmployeeName(),boss.getEmployeeName(),
                "请登录AchieveIt系统查看详情。");
        LogUtil.i("已经向项目经理" + manager.getEmployeeName() + "发送邮件");

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
            LogUtil.i("已经向项目经理" + manager.getEmployeeName() + "发送邮件");

        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.i("发送邮件给项目经理失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean setProjectConfig(String taskId, String userId, ProjectBasicInfo projectBasicInfo) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null){
            return false;
        }
        task.setAssignee(userId);

        String managerId = (String)taskService.getVariables(taskId).get("managerId");
        projectService.updateProject(projectBasicInfo);
        taskService.complete(taskId);

        sendConfigEmail(managerId,projectBasicInfo.getProjectName(),userId);
        return true;
    }

    @Override
    public boolean setProjectQaOrEpg(String taskId, String userId, String role, String... ids) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null){
            return false;
        }
        task.setAssignee(userId);

        Map<String, Object> preVariables = taskService.getVariables(taskId);
        ProjectBasicInfo projectBasicInfo = (ProjectBasicInfo)preVariables.get("project");
        String managerId = (String)preVariables.get("managerId");
        boolean result = true;
        for(String id : ids){
            result = result && projectMemberService.addProjectMember(projectBasicInfo.getProjectId(), id, managerId, role);
        }
        if(result){
            sendSetMemberEmail(managerId,projectBasicInfo.getProjectName(),userId,role);
            taskService.complete(taskId);
        }
        return result;
    }

    @Override
    public boolean sendConfigEmail(String projectManagerId, String projectName, String configId) {
        Employee manager = employeeService.queryBasicEmployeeById(projectManagerId);
        Employee config = employeeService.queryBasicEmployeeById(configId);
        try {
            Context context = new Context();
            context.setVariable("project_name", projectName);
            context.setVariable("user", manager.getEmployeeName());
            context.setVariable("org_config", config.getEmployeeName());
            String emailContent = templateEngine.process("configed_notify", context);

            mailService.sendHtmlMail(manager.getEmail(), "新项目配置库已建立", emailContent);
            LogUtil.i("成功给项目经理" + manager.getEmployeeName() + manager.getEmail() + "发送邮件");

        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.i("发送邮件失败");
            return false;
        }
        return true;
    }

    @Override
    public boolean sendSetMemberEmail(String projectManagerId, String projectName, String leaderId, String employeeTitle) {
        Employee manager = employeeService.queryBasicEmployeeById(projectManagerId);
        Employee leader = employeeService.queryBasicEmployeeById(leaderId);
        try {
            Context context = new Context();
            context.setVariable("project_name", projectName);
            context.setVariable("user", manager.getEmployeeName());
            context.setVariable("title", employeeTitle);
            context.setVariable("leader_name", leader.getEmployeeName());
            String emailContent = templateEngine.process("set_member_notify", context);

            mailService.sendHtmlMail(manager.getEmail(), "新的" + employeeTitle + "已添加到项目", emailContent);
            LogUtil.i("成功给项目经理" + manager.getEmployeeName() + manager.getEmail() + "发送邮件");

        }catch (Exception ex){
            ex.printStackTrace();
            LogUtil.i("发送邮件失败");
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

    public boolean sendApplieddEmailToBoss(String projectName, String userId){

        Employee manager = employeeService.queryBasicEmployeeById(userId);
        List<Employee> bosses = employeeService.queryBasicEmployeeGroup(EmployeeTitle.BOSS.getTitle());

        if(!ObjectUtils.isEmpty(bosses)){
            String to = bosses.stream().map(Employee::getEmail).collect(Collectors.joining(","));
            LogUtil.i("即将发送邮件给" + to);
            try {
                Context context = new Context();
                context.setVariable("manager", manager.getEmployeeName());
                context.setVariable("project_name", projectName);
                String emailContent = templateEngine.process("new_project_applied_notify", context);

                mailService.sendHtmlMail(to, "有新项目提交", emailContent);

            }catch (Exception ex){
                ex.printStackTrace();
                LogUtil.i("发送邮件失败");
                return false;
            }
            LogUtil.i("已经向Boss " + to + "发送邮件");
        }


        return true;
    }


}
