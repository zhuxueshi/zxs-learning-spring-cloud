package org.jeecg.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.mqtt.MqttPushClient;
import org.jeecg.boot.starter.mqtt.enums.QosEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SubScribleDeviceMessage implements CommandLineRunner {
    /**
     * @apiNote 订阅设备端发布主题
     */
    @Value("${scx.mqtt.publishTopic:scx/#}")
    private String publishTopic;
    /**
     * @apiNote 订阅设备端状态上下线主题
     */
    @Value("${mqttTopic.topic.base.device.status:$SYS/brokers/+/clients/#}")
    private String subScribleDeviceStatusTopic;
    @Autowired
    private MqttPushClient mqttPushClient;

    public void subscribleTopic(){
        // 此处暂且如此，后面再修改成比较优雅的方式
        mqttPushClient.subscribe(publishTopic, Integer.parseInt(QosEnum.QOS_1.getValue()),(topic, message) -> {
            log.info("\r\n ------------------------------------接收到设备消息---------------------------------------------");
            String messageBody = new String(message.getPayload(), "utf-8");
            log.info("\r\n接收到设备上传消息:{}",messageBody);
        });

        mqttPushClient.subscribe(subScribleDeviceStatusTopic, (topic, message) -> {
            String messageBody = new String(message.getPayload());
            log.info("\r\n 设备上传状态,MQTT主题:{},设备状态消息:{}", topic.contains("disconnected")?"下线了":"上线了",topic, messageBody);
        });
    }

    @Override
    public void run(String... args) {
        subscribleTopic();
        log.info("\r\n 初始化订阅设备主题:[{}],[{}]成功!",publishTopic,subScribleDeviceStatusTopic);
    }
}
