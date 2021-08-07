package com.ls.mqtt;

public interface MQTTMessageService {
    void deviceMessageInfo(byte [] bytes);
    void deviceStatus(byte [] bytes);
}
