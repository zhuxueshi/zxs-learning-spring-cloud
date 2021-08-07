package com.ls.modules.controller;

import com.alibaba.fastjson.JSONObject;
import com.ls.modules.hbase.HbaseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Hbase 数据库操作:创建表,列族,以及增删改查等
 * </p>
 *
 * @Author zhuxueshi
 * @since 2021-08-07
 */
@RestController
@RequestMapping("/hbase")
@Slf4j
public class HbaseOperateController {
    @Autowired
    private HbaseUtils hbaseUtils;

    @RequestMapping(value = "/createTable")
    public JSONObject createTable(@RequestParam String tableName,@RequestParam String family) {
        hbaseUtils.createTable(tableName,family);
        JSONObject object = new JSONObject();
        object.put("tableName",tableName);
        object.put("family",family);
        return object;
    }

    @RequestMapping(value = "/deleteTable")
    public JSONObject deleteTable(@RequestParam String tableName) {
        hbaseUtils.deleteTable(tableName);
        JSONObject object = new JSONObject();
        object.put("tableName",tableName);
        return object;
    }

    @RequestMapping(value = "/addColumnFamily")
    public JSONObject addColumnFamily(@RequestParam String tableName,@RequestParam String family) {
        hbaseUtils.addColumnFamily(tableName,family);
        JSONObject object = new JSONObject();
        object.put("tableName",tableName);
        object.put("family",family);
        return object;
    }
}
