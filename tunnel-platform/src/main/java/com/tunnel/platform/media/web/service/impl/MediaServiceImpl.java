package com.tunnel.platform.media.web.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.tunnel.platform.media.push.rtspPlay.MediaStreamCatch;
import com.tunnel.platform.media.push.rtspPlay.RtspPlay;
import com.tunnel.platform.media.utiles.StreamUtil;
import com.tunnel.platform.media.utiles.uuid.IdUtils;
import com.tunnel.platform.media.web.domain.MediaStream;
import com.tunnel.platform.media.web.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MediaServiceImpl implements MediaService {
    @Autowired
    private Cache<String, Object> caffeineCache;
    @Autowired
    private RtspPlay rtspPlay;
    @Autowired
    private StreamUtil streamUtil;
    @Override
    public List<MediaStream> getList() {
        Map<String,Object> map=caffeineCache.asMap();
        System.out.println(caffeineCache);
        List<MediaStream> list=new ArrayList<MediaStream>();
        Set<Map.Entry<String,Object>> entrySet = map.entrySet();
        for(Map.Entry<String,Object> entry : entrySet){
            MediaStreamCatch mediaStreamCatch=(MediaStreamCatch) entry.getValue();
            MediaStream mediaStream=new MediaStream();
            mediaStream.setRtspUrl(mediaStreamCatch.getRtspUrl());
            mediaStream.setHttpFlvUrl(mediaStreamCatch.getHttpFlvUrl());
            mediaStream.setRtmpUrl(mediaStreamCatch.getRtmpUrl());
            mediaStream.setStreamId(mediaStreamCatch.getStreamId());
            list.add(mediaStream);
        }
        return list;
    }
    /**
     * 添加推流
     * */
    @Override
    public boolean add(MediaStream mediaStream) {
        String streamId= IdUtils.simpleUUID();
        mediaStream.setStreamId(streamId);
        String rtsp= mediaStream.getRtspUrl();
        String rtmp = streamUtil.getRtmpUrl(streamId);
        String httpFlv=streamUtil.getHttpFlvUrl(streamId);
        Process process=rtspPlay.pushStream(rtsp,rtmp);
        if(process!=null){
            //缓存流
            MediaStreamCatch streamCatch=new MediaStreamCatch();
            streamCatch.setStreamId(mediaStream.getStreamId());
            streamCatch.setRtmpUrl(rtmp);
            streamCatch.setProcess(process);
            streamCatch.setRtspUrl(rtsp);
            streamCatch.setHttpFlvUrl(httpFlv);
            caffeineCache.put(rtsp,streamCatch);
            mediaStream.setRtmpUrl(rtmp);
            mediaStream.setHttpFlvUrl(httpFlv);
            return true;
        }
        return false;
    }

    @Override
    public MediaStream getStream(MediaStream mediaStream) {
        String rtspUrl=mediaStream.getRtspUrl();
        if(rtspUrl==null){
            return null;
        }
        MediaStreamCatch mediaStreamCatch=(MediaStreamCatch)caffeineCache.getIfPresent(rtspUrl);
        if(mediaStreamCatch!=null){
            //正在推流直接返回
            mediaStream.setStreamId(mediaStreamCatch.getStreamId());
            mediaStream.setRtmpUrl(mediaStreamCatch.getRtmpUrl());
            mediaStream.setHttpFlvUrl(mediaStreamCatch.getHttpFlvUrl());
            return mediaStream;
        }
        //未推流
        if(add(mediaStream)){
            return mediaStream;
        }
        return null;
    }
}
