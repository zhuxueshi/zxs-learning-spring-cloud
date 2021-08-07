package test.udp;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * UDP 搜索者，用于搜索服务支持方
 */
public class UDPSearcher {
    private static DatagramSocket socket = null;
    private static final int LISTEN_PORT = 50727;

static {
    try{
        socket = new DatagramSocket(LISTEN_PORT);
    }catch (Exception e){
        e.printStackTrace();
    }
}
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("UDPSearcher Started.");
        Listener listener = listen();
        sendBroadcast();
        System.in.read();
        List<Device> devices = listener.getDevicesAndClose();
        for (Device device : devices) {
            System.out.println("Device:" + device.toString());
        }
        // 完成
        System.out.println("UDPSearcher Finished.");
    }

    private static Listener listen() throws InterruptedException {
        System.out.println("UDPSearcher start listen.");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        listener.start();

        countDownLatch.await();
        return listener;
    }

    private static void sendBroadcast() throws IOException {
        System.out.println("UDPSearcher sendBroadcast started.");
//        ds = new DatagramSocket(LISTEN_PORT);
//        String requestData = Ciphertext.buildWithPort(LISTEN_PORT);
        String requestData = "{\"timeStamp\":"+System.currentTimeMillis()+",\"Platform\":1,\"AllSearch\":1,\"LocalWebPort\":8686,\"WebServerSend\":true}";
        requestData = JSONObject.parseObject(requestData).toJSONString();
        System.out.println("send udp data:" + requestData);
        byte[] requestDataBytes = requestData.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes, requestDataBytes.length);
//        requestPacket.setAddress(InetAddress.getByName("239.255.255.250"));
//        requestPacket.setAddress(InetAddress.getByName("172.18.199.255"));
//        requestPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        requestPacket.setAddress(InetAddress.getByName("172.18.192.236"));
//        requestPacket.setAddress(InetAddress.getByName("172.18.199.255"));
        requestPacket.setPort(LISTEN_PORT);
        socket.send(requestPacket);
//        socket.close();
        System.out.println("UDPSearcher sendBroadcast finished.");
    }

    private static class Device {
        final int port;
        final String ip;
        final String sn;

        private Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    private static class Listener extends Thread {
        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Device> devices = new ArrayList<>();
        private boolean done = false;
        private DatagramSocket ds = null;


        public Listener(int listenPort, CountDownLatch countDownLatch) {
            super();
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            super.run();
            // 通知已启动
            countDownLatch.countDown();
            try {
                // 监听回送端口
                System.out.println("监听会送端口:" + listenPort);
//                DatagramSocket ds = new DatagramSocket(5789);
                while (!done) {
                    // 构建接收实体
                    final byte[] buf = new byte[1024];
                    DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
                    // 接收
                    socket.receive(receivePack);

                    // 打印接收到的信息与发送者的信息
                    // 发送者的IP地址
                    String ip = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();
                    String data = new String(receivePack.getData(), 0, dataLen);
                    System.out.println("UDPSearcher receive form ip:" + ip + ",port:" + port + ",data:" + data);
                    String sn = Ciphertext.parseSn(data);
                    if (sn != null) {
                        Device device = new Device(port, ip, sn);
                        devices.add(device);
                    }
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            } finally {
                close();
            }
            System.out.println("UDPSearcher listener finished.");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        List<Device> getDevicesAndClose() {
            done = true;
            close();
            return devices;
        }
    }
}




