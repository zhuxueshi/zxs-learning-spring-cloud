package org.jeecg.modules.controller;


import lombok.extern.slf4j.Slf4j;
import org.jeecg.entity.BasDevice;
import org.jeecg.modules.service.IBasDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 设备表 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-26
 */
@RestController
@RequestMapping("/dev")
@Slf4j
public class BasDeviceController {
	
	@Autowired
	private IBasDeviceService basDeviceService;
	
	/**
	 * @功能：查询日志记录
	 * @param BasDevice
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<BasDevice> queryPageList() {
		List<BasDevice> list = basDeviceService.queryDeviceList(null);
		return list;
	}
	
//	/**
//	 * @功能：删除单个日志记录
//	 * @param id
//	 * @return
//	 */
//	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
//	public Result<BasDevice> delete(@RequestParam(name="id",required=true) String id) {
//		Result<BasDevice> result = new Result<BasDevice>();
//		BasDevice BasDevice = BasDeviceService.getById(id);
//		if(BasDevice==null) {
//			result.error500("未找到对应实体");
//		}else {
//			boolean ok = BasDeviceService.removeById(id);
//			if(ok) {
//				result.success("删除成功!");
//			}
//		}
//		return result;
//	}
//	
//	/**
//	 * @功能：批量，全部清空日志记录
//	 * @param ids
//	 * @return
//	 */
//	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
//	public Result<SysRole> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		Result<SysRole> result = new Result<SysRole>();
//		if(ids==null || "".equals(ids.trim())) {
//			result.error500("参数不识别！");
//		}else {
//			if("allclear".equals(ids)) {
//				this.BasDeviceService.removeAll();
//				result.success("清除成功!");
//			}
//			this.BasDeviceService.removeByIds(Arrays.asList(ids.split(",")));
//			result.success("删除成功!");
//		}
//		return result;
//	}
	
	
}
