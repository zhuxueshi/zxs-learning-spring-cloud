package org.jeecg.modules.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.entity.BasDevice;
import org.jeecg.modules.mapper.BasDeviceMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class BasDeviceServiceImpl extends ServiceImpl<BasDeviceMapper, BasDevice> implements IBasDeviceService {
    @Resource
    private BasDeviceMapper basDeviceMapper;
    @Override
    public List<BasDevice> queryDeviceList(String projectId) {
        List<BasDevice> list = basDeviceMapper.selectList(null);
        log.info("\r\n 查询设备信息:{}",list);
        return list;
    }
}
