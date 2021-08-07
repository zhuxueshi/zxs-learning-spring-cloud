//package com.ls.modules.hbase;
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.hadoop.hbase.HbaseTemplate;
//
//@Slf4j
//@Configuration
//public class HBaseConfig {
//    @Value("${hbase.zookeeper.quorum}")
//    private String zookeeperQuorum;
//
//    @Value("${hbase.zookeeper.property.clientPort}")
//    private String clientPort;
//
//    @Value("${zookeeper.znode.parent}")
//    private String znodeParent;
//
//    @Bean
//    public HbaseTemplate hbaseTemplate() {
////        System.setProperty("hadoop.home.dir", "D:\\zhuxueshi\\hadoop-common-2.2.0-bin-master");
////        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
//
////        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
////        conf.set("hbase.zookeeper.quorum", zookeeperQuorum);
////        conf.set("hbase.zookeeper.property.clientPort", clientPort);
////        conf.set("zookeeper.znode.parent", znodeParent);
////        return new HbaseTemplate(conf);
//
//
//        HbaseTemplate hbaseTemplate = new HbaseTemplate();
//        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum", zookeeperQuorum);
//        conf.set("hbase.zookeeper.port", clientPort);
//        hbaseTemplate.setConfiguration(conf);
//        hbaseTemplate.setAutoFlush(true);
//
//        log.info("hadoop配置信息:{}", conf);
//        return hbaseTemplate;
//    }
//}
