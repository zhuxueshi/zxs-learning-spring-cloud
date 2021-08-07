package org.jeecg.boot.starter.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MqttStarter {

	@Autowired
	private MqttPushClient mqttPushClient;

	@Autowired
	MqttConfig mqttConfig;



	private boolean connect() {
		log.info("\r\n 连接MQTT配置信息:\r\n host:{} \r\n clientId:{} \r\n userName:{} \r\n password:{} \r\n timeOut:{} \r\n keepalive:{}",mqttConfig.getHostUrl(), MqttContext.clientId, mqttConfig.getUsername(),
				mqttConfig.getPassword(), mqttConfig.getTimeout(), mqttConfig.getKeepalive());
		boolean isTrue = mqttPushClient.connect(mqttConfig.getHostUrl(), MqttContext.clientId, mqttConfig.getUsername(),
				mqttConfig.getPassword(), mqttConfig.getTimeout(), mqttConfig.getKeepalive());
		return isTrue;
	}

	public void start(String clientId) {
		MqttContext.clientId = clientId ;
		if (MqttContext.runStarted) {
			log.info("启动过,请勿重复启动");
			return;
		}
		MqttContext.runStarted = true;
		if (connect()) {
		} else {
			log.info("连接mqtt失败,稍后会重连");
			new Thread("mqttReconectThread") {
				public void run() {
					while (true) {
						try {
							Thread.sleep(60 * 1000); // 等 一分钟尝试重连一下
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						log.info("mqtt尝试重连");
						if (connect()) {
							break;
						}
					}
				}

			}.start();

		}
	}

}
