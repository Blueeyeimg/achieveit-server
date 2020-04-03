package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectId;
import com.ecnu.achieveit.model.Timesheet;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface TimesheetService {
    List<String> querryProjectIdByEmployeeId(String employeeId);

    List<String> querryPrimaryFunction();

    List<String> querrySecondaryFunction(String primaryFunction);

    List<String> querryPrimaryActivity();

    List<String> querrySecondaryActivity(String primaryActivity);

    Date getCurrentDate(String date);

    Time  stringToTime(String time);

    boolean insertTimeSheet(Timesheet timesheet);

    boolean updateTimeSheet(Timesheet timesheet);

    List<Timesheet> querryTimeSheetByBossId(String bossId);

    Timesheet querryByPrimaryKey();


}
