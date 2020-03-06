package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * This class corresponds to the database table project_risk
 */
@Getter
@Setter
@ToString
public class ProjectRiskKey implements Serializable {

    private String projectId;

    private String riskId;

    private static final long serialVersionUID = 1L;

}