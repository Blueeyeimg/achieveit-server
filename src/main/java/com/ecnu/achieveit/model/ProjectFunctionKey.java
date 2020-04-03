package com.ecnu.achieveit.model;

import lombok.*;

import java.io.Serializable;

/**
 * This class corresponds to the database table project_function
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFunctionKey implements Serializable {

    private String projectId;

    private String primaryFunction;

    private String secondaryFunction;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjectFunctionKey)) {
            return false;
        }

        ProjectFunctionKey that = (ProjectFunctionKey) o;

        if (!getProjectId().equals(that.getProjectId())) {
            return false;
        }
        if (!getPrimaryFunction().equals(that.getPrimaryFunction())) {
            return false;
        }
        return getSecondaryFunction().equals(that.getSecondaryFunction());
    }

    @Override
    public int hashCode() {
        int result = getProjectId().hashCode();
        result = 31 * result + getPrimaryFunction().hashCode();
        result = 31 * result + getSecondaryFunction().hashCode();
        return result;
    }
}