package com.ecnu.achieveit.controller;

import com.ecnu.achieveit.constant.EmployeeTitle;
import com.ecnu.achieveit.constant.ProjectRole;
import com.ecnu.achieveit.model.ProjectDevice;
import com.ecnu.achieveit.model.ProjectMember;
import com.ecnu.achieveit.model.ProjectMemberKey;
import com.ecnu.achieveit.service.DeviceService;
import com.ecnu.achieveit.service.EmployeeService;
import com.ecnu.achieveit.service.ProjectDeviceService;
import com.ecnu.achieveit.service.ProjectMemberService;
import com.ecnu.achieveit.util.LogUtil;
import com.ecnu.achieveit.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional(rollbackFor = Exception.class)
public class ProjectDeviceController {

    @Autowired
    private ProjectMemberService projectMemberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectDeviceService projectDeviceService;

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/device/new")
    public Object getNew(@RequestAttribute("userId") String userId){
        if(!employeeService.checkTitle(userId, EmployeeTitle.MANAGER.getTitleName())){
            return RestResponse.noPermission("该用户不是项目经理!");
        }
        return RestResponse.success(deviceService.queryDeviceList());
    }

    @GetMapping("/device/project/{projectId}")
    public Object listByProject(@PathVariable("projectId") String projectId){
        return RestResponse.success(projectDeviceService.queryProjectDevice(projectId));
    }

    @GetMapping("/device/owner/{employeeId}")
    public Object listByEmployee(@PathVariable("employeeId") String employeeId){
        return RestResponse.success(projectDeviceService.queryEmployeeDevice(employeeId));
    }

    @PostMapping("/device")
    public Object add(@RequestBody ProjectDevice projectDevice, @RequestAttribute("userId") String userId){
        ProjectMember user = projectMemberService.queryMemberByKey(new ProjectMemberKey(projectDevice.getProjectId(),userId));
        if(ObjectUtils.isEmpty(user) || !ProjectRole.MANAGER.in(user.getRole())){
            return RestResponse.noPermission("当前用户不是该项目的项目经理，无法为项目添加设备" + user.getRole());
        }

        if(projectDeviceService.registerDevice(projectDevice)){
            return RestResponse.success();
        }
        return RestResponse.fail();
    }

    @PutMapping("/device/check")
    public Object check(@RequestBody ProjectDevice projectDevice, @RequestAttribute("userId") String userId){
        if(!projectDeviceService.queryByKey(projectDevice).getDeviceManagerId().equals(userId)){
            return RestResponse.noPermission("当前用户不是该设备的管理员，无法核对设备完好");
        }

        if(projectDeviceService.checkDevice(projectDevice)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

    @PutMapping("/device/return")
    public Object update(@RequestBody ProjectDevice projectDevice, @RequestAttribute("userId") String userId){
        if(!projectDeviceService.queryByKey(projectDevice).getDeviceManagerId().equals(userId)){
            return RestResponse.noPermission("当前用户不是该设备的管理员，无法核对设备完好");
        }

        if(projectDeviceService.returnDevice(projectDevice)){
            return RestResponse.success();
        }

        return RestResponse.fail();
    }

}
