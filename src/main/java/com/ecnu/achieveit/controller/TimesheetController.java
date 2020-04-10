package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.model.Timesheet;
import com.ecnu.achieveit.service.TimesheetService;
import com.ecnu.achieveit.service.impl.TimesheetServiceImpl;
import com.ecnu.achieveit.util.LogUtil;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import static com.ecnu.achieveit.constant.TimesheetState.*;

@RestController
public class TimesheetController {
    @Autowired(required = false)
    private TimesheetService timesheetService;

    @GetMapping("/timesheet/projectid")
    public Object listProjectId(@RequestAttribute("userId")String employeeId){
        List<String> result = timesheetService.queryProjectIdByEmployeeId(employeeId);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/primaryfunction")
    public Object listPrimaryFunction(@RequestParam("projectId")String projectId){
        List<String> result = timesheetService.queryPrimaryFunction(projectId);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/secondaryfunction")
    public Object listSecondaryFunction(@RequestParam("projectId")String projectId,
                                        @RequestParam("primaryFunction")String primaryFunction){
        List<String> result = timesheetService.querySecondaryFunction(projectId,primaryFunction);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/primaryactivity")
    public Object listPrimaryActivity(){
        List<String> result = timesheetService.queryPrimaryActivity();

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/secondaryactivity")
    public Object listSecondaryActivity(@RequestParam("primaryActivity")String primaryActivity){
        List<String> result = timesheetService.querySecondaryActivity(primaryActivity);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @PostMapping("/timesheet")
    public Object saveTimesheet(Timesheet record,
                                @RequestAttribute("userId")String employeeId){
        LogUtil.i("record = " + record + ", employeeId = " + employeeId);
        LogUtil.i("record.getDate() = " + record.getDate().toString());
        //先判断是否超时
        Date previousDate = new Date(record.getDate().getTime());
        LogUtil.i("previousDate = " + previousDate);
        Date currentDate = new java.sql.Date(System.currentTimeMillis());
        if(isOvertime(previousDate,currentDate)) return RestResponse.fail("超时填写");


        record.setEmployeeId(employeeId);
        boolean result = timesheetService.insertTimesheet(record);
        if(!result) return RestResponse.fail("失败");
        return RestResponse.success();
    }

    @GetMapping("/timesheet/user")
    /**
     * 前端应对不同的状态有不同的权限，例如“草稿”和“驳回”是可以编辑的，“已提交和已确认”是不能编辑的
     */
    public Object listTimesheetForEdit(@RequestAttribute("userId")String employeeId){
        List<Timesheet> result = timesheetService.queryTimesheetByEmployeeId(employeeId);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/boss")
    public Object listTimesheetForConfirm(@RequestAttribute("userId") String bossId){
        System.out.println("bossId = " + bossId);
        List<Timesheet> result = timesheetService.queryTimesheetByBossId(bossId);

        if(result == null) return RestResponse.fail("yes");
        return RestResponse.success(result);
    }

    @PostMapping("/timesheet/confirm")
    public Object changeTimesheetState(@RequestParam("timesheetId")Integer timesheetId,
                                       @RequestParam("state")String state){
        if(!(state.equals(CONFIRMED.getState()) || state.equals(BACK.getState()))){
            return RestResponse.fail("状态设置错误");
        }
        boolean result = timesheetService.updateStateByTimesheetId(timesheetId,state);

        if(!result) return RestResponse.fail();
        return RestResponse.success();
    }

    @DeleteMapping("/timesheet")
    public Object deletTimesheetDraft(@RequestParam("timesheetId")Integer timesheetId,
                                      @RequestAttribute("userId")String userId){
        Timesheet result = timesheetService.queryByPrimaryKey(timesheetId);
        if(!(result.getEmployeeId().equals(userId)))
            return RestResponse.fail("只能删除属于该用户自己的工时信息");
        if (!(result.getState().equals(DRAFT.getState())))
            return RestResponse.fail("只能删除状态为草稿的工时信息");

        timesheetService.deleteTimesheetByPrimaryKey(timesheetId);
        LogUtil.i("已删除id为："+timesheetId+"的工时信息");
        return RestResponse.success();
    }

    public static boolean isOvertime(Date date1,Date date2){
        Date obj1 = java.sql.Date.valueOf(date1.toString());
        Date obj2 = java.sql.Date.valueOf(date2.toString());
        long time1 = obj1.getTime();
        long time2 = obj2.getTime();
        final long threeDays = 3*24*3600*1000;
        return ((time1+threeDays) >= time2) ? false:true;
    }

}
