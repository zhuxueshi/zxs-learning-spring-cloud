package test.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.util.UUID;

/**
 * UDP 提供者，用于提供服务
 */
public class UDPProvider {

    public static void main(String[] args) throws IOException {
        // 生成一份唯一标示
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);
        provider.start();

        System.in.read();
        provider.exit();
    }

    private static class Provider extends Thread {
        private final String sn;
        private boolean done = false;
        private DatagramSocket ds = null;

        public Provider(String sn) {
            super();
            this.sn = sn;
        }

        @Override
        public void run() {
            System.out.println("UDPProvider Started.");
            try {
//                ds = new DatagramSocket(6789);
                ds = new MulticastSocket(6789);
                System.out.println("监听广播消息中......");
                while (!done) {
                    final byte[] buf = new byte[4096];
                    DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
                    ds.receive(receivePack);

                    String ip = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();
                    String data = new String(receivePack.getData(), 0, dataLen,"utf-8");
                    System.out.println("UDPProvider receive form ip:" + ip + ",port:" + port + ",data:" + data);
                    // 解析端口号
                    int responsePort = Ciphertext.parsePort(data);
                    if (responsePort != -1) {
                        // 构建一份回送数据
                        String responseData = Ciphertext.buildWithSn(sn);
                        byte[] responseDataBytes = responseData.getBytes();
                        // 直接根据发送者构建一份回送信息
                        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes, responseDataBytes.length,
                                receivePack.getAddress(),responsePort);
                        ds.send(responsePacket);
                        System.out.println("构建一份回复消息响应");
                    }
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
            } finally {
                close();
            }
            System.out.println("UDPProvider Finished.");
        }
        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        void exit() {
            done = true;
            close();
        }
    }
}

