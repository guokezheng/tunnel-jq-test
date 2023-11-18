package com.ruoyi.quartz.task;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.dataReport.ExternalSystemCode;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.business.datacenter.domain.enumeration.TopicEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.datacenter.domain.enumeration.WjCarVolumeEnum;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.digitalmodel.SdRadarDevice;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备数据同步
 *
 * @author zhai
 * @date 2023/10/30
 */
@Component("devDataTask")
public class DevDataTask {

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    @Qualifier("kafkaTwoTemplate")
    private KafkaTemplate<String, String> kafkaTwoTemplate;

    /**
     * 线程池
     */
    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${wj_carVolume}")
    private String glzName;

    @Value("${authorize.name}")
    private String authorizeName;

    @Value("${tunnel_dept_id}")
    private String tunnelDeptId;

    @Autowired
    private SdTunnelsMapper tunnelsMapper;

    @Autowired
    private SdDevicesMapper devicesMapper;

    @Resource(name = "HttpTemplate")
    private RestTemplate template;

    public void sentDevData(){
        List<String> list = new ArrayList<>();
        if("wzb".equals(glzName)){
            list.add(TunnelEnum.HU_SHAN.getCode());
        }else if("th".equals(glzName)){
            list.add(TunnelEnum.QING_FENG_LING.getCode());
            list.add(TunnelEnum.PAN_DING_SHAN.getCode());
        }else if("mz".equals(glzName)){
            list.add(TunnelEnum.TAI_HE_SHAN.getCode());
            list.add(TunnelEnum.TIAN_CI_SHAN.getCode());
        }else if("jly".equals(glzName)){
            list.add(TunnelEnum.JIN_JIA_LOU.getCode());
            list.add(TunnelEnum.MA_AN_SHAN.getCode());
        }else if("yts".equals(glzName)){
            list.add(TunnelEnum.SHUANG_ZI_SHAN.getCode());
            list.add(TunnelEnum.YANG_TIAN_SHAN.getCode());
        }
        //推送设备运行状态
        for(String tunnelId : list){
            List<Map<String, Object>> devRealData = sdDeviceDataMapper.getDevRealData(tunnelId);
            devRealData.stream().forEach(item -> {
                JSONObject definitionObject = definitionParam(item);
                JSONObject jsonObject = devReaStatus(Long.valueOf(item.get("eqType").toString()), definitionObject, item.get("tunnelId").toString());
                threadPoolTaskExecutor.execute(()->{
                    sentWlData(jsonObject);
                });
            });
        }
        //推送设备在线离线状态
        for(String tunnelId : list){
            List<SdDevices> devices = sdDevicesMapper.selectDevice(tunnelId);
            devices.stream().forEach(item -> {
                JSONObject jsonObject = devStatus(item);
                threadPoolTaskExecutor.execute(()->{
                    sentWlData(jsonObject);
                });
            });

        }
    }

    /**
     * 重新定义参数名
     *
     * @param item
     * @return
     */
    public JSONObject definitionParam(Map<String, Object> item){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId",item.get("deviceId").toString());
        jsonObject.put("deviceData",item.get("deviceData").toString());
        jsonObject.put("deviceItemId",Long.valueOf(item.get("devicesItemId").toString()));
        jsonObject.put("laneNo",item.get("lane").toString());
        return jsonObject;
    }

    /**
     * 实时设备运行状态上传高速云共通方法
     *
     * @param eqType
     * @param object
     * @return
     */
    public JSONObject devReaStatus(Long eqType, JSONObject object, String tunnelId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "S00063700001980001");
        jsonObject.put("devType",eqType);
        jsonObject.put("loginTime", DateUtils.getNowDate());
        jsonObject.put("devStatus","1");
        jsonObject.put("netstatus","1");
        jsonObject.put("source","suidao");
        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        jsonObject.put("tunnelId",tunnelId);
        JSONObject obj = new JSONObject();
        obj.put("deviceData",object);
        jsonObject.put("expands",obj);
        return jsonObject;
    }

    /**
     * 实时设备状态上传高速云共通方法
     *
     * @param sdDevices
     * @return
     */
    public JSONObject devStatus(SdDevices sdDevices){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("devNo", "S00063700001980001");
        jsonObject.put("devType",sdDevices.getEqType());
        jsonObject.put("loginTime",DateUtils.getNowDate());
        jsonObject.put("devStatus",sdDevices.getEqStatus());
        jsonObject.put("source","suidao");
        jsonObject.put("tunnelId",sdDevices.getEqTunnelId());
        if("1".equals(sdDevices.getEqStatus())){
            jsonObject.put("netstatus",sdDevices.getEqStatus());
            jsonObject.put("netStatusRemark","连通");
            jsonObject.put("devStatusRemark","正常");
        }else if("2".equals(sdDevices.getEqStatus())){
            jsonObject.put("netstatus",sdDevices.getEqStatus());
            jsonObject.put("netStatusRemark","离线");
            jsonObject.put("devStatusRemark","离线");
        }else if("3".equals(sdDevices.getEqStatus())){
            jsonObject.put("devStatusRemark","故障");
        }else {
            jsonObject.put("netstatus","1");
            jsonObject.put("netStatusRemark","连通");
        }
        jsonObject.put("timeStamp", DateUtil.format(DateUtil.date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        JSONObject obj = new JSONObject();
        obj.put("deviceStatus",sdDevices);
        jsonObject.put("expands",obj);
        return jsonObject;
    }

    /**
     * 推送物联
     */
    public void sentWlData(JSONObject jsonObject){
        try {
            kafkaTwoTemplate.send(TopicEnum.DEV_STATUS_TOPIC.getCode(),jsonObject.toString());
        }catch (Exception e){
            e.getMessage();
        }
    }

    public void syncDevTunnelData(){
        if(PlatformAuthEnum.GLZ.getCode().equals(authorizeName)){
            List<SdTunnels> tunnelDataDept = tunnelsMapper.getTunnelDataDept(tunnelDeptId);
            String collect = tunnelDataDept.stream().map(SdTunnels::getTunnelId).collect(Collectors.joining(","));
            List<SdDevices> devTunnelData = devicesMapper.getDevTunnelData(collect);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tunnelList", tunnelDataDept);
            jsonObject.put("deviceList", devTunnelData);
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HashMap<String, Object> requestBody = new HashMap<>();
            requestBody.put("requestData",jsonObject.toString());

            HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);
            try{
                ResponseEntity<Map> exchange = template.exchange("http://10.166.157.192:31028/dev-api/devices/syncData", HttpMethod.POST, httpEntity, Map.class);
                Map body = exchange.getBody();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else {
            return;
        }
    }
}
