package com.ls.modules.controller;


import cn.hutool.core.util.RandomUtil;
import com.ls.modules.hbase.HbaseUtils;
import com.ls.modules.service.IBasDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.jeecg.entity.BasDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
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
	 * 内部已实现线程安全的连接池
	 */
//	@Autowired
//	private Connection hbaseConnection;
	@Autowired
	private HbaseUtils hbaseUtils;
//	@Autowired
//	private HBaseService hBaseService;

	/**
	 * @功能：查询日志记录
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<BasDevice> queryPageList() {
//		List<String> rowKeys = Lists.newArrayList();
//		rowKeys.add("p001");
//		rowKeys.add("p002");
//		hbaseUtils.deleteTable("snapRecord");
//		hBaseService.getListRowkeyData("car",rowKeys,"personInfo","name");
//		hbaseUtils.createTable("snapRecord","personInfo");
//		hbaseUtils.addColumnFamily("snapRecord","carInfo");
//		hbaseUtils.addColumnFamily("snapRecord","info");
		hbaseUtils.getALLData("bas_device");//snapRecord,bas_device




//		try (Table table = hbaseConnection.getTable(TableName.valueOf("car"))) {//获取表连接
//			log.info("\r\n========开始往hbase插入数据========================");
//			String number = RandomUtil.randomNumbers(10);
//			Put put = new Put(Bytes.toBytes("p" + number));
//			put.addColumn(Bytes.toBytes("personInfo"), Bytes.toBytes("name"), Bytes.toBytes("zxy" + number));
//			put.addColumn(Bytes.toBytes("personInfo"), Bytes.toBytes("carNo"), Bytes.toBytes("粤.E-" + number));
//			put.addColumn(Bytes.toBytes("personInfo"), Bytes.toBytes("color"), Bytes.toBytes("blue"));
//			table.put(put);
//			log.info("\r\n========hbase插入数据成功:\r\n{}",put);
//
//			Scan scan = new Scan();
////			scan.addFamily(Bytes.toBytes("personInfo"));
//			ResultScanner resultScanner = table.getScanner(scan);
//			for (Iterator<Result> it = resultScanner.iterator(); it.hasNext();) {
//				Result result = it.next();
//				List<Cell> cells = result.listCells();
//				for (Cell cell : cells) {
//					String qualifier = new String(CellUtil.cloneQualifier(cell));
//					String value = new String(CellUtil.cloneValue(cell), "UTF-8");
//					log.info("\r\nid:{}\t{}:{}",new String(result.getRow()), qualifier ,value);
//				}
//			}
//		} catch (Exception e){
//			e.printStackTrace();
//		}
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
//			result.success(".!");
//		}
//		return result;
//	}
	
	
}
