package com.ls.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.entity.BasDevice;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 设备 Mapper 接口
 * <p>
 * 
 * @Author: Steve
 * @Since：   2019-01-22
 */
public interface BasDeviceMapper extends BaseMapper<BasDevice> {
	/**
	 * 根据查询设备集合
	 */
	List<BasDevice> queryDeviceList(@Param("projectId") String projectId);

}
