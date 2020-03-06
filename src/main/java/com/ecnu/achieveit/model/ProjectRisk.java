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
public class ProjectRisk extends ProjectRiskKey implements Serializable {

    private String type;

    private String description;

    private Integer riskLevel;

    private String influence;

    private String reactiveStrategy;

    private String riskState;

    private String riskOwnerId;

    private Long riskTrackFrequency;

    private static final long serialVersionUID = 1L;

}