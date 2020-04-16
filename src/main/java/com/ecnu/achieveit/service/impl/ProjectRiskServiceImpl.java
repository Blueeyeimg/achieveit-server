package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.dao.ProjectMemberMapper;
import com.ecnu.achieveit.dao.ProjectRiskMapper;
import com.ecnu.achieveit.dao.RiskRelatedMapper;
import com.ecnu.achieveit.model.*;
import com.ecnu.achieveit.modelview.RiskTrackEmail;
import com.ecnu.achieveit.modelview.RiskView;
import com.ecnu.achieveit.service.IMailService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.service.ProjectRiskService;
import com.ecnu.achieveit.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class ProjectRiskServiceImpl implements ProjectRiskService {

    @Autowired(required = false)
    private ProjectRiskMapper riskMapper;

    @Autowired(required = false)
    private RiskRelatedMapper riskRelatedMapper;

    @Autowired
    private IMailService iMailService;

    @Qualifier("templateEngine")
    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public List<ProjectRisk> queryByProjectId(String projectId) {
        return riskMapper.selectByProjectId(projectId);
    }

    @Override
    public List<ProjectRisk> queryByOwnerId(String ownerId) {
        return riskMapper.selectByOwnerId(ownerId);
    }

    @Override
    public List<ProjectRisk> queryByRelatedId(String relatedId) {
        return riskMapper.selectByRelatedId(relatedId);
    }

    @Override
    public boolean add(List<ProjectRisk> risks) {

        boolean result = true;

        for(ProjectRisk risk : risks){
            result = result && riskMapper.insert(risk) == 1;
        }

        return result;
    }

    @Override
    public boolean add(ProjectRisk risk) {

        return riskMapper.insert(risk) == 1;
    }

    @Override
    public boolean modify(ProjectRisk risk) {
        return riskMapper.updateByPrimaryKeySelective(risk) == 1;
    }

    @Override
    public boolean remove(ProjectRiskKey riskKey) {

        riskRelatedMapper.clear(riskKey.getProjectId(),riskKey.getRiskId());
        return riskMapper.deleteByPrimaryKey(riskKey) == 1;
    }

    @Override
    public List<RiskRelatedKey> queryRelatesByProjectIdAndRiskId(String projectId, String riskId) {
        return riskRelatedMapper.selectByProjectRiskId(projectId, riskId);
    }

    @Override
    public boolean addRelated(RiskRelatedKey relatedKey) {
        return riskRelatedMapper.insert(relatedKey) == 1;
    }

    @Override
    public boolean removeRelated(RiskRelatedKey relatedKey) {
        return riskRelatedMapper.deleteByPrimaryKey(relatedKey) == 1;
    }

    @Override
    public boolean modifyRelates(List<RiskRelatedKey> riskRelatedKeys) {

        if(ObjectUtils.isEmpty(riskRelatedKeys)){
            return false;
        }

        riskRelatedMapper.clear(riskRelatedKeys.get(0).getProjectId(),riskRelatedKeys.get(0).getRiskId());

        boolean result = true;

        for(RiskRelatedKey riskRelatedKey : riskRelatedKeys){
            result = result && riskRelatedMapper.insert(riskRelatedKey) == 1;
        }

        return result;
    }

    @Override
    @Scheduled(cron = "0 0 9 ? * 5")
    public void autoEmailToManager() throws Exception{

        List<RiskTrackEmail> projects = riskMapper.selectProjectListWithManager();

        for(RiskTrackEmail project : projects){
            List<ProjectRisk> risks = riskMapper.selectByProjectId(project.getProjectId());

            if(ObjectUtils.isEmpty(risks)){
                continue;
            }
            String to = project.getManagerEmail();
            String name = project.getManagerName();
            String projectName = project.getProjectName();
            List<RiskView> riskViews = RiskView.convertFromProjectRisk(risks);

            LogUtil.i("即将发送邮件给" + to);
            try {
                Context context = new Context();
                context.setVariable("user", name);
                context.setVariable("project_name", projectName);
                context.setVariable("risks", riskViews);
                String emailContent = templateEngine.process("risk_manager_reminder", context);

                iMailService.sendHtmlMail(to, "请及时召开风险跟踪会议", emailContent);

            }catch (Exception ex){
                ex.printStackTrace();
                LogUtil.i("发送邮件失败");
                return ;
            }
            LogUtil.i("已经向项目经理 " + to + "发送邮件");
        }

    }

    @Override
    @Scheduled(cron = "0 0 9 ? * 5")
    public void autoEmailToRelates() throws Exception{
        List<Employee> relates = riskRelatedMapper.selectRelates();

        for(Employee relate : relates){
            String to = relate.getEmail();
            String name = relate.getEmployeeName();
            List<RiskView> riskViews = RiskView.convertFromProjectRisk(riskMapper.selectByRelatedId(relate.getEmployeeId()));

            LogUtil.i("即将发送邮件给" + to);
            try {
                Context context = new Context();
                context.setVariable("user", name);
                context.setVariable("risks", riskViews);
                String emailContent = templateEngine.process("risk_relates_reminder", context);

                iMailService.sendHtmlMail(to, "请及时进行风险跟踪", emailContent);

            }catch (Exception ex){
                ex.printStackTrace();
                LogUtil.i("发送邮件失败");
                return ;
            }
            LogUtil.i("已经向风险相关者 " + to + "发送邮件");
        }
    }

}
