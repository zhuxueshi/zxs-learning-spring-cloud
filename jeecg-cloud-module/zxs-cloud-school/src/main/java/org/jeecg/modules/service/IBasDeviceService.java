package org.jeecg.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.entity.BasDevice;

import java.util.List;

public interface IBasDeviceService extends IService<BasDevice> {
    List<BasDevice> queryDeviceList(String projectId);
}
