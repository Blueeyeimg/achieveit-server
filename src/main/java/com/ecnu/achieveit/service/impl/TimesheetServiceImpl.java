package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.*;
import com.ecnu.achieveit.model.ProjectId;
import com.ecnu.achieveit.model.Timesheet;
import com.ecnu.achieveit.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TimesheetServiceImpl implements TimesheetService {
    @Autowired(required = false)
    private ProjectMemberMapper projectMemberMapper;
    @Autowired(required = false)
    private ActivityMapper activityMapper;
    @Autowired(required = false)
    private TimesheetMapper timesheetMapper;
    @Autowired(required = false)
    private ProjectFunctionMapper projectFunctionMapper;

    /**
     *
     * @param strDate
     * @return java.sql.Date
     */
    public static java.sql.Date strToDate(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(d.getTime());
        return date;
    }

    /**
     *
     * @param strDate
     * @return java.sql.Time
     */
    public static java.sql.Time strToTime(String strDate) {
        String str = strDate;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        java.util.Date d = null;
        try {
            d = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.sql.Time time = new java.sql.Time(d.getTime());
        return time.valueOf(str);
    }

    @Override
    public List<String> querryPrimaryFunction(String projectId) {
        return projectFunctionMapper.selectPrimaryFunctionByProjectId(projectId);
    }

    @Override
    public List<String> querrySecondaryFunction(String projectId,String primaryFunction) {
        return projectFunctionMapper.selectSecondaryFunctionByPrimaryFunctionAndProjectId(projectId,primaryFunction);
    }

    @Override
    public List<String> querryProjectIdByEmployeeId(String employeeId) {
        return projectMemberMapper.selectProjectIdByEmployeeId(employeeId);
    }

    @Override
    public List<String> querryPrimaryActivity() {
        return activityMapper.selectAllPrimaryActivity();
    }

    @Override
    public List<String> querrySecondaryActivity(String primaryActivity) {
        return activityMapper.selectSecondaryActivityByPrimaryActivity(primaryActivity);
    }

    @Override
    public Date getCurrentDate(String date) {
        return  strToDate(date);
    }

    @Override
    public Time stringToTime(String time) {
        return strToTime(time);
    }

    @Override
    public boolean insertTimeSheet(Timesheet timesheet) {
        int result = timesheetMapper.insertSelective(timesheet);
        return result != 0;
    }

    @Override
    public boolean updateTimeSheet(Timesheet timesheet) {
        int result = timesheetMapper.updateByPrimaryKey(timesheet);
        return result != 0;
    }

    @Override
    public List<Timesheet> querryTimeSheetByBossId(String bossId) {
        return timesheetMapper.selectTimesheetByBossId(bossId);
    }

    @Override
    public Timesheet querryByPrimaryKey() {
        return timesheetMapper.selectByPrimaryKey(1);
    }
}
