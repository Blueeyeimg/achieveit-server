package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.ProjectDeviceMapper;
import com.ecnu.achieveit.model.Device;
import com.ecnu.achieveit.model.ProjectDevice;
import com.ecnu.achieveit.model.ProjectDeviceKey;
import com.ecnu.achieveit.service.DeviceService;
import com.ecnu.achieveit.service.ProjectDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProjectDeviceServiceImpl implements ProjectDeviceService {

    @Autowired(required = false)
    private ProjectDeviceMapper projectDeviceMapper;

    @Autowired
    private DeviceService deviceService;

    @Override
    public boolean registerDevice(ProjectDevice projectDevice) {
        if(!projectDevice.valid()){
            return false;
        }

        projectDevice.setCheckinDate(new Date());
        projectDevice.setLastVerifyDate(new Date());

        projectDeviceMapper.insertSelective(projectDevice);
        deviceService.removeDevice(new Device(projectDevice.getDeviceId(),projectDevice.getType()));

        return true;
    }

    @Override
    public boolean checkDevice(ProjectDevice projectDevice) {
        if(!projectDevice.valid()){
            return false;
        }
        projectDevice.setLastVerifyDate(new Date());

        projectDeviceMapper.updateByPrimaryKeySelective(projectDevice);
        return true;
    }

    @Override
    public boolean returnDevice(ProjectDevice projectDevice) {
        if(!projectDevice.valid()){
            return false;
        }
        projectDevice.setState(1);
        projectDevice.setReturnDate(new Date());

        projectDeviceMapper.updateByPrimaryKeySelective(projectDevice);
        /**
         * 删除这段代码原因是project_device表以pojectId和deviceId为主键，同一个人归还后再借该设备会导致主键冲突
         * deviceService.addDevice(new Device(projectDevice.getDeviceId(),projectDevice.getType()));*/

        return true;
    }

    @Override
    public ProjectDevice queryByKey(ProjectDeviceKey projectDeviceKey) {
        return projectDeviceMapper.selectByPrimaryKey(projectDeviceKey);
    }

    @Override
    public List<ProjectDevice> queryEmployeeDevice(String employeeId) {
        return projectDeviceMapper.selectListByEmployeeId(employeeId);
    }

    @Override
    public List<ProjectDevice> queryProjectDevice(String projectId) {
        return projectDeviceMapper.selectListByProjectId(projectId);
    }
}
