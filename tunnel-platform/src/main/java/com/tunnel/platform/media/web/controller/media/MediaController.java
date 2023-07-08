package com.tunnel.platform.media.web.controller.media;

import com.alibaba.fastjson.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import com.tunnel.platform.media.web.domain.AjaxResult1;
import com.tunnel.platform.media.web.domain.MediaStream;
import com.tunnel.platform.media.web.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/media")
public class MediaController {
    @Autowired
    private MediaService mediaService;
    @Autowired
    Cache<String, Object> caffeineCache;
    /**
     * 获取参数配置列表
     */
    @GetMapping("/list")
    public AjaxResult1 list()
    {
        List<MediaStream> list = mediaService.getList();
        return AjaxResult1.success(list);
    }
    /**
     * 新增流
     */
    @PostMapping("/add")
    public AjaxResult1 add(@RequestBody MediaStream mediaStream)
    {
        if(mediaService.add(mediaStream)){
            return AjaxResult1.success();
        }
        return AjaxResult1.error();
    }
    /**
     * 获取流
     * */
    @PostMapping("/getStream")
    public AjaxResult1 getStream(@RequestBody MediaStream mediaStream)
    {
        MediaStream reMediaStream=mediaService.getStream(mediaStream);
        if(reMediaStream!=null){
            return AjaxResult1.success(reMediaStream);
        }
        return AjaxResult1.error();
    }
    /**
     * 获取流
     * */
    @PostMapping("/getStreamApi")
    public AjaxResult1 getStream(String data, String endtime)
    {
        if(data!=null){
            MediaStream mediaStream=new MediaStream();
            mediaStream.setRtspUrl(data+"&endtime="+endtime);
            MediaStream reMediaStream=mediaService.getStream(mediaStream);
            if(reMediaStream!=null){
                return AjaxResult1.success(reMediaStream);
            }
        }
        return AjaxResult1.error();
    }
    @PostMapping("/deleteStreamApi")
    public AjaxResult1 stop(String data) {
        //移除缓存
        caffeineCache.invalidate(data);
        return AjaxResult1.success("删除成功");
    }


    /**
     * 解析请求json字符串
     * */
    private JSONObject formatterReqJson(String json){
        JSONObject obj= JSONObject.parseObject(json);
        if(obj!=null){
            return obj.getJSONObject("data");
        }else {
            System.out.println("请求失败！");
            return null;
        }
    }
}
