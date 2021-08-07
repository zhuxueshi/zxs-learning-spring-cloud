package org.jeecg.boot.starter.mqtt.annotation;

import java.lang.annotation.*;

/**
 * 该标注自动使得参数自动获得messageBody对象
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqttMessageBody {

}
