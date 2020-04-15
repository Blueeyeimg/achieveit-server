package com.ecnu.achieveit.modelview;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RiskTrackEmail implements Serializable {

    private String projectId;

    private String projectName;

    private String managerId;

    private String managerName;

    private String managerEmail;

    private static final long serialVersionUID = 1L;
}
