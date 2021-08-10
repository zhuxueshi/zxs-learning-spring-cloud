package com.ls.modules.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.ls.modules.hbase.HbaseUtils;
import com.ls.modules.mapper.BasDeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.jeecg.entity.BasDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class BasDeviceServiceImpl extends ServiceImpl<BasDeviceMapper, BasDevice> implements IBasDeviceService {
    @Resource
    private BasDeviceMapper basDeviceMapper;
    @Autowired
    @Lazy
    private BroadcastUdpDeviceService broadcastUdpDeviceService;
    @Resource
    private HbaseUtils hbaseUtils;
    @Override
    public List<BasDevice> queryDeviceList(String projectId) {
//        List<BasDevice> list = basDeviceMapper.selectList(null);
        List<BasDevice> list = basDeviceMapper.queryDeviceList(null);
        log.info("\r\n 查询设备信息:{}",list);
        return list;
    }

    @Override
    public void saveDeviceInfo(byte [] bytes) {
        doData(bytes);
    }

    @Override
    public List<JSONObject> searchDevice() {
        broadcastUdpDeviceService.searchFK0();
        List<JSONObject> list = hbaseUtils.getALLData("bas_device");
        return list;
    }

    @Override
    public void batchSaveDeviceFR0(List<JSONObject> list) {
        if(CollectionUtils.isNotEmpty(list)) hbaseUtils.batchInsertOrUpdate("bas_device","info",list);
    }

    public void doData(byte [] datas){
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String data = new String(datas);
                String sign [] = data.split("\r\n");
                int i = 0;
                int index = 0;
                TreeMap<String,byte[]> deviceInfoList = new TreeMap<String, byte[]>();
                deviceInfoList.put("00", new byte[24]);
                deviceInfoList.put("01", new byte[16]);
                deviceInfoList.put("02", new byte[16]);
                deviceInfoList.put("03", new byte[16]);
                deviceInfoList.put("04", new byte[16]);
                deviceInfoList.put("05", new byte[33]);
                deviceInfoList.put("06", new byte[23]);
                deviceInfoList.put("07", new byte[16]);
                deviceInfoList.put("08", new byte[144]);
                deviceInfoList.put("09", new byte[16]);
                deviceInfoList.put("10", new byte[128]);
                deviceInfoList.put("11", new byte[32]);
                deviceInfoList.put("12", new byte[32]);
                deviceInfoList.put("13", new byte[73]);
                deviceInfoList.put("14", new byte[32]);
                deviceInfoList.put("15", new byte[32]);
                deviceInfoList.put("16", new byte[64]);
                for (byte bytedata : datas) {
                    byte [] objByte_00 = deviceInfoList.get("00");
                    int start_00 = 0;
                    int end_00 = objByte_00.length;
                    if(i >= start_00 && i < end_00) {
                        if(i==0) index = 0;
                        objByte_00[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_01 = deviceInfoList.get("01");
                    int start_01 = end_00;
                    int end_01 = start_01 + objByte_01.length;
                    if(i >= start_01 && i < end_01) {
                        if(i==start_01) index = 0;
                        objByte_01[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_02 = deviceInfoList.get("02");
                    int start_02 = end_01;
                    int end_02 = start_02 + objByte_02.length;
                    if(i >= start_02 && i < end_02) {
                        if(i==start_02) index = 0;
                        objByte_02[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_03 = deviceInfoList.get("03");
                    int start_03 = end_02;
                    int end_03 = start_03 + objByte_03.length;
                    if(i >= start_03 && i < end_03) {
                        if(i==start_03) index = 0;
                        objByte_03[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_04 = deviceInfoList.get("04");
                    int start_04 = end_03;
                    int end_04 = start_04 + objByte_04.length;
                    if(i >= start_04 && i < end_04) {
                        if(i==start_04) index = 0;
                        objByte_04[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_05 = deviceInfoList.get("05");
                    int start_05 = end_04;
                    int end_05 = start_05 + objByte_05.length;
                    if(i >= start_05 && i < end_05) {
                        if(i==start_05) index = 0;
                        objByte_05[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_06 = deviceInfoList.get("06");
                    int start_06 = end_05;
                    int end_06 = start_06 + objByte_06.length;
                    if(i >= start_06 && i < end_06) {
                        if(i==start_06) index = 0;
                        objByte_06[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_07 = deviceInfoList.get("07");
                    int start_07 = end_06;
                    int end_07 = start_07 + objByte_07.length;
                    if(i >= start_07 && i < end_07) {
                        if(i==start_07) index = 0;
                        objByte_07[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_08 = deviceInfoList.get("08");
                    int start_08 = end_07;
                    int end_08 = start_08 + objByte_08.length;
                    if(i >= start_08 && i < end_08) {
                        if(i==start_08) index = 0;
                        objByte_08[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_09 = deviceInfoList.get("09");
                    int start_09 = end_08;
                    int end_09 = start_09 + objByte_09.length;
                    if(i >= start_09 && i < end_09) {
                        if(i==start_09) index = 0;
                        objByte_09[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_10 = deviceInfoList.get("10");
                    int start_10 = end_09;
                    int end_10 = start_10 + objByte_10.length;
                    if(i >= start_10 && i < end_10) {
                        if(i==start_10) index = 0;
                        objByte_10[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_11 = deviceInfoList.get("11");
                    int start_11 = end_10;
                    int end_11 = start_11 + objByte_11.length;
                    if(i >= start_11 && i < end_11) {
                        if(i==start_11) index = 0;
                        objByte_11[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_12 = deviceInfoList.get("12");
                    int start_12 = end_11;
                    int end_12 = start_12 + objByte_12.length;
                    if(i >= start_12 && i < end_12) {
                        if(i==start_12) index = 0;
                        objByte_12[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_13 = deviceInfoList.get("13");
                    int start_13 = end_12;
                    int end_13 = start_13 + objByte_13.length;
                    if(i >= start_13 && i < end_13) {
                        if(i==start_13) index = 0;
                        objByte_13[index] = bytedata;
                        index++;
                    }

                    byte [] objByte_14 = deviceInfoList.get("14");
                    int start_14 = end_13;
                    int end_14 = start_14 + objByte_14.length;
                    if(i >= start_14 && i < end_14) {
                        if(i==start_14) index = 0;
                        objByte_14[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_15 = deviceInfoList.get("15");
                    int start_15 = end_14;
                    int end_15 = start_15 + objByte_15.length;
                    if(i >= start_15 && i < end_15) {
                        if(i==start_15) index = 0;
                        objByte_15[index] = bytedata;
                        index++;
                    }
                    byte [] objByte_16 = deviceInfoList.get("16");
                    int start_16 = end_15;
                    int end_16 = start_16 + objByte_16.length;
                    if(i >= start_16 && i < end_16) {
                        if(i==start_16) index = 0;
                        objByte_16[index] = bytedata;
                        index++;
                    }
                    i++;
                }
                if(data.contains("IPC") && sign.length < 2) {
                    System.out.println("================================================================================");
                    saveToHbase(deviceInfoList);
                }
            }
        });
    }

    public void saveToHbase(TreeMap<String, byte[]> deviceInfoList){
        byte [] bytes = deviceInfoList.get("11");
        if(bytes.length == 0) return;
        Map<String,byte []> keyVal = Maps.newHashMap();
        keyVal.put("00","port".getBytes());
        keyVal.put("01","ip".getBytes());
        keyVal.put("02","mask".getBytes());
        keyVal.put("03","getway".getBytes());
        keyVal.put("04","dns".getBytes());
        keyVal.put("05","mac".getBytes());
        keyVal.put("07","device_name".getBytes());
        keyVal.put("09","device_type".getBytes());
        keyVal.put("10","version".getBytes());
        keyVal.put("11","device_sn".getBytes());
        keyVal.put("12","account".getBytes());
        keyVal.put("13","password".getBytes());
        Put put = new Put(Bytes.toBytes(new String(deviceInfoList.get("11")).trim()));
        deviceInfoList.forEach((key,value)->{
            byte [] dataBytes = keyVal.get(key);
            boolean isNotEmpy = dataBytes != null;
            if(key != null && !"".equals(key) && isNotEmpy){
                try {
                    String keyName = new String(dataBytes).trim();
                    String str = new String(value,0,value.length, Charset.forName("UTF-8")).trim();//ISO-8859-1,GB18030
                    put.addColumn(Bytes.toBytes("info"), Bytes.toBytes(keyName), str.getBytes());
                    System.out.println(key + "：" + str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                log.info("\r\n key:{};isNotEmpy:{}",key,isNotEmpy);
            }
        });
        hbaseUtils.insert("bas_device",put);
    }
}
