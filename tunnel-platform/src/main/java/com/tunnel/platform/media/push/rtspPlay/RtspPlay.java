package com.tunnel.platform.media.push.rtspPlay;

import com.github.benmanes.caffeine.cache.Cache;
import com.tunnel.platform.media.utiles.FfmpegUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class RtspPlay implements CallbBack {
    @Autowired
    Cache<String, Object> caffeineCache;
    /**
     * 推流rtmp + http-flv
     *  ffmpeg 已经在系统环境变量中配置好了 ,下面为命令说明
     * -rtsp_transport tcp设置tcp的方式连接流，不设置默认用udp流丢包严重
     * -loglevel quiet 设置不向子进程输入日志，防止子进程日志没有及时输出导致的缓存一处，导致断流。
     *  //这里也可以用process.getErrorStream()获取日志打印，但是容易readLine()阻塞
     * -stimeout 2000000 设置链接rtsp时的超时时间。防止rtsp不可用导致线程阻塞
     * -i设指定输入文件或者流
     * -vcodec视频选项，一般后面加copy表示拷贝
     * -an去掉音频，当前浏览器对video标签不支持音频自动播放，导致拉流失败。去掉音频可以防止此问题
     * -s设置帧的尺寸
     * -acodec音频选项，一般后面加copy表示拷贝
     * -f指定输出文件格式 这里指定了flv和rtmp
     * */
    public Process pushStream(String rtspUrl, String nginxRtmpUrl){
        Process process=null;
        try {
            String command= FfmpegUtil.getPushCommand(rtspUrl,nginxRtmpUrl);
            System.out.println("执行ffmpeg推流命令：" + command);
            //执行命令行
            process = Runtime.getRuntime().exec(command);
            //注册进程监听
            new ProcessListener(process,rtspUrl,this).start();
        }catch (Exception e){
            e.printStackTrace();
        }
        return process;
    }
    /**
     * 停止推流的回调
     * */
    @Override
    public void stop(String rtspUrl) {
        //移除缓存
        caffeineCache.invalidate(rtspUrl);
    }
    /**
     * 系统推出关闭所有推流进程
     * */
    @PreDestroy
    public void destroyProcess(){
        System.out.println("程序退出清空缓存");
        caffeineCache.invalidateAll();
    }
}
