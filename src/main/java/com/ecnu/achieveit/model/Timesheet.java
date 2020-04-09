package com.ecnu.achieveit.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * timesheet
 * @author 
 */
public class Timesheet implements Serializable {
    private Integer timesheetId;

    private String projectId;

    private String employeeId;

    private String primaryFunction;

    private String secondaryFunction;

    private String primaryActivity;

    private String secondaryActivity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date date;

//    @DateTimeFormat(pattern = "HH:mm:ss")

    @JsonFormat(pattern="HH:mm:ss", timezone="GMT+8")
    private Time startTime;

//    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss", timezone="GMT+8")
    private Time endTime;

    /**
     * 草稿、已提交、已确认、打回
     */
    private String state;

    private static final long serialVersionUID = 1L;



    public Integer getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(Integer timesheetId) {
        this.timesheetId = timesheetId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getPrimaryFunction() {
        return primaryFunction;
    }

    public void setPrimaryFunction(String primaryFunction) {
        this.primaryFunction = primaryFunction;
    }

    public String getSecondaryFunction() {
        return secondaryFunction;
    }

    public void setSecondaryFunction(String secondaryFuntion) {
        this.secondaryFunction = secondaryFuntion;
    }

    public String getPrimaryActivity() {
        return primaryActivity;
    }

    public void setPrimaryActivity(String primaryActivity) {
        this.primaryActivity = primaryActivity;
    }

    public String getSecondaryActivity() {
        return secondaryActivity;
    }

    public void setSecondaryActivity(String secondaryActivity) {
        this.secondaryActivity = secondaryActivity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Timesheet other = (Timesheet) that;
        return (this.getTimesheetId() == null ? other.getTimesheetId() == null : this.getTimesheetId().equals(other.getTimesheetId()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getEmployeeId() == null ? other.getEmployeeId() == null : this.getEmployeeId().equals(other.getEmployeeId()))
            && (this.getPrimaryFunction() == null ? other.getPrimaryFunction() == null : this.getPrimaryFunction().equals(other.getPrimaryFunction()))
            && (this.getSecondaryFunction() == null ? other.getSecondaryFunction() == null : this.getSecondaryFunction().equals(other.getSecondaryFunction()))
            && (this.getPrimaryActivity() == null ? other.getPrimaryActivity() == null : this.getPrimaryActivity().equals(other.getPrimaryActivity()))
            && (this.getSecondaryActivity() == null ? other.getSecondaryActivity() == null : this.getSecondaryActivity().equals(other.getSecondaryActivity()))
            && (this.getDate() == null ? other.getDate() == null : this.getDate().equals(other.getDate()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTimesheetId() == null) ? 0 : getTimesheetId().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getEmployeeId() == null) ? 0 : getEmployeeId().hashCode());
        result = prime * result + ((getPrimaryFunction() == null) ? 0 : getPrimaryFunction().hashCode());
        result = prime * result + ((getSecondaryFunction() == null) ? 0 : getSecondaryFunction().hashCode());
        result = prime * result + ((getPrimaryActivity() == null) ? 0 : getPrimaryActivity().hashCode());
        result = prime * result + ((getSecondaryActivity() == null) ? 0 : getSecondaryActivity().hashCode());
        result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", timesheetId=").append(timesheetId);
        sb.append(", projectId=").append(projectId);
        sb.append(", employeeId=").append(employeeId);
        sb.append(", primaryFunction=").append(primaryFunction);
        sb.append(", secondaryFuntion=").append(secondaryFunction);
        sb.append(", primaryActivity=").append(primaryActivity);
        sb.append(", secondaryActivity=").append(secondaryActivity);
        sb.append(", date=").append(date);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", state=").append(state);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}