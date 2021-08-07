
package org.jeecg.boot.starter.mqtt.remote;

import lombok.Data;

import java.io.Serializable;


@Data
public class MqttMsg implements Serializable {

	private static final long serialVersionUID = 6648680154051903549L;

	/**
	 * 是否需要同步返回，默认为true
	 */
	private boolean syncFlag = true;

	/**
	 * 生成的id，用uuid生成把
	 */
	private final String mId;

	/**
	 * 客户端返回消息的Topic
	 */
	private final String replyTopic;

	/**
	 * 发送数据
	 */
	private Object data;

	public MqttMsg(String mId, String replyTopic, Object data) {
		this.mId = mId;
		this.replyTopic = replyTopic;
		this.data = data;
	}
}
