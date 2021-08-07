package org.jeecg.boot.starter.mqtt;

import com.alibaba.fastjson.JSON;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.jeecg.boot.starter.mqtt.enums.QosEnum;
import org.jeecg.boot.starter.mqtt.exception.MqttRemoteException;
import org.jeecg.boot.starter.mqtt.remote.MqttFuture;
import org.jeecg.boot.starter.mqtt.remote.MqttMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MqttPushClient {

    private static final Logger logger = LoggerFactory.getLogger(MqttPushClient.class);

    @Autowired
    private PushCallback pushCallback;
    @Autowired
    MqttConfig mqttConfig;
    private static MqttClient client;

    private static MqttClient getClient() {
        return client;
    }

    private static void setClient(MqttClient client) {
        MqttPushClient.client = client;
    }

    /**
     * 客户端连接
     *
     * @param host      ip+端口
     * @param clientID  客户端Id
     * @param username  用户名
     * @param password  密码
     * @param timeout   超时时间
     * @param keepalive 保留数
     */
    public boolean connect(String host, String clientID, String username, String password, int timeout, int keepalive) {
        MqttClient client;
        try {
            client = new MqttClient(host, clientID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setAutomaticReconnect(true);
            options.setKeepAliveInterval(keepalive);
            MqttPushClient.setClient(client);
            try {
                client.setCallback(pushCallback);
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public static void disconnect() {

        try {
            if (client != null) {
                client.disconnect();
                MqttContext.runStarted = false;//可以再次启动
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 发布
     *
     * @param qos         连接方式
     * @param retained    是否保留
     * @param topic       主题
     * @param pushMessage 消息体
     */
    public void publish(int qos, boolean retained, String topic, String pushMessage) {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if (null == mTopic) {
            logger.error("topic not exist");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void publish( String topic, String pushMessage) {
    	logger.info("\r\n MQTT【转换服务MQTT-->设备】下发消息到设备，下发主题:{}，消息内容：{}:" , topic , pushMessage);
    	publish(Integer.valueOf(QosEnum.QOS_1.getValue()), false, topic, pushMessage);
    }


    public String publishSync(String topic, int qos, boolean retained, Object data, int timeout)
            throws MqttRemoteException {
        String mId = UUID.randomUUID().toString().replaceAll("-", "");
        MqttMsg mqttMsg = new MqttMsg(mId, mqttConfig.getClientID() + "/" + MqttConfig.replyParentTopic + "/" + mId, data);
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(JSON.toJSONString(mqttMsg).getBytes());
        MqttFuture mqttFuture = new MqttFuture(mqttMsg, timeout);
        MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
        if (null == mTopic) {
            logger.error("topic not exist");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return mqttFuture.get();
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, IMqttMessageListener listner) {
        logger.info("\r\n 开始订阅主题" + topic);
        try {
            MqttPushClient.getClient().subscribe(topic, listner);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, int qos,IMqttMessageListener listner) {
    	logger.info("\r\n 开始订阅主题" + topic);
    	try {
    		MqttPushClient.getClient().subscribe(topic, qos, listner);
    	} catch (MqttException e) {
    		e.printStackTrace();
    	}
    }

    /**
     * 订阅某个主题
     *
     * @param topic 主题
     * @param qos   连接方式
     */
    public void subscribe(String topic, int qos) {
        logger.info("\r\n 开始订阅主题" + topic);
        try {
            MqttPushClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
