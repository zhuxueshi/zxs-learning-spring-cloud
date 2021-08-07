package org.jeecg.boot.starter.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.mqtt.annotation.MqttController;
import org.jeecg.boot.starter.mqtt.annotation.MqttTopicMapping;
import org.jeecg.boot.starter.mqtt.exception.MqttBeansException;
import org.jeecg.boot.starter.mqtt.handler.MqttHandlerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
@Slf4j
public class MqttBeanPostProcessor  implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<? extends Object> beanClazz = bean.getClass();
		if (beanClazz.isAnnotationPresent(MqttController.class)) {
			String parentTopic = ((MqttController) beanClazz.getAnnotation(MqttController.class)).parentTopic();
			MqttHandlerFactory.getParentTopicSet().add(parentTopic);
			for (Method method : beanClazz.getMethods()) {
				if (method.isAnnotationPresent(MqttTopicMapping.class)) {
					String subTopic = method.getAnnotation(MqttTopicMapping.class).subTopic();
					String realTopic;
					if ("".equals(subTopic)) {
						realTopic = parentTopic + "/";
					} else {
						realTopic = (parentTopic + "/" + subTopic ).replaceAll("/+", "/");
					}
					if (null != MqttHandlerFactory.getMqttHandler(realTopic)) {
						throw new MqttBeansException(bean.getClass().getSimpleName() + " topic 重复定义,值为" + realTopic);
					}
					MqttHandlerFactory.registerMqttHandler(realTopic, method);
					MqttHandlerFactory.registerMqttController(realTopic, bean);
					log.info("MqttHandler Mapped \"{}\" onto {}", realTopic, method.toString());
				}
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
