package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectRisk;
import com.ecnu.achieveit.model.ProjectRiskKey;
import com.ecnu.achieveit.model.RiskRelatedKey;

import java.util.List;

public interface ProjectRiskService {

    List<ProjectRisk> queryByProjectId(String projectId);

    List<ProjectRisk> queryByOwnerId(String ownerId);

    List<ProjectRisk> queryByRelatedId(String relatedId);

    boolean add(List<ProjectRisk> risks);

    boolean add(ProjectRisk risk);

    boolean modify(ProjectRisk risk);

    boolean remove(ProjectRiskKey riskKey);

    List<RiskRelatedKey> queryRelatesByProjectIdAndRiskId(String projectId, String riskId);

    boolean addRelated(RiskRelatedKey relatedKey);

    boolean removeRelated(RiskRelatedKey relatedKey);

    boolean modifyRelates(List<RiskRelatedKey> riskRelatedKeys);
}
