package com.ecnu.achieveit.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RiskRelatedKey implements Serializable {

    private String projectId;


    private String riskId;


    private String riskRelatedId;


    private static final long serialVersionUID = 1L;

}