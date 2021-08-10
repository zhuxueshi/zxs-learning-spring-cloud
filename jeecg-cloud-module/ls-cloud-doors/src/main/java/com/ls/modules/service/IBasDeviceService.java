package com.ls.modules.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.entity.BasDevice;

import java.util.List;

public interface IBasDeviceService extends IService<BasDevice> {
    List<BasDevice> queryDeviceList(String projectId);
    List<JSONObject> searchDevice();
    void saveDeviceInfo(byte [] deviceInfo);
    void batchSaveDeviceFR0(List<JSONObject> list);
}
