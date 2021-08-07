package org.jeecg.boot.starter.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class MqttApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Value("${mqtt.clientID}")
	private String clientId;

	@Autowired
	MqttStarter mqttStarter ;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		mqttStarter.start(clientId);
	}

}
