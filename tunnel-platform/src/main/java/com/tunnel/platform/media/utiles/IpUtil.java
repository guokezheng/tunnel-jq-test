package com.tunnel.platform.media.utiles;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {
    public static String getLocalIp(){
        InetAddress ip4 = null;
        try {
            ip4 = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip4.getHostAddress();
    }
}
