package org.jeecg.boot.starter.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PushCallback implements MqttCallbackExtended {
	@Override
	public void connectionLost(Throwable throwable) {
		// 连接丢失后，一般在这里面进行重连
		log.info("连接断开，可以做重连");
	}

	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		// subscribe后得到的消息会执行到这里面
		log.info("\r\n 设备消息到达,主题 : {}",topic);
		log.info("\r\n 设备消息Qos : {}",mqttMessage.getQos());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		log.info("\r\n MQTT发送数据结果deliveryComplete---------{}",iMqttDeliveryToken.isComplete());
	}

	@Override
	public void connectComplete(boolean reconnect, String serverURI) {
		log.info("\r\n 连接成功");
	}

}
