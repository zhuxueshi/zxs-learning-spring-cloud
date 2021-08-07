package org.jeecg.boot.starter.mqtt.exception;

import org.springframework.beans.BeansException;


public class MqttBeansException extends BeansException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MqttBeansException(String msg) {
		super(msg);
	}

	public MqttBeansException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
