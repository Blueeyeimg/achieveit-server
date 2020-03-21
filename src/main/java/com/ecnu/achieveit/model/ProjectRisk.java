package com.ecnu.achieveit.model;

import lombok.*;

import java.io.Serializable;

/**
 * This class corresponds to the database table project_risk
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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


    public ProjectRisk(String projectId, String riskId, String type, String description, Integer riskLevel, String influence, String reactiveStrategy, String riskState, String riskOwnerId, Long riskTrackFrequency) {
        super(projectId, riskId);
        this.type = type;
        this.description = description;
        this.riskLevel = riskLevel;
        this.influence = influence;
        this.reactiveStrategy = reactiveStrategy;
        this.riskState = riskState;
        this.riskOwnerId = riskOwnerId;
        this.riskTrackFrequency = riskTrackFrequency;
    }
}