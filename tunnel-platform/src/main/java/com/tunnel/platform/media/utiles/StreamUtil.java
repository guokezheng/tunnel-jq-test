package com.tunnel.platform.media.utiles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 构建流
 * */
@Component
public class StreamUtil {
    /*@Value("${mediaPort.rtmpPort}")*/
    private  String rtmpPort = "1935";
    /*@Value("${mediaPort.httpFlvPort}")*/
    private  String httpFlvPort ="8554";
    /**
     * 获取rtsp流地址
     * */
    public  String getRtspUrl(String userName,String password,String ip,String streamName){
        return "rtsp://"+userName+":"+password+"@"+ip+":554/h264/ch1/sub/"+streamName;
    }
    /**
     *获取rtmp流地址
     * */
    public  String getRtmpUrl(String streamName){
        return "rtmp://"+ IpUtil.getLocalIp()+":"+rtmpPort+"/live/"+streamName;
    }
    /**
     * 获取http-flv流地址
     * */
    public  String getHttpFlvUrl(String streamName){
        return "http://"+ IpUtil.getLocalIp()+":"+httpFlvPort+"/rtmpLive?port=1935&app=live&stream="+streamName;
    }
}
