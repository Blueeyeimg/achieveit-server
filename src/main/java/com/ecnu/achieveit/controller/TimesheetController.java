package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.model.Timesheet;
import com.ecnu.achieveit.service.TimesheetService;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimesheetController {
    @Autowired(required = false)
    private TimesheetService timesheetService;

    @GetMapping("/timesheet/primaryfunction")
    public Object listPrimaryFunction(@RequestParam("projectId")String projectId){
        List<String> result = timesheetService.querryPrimaryFunction(projectId);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/secondaryfunction")
    public Object listSecondaryFunction(@RequestParam("projectId")String projectId,
                                        @RequestParam("primaryFunction")String primaryFunction){
        List<String> result = timesheetService.querrySecondaryFunction(projectId,primaryFunction);
        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/primaryactivity")
    public Object listPrimaryActivity(){
        List<String> result = timesheetService.querryPrimaryActivity();

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @GetMapping("/timesheet/secondaryactivity")
    public Object listSecondaryActivity(@RequestParam("primaryActivity")String primaryActivity){
        List<String> result = timesheetService.querrySecondaryActivity(primaryActivity);

        if(result == null) return RestResponse.fail();
        return RestResponse.success(result);
    }

    @PostMapping("/timesheet")
    public Object insertTimesheet(Timesheet record){
        boolean result = timesheetService.insertTimeSheet(record);

        if(result) return RestResponse.fail();
        return RestResponse.success();
    }

    @GetMapping("/timesheet")
    public Object listTimesheet(@RequestAttribute("userId") String bossId){
        System.out.println("bossId = " + bossId);
        List<Timesheet> result = timesheetService.querryTimeSheetByBossId(bossId);

        if(result == null) return RestResponse.fail("yes");
        return RestResponse.success(result);
    }

}
