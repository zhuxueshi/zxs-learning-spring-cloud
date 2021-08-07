package org.jeecg.modules.cloud.feign.controller;


import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.client.RabbitMqClient;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.base.BaseMap;
import org.jeecg.entity.BasDevice;
import org.jeecg.modules.cloud.constant.CloudConstant;
import org.jeecg.modules.cloud.feign.feign.DoorsServiceClient;
import org.jeecg.modules.cloud.feign.feign.JeecgTestClient;
import org.jeecg.modules.cloud.feign.feign.JeecgTestClientDyn;
import org.jeecg.modules.cloud.feign.feign.ZxsCloudSchoolClient;
import org.jeecg.starter.cloud.feign.impl.JeecgFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/test")
@Api(tags = "【微服务】单元测试")
public class JeecgTestFeignController {

    @Autowired
    private JeecgFeignService jeecgFeignService;
    @Autowired
    private JeecgTestClient jeecgTestClient;
    @Autowired
    private ZxsCloudSchoolClient zxsCloudSchoolClient;
    @Autowired
    private DoorsServiceClient doorsServiceClient;
    @Autowired
    private RabbitMqClient rabbitMqClient;

    @GetMapping("getDeviceList")
    @ApiOperation(value = "服务调用获取设备", notes = "服务调用获取设备")
    public List<BasDevice> getDeviceList() {
        List<BasDevice> result = doorsServiceClient.getDeviceList();
        log.info("\r\n设备信息 doorsServiceClient:{}",result);
        result = zxsCloudSchoolClient.getDeviceList();
        log.info("\r\n设备信息 zxsCloudSchoolClient:{}",result);
        return result;
    }

    @GetMapping("getMessage")
    @ApiOperation(value = "测试feign", notes = "测试feign")
    public Result<String> getMessage() {
        return jeecgTestClient.getMessage("jeecg-boot");
    }

    @GetMapping("getMessage2")
    @ApiOperation(value = "测试动态feign", notes = "测试动态feign")
    public Result<String> getMessage2() {
        JeecgTestClientDyn myClientDyn = jeecgFeignService.newInstance(JeecgTestClientDyn.class, CloudConstant.SERVER_NAME_JEECGDEMO);
        return myClientDyn.getMessage("动态fegin——jeecg-boot2");
    }

    @GetMapping(value = "/rabbitmq")
    @ApiOperation(value = "测试rabbitmq", notes = "测试rabbitmq")
    public Result<?> rabbitMqClientTest(HttpServletRequest req) {
        //rabbitmq消息队列测试
        BaseMap map = new BaseMap();
        map.put("orderId", RandomUtil.randomNumbers(10));
        rabbitMqClient.sendMessage(CloudConstant.MQ_JEECG_PLACE_ORDER, map);
        rabbitMqClient.sendMessage(CloudConstant.MQ_JEECG_PLACE_ORDER_TIME, map,10);

        //rabbitmq消息总线测试
        BaseMap params = new BaseMap();
        params.put("orderId", "123456");
        rabbitMqClient.publishEvent(CloudConstant.MQ_DEMO_BUS_EVENT, params);
        return Result.OK("MQ发送消息成功");
    }
}
