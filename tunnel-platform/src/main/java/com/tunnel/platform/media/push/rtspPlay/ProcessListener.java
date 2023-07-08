package com.tunnel.platform.media.push.rtspPlay;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 进程监听线程
 * 当进程关闭时，需要移除缓存中的流信息（防止缓存中存在无用流）
 * */
public class ProcessListener extends Thread {
    private Process process;
    private String rtspUrl;
    private CallbBack rstspPlay;
    /**
     * 构造函数
     * */
    ProcessListener(){}
    ProcessListener(Process process, String rtspUrl, CallbBack rstspPlay) {
        this.rtspUrl = rtspUrl;
        this.rstspPlay=rstspPlay;
        this.process=process;
    }
    /**
     *等待进程结束，通知调用方
     * */
    public void run() {
        try {
            System.out.println( ">>>等待进程结束！"+rtspUrl);
            BufferedReader br= new BufferedReader(new InputStreamReader(process.getErrorStream()));
             String line = "";
             while ((line = br.readLine()) != null) {
                     System.out.println("视频推流信息["+ line + "]"+"源流RTSP:"+rtspUrl);
                 }
            process.waitFor();
            System.out.println( ">>>停止推流！"+rtspUrl);
            //通知推流已经停止
            rstspPlay.stop(rtspUrl);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
}
