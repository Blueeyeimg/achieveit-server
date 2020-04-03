package com.ecnu.achieveit.service.impl;

import com.ecnu.achieveit.dao.DeviceMapper;
import com.ecnu.achieveit.model.Device;
import com.ecnu.achieveit.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceServiceImpl implements DeviceService {

    @Autowired(required = false)
    private DeviceMapper deviceMapper;

    @Override
    public boolean addDevice(Device device) {
        return deviceMapper.insertSelective(device) == 1;
    }

    @Override
    public boolean removeDevice(Device device) {
        return deviceMapper.deleteByPrimaryKey(device.getDeviceId()) == 1;
    }

    @Override
    public List<Device> queryDeviceList() {
        return deviceMapper.selectList();
    }

    @Override
    public Device queryDeviceById(String deviceId) {
        return deviceMapper.selectByPrimaryKey(deviceId);
    }
}
