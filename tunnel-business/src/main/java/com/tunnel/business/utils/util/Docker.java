package com.tunnel.business.utils.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.domain.videoevents.SdCreateDocker;
import com.tunnel.business.domain.videoevents.SdkEventAnalysis;
import com.tunnel.business.domain.videoevents.SdkEventLane;
import com.tunnel.business.domain.videoevents.SdkEventTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Docker {
        /**
         * 创建容器
         * @param docker
         * @return
         * @throws IOException
         */
    public static Boolean createDocker(SdCreateDocker docker){
        HashMap<String, Object> map = new HashMap<>();
        map.put("deviceID", docker.getDeviceID());
        map.put("dockerPort", docker.getDockerPort());
        map.put("dockerName", docker.getDockerName());
        map.put("vcpus", docker.getVcpus());
        map.put("mems",docker.getMems());
        JSONObject json = new JSONObject(map);
        String  url = docker.getDockerIp()+":3101/V1/docker/create";
        return sendPostJson(url,json);
    }

    /**
     * 删除容器
     * @param docker
     * @return
     * @throws IOException
     */
    public static Boolean delete(SdCreateDocker docker){
        HashMap<String, Object> map = new HashMap<>();
        map.put("deviceID", docker.getDeviceID());
        map.put("dockerName",docker.getDockerName());
        JSONObject json = new JSONObject(map);
        String  url = docker.getDockerIp()+":3101/V1/docker/delete";
        if (sendPostJson(url, json)){
            return  true;
        }
        return false ;
    }
    /**
     * 初始化算子
     */
    public static Boolean  dockerInit(SdCreateDocker docker){
        HashMap<String, Object> map = new HashMap<>();
        map.put("nGpuId", docker.getnGpuId());
        /*map.put("nFaceEnable", docker.getnFaceEnable());
        map.put("nBodyEnable", docker.getnBodyEnable());
        map.put("nMotorEnable", docker.getnMotorEnable());
        map.put("nNonMotorEnable", docker.getnNonMotorEnable());*/
        map.put("nEventEnable",docker.getnEventEnable());
        map.put("nEventMode",docker.getnEventMode());
        map.put("authPath",docker.getAuthPath());
        /*    map.put("params", mqSet);*/
//        将map转为json对象
        JSONObject json = new JSONObject(map);
        String  url = docker.getDockerIp()+":"+docker.getDockerPort()+"/V1/Task/Init";
        return sendPostJson(url,json);
    }
    /**
     * 创建（添加）视频分析任务
     */
    public static Boolean addTask(SdkEventTask eventTask, List<SdkEventLane> eventLane, List<SdkEventAnalysis> eventAnalysis, SdCreateDocker docker){
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskType", eventTask.getTaskType());
        map.put("url",eventTask.getUrl() );
        map.put("channelId", eventTask.getChannelId());
       /* List<String> result=new ArrayList<>();
        for (String rUrl:eventTask.getResults().split(",")){
            result.add(rUrl);//"http://172.31.7.161:8080/testEvent/event"
        }*/
        map.put("result",eventTask.getResults());
        Map<String, Object> mapConfig = addMapConfig(eventLane, eventAnalysis);
        //SceneMode 场景类型： MainRoad - 主干道路  CrossRoad – 十字路口  ForkRoad – 叉子路口
        mapConfig.put("SceneMode","MainRoad");
        //上报间隔（单位秒）
        mapConfig.put("ReportingInterval",eventTask.getReportingInterval());
        if ("1".equals(eventTask.getNightMode())){
            //夜间模式 非必填，夜间模式开关 true 不开启检测
            mapConfig.put("NightMode",true);
            //夜间模式 开始时间
            mapConfig.put("NightStart",eventTask.getNightStart());
            //夜间模式 结束时间
            mapConfig.put("NightEnd",eventTask.getNightEnd());
        }else {
            //夜间模式 非必填，夜间模式开关 false 夜间开启检测
            mapConfig.put("NightMode",false);
        }
        //开启告警视频
        if ("0".equals(eventTask.getAlarmVideo())){
            //告警视频存放路径
            mapConfig.put("VideoPath","/home/video/"+eventTask.getChannelId()+"/");
            //告警前缓存视频时长
            mapConfig.put("TimeBeforeAlarm",15);
            //告警后缓存视频时长
            mapConfig.put("TimeAfterAlarm",15);
        }
        map.put("config",mapConfig);
        JSONObject json = new JSONObject(map);
        String  url = docker.getDockerIp()+":"+docker.getDockerPort()+"/V1/Task/Add";
        return sendPostJson(url,json);
    }
    public static Map<String, Object> addMapConfig(List<SdkEventLane> eventLane, List<SdkEventAnalysis> eventAnalysis){
        HashMap<String, Object> mapConfig = new HashMap<>();
        //车道区域
        mapConfig.put("Lane",addLane(eventLane));
        //分析区域
        mapConfig.put("AnalysisRegion",addAnalysisRegion(eventAnalysis));
        return mapConfig;
    }
    public static List<Map<String,Object>> addLane(List<SdkEventLane> eventLaneArr){
        List<Map<String,Object>> lane=new ArrayList<Map<String,Object>>();
        for (SdkEventLane eventLane:eventLaneArr){
            HashMap<String, Object> laneMap = new HashMap<>();
            //车流方向坐标
            HashMap<String, Object> mapDirection = new HashMap<>();
            List<Integer> start=new ArrayList<>();
            String[] startArr = eventLane.getStart().split(",");
            start.add(getWidth(startArr[0]));
            start.add(getHeight(startArr[1]));
            mapDirection.put("Start",start);
            List<Integer> end=new ArrayList<>();
            String[] endArr = eventLane.getEnd().split(",");
            end.add(getWidth(endArr[0]));
            end.add(getHeight(endArr[1]));
            mapDirection.put("End",end);
            laneMap.put("Direction",mapDirection);
            //车道坐标
            String coordinatesStr=eventLane.getCoordinates().replace("  ",",").split(" ")[0];//"2094,2752,3737,9346,8458,6762,2849,2114,2122,2752,2122,2752"
            String[] coordinatesArr = coordinatesStr.split(",");
            List<Integer> coordinates=new ArrayList<>();
            for (int i = 1; i <= coordinatesArr.length; i++) {
                if (i%2==0){
                    coordinates.add(getHeight(coordinatesArr[i-1]));
                }else {
                    coordinates.add(getWidth(coordinatesArr[i-1]));
                }
            }
            laneMap.put("Coordinates",coordinates);
            //车道类型 应急车道 "EmergencyLane"  普通车道 "BusLane"
            laneMap.put("Type",eventLane.getLaneType());
            laneMap.put("Id",(eventLane.getLaneType().equals("BusLane") ? 1:2));
            //虚拟线圈坐标
            String virtualLoopStr=eventLane.getVirtualLoop().replace("  ",",").split(" ")[0];//"3038,6745,5824,4597,3265,2450,2226,3272,3095,6762";
            String[] virtualLoopArr = virtualLoopStr.split(",");
            List<Integer> virtualLoop=new ArrayList<>();
            for (int i = 1; i <= virtualLoopArr.length; i++) {
                if (i%2==0){
                    virtualLoop.add(getHeight(virtualLoopArr[i-1]));
                }else {
                    virtualLoop.add(getWidth(virtualLoopArr[i-1]));
                }
            }
            laneMap.put("VirtualLoop",virtualLoop);
            lane.add(laneMap);
        }
        return lane;
    }
    public static List<Map<String,Object>> addAnalysisRegion(List<SdkEventAnalysis> eventAnalysisArr){
        List<Map<String,Object>> analysisRegionList=new ArrayList<>();
        for (SdkEventAnalysis eventAnalysis:eventAnalysisArr){
            HashMap<String, Object> analysisRegion = new HashMap<>();
            //事件参数
            HashMap<String, Object> incidentParam = new HashMap<>();
           /* incidentParam.put("CongestionSense",60);
            incidentParam.put("FogSense",50);
            incidentParam.put("FogReportInterval",21600);
            incidentParam.put("SnowReportInterval",21600);
            //连续上报行人事件的最小时间间隔(秒)
            incidentParam.put("PedestrianReportInterval",10);
            incidentParam.put("AmbleSense",60);
            incidentParam.put("FireSmokeReportInterval",21600);*/
            incidentParam.put("AmbleSense",100);
            //抛洒物检测灵敏度[0,100]
            incidentParam.put("AbandonedObjectSense",70);
            //拥堵判定标准（秒），当拥堵达到该时长时上报拥堵事件
            incidentParam.put("CongestionInterval",300);
            //非法停车时间(秒)
            incidentParam.put("IllegalParkTime",20);
            analysisRegion.put("IncidentParam",incidentParam);
            //分析区域坐标 万分比表示，以x, y, x, y形式排列
            String arCoordinatesStr=eventAnalysis.getCoordinates().replace("  ",",").split(" ")[0];//"2094,2852,3633,9228,7391,5906,2981,2215,2896,2215,2896,2215";
            String[] arCoordinatesArr = arCoordinatesStr.split(",");
            List<Integer> arCoordinates=new ArrayList<>();
            for (int i = 1; i <= arCoordinatesArr.length; i++) {
                if (i%2==0){
                    arCoordinates.add(getHeight(arCoordinatesArr[i-1]));
                }else {
                    arCoordinates.add(getWidth(arCoordinatesArr[i-1]));
                }
            }
            analysisRegion.put("Coordinates",arCoordinates);
            analysisRegion.put("Id",eventAnalysis.getId());
            //违章类型
            List<String> incident=new ArrayList<>();
            String[] incidentArr = eventAnalysis.getIncident().split(",");
            for (String i :incidentArr){
                incident.add(i);
            }
            analysisRegion.put("Incident",incident);
            analysisRegionList.add(analysisRegion);
        }
        return analysisRegionList;
    }
    public static Boolean deleteTask(String id,SdCreateDocker docker) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("channelId", id);
        JSONObject json = new JSONObject(map);
        String  url = docker.getDockerIp()+":"+docker.getDockerPort()+"/V1/Task/delete";
        return sendPostJson(url,json);
    }
    public static JSONObject getTaskAllStatus(SdCreateDocker docker) {
        String  url = "http://"+docker.getDockerIp()+":"+docker.getDockerPort()+"/V1/Task/AllStatus";
        try{
            JSONObject jsonObject = HttpRequest.sendGetJson(url);
            return jsonObject;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static Boolean sendPostJson(String url,JSONObject json){
        try{
            System.out.println(json.toJSONString());
            JSONObject jsonObject = JSON.parseObject(HttpRequest.sendPost("http://" + url, json.toJSONString()));
            if (StringUtils.isNotNull(jsonObject) && StringUtils.isNotEmpty(jsonObject)){
                if ("true".equals(jsonObject.get("success"))){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static int getWidth(String x){
        //(X/宽度)*10000
       return  (int) Math.round((Double.parseDouble(x)/960)*10000);
    }
    public static int getHeight(String y){
        //(X/高度)*10000
       return (int) Math.round((Double.parseDouble(y)/540)*10000);
    }
}
