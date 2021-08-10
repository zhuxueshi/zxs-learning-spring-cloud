package com.ls.modules.hbase;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class HbaseUtils {
    @Autowired
    private Connection hbaseConnection;
//    public static Admin admin = null;
//    public static Configuration conf = null;
//    public static Connection conn = null;
    /**
     * 构造函数加载配置
     */
//    public HbaseUtils() {
//        Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum","hadoop01");
//        try {
//            conn = ConnectionFactory.createConnection(conf);
//            admin = conn.getAdmin();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public static void main(String[] args) throws Exception {
        HbaseUtils hbase = new HbaseUtils();
        //创建一张表
//		hbase.createTable("doc","cf");
//		//查询所有表名
//		hbase.getALLTable();
//		//往表中添加一条记录
//		hbase.put(hbase.TABLE_NAME, "1", hbase.COLUMNFAMILY_1, hbase.COLUMNFAMILY_1_AUTHOR, "sxt");
//		hbase.addOneRecord("stu","key1","cf","age","24");
//		//查询一条记录
//		hbase.getKey("stu","key1");
//		//获取表的所有数据
//        hbase.getALLData("car");
//		//删除一条记录
//		hbase.deleteOneRecord("stu","key1");
//		//删除表
		hbase.deleteTable("snapRecord");
        //scan过滤器的使用
//		hbase.getScanData("stu","cf","age");
        //rowFilter的使用
        //84138413_20130313145955
        //hbase.getRowFilter("waln_log","^*_201303131400\\d*$");
    }
    /**
     * rowFilter的使用
     * @param tableName
     * @param reg
     * @throws Exception
     */
    public void getRowFilter(String tableName, String reg) throws Exception {
        Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
//		Filter
        RowFilter rowFilter = new RowFilter(CompareOp.NOT_EQUAL, new RegexStringComparator(reg));
        scan.setFilter(rowFilter);
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(new String(result.getRow()));
        }
    }

    public void getScanData(String tableName, String family, String qualifier) throws Exception {
        Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addColumn(family.getBytes(), qualifier.getBytes());
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            if(result.raw().length==0){
                System.out.println(tableName+" 表数据为空！");
            }else{
                for (KeyValue kv: result.raw()){
                    System.out.println(new String(kv.getKey())+"\t"+new String(kv.getValue()));
                }
            }
        }
    }
    public void deleteTable(String tableName) {
        try {
            Admin admin = hbaseConnection.getAdmin();
            if (admin.tableExists(TableName.valueOf(tableName))) {
                admin.disableTable(TableName.valueOf(tableName));
                admin.deleteTable(TableName.valueOf(tableName));
                System.out.println(tableName+"表删除成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(tableName+"表删除失败！");
        }

    }
    /**
     * 删除一条记录
     * @param tableName
     * @param rowKey
     */
    public void deleteOneRecord(String tableName, String rowKey) throws IOException {
        Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        try {
            table.delete(delete);
            System.out.println(rowKey+"记录删除成功！");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(rowKey+"记录删除失败！");
        }
    }
    /**
     * 获取表的所有数据
     * @param tableName
     */
    public List<JSONObject> getALLData(String tableName) {
        List<JSONObject> list = Lists.newArrayList();
        try {
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            int recordCount = 0;
            for (Result result : scanner) {
                List<Cell> cells = result.listCells();
                JSONObject object = new JSONObject();
                object.put("id",new String(result.getRow()));
				for (Cell cell : cells) {
					String qualifier = new String(CellUtil.cloneQualifier(cell));
					String value = new String(CellUtil.cloneValue(cell), "UTF-8");
//					log.info("\r\n【id】:{}\t【{}】:{}",new String(result.getRow()), qualifier ,value);
                    object.put(qualifier,value);
				}
				list.add(object);
                recordCount++;
//				log.info("\r\n===============================================");

//                if(result.raw().length==0){
//                    System.out.println(tableName+" 表数据为空！");
//                }else{
//                    for (KeyValue kv: result.raw()){
//                        System.out.println(new String(kv.getKey(),"utf-8")+"\t"+new String(kv.getValue()));
//                    }
//                }
            }
            log.info("\r\n 查询记录数:共{}条",recordCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 读取一条记录
    @SuppressWarnings({ "deprecation", "resource" })
    public void get(String tableName, String row) throws IOException {
        Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
        Get get = new Get(row.getBytes());
        try {

            Result result = table.get(get);
            KeyValue[] raw = result.raw();
            System.out.println("KeyValue:" + raw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 添加一条记录
    public  void put(String tableName, String row, String columnFamily,
                     String column, String data) throws IOException {
        Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
        Put p1 = new Put(Bytes.toBytes(row));
        p1.addColumn(columnFamily.getBytes(),column.getBytes(),data.getBytes());
        table.put(p1);
        System.out.println("put'" + row + "'," + columnFamily + ":" + column + "','" + data + "'");
    }

    @Before
    public void setup() throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum","node1,node2,node3");
        hbaseConnection = ConnectionFactory.createConnection(config);
    }

    @Test
    public void insert(String tableName,Put put) {
        try {
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
//        Put put = new Put(("1234").getBytes());
//        put.addColumn("cf1".getBytes(),"data1".getBytes(),"abcdf111".getBytes());
//        table.put(put);
            table.put(put);
            log.info("\r\n 保存hbase数据成功[{}]:{}",tableName,put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insert(String tableName,String family,String rowKey,JSONObject object) {
        try {
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            Put put = new Put((rowKey).getBytes());
            object.forEach((key,val)->{
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(key), val.toString().getBytes());
            });
            table.put(put);
            log.info("\r\n 保存hbase数据成功[{}]:{}",tableName,put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量插入或更新
     * @param tableName
     * @param family
     * @param list
     */
    public void batchInsertOrUpdate(String tableName,String family,List<JSONObject> list) {
        if(list == null || CollectionUtils.isEmpty(list) || tableName == null || family == null){
            log.info("\r\n 存在参数为空:tableName:{};family:{};list:{}",tableName,family,list);
            return;
        }
        List<Put> putList = Lists.newArrayList();
        for (JSONObject object : list) {
            String rowKey = object.getString("SerialNum") + "_" + object.getString("Ipaddr");
            Put put = new Put((rowKey).getBytes());
            object.forEach((key,val)->{
                put.addColumn(Bytes.toBytes(family), Bytes.toBytes(key), val.toString().getBytes());
                putList.add(put);
            });
        }
        try {
            long beginTime = System.currentTimeMillis();
            Table table = hbaseConnection.getTable(TableName.valueOf(tableName));
            table.put(putList);
            long endTime = System.currentTimeMillis();
            log.info("\r\n 批量操作hbase成功:共{}条,耗时:{}ms",list.size(),endTime-beginTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询所有表名
     * @return
     * @throws Exception
     */
    public List<String> getALLTable() throws Exception {
        Admin admin = hbaseConnection.getAdmin();
        ArrayList<String> tables = new ArrayList<String>();
        if(admin!=null){
            HTableDescriptor[] listTables = admin.listTables();
            if (listTables.length>0) {
                for (HTableDescriptor tableDesc : listTables) {
                    tables.add(tableDesc.getNameAsString());
                    System.out.println(tableDesc.getNameAsString());
                }
            }
        }
        return tables;
    }
    /**
     * 创建一张表
     * @param tableName
     * @param column
     * @throws Exception
     */
    public void createTable(String tableName, String column){
        try {
            Admin admin = hbaseConnection.getAdmin();
            if(admin.tableExists(TableName.valueOf(tableName))){
                System.out.println(tableName+"表已经存在！");
            }else{
                HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
                tableDesc.addFamily(new HColumnDescriptor(column.getBytes()));
                admin.createTable(tableDesc);
                System.out.println(tableName+"表创建成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 添加列族
     * @param tableName
     * @param column
     * @throws Exception
     */
    public void addColumnFamily(String tableName, String column){
        try {
            Admin admin = hbaseConnection.getAdmin();
            if(!admin.tableExists(TableName.valueOf(tableName))){
                log.info("{}表不存在！",tableName);
            }else{
                admin.addColumn(TableName.valueOf(tableName),new HColumnDescriptor(column.getBytes()));
                log.info("{}表添加列族[{}]成功！",tableName,column);
            }
        } catch (Exception e) {
            log.info("\r\n 添加列族发生异常:{}",e.getMessage());
//            e.printStackTrace();
        }
    }
}
