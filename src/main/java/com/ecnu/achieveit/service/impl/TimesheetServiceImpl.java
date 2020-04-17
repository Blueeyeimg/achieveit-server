package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.*;
import com.ecnu.achieveit.model.Timesheet;
import com.ecnu.achieveit.service.TimesheetService;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class TimesheetServiceImpl implements TimesheetService {
    @Autowired(required = false)
    private ProjectMemberMapper projectMemberMapper;
    @Autowired(required = false)
    private TimesheetMapper timesheetMapper;

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
    public Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    @Override
    public Time stringToTime(String time) {
        return strToTime(time);
    }

    @Override
    public List<String> queryPrimaryFunction(String projectId) {
        return timesheetMapper.selectPrimaryFunctionByProjectId(projectId);
    }

    @Override
    public List<String> querySecondaryFunction(String projectId,String primaryFunction) {
        return timesheetMapper.selectSecondaryFunctionByProjectIdAndPrimaryFunction(projectId,primaryFunction);
    }

    @Override
    public List<String> queryProjectIdByEmployeeId(String employeeId) {
        return projectMemberMapper.selectProjectIdByEmployeeId(employeeId);
    }

    @Override
    public List<String> queryPrimaryActivity() {
        return timesheetMapper.selectAllPrimaryActivity();
    }

    @Override
    public List<String> querySecondaryActivity(String primaryActivity) {
        return timesheetMapper.selectSecondaryActivityByPrimaryActivity(primaryActivity);
    }


    /**
     * 如果是对草稿状态的timesheet进行修改后的提交，则要先删除之前的timesheet记录，然后插入一条新的记录
     * @param timesheet
     * @return
     */
    @Override
    public boolean insertTimesheet(Timesheet timesheet) {
        timesheetMapper.deleteByPrimaryKey(timesheet.getTimesheetId());
        int result = timesheetMapper.insertSelective(timesheet);
        return result != 0;
    }



    @Override
    public boolean updateTimesheet(Timesheet timesheet) {
        int result = timesheetMapper.updateByPrimaryKey(timesheet);
        return result != 0;
    }

    @Override
    public List<Timesheet> queryTimesheetByBossId(String bossId) {
        return timesheetMapper.selectTimesheetByBossId(bossId);
    }

    @Override
    public List<Timesheet> queryTimesheetByEmployeeId(String employeeId) {
        return timesheetMapper.selectIimesheetByEmployeeId(employeeId);
    }

    @Override
    public Timesheet queryByPrimaryKey(Integer timesheetId) {
        return timesheetMapper.selectByPrimaryKey(timesheetId);
    }

    @Override
    public List<Timesheet> queryTimesheetByDate(Date date, String employeeId) {
        return timesheetMapper.selectTimesheetByDate(date,employeeId);
    }

    @Override
    public boolean updateStateByTimesheetId(Integer timesheetId, String state) {
        int result = timesheetMapper.updateStateByTimesheetId(timesheetId,state);
        return result!=0;
    }

    @Override
    public boolean deleteTimesheetByPrimaryKey(Integer timesheetId) {
        int result = timesheetMapper.deleteByPrimaryKey(timesheetId);
        return result!=0;
    }
}
