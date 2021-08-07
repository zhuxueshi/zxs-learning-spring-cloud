
package org.jeecg.boot.starter.mqtt.exception;


public class MqttTimeoutException extends MqttRemoteException {

	private static final long serialVersionUID = -4541083227944111553L;

	public MqttTimeoutException() {
		super();
	}

	public MqttTimeoutException(String message) {
		super(message);
	}
}
