package org.jeecg.boot.starter.mqtt.config;

import org.jeecg.boot.starter.mqtt.MqttConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ MqttConfig.class})
@Configuration
public class MqttConfigure {

}
