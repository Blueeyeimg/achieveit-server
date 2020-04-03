package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.Device;

import java.util.List;

public interface DeviceService {

    boolean addDevice(Device device);

    boolean removeDevice(Device device);

    List<Device> queryDeviceList();

    Device queryDeviceById(String deviceId);

}
