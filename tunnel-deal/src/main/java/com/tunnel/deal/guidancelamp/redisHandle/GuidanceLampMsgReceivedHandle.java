package com.tunnel.deal.guidancelamp.redisHandle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.deal.guidancelamp.control.NettyClient;
import com.tunnel.deal.guidancelamp.control.inductionlamp.InductionlampUtil;
import com.tunnel.platform.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.platform.domain.dataInfo.SdDeviceData;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.digitalmodel.RadarEventService;
import com.tunnel.platform.utils.util.SpringContextUtils;
import com.zc.common.core.redis.RedisMessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理接收 Redis 诱导灯控制缓存订阅消息
 */
@AutoService(RedisMessageDispatcher.class)
public class GuidanceLampMsgReceivedHandle implements RedisMessageDispatcher {

    private static Logger logger = LoggerFactory.getLogger(GuidanceLampMsgReceivedHandle.class);

    @Autowired
    ISdDevicesService devicesService;

    private static SdDeviceDataMapper deviceDataMapper = SpringUtils.getBean(SdDeviceDataMapper.class);
    private static RadarEventService radarEventService = SpringUtils.getBean(RadarEventService.class);

    private Integer a = 0;

    @Override
    public boolean isChannelExist(String channel) {
        if ("GL:CONTROL".equals(channel)) {
            return true;
        }
        return false;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        a++;
        String ctrBody = new String(message.getBody());
        logger.info("收到的mq消息" + ctrBody);
        toControlDev(ctrBody);
    }

    private String handleDeviceStatus(SdDevices sdDevices, Map<String, Object> codeMap) {
        String state = "";
        if (codeMap.isEmpty()) {
            //当前诱导灯控制器已经离线，存储状态到devices
            sdDevices.setEqStatus("2");
            sdDevices.setEqStatusTime(new Date());
        } else {
            state = codeMap.get("openState").toString();
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
        }
        devicesService.updateSdDevices(sdDevices);
        return state;
    }

    private static void sendDataToWanJi(SdDevices sdDevices, String runStatus, String runMode) {
        Map<String, Object> map = new HashMap<>();
        map.put("deviceId", sdDevices.getEqId());
        map.put("deviceType", sdDevices.getEqType());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("runStatus", runStatus);
        jsonObject.put("runMode", runMode);
        map.put("deviceData", jsonObject);
        radarEventService.sendBaseDeviceStatus(map);
    }

    private static void handleCodeMap(SdDevices sdDevices, Map<String, Object> codeMap) {
        String mode = codeMap.get("mode").toString();
        saveDataIntoSdDeviceData(sdDevices, mode, DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode());
        sendDataToWanJi(sdDevices, "lightOn", mode);
        String brightness = codeMap.get("brightness").toString();
        saveDataIntoSdDeviceData(sdDevices, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode());
        String frequency = codeMap.get("frequency").toString();
        saveDataIntoSdDeviceData(sdDevices, frequency, DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode());
    }

    private static void saveDataIntoSdDeviceData(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = deviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        SdDeviceData data = deviceData.get(0);
        data.setData(value);
        if (deviceData.size() > 0) {
            data.setUpdateTime(new Date());
            deviceDataMapper.updateSdDeviceData(data);
        } else {
            data.setCreateTime(new Date());
            deviceDataMapper.insertSdDeviceData(data);
        }
    }

    // 进行诱导灯设备控制
    private void toControlDev(String ctrBody) {
        Map<String, Object> ctrResult = (Map<String, Object>) JSON.parse(ctrBody);
        String deviceId = ctrResult.get("deviceId").toString();
        devicesService = (ISdDevicesService) SpringContextUtils.getBean(ISdDevicesService.class);
        SdDevices sdDevices = devicesService.selectSdDevicesById(deviceId);
        String ip = sdDevices.getIp();
        int port = Integer.parseInt(sdDevices.getPort());
        if (ctrResult.get("checkMode") != null) {
            try {
//                String code = "1GH+STATUS?\r\n";
//                NettyClient client = new NettyClient(ip, port,code,1);
//                client.start(null);
                Map codeMap = InductionlampUtil.getNowOpenState(ip, port);
                String state = handleDeviceStatus(sdDevices, codeMap);
                if (state != "" && state.equals("1")) {
                    codeMap = InductionlampUtil.getNowRunMode(ip, port);
                    state = handleDeviceStatus(sdDevices, codeMap);
                    String code = "";
                    if (state != "" && state.equals("1")) {
                        //当前属于闪烁模式，查询当前闪灯模式、亮度、频率
                        code = "1GH+BMODE?,TPWM?,BFREQ?\r\n";
                        codeMap = InductionlampUtil.getNowModeAndParameter(code, ip, port);
                        handleDeviceStatus(sdDevices, codeMap);
                        //codeMap中包含的数据解析
                        handleCodeMap(sdDevices, codeMap);
                    } else if (state != "" && state.equals("2")) {
                        //当前属于流水模式，查询当前闪灯模式、亮度、频率
                        code = "1GH+WMODE?,TPWM?,WFREQ?\r\n";
                        codeMap = InductionlampUtil.getNowModeAndParameter(code, ip, port);
                        handleDeviceStatus(sdDevices, codeMap);
                        //codeMap中包含的数据解析
                        handleCodeMap(sdDevices, codeMap);
                    }
                } else if (state != "" && state.equals("0")) {
                    sendDataToWanJi(sdDevices, "lightOf", "");
                }
//                client.pushCode(codeMap.get("code").toString());
//                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        int brightness = Integer.parseInt(ctrResult.get("brightness").toString());
        int frequency = Integer.parseInt(ctrResult.get("frequency").toString());
        Integer controlModeType = Integer.parseInt(ctrResult.get("ctrState").toString());
        //当前已经拿到了哪个诱导灯控制器需要改成什么模式，亮度值和频率，可以开始直接控制
        //发送诱导灯控制指令
        try {
            String code = "1GH+STATUS?\r\n";
            NettyClient client = new NettyClient(ip, port,code,1);
            client.start(null);
            Map codeMap = InductionlampUtil.getPilotLightMode(controlModeType,brightness,frequency);
            client.pushCode(codeMap.get("code").toString());
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
