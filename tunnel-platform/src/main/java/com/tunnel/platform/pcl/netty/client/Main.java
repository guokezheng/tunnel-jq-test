package com.tunnel.platform.pcl.netty.client;


import com.tunnel.platform.pcl.netty.utils.ModBusTCP;

public class Main {

    public static void main(String[] args) throws Exception{
        String code = "00000000000601030000000A";
        String ip = "127.0.0.1";
        int port = 60000;
        Thread thread = new Thread(new ModBusTCP(ip,port,0));
        thread.start();
        String code1 = "00000000000601030000000B";
        Thread thread1 = new Thread(new ModBusTCP(ip,port,14));
        thread1.start();
    }
}
