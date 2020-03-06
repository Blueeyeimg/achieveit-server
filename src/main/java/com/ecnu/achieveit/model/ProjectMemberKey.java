package com.ecnu.achieveit.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**.
 * This class corresponds to the database table project_member
 */
@Getter
@Setter
@ToString
public class ProjectMemberKey implements Serializable {

    private String projectId;

    private String employeeId;

    private static final long serialVersionUID = 1L;

}