package org.jeecg.boot.starter.mqtt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("mqtt")
@Setter
@Getter
@Configuration
public class MqttConfig implements BeanPostProcessor {
   boolean enable ;
	/**
	 * 用户名
	 */
	// @Value("username")
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 连接地址
	 */
	private String hostUrl;
	/**
	 * 客户Id
	 */
	private String clientID;
	/**
	 * 默认连接话题
	 */
	private String defaultTopic;
	/**
	 * 超时时间
	 */
	private int timeout;
	/**
	 * 保持连接数
	 */
	private int keepalive;
	
	
	

	public final static  String replyParentTopic = "reply";

}
