package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectId;
import com.ecnu.achieveit.model.Timesheet;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface TimesheetService {
    List<String> queryProjectIdByEmployeeId(String employeeId);

    List<String> queryPrimaryFunction(String projectId);

    List<String> querySecondaryFunction(String projectId,String primaryFunction);

    List<String> queryPrimaryActivity();

    List<String> querySecondaryActivity(String primaryActivity);

    Date getCurrentDate();

    Time  stringToTime(String time);

    boolean insertTimesheet(Timesheet timesheet);

    boolean updateTimesheet(Timesheet timesheet);

    boolean updateStateByTimesheetId(String timesheetId,String state);

    List<Timesheet> queryTimesheetByBossId(String bossId);

    List<Timesheet> queryTimesheetByEmployeeId(String employeeId);

    Timesheet queryByPrimaryKey();


}
