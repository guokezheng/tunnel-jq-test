package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.tunnel.business.datacenter.domain.enumeration.DeviceControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
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

import java.util.*;

/**
 * @author zhai
 * @date 2022/12/28
 */
@Service
public class HongMengDevService {

    private static final Logger log = LoggerFactory.getLogger(HongMengDevService.class);

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdDeviceTypeItemMapper sdDeviceTypeItemMapper;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    @Qualifier("OkHttpRestTemplate")
    private RestTemplate restTemplate;

    /**
     * 风机控制地址
     */
    @Value("${device.hongmeng.dev.fan}")
    private String fanUrl;

    /**
     * 交通信号灯控制地址
     */
    @Value("${device.hongmeng.dev.trafficLight}")
    private String trafficLightUrl;

    /**
     * 车道指示器控制地址
     */
    @Value("${device.hongmeng.dev.laneIndicator}")
    private String laneIndicatorUrl;

    /**
     * 卷帘门控制地址
     */
    @Value("${device.hongmeng.dev.rollDoor}")
    private String rollDoorUrl;

    /**
     * 杭山东鸿蒙控制器Http接口控制方法
     * 控制设备+操作日志（手动控制）
     * @param deviceId
     * @param state
     * @return
     */
    public Map<String, String> updateHua(String deviceId, String state){
        Integer controlState = 0;
        //查询设备信息
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);

        //查询设备对应的设备类型数据项
        SdDeviceTypeItem deviceTypeItem = getDeviceTypeItem(sdDevices);
        //状态匹配
        String devStatus = devStatusCync(deviceTypeItem.getId(), state);
        //修改地址
        String url = getUrl(deviceTypeItem.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId",deviceId);
        jsonObject.put("runStatus",devStatus);
        Map<String,String> map = executeHttp(url,jsonObject);
        String code = map.get("code");
        if(Integer.valueOf(code) == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }
        //手动控制
        String controlType = DeviceControlTypeEnum.AUTO_CONTROL.getCode();
        addOperationLog(sdDevices,state,deviceTypeItem,controlState,controlType);
        return map;
    }


    /**
     * 设备控制+操作日志
     * @param map 设备信息、控制状态、控制方式等信息
     * @return
     */
    public Map<String,String> hongMengHttpControl(Map<String, Object> map){

        //默认是手动控制
        String defaultControlType = DeviceControlTypeEnum.AUTO_CONTROL.getCode();

        //设备ID
        String deviceId = Optional.ofNullable(map.get("devId")).orElse("").toString();
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //控制方式
        String controlType = Optional.ofNullable(map.get("controlType")).orElse(defaultControlType).toString();

        Integer controlState = 0;
        //查询设备信息
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);

        //查询设备对应的设备类型数据项
        SdDeviceTypeItem deviceTypeItem = getDeviceTypeItem(sdDevices);
        //状态匹配
        String devStatus = devStatusCync(deviceTypeItem.getId(), state);
        //获取控制地址
        String url = getUrl(deviceTypeItem.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId",deviceId);
        jsonObject.put("runStatus",devStatus);
        Map<String,String> resultMap = executeHttp(url,jsonObject);
        String code = resultMap.get("code");
        if(Integer.valueOf(code) == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }
        //生成日志
        addOperationLog(sdDevices,state,deviceTypeItem,controlState,controlType);
        return resultMap;
    }


    /**
     * 查询设备对应的设备类型数据项
     * @param sdDevices 设备信息
     * @return
     */
    public SdDeviceTypeItem getDeviceTypeItem(SdDevices sdDevices){
        //查询设备类型数据项表
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        SdDeviceTypeItem deviceTypeItem = sdDeviceTypeItemMapper.selectSdDeviceTypeItemList(sdDeviceTypeItem).get(0);
        return deviceTypeItem;
    }

    /**
     * 执行控制请求
     * @param url 请求地址
     * @param jsonObject 请求参数
     * @return
     */
    public Map<String,String> executeHttp(String url,JSONObject jsonObject){

        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), requestHeaders);
        Map<String, String> map = new HashMap<>();
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            JSONObject jsonObject1 = JSONObject.parseObject(stringResponseEntity.getBody());
            String code = jsonObject1.getString("code");
            map.put("code",code);
            map.put("msg",jsonObject1.getString("msg"));

        } catch (Exception e) {
            log.error(IpUtils.getIpAddr(ServletUtils.getRequest()) + "设备控制失败！{}", e.getMessage());
            map.put("code","500");
            map.put("msg","设备控制失败");
        }

        return map;
    }

    /**
     * 生成日志
     * @param sdDevices 设备信息
     * @param state 控制状态
     * @param deviceTypeItem 设备类型数据项
     * @param controlState 控制结果
     * @param controlType 控制方式
     */
    public void addOperationLog(SdDevices sdDevices,String state,SdDeviceTypeItem deviceTypeItem,Integer controlState,String controlType){

        //查询实时数据表
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(deviceTypeItem.getId());
        List<SdDeviceData> sdDeviceDataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);

        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());
        if (sdDeviceDataList.size() > 0 && sdDeviceDataList.get(0) != null) {
            sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
        }
        sdOperationLog.setOperationState(state);
        //传参controlType，对应其他模块调用时的控制方式
        sdOperationLog.setControlType(controlType);

        sdOperationLog.setState(String.valueOf(controlState));

        sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
    }

    /**
     * 设备运行状态与数据库匹配
     * @param itemId
     * @param runStatus
     * @return
     */
    public String devStatusCync(Long itemId,String runStatus){
        if(DevicesTypeItemEnum.FENG_JI_STATUS.getCode() == itemId){
            if("3".equals(runStatus)){
                return "00";
            }
        }
        if(DevicesTypeItemEnum.XIN_HAO_DENG.getCode() == itemId){
            if("4".equals(runStatus)){
                return "00";
            }
            if("2".equals(runStatus)){
                return "01";
            }
            if("1".equals(runStatus)){
                return "02";
            }
        }
        if(DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode() == itemId){
            if("1".equals(runStatus)){
                return "02";
            }
            if("2".equals(runStatus)){
                return "01";
            }
            if("4".equals(runStatus)){
                return "00";
            }
            if("5".equals(runStatus)){
                return "04";
            }
        }
        if(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() == itemId){
            if("4".equals(runStatus)){
                return "00";
            }
        }
        if(DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode() == itemId){
            if("5".equals(runStatus)){
                return "04";
            }
            if("4".equals(runStatus)){
                return "00";
            }
        }
        if(DevicesTypeItemEnum.JUAN_LIAN_MEN.getCode() == itemId){
            if("3".equals(runStatus)){
                return "00";
            }
        }
        return "0" + runStatus;
    }

    /**
     * 获取相关设备修改地址
     * @param itemId
     * @return
     */
    public String getUrl(Long itemId){
        if(itemId == DevicesTypeItemEnum.FENG_JI_STATUS.getCode()){
            return fanUrl;
        }
        if(itemId == DevicesTypeItemEnum.XIN_HAO_DENG.getCode() || itemId == DevicesTypeItemEnum.ZHUO_ZHUAN_XIN_HAO_DENG.getCode()){
            return trafficLightUrl;
        }
        if(itemId == DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode() || itemId == DevicesTypeItemEnum.ZHUO_ZHUAN_CHE_ZHI.getCode()){
            return laneIndicatorUrl;
        }
        if(itemId == DevicesTypeItemEnum.JUAN_LIAN_MEN.getCode()){
            return rollDoorUrl;
        }
        return null;
    }


}
