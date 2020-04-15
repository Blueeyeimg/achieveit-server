package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.dao.ProjectMemberMapper;
import com.ecnu.achieveit.dao.ProjectRiskMapper;
import com.ecnu.achieveit.dao.RiskRelatedMapper;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectRisk;
import com.ecnu.achieveit.model.ProjectRiskKey;
import com.ecnu.achieveit.model.RiskRelatedKey;
import com.ecnu.achieveit.modelview.RiskTrackEmail;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.service.ProjectRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class ProjectRiskServiceImpl implements ProjectRiskService {

    @Autowired(required = false)
    private ProjectRiskMapper riskMapper;

    @Autowired(required = false)
    private RiskRelatedMapper riskRelatedMapper;


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
    @Scheduled(cron = "0 0 9 ? * 6")
    public void autoEmailToManager() {

        List<RiskTrackEmail> projects = riskMapper.selectProjectListWithManager();

        for(RiskTrackEmail project : projects){
            List<ProjectRisk> risks = riskMapper.selectByProjectId(project.getProjectId());
            String to = project.getManagerEmail();
            String name = project.getManagerName();
            String projectName = project.getProjectName();
            StringBuffer riskMassage = new StringBuffer();

            for(ProjectRisk risk : risks){
                riskMassage.append("风险");
            }
        }

    }

    @Override
    public List<RiskTrackEmail> test(){
        return riskMapper.selectProjectListWithManager();
    }
}
