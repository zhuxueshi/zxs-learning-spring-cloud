package org.jeecg.boot.starter.mqtt.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 用于mqtt处理类的注解，需要注明父topic值
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MqttController {

	/**
	 * 监听的父topic
	 *
	 * @return 监听的父topic
	 */
	String parentTopic();
}
