package com.ls.modules.service.snaprecord;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.ls.modules.hbase.HbaseUtils;
import com.ls.mqtt.MQTTMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class SnapRecordServiceImpl implements MQTTMessageService {
    @Autowired
    private HbaseUtils hbaseUtils;
    @Override
    public void deviceMessageInfo(byte[] bytes) {
        try {
            String messageBody = new String(bytes, "utf-8");
            JSONObject messageObj = JSONObject.parseObject(messageBody);
            messageObj.getJSONObject("datas").put("imageFile","Don't store pictures");
            String number = RandomUtil.randomNumbers(10);
            Put put = new Put(Bytes.toBytes("sr" + number));
            messageObj.forEach((key,value)->{
                put.addColumn(Bytes.toBytes("info"), Bytes.toBytes(key), Bytes.toBytes(value.toString()));
            });
            hbaseUtils.insert("snapRecord",put);
            log.info("\r\n 抓拍记录保存hbase成功:{}",messageObj);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deviceStatus(byte[] bytes) {
        try {
            String messageBody = new String(bytes, "utf-8");
            JSONObject messageObj = JSONObject.parseObject(messageBody);
            log.info("\r\n 设备状态:{}",messageObj);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
