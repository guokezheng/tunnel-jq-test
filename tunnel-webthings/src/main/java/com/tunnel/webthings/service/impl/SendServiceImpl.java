package com.tunnel.webthings.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.webthings.service.SendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendServiceImpl implements SendService {
    private static final Logger log = LoggerFactory.getLogger(SendService.class);

    /**
     * 时间戳格式
     */
    private static final String sdf_pattern = "yyyy-MM-dd HH:mm:ss.SSS";

//    @Value("${iot.url}")
//    private String url;


    @Autowired
    @Qualifier("OkHttpRestTemplate")
    private RestTemplate restTemplate;

    /**
     * 测试设备编号
     */
    private static final String devNo1 = "S00063700001980001";

    private static final String direct = "up";
    private static final String msgVer = "V1.0";

    /**
     * 设备类型
     */
    private static final String devType = "198";
    /**
     * 消息类型
     */
    private static final String msgType = "31";
    /**
     * 消息编码
     */
    private static final String msgId01 = "01"; //状态数据
    private static final String msgId02 = "02"; //事件数据
    private static final String msgId03 = "03"; //指令数据

//    @Override
//    public void sendStatus() {
//        //模拟状态数据
//        Map<String, Object> dev01Map = new HashMap<>();
//        dev01Map.put("devNo", devNo1); //设备编号，必填
//        dev01Map.put("devType", devType); //设备类型，必填
//        dev01Map.put("loginTime", DateUtil.format(DateUtil.date(), sdf_pattern)); //设备上线时间，必填
//        dev01Map.put("devStatus", "1"); //设备状态，1：正常，2：故障，必填
//        dev01Map.put("netStatus", "1"); //网络连通状态，1：连通，2：下线，必填
//        dev01Map.put("devStatusRemark", "正常"); //设备状态说明，选填
//        dev01Map.put("netStatusRemark", "连通");  //网络连通状态说明，选填
//        dev01Map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
//        dev01Map.put("expands", new HashMap<>()); //自定义扩展数据，选填
//
//        List<String> devNos = CollUtil.newArrayList(devNo1);
//        List<Map<String, Object>> devList = CollUtil.newArrayList(dev01Map);
//        Map<String, Object> content = new HashMap<>();
//        content.put("devList", devList);
//
//        //调用接口
//        this.sendData(msgType, msgId01, devType, devNos, direct,  content);
//    }

//    @Override
//    public void sendEvent() {
//        //模拟事件数据
//        Map<String, Object> dev01Map = new HashMap<>();
//        dev01Map.put("devNo", devNo1); //设备编号，必填
//        dev01Map.put("devType", devType); //设备类型，必填
//
//        //事件数据，格式根据业务自定义开始
//        dev01Map.put("eventType", "EVENT_TYPE_A"); //事件A
//        dev01Map.put("eventTypeDesc", "测试事件A描述"); //事件A描述
//        dev01Map.put("random", RandomUtil.randomString(128)); //随机写入
//        //TODO 自定义key和value
//
//        //事件数据，格式根据业务自定义结束
//
//        dev01Map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
//        dev01Map.put("expands", new HashMap<>()); //自定义扩展数据，选填
//        //TODO 自定义key和value
//
//        //事件数据，格式根据业务自定义结束
//
//        List<String> devNos = CollUtil.newArrayList(devNo1);
//        List<Map<String, Object>> devList = CollUtil.newArrayList(dev01Map);
//        Map<String, Object> content = new HashMap<>();
//        content.put("devList", devList);
//
//        //调用接口
//        this.sendData(msgType, msgId02, devType, devNos, direct,  content);
//    }

//    @Override
//    public void sendDirect() {
//        //模拟指令数据
//        Map<String, Object> dev01Map = new HashMap<>();
//        dev01Map.put("devNo", devNo1); //设备编号，必填
//        dev01Map.put("devType", devType); //设备类型，必填
//
//        //指令数据，格式根据业务自定义开始
//        dev01Map.put("directType", "DIRECT_TYPE_A"); //指令A
//        dev01Map.put("directTypeDesc", "测试指令A描述"); //指令A描述
//        dev01Map.put("random", RandomUtil.randomString(128)); //随机写入
//        //TODO 自定义key和value
//
//        //指令数据，格式根据业务自定义结束
//
//        dev01Map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
//        dev01Map.put("expands", new HashMap<>()); //自定义扩展数据，选填
//        //TODO 自定义key和value
//
//        //指令数据，格式根据业务自定义结束
//
//        List<String> devNos = CollUtil.newArrayList(devNo1);
//        List<Map<String, Object>> devList = CollUtil.newArrayList(dev01Map);
//        Map<String, Object> content = new HashMap<>();
//        content.put("devList", devList);
//
//        //调用接口
//        this.sendData(msgType, msgId03, devType, devNos, direct,  content);
//    }


    ////////////////////////以下为私有方法////////////////////////

//    /**
//     * 发送数据
//     *
//     * @param msgType
//     * @param msgId
//     * @param devType
//     * @param devNos
//     * @param direct
//     * @param content
//     * @return
//     */
//    private void sendData(String msgType, String msgId, String devType, List<String> devNos, String direct, Map<String, Object> content) {
//        JSONObject requestData = this.getBaseRequestData(msgType, msgId, devType, devNos, direct);
//        requestData.put("content", content);
//        //请求头
//        HttpHeaders requestHeaders = new HttpHeaders();
//        //设置JSON格式数据
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> requestEntity = new HttpEntity<>(requestData.toString(), requestHeaders);
//        //执行请求
//        log.info("参数 --> {}", requestData);
//        try {
//            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
//            log.info("返回值 --> {}", responseEntity.getBody());
//        } catch (Exception e) {
//            log.error("物联网关数据发送失败！{}", e.getMessage());
//        }
//    }

    /**
     * 获取上报外层数据
     *
     * @param msgType
     * @param msgId
     * @param devType
     * @param devNos
     * @param direct
     * @return
     */

    private JSONObject getBaseRequestData(String msgType, String msgId, String devType, List<String> devNos, String direct) {
        JSONObject requestData = new JSONObject();
        String timeStamp = String.valueOf(DateUtil.current());
        //设备类型
        requestData.put("msgType", msgType);
        //消息编码
        requestData.put("msgId", msgId);
        //版本号
        requestData.put("msgVer", msgVer);
        //设备编号
        requestData.put("devNo", devNos);
        //设备类型
        requestData.put("devType", devType);
        //时间戳
        requestData.put("timeStamp", timeStamp);
        //消息方向
        requestData.put("direct", direct);
        //MD5
        String md5 = this.getMd5(timeStamp, devNos);
        requestData.put("md5", md5);
        return requestData;
    }

    /**
     * 获取MD5值
     *
     * @param timeStamp
     * @param devNos
     * @return
     */
    private String getMd5(String timeStamp, List<String> devNos) {
        String plaintext = timeStamp + this.getDevSecureKeys(devNos);
        return SecureUtil.md5(plaintext);
    }

    /**
     * 获取设备秘钥，目前和设备编号一致
     *
     * @param devNos
     * @return
     */
    private String getDevSecureKeys(List<String> devNos) {
        //TODO 目前设备秘钥和设备编号一致
        return StrUtil.join("", devNos);
    }


}
