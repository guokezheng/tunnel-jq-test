package com.tunnel.deal.phone;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientSend {
    public static void main(String[] args) throws Exception{
        //准备需要发送的数据
        BufferedReader reader = null;
        //被发送的数据
        String sendmsg = null;
       //服务器地址 端口号
        InetAddress serveraddress = null;
        int sercerport = 2000;
        DatagramPacket sendpacket = null;
        DatagramSocket sendsocket = null;
        boolean flag = true;
        reader = new BufferedReader(new InputStreamReader(System.in));
        serveraddress = InetAddress.getLocalHost();
        sendsocket = new DatagramSocket();
        while (flag) {
            System.out.println("请输入发送的内容");
            sendmsg = reader.readLine();
            sendpacket = new DatagramPacket(sendmsg.getBytes(), sendmsg.getBytes().length, serveraddress, sercerport);
            sendsocket.send(sendpacket);
            if (sendmsg.equals("886")){
                flag=false;
            }
        }
        sendsocket.close();
    }
}
