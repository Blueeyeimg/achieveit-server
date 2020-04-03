package com.ecnu.achieveit.dao;

import com.ecnu.achieveit.model.Timesheet;

import java.util.List;

public interface TimesheetMapper {
    int deleteByPrimaryKey(Integer timesheetId);

    int insert(Timesheet record);

    int insertSelective(Timesheet record);

    Timesheet selectByPrimaryKey(Integer timesheetId);

    int updateByPrimaryKeySelective(Timesheet record);

    int updateByPrimaryKey(Timesheet record);

    List<Timesheet> selectTimesheetByBossId(String bossId);
}