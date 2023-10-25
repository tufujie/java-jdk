package com.jef.io.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * @author Jef
 * @date 2023/10/25
 */
public class UDPReceive {

    public static void main(String[] args) throws UnknownHostException {
        // 此类表示用于发送和接收数据报数据包的套接字。
        DatagramSocket datagramSocket = null;
        try {
            // 绑定到10086端口，方便从10086端口接收数据（此时我们编写的是接收端）
            datagramSocket = new DatagramSocket(10086);

            // 用来接收消息的包
            byte[] bytes = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length);

            // 接收数据
            datagramSocket.receive(datagramPacket);
            // 获取数据
            byte[] data = datagramPacket.getData();
            // 获取数据长度
            int length = datagramPacket.getLength();
            // 接收到哪个ip发来的数据
            InetAddress address = datagramPacket.getAddress();
            // 对方使用哪个端口发送的
            int port = datagramPacket.getPort();
            String str = new String(data, 0, length, StandardCharsets.UTF_8);
            System.out.println("接收数据:");
            System.out.println(str);
            System.out.println("从哪个ip发送来的数据：" + address + " 对方使用哪个端口发送数据：" + port);
            // 打印消息：
            // 接收数据:
            // 一个简单下消息：
            // 您好朋友
            // 从哪个ip发送来的数据：/183.16.238.103 对方使用哪个端口发送数据8081
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭Socket对象
            if (datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }
}