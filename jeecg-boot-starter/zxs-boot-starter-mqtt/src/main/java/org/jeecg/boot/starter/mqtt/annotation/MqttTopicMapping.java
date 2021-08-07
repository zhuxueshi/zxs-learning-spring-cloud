package org.jeecg.boot.starter.mqtt.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqttTopicMapping {

	/**
	 * 订阅的子topic，默认可以只订阅1级topic
	 *
	 * @return 订阅的子topic
	 */
	String subTopic() default "";
}
