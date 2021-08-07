package test.udp;

import com.alibaba.fastjson.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpTest {
    public static void main(String[] args) {
//        ipAddressIsable();
        udpGB();
    }

    public static void ipAddressIsable(){
        try {
            boolean status = InetAddress.getByName("171.18.193.114").isReachable(1000);
            System.out.println("IP 地址是否可通信:" + status);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void udpGB() {
        try {
            // 创建广播
            InetAddress broadcastAddress = InetAddress.getByName("172.18.199.255");
            DatagramSocket socket = new DatagramSocket();
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
                    String message = new String(receivePacket.getData()).trim();
                    System.out.println("广播获取消息包：" + message);
                    count++;
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("搜索不到设备了");
                    System.out.println("总共搜索设备数:" + count + "个");
                    break;
                }
            }
            // 回复数据
            String replyData = "ok";
            DatagramPacket reply = new DatagramPacket(replyData.getBytes(), replyData.getBytes().length, receivePacket.getAddress(), receivePacket.getPort());
            socket.send(reply);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
