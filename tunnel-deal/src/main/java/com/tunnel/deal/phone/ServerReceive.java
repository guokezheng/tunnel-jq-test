package com.tunnel.deal.phone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerReceive {
    public static void main(String[] args) throws IOException {
        DatagramSocket receivesocket = null;
        DatagramPacket receivepacket = null;
        String receivemsg = null;
        InetAddress clientaddress = null;
        int serverport = 3000;
        byte data[] = null;
        int len = 0;
        int clientport = 0;
        boolean flag = true;
        receivesocket = new DatagramSocket(serverport);
        byte buf[] = new byte[1024];
        receivepacket = new DatagramPacket(buf, buf.length);
        while (flag) {
            receivesocket.receive(receivepacket);
            data = receivepacket.getData();
            len = receivepacket.getLength();
            clientaddress = receivepacket.getAddress();
            clientport = receivesocket.getPort();
            receivemsg = new String(data, 0, len);
         //   receivemsg = receivemsg.substring(receivemsg.lastIndexOf("*") + 1, receivemsg.indexOf("&"));
            String s = receivemsg.replaceAll("#", "");
        //    System.out.println(s);     去除#
            String[] split = receivemsg.split("@");
            String pile = split[1];
            String instructionCode = split[2];
            String dt = split[3];
            System.out.println("服务器接收到来自：" + clientaddress.getAddress() + ":" + clientport + "的桩号为:" + pile + " 指令码为：" + instructionCode + " 数据为：" + dt);
            if (receivemsg.equals("886")) {
                flag = false;
            }
        }
        receivesocket.close();
    }
}

