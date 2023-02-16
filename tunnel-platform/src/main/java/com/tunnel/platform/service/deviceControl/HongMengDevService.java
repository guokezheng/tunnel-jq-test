package com.tunnel.platform.service.deviceControl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //风机修改地址
    @Value("${device.hongmeng.dev.fan}")
    private String fanUrl;

    //风机修改地址
    @Value("${device.hongmeng.dev.trafficLight}")
    private String trafficLightUrl;

    //风机修改地址
    @Value("${device.hongmeng.dev.laneIndicator}")
    private String laneIndicatorUrl;

    //风机修改地址
    @Value("${device.hongmeng.dev.rollDoor}")
    private String rollDoorUrl;

    public Map<String, String> updateHua(String deviceId, String devStatus){
        String oldStatus = devStatus;
        //查询设备信息
        SdDevices sdDevices = sdDevicesMapper.selectSdDevicesById(deviceId);
        //查询设备类型数据项表
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        SdDeviceTypeItem deviceTypeItem = sdDeviceTypeItemMapper.selectSdDeviceTypeItemList(sdDeviceTypeItem).get(0);
        //查询实时数据表
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);
        sdDeviceData.setItemId(deviceTypeItem.getId());
        List<SdDeviceData> sdDeviceDataList = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        //状态匹配
        devStatus = devStatusCync(deviceTypeItem.getId(), devStatus);
        //修改地址
        String url = getUrl(deviceTypeItem.getId());
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId",deviceId);
        jsonObject.put("runStatus",devStatus);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), requestHeaders);
        Map<String, String> map = new HashMap<>();
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            JSONObject jsonObject1 = JSONObject.parseObject(stringResponseEntity.getBody());
            map.put("code",jsonObject1.getString("code"));
            map.put("msg",jsonObject1.getString("msg"));
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(deviceTypeItem.getDeviceTypeId());
            sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
            sdOperationLog.setEqId(sdDevices.getEqId());
            sdOperationLog.setCreateTime(new Date());
            if (sdDeviceDataList.size() > 0 && sdDeviceDataList.get(0) != null) {
                sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
            }
            sdOperationLog.setOperationState(oldStatus);
            sdOperationLog.setControlType("0");
            if(jsonObject1.getInteger("code") == 200){
                sdOperationLog.setState("1");
            }else {
                sdOperationLog.setState("0");
            }
            sdOperationLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
            return map;
        } catch (Exception e) {
            log.error(IpUtils.getIpAddr(ServletUtils.getRequest()) + "设备控制失败！{}", e.getMessage());
        }
        return map;
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
