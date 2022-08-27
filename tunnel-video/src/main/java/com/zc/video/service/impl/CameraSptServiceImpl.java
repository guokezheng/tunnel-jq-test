package com.zc.video.service.impl;


import com.zc.video.domain.PostDto;
import com.zc.video.service.CameraSptService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


/**
 * @author ：xieyuwei
 * @date ：Created in 2022/7/12 9:29
 * 文件说明： </p>
 */
@Service
public class CameraSptServiceImpl implements CameraSptService {
    /**
     * 相机实时流
     *
     * @param type  系统编号 默认1 后期通知更改
     * @param camId 相机编号
     * @return
     */
    @Override
    public String liveStreaming(String type, String camId) {
        String url = "http://10.191.40.204:9021/videoInfo/api/videoStreaming?camId=" + camId + "&type=" + type;
        RestTemplate restTemplate = new RestTemplate();
        PostDto postDto = new PostDto();
        postDto.setType(type);
        postDto.setCamId(camId);
        Map map = restTemplate.postForObject(url, postDto, Map.class);
        Object data = map.get("data");
        Map entity = (Map) data;
        String liveUrl = entity.get("liveUrl").toString();
        if (liveUrl != null) {
            return liveUrl;
        }
        return "未查到数据";
    }

    /**
     * 查询附近相机
     *
     * @param devLat   摄像机纬度
     * @param devLon   摄像机经度
     * @param distance 距离单位米
     * @return
     */
    @Override
    public Object queryNearbyCamera(String devLat, String devLon, String distance) {
        String url = "http://10.191.40.204:9021/videoInfo/api/nearCamListDistance?devLat=" + devLat + "&devLon=" + devLon + "&distance=" + distance;
        RestTemplate restTemplate = new RestTemplate();
        Map map = restTemplate.getForObject(url, Map.class);
        Object data = map.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 云台控制
     *
     * @param msgType 传入值“3”
     * @param camID   相机编号
     * @param cmdType
     * @param speed   当cmdType小于等于28时为云台动作的速度，预置位设置及调用表示对应的预置位编号；
     * @param type
     * @return
     */
    @Override
    public Object gimbalControl(String type, String camID, String msgType, String cmdType, String speed) {
        String url = "http://10.191.40.204:9021/videoInfo/api/PTZControl?type=" + type + "&camID=" + camID + "&msgType=" + msgType + "&cmdType=" + cmdType + "&speed=" + speed;
        RestTemplate restTemplate = new RestTemplate();
        Map map = restTemplate.getForObject(url, Map.class);
        Object data = map.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }
}
