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
public class UDPSend {
    public static void main(String[] args) throws UnknownHostException {
        // 此类表示用于发送和接收数据报数据包的套接字。
        DatagramSocket datagramSocket = null;
        try {
            // 获取指定IP地址
            InetAddress id = InetAddress.getByName("183.16.238.103");
            // 绑定8081端口，从8081端口发送数据（此时我们编写的是发送端）
            datagramSocket = new DatagramSocket(8081);
            // 把要发送的消息打包
            byte[] bytes = "一个简单的消息：\r\nHello World".getBytes(StandardCharsets.UTF_8);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, 0, bytes.length, id, 10086);
            // 发送消息
            datagramSocket.send(datagramPacket);
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