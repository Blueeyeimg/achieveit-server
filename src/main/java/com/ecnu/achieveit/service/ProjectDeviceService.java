package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectDevice;
import com.ecnu.achieveit.model.ProjectDeviceKey;

import java.util.List;

public interface ProjectDeviceService {

    /**
     * @description 注册设备给
     * @param projectDevice 不为空的数据 projectId，deviceId，deviceManagerId，type，totalUseTime
     * @return
     */
    boolean registerDevice(ProjectDevice projectDevice);

    boolean checkDevice(ProjectDevice projectDevice);

    boolean returnDevice(ProjectDevice projectDevice);

    ProjectDevice queryByKey(ProjectDeviceKey projectDeviceKey);

    List<ProjectDevice> queryEmployeeDevice(String employeeId);

    List<ProjectDevice> queryProjectDevice(String projectId);

}
