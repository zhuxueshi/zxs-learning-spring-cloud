package com.ls.modules.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

@Slf4j
@Service
public class BroadcastUdpDeviceService {
    @Autowired
    private IBasDeviceService iBasDeviceService;
    public void listenDevice(){
        System.out.println("开始接受广播消息....");
        MulticastSocket receSocket = null;
        try {
            receSocket = new MulticastSocket(6789);
            InetAddress group = InetAddress.getByName("228.5.6.7");
            receSocket.joinGroup(group);
            while (true) {
                byte[] buff = new byte[1024 * 6];
                DatagramPacket packet = new DatagramPacket(buff, buff.length);
                receSocket.receive(packet);
                byte [] bytes = packet.getData();
                String message = new String(bytes).trim();
                System.out.println("UDP广播设备消息包：" + message);
                iBasDeviceService.saveDeviceInfo(bytes);
            }
        } catch (Exception e) {
            throw new RuntimeException("接受端失败：" + e.getMessage());
        } finally {
            if(receSocket != null) receSocket.close();
        }
    }
}
