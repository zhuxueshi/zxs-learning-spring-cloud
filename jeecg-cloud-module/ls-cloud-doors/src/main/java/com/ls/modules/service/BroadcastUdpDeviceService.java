package com.ls.modules.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;

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
    @Async
    public void searchFK0() {
        List<JSONObject> list = Lists.newArrayList();
        DatagramSocket socket = null;
        log.info("\r\n 开始广播搜索设备.....");
        try {
            socket = new DatagramSocket();
            InetAddress broadcastAddress = InetAddress.getByName("172.18.199.255");
            socket.setSoTimeout(1000); // 1秒时间应该足够了
            JSONObject broadMsg = new JSONObject();
            broadMsg.put("AllSearch", 1);
            broadMsg.put("LocalWebPort", 8080);
            broadMsg.put("WebServerSend", true);
            broadMsg.put("Platform", 1);
            broadMsg.put("timeStamp", System.currentTimeMillis());
            // 发送数据
            String sendData = broadMsg.toJSONString();
            DatagramPacket sendPacket = new DatagramPacket(sendData.getBytes(), sendData.getBytes().length, broadcastAddress, 50727);
            socket.send(sendPacket);
            // 接收数据
            byte[] buffer = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            int count = 0;
            while (true) {
                try {
                    socket.receive(receivePacket);
                    byte [] bytes = receivePacket.getData();
                    String message = new String(bytes,0,receivePacket.getLength());
                    JSONObject device = JSONObject.parseObject(message.trim());
                    log.info("\r\n {}",device);
                    list.add(device);
                    count++;
                    Thread.sleep(200);
                } catch (Exception e) {
                    System.out.println("搜索不到设备了");
                    System.out.println("总共搜索设备数:" + count + "个");
                    break;
                }
            }
            iBasDeviceService.batchSaveDeviceFR0(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("\r\n 设备搜索结束.....");
            if(socket!=null)socket.close();
        }
//        return list;
    }
}
