package com.ecnu.achieveit.dao;

import com.ecnu.achieveit.model.Timesheet;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TimesheetMapper {
    int deleteByPrimaryKey(Integer timesheetId);

    int insert(Timesheet record);

    int insertSelective(Timesheet record);

    Timesheet selectByPrimaryKey(Integer timesheetId);

    int updateByPrimaryKeySelective(Timesheet record);

    int updateByPrimaryKey(Timesheet record);

    List<Timesheet> selectTimesheetByBossId(String bossId);

    List<Timesheet> selectIimesheetByEmployeeId(String employeeId);

    int updateStateByTimesheetId(@Param("timesheetId") Integer timesheetId,
                                 @Param("state") String state);

    List<String> selectPrimaryFunctionByProjectId(String projectId);

    List<String> selectSecondaryFunctionByProjectIdAndPrimaryFunction(@Param("projectId") String projectId,
                                                                      @Param("primaryFunction") String primaryFunction);
    List<String> selectAllPrimaryActivity();

    List<String> selectSecondaryActivityByPrimaryActivity(String primaryActivity);
}