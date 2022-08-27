package com.tunnel.platform.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.core.controller.BaseController;
import com.tunnel.platform.pcl.config.Infovision.CamerasPreviewURLs;
import com.tunnel.platform.pcl.config.Infovision.ResourceCameras;
import com.tunnel.platform.pcl.config.dataVo.CamerasRequest;
import com.tunnel.platform.pcl.config.dataVo.PreviewURLsRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/camera")
public class CameraController extends BaseController {

    /**
     * 获取监控点预览取流URL
     * @return
     */
    @PostMapping("/previewURLs")
    public Map previewURLs(@Param("cameraIndexCode")String cameraIndexCode)
    {

        PreviewURLsRequest request = new PreviewURLsRequest();
        request.setCameraIndexCode(cameraIndexCode);
        request.setStreamType(0);
        request.setProtocol("rtsp");
        request.setTransmode(0);
//        String code = CamerasPreviewURLs.previewURLs(request);
//        //先转换成Object
//        Object succesResponse = JSON.parse(code);
//        //Object强转换为Map
//        Map map = (Map)succesResponse;

        //测试数据
        Map result = new HashMap<>();
        result.put("code",0);
        result.put("msg","success");

        Map data = new HashMap<>();
        data.put("url","rtsp://ip:port/BSwvVkAUrG6XAMhIEeIMYb66A84s");
        result.put("data",data);
        return result;
    }

    /**
     * 分页获取监控点资源
     * @return
     */
    @PostMapping("/list")
    public Map list(@Param("pageNo")Integer pageNo, @Param("pageSize")Integer pageSize)
    {
        CamerasRequest request=new CamerasRequest();
        request.setPageNo(pageNo);
        request.setPageSize(pageSize);
        request.setTreeCode("0");
//        String code = ResourceCameras.cameras(request);
//        //先转换成Object
//        Object succesResponse = JSON.parse(code);
//        //Object强转换为Map
//        Map map = (Map)succesResponse;

        //测试数据
        Map result = new HashMap<>();
        result.put("code",0);
        result.put("msg","success");
        Map data = new HashMap<>();
        data.put("total",1);
        data.put("pageSize",20);
        data.put("pageNo",1);
        List list = new ArrayList();
        data.put("list",list);
        result.put("data",data);
        String code = "{\n" +
                "                \"cameraIndexCode\": \"90ad77d8057c43dab140b77361606927\",\n" +
                "                \"gbIndexCode\": \"12000000051210000000\",\n" +
                "                \"name\": \"Camera_01\",\n" +
                "                \"deviceIndexCode\": \"f5da3e320bcb483da6bef4b3f86de779\",\n" +
                "                \"longitude\": \"120.216123284763\",\n" +
                "                \"latitude\": \"30.21168569675452\",\n" +
                "                \"altitude\": \"88\",\n" +
                "                \"pixel\": 1,\n" +
                "                \"cameraType\": 1,\n" +
                "                \"cameraTypeName\": \"半球\",\n" +
                "                \"installPlace\": \"街道\",\n" +
                "                \"matrixCode\": \"2076c586b0a94a6ba639b44eda9e76e9\",\n" +
                "                \"chanNum\": 1,\n" +
                "                \"viewshed\": \"{\\\"horizontalValue\\\":\\\"13.80000\\\",\\\"azimuth\\\":\\\"109.23000\\\",\\\"visibleRadius\\\":\\\"48.00000\\\"}\",\n" +
                "                \"capabilitySet\": \"@event_face@\",\n" +
                "                \"capabilitySetName\": \"人脸采集能力\",\n" +
                "                \"intelligentSet\": \"@face@\",\n" +
                "                \"intelligentSetName\": \"人脸结构化能力\",\n" +
                "                \"recordLocation\": \"0\",\n" +
                "                \"recordLocationName\": \"中心存储\",\n" +
                "                \"ptzController\": null,\n" +
                "                \"ptzControllerName\": \"\",\n" +
                "                \"deviceResourceType\": \"ENCODE_DEVICE\",\n" +
                "                \"deviceResourceTypeName\": \"编码设备\",\n" +
                "                \"channelType\": \"digital\",\n" +
                "                \"channelTypeName\": \"数字通道\",\n" +
                "                \"transType\": 0,\n" +
                "                \"transTypeName\": \"UDP\",\n" +
                "                \"updateTime\": \"2017-06-15T00:00:00.000+08:00\",\n" +
                "                \"unitIndexCode\": \"083b2031c1db4f368f015fe2562e0012\",\n" +
                "                \"treatyType\": \"20005\",\n" +
                "                \"treatyTypeName\": \"ONVIF\",\n" +
                "                \"createTime\": \"2017-06-15T00:00:00.000+08:00\",\n" +
                "                \"status\": 0,\n" +
                "                \"statusName\": \"不在线\"\n" +
                "            }";
        Object succesResponse = JSON.parse(code);    //先转换成Object
        Map map = (Map)succesResponse;         //Object强转换为Map
        list.add(map);
        return result;
    }
}
