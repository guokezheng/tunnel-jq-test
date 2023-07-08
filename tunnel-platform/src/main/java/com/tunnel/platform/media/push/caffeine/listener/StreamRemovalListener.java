package com.tunnel.platform.media.push.caffeine.listener;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.tunnel.platform.media.push.rtspPlay.MediaStreamCatch;

/**
 * 自定义的移除监听器
 * 当流在缓存中被移除时，关闭推流（防止下一次访问时产生重复推流）
 * */
public class StreamRemovalListener implements RemovalListener<String,Object> {
    @Override
    public void onRemoval(String s, Object o, RemovalCause remoalCause) {
        System.out.println("缓存被移除！");
        //关闭推流
        MediaStreamCatch streamEntity=(MediaStreamCatch)o;
        Process process=streamEntity.getProcess();
        System.out.println(process);
        if(process!=null){
            try {
                int a=process.exitValue();
                System.out.println("进程状态"+a);
            } catch (IllegalThreadStateException e) {
                //强制退出
                process.destroyForcibly();
                System.out.println("销毁进程！");
            }
        }
    }
}
