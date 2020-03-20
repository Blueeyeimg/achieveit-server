package com.ecnu.achieveit.modelview;

import com.ecnu.achieveit.model.ProjectBasicInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProjectBasicInfoView implements Serializable{

    private String taskId;

    private ProjectBasicInfo projectBasicInfo;

    private static final long serialVersionUID = 1L;
}
