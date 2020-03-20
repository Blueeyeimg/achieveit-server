package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.modelview.ProjectBasicInfoView;
import com.ecnu.achieveit.service.NewProjectFlowService;
import com.ecnu.achieveit.util.LogUtil;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Map<String, Object> projectInfoMap= new HashMap<>();
        projectInfoMap.put("project", projectBasicInfo);
        taskService.complete(task.getId(),projectInfoMap);
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
        return false;
    }

    @Override
    public boolean disapproveProject(String taskId, String userId) {

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(task == null){
            return false;
        }
        task.setAssignee(userId);
        Map<String, Object> passOrNot= new HashMap<>();
        passOrNot.put("pass",0);
        taskService.complete(taskId,passOrNot);

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        if(processInstance == null){
            LogUtil.i("process 执行完毕");
        }else{
            LogUtil.i("process 未完成");
        }

        return true;
    }
}
