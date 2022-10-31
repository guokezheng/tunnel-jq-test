package com.tunnel.platform.service;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdDeviceNowState;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.zc.common.core.websocket.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class SdDeviceControlService {

    private static final Logger log = LoggerFactory.getLogger(SdDeviceControlService.class);

    @Value("${authorize.name}")
    private String deploymentType;

    @Autowired
    private SdOptDeviceService sdOptDeviceService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    @Autowired
    private ISdOperationLogService sdOperationLogService;


    /**
     * 批量控制设备方法参数不能为空，否则直接返回0（控制失败）
     * 控制车指必传参数：devId（设备ID）、state（变更的状态）
     * 控制诱导灯：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）
     * 控制疏散标志：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）、fireMark（设备地址标号，正常情况下为255,0为关灯）
     * 控制方式controlType(控制方式   0：手动 1：时间控制 2：光强控制 3:预案控制)
     * */
    public Integer controlDevices(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            //当前设备控制参数为空，直接返回
            log.error("当前设备控制参数为空");
            return 0;
        }

        if ("GSY".equals(deploymentType)) {
            sdOptDeviceService.optSingleDevice(map);
            return 1;
        }

        //控制车指
        if (map.get("devId") == null || map.get("devId").toString().equals("")) {
            throw new RuntimeException("未指定设备，请联系管理员");
        } else if (map.get("state") == null || map.get("state").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息，请联系管理员");
        } else if (map.get("controlType") == null || map.get("controlType").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的状态信息，请联系管理员");
        }
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String controlType = map.get("controlType").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);
        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.PU_TONG_CHE_ZHI.getCode()));
        List<SdDeviceData> data = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setCreateTime(new Date());
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType(controlType);
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        sdOperationLog.setOperIp(ip);
        int controlState = 0;
        String fireMark = "";
        //控制车指
        if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().longValue()) {
            if (data.size() > 0) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }
            controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
            sdOperationLog.setState(String.valueOf(controlState));
            //通过websocket推送到前端
            String[] states = new String[4];
            states[0] = state;
            sendNowDeviceStatusByWebsocket(sdDevices,states,"cz");
            //控制诱导灯
        } else if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG.getCode().longValue()) {
            if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的亮度信息，请联系管理员");
            } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的频率信息，请联系管理员");
            }
            String brightness = map.get("brightness").toString();
            String frequency = map.get("frequency").toString();
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
            data = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
            if (data.size() > 0) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }
            controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, null);
            sdOperationLog.setState(String.valueOf(controlState));
            //通过websocket推送到前端
            String[] states = new String[4];
            states[0] = state;
            states[1] = brightness;
            states[2] = frequency;
            sendNowDeviceStatusByWebsocket(sdDevices,states,"ydd");
            //控制疏散标志
        } else if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().longValue()) {
            if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的亮度信息，请联系管理员");
            } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的频率信息，请联系管理员");
            } else if (map.get("fireMark") == null || map.get("fireMark").toString().equals("")) {
                throw new RuntimeException("未指定设备需要变更的标号位置信息，请联系管理员");
            }
            fireMark = map.get("fireMark").toString();
            String brightness = map.get("brightness").toString();
            String frequency = map.get("frequency").toString();
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
            data = sdDeviceDataMapper.selectSdDeviceDataList(sdDeviceData);
            if (data.size() > 0) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }
            controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
            sdOperationLog.setState(String.valueOf(controlState));
            //通过websocket推送到前端
            String[] states = new String[4];
            states[0] = state;
            states[1] = brightness;
            states[2] = frequency;
            states[3] = fireMark;
            sendNowDeviceStatusByWebsocket(sdDevices,states,"ydd");
        }
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return controlState;
    }

    public void sendNowDeviceStatusByWebsocket(SdDevices sdDevices, String[] state, String type) {
        List<SdDeviceNowState> dataList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        SdDeviceNowState sdDeviceNowState = new SdDeviceNowState();
        sdDeviceNowState.setEqId(sdDevices.getEqId());
        sdDeviceNowState.setEqType(sdDevices.getEqType());
        sdDeviceNowState.setEqStatus(sdDevices.getEqStatus());
        sdDeviceNowState.setEqDirection(sdDevices.getEqDirection());
        sdDeviceNowState.setEqName(sdDevices.getEqName());
        sdDeviceNowState.setEqTunnelId(sdDevices.getEqTunnelId());
        sdDeviceNowState.setPile(sdDevices.getPile());
        sdDeviceNowState.setState(state[0]);
        if (type.equals("ydd")) {
            sdDeviceNowState.setBrightness(state[1]);
            sdDeviceNowState.setFrequency(state[2]);
        } else if (type.equals("ssbz")) {
            sdDeviceNowState.setBrightness(state[1]);
            sdDeviceNowState.setFrequency(state[2]);
            sdDeviceNowState.setFireMark(state[3]);
        }
        dataList.add(sdDeviceNowState);
        jsonObject.put("deviceStatusChangeLog", dataList);
        WebSocketService.broadcast("deviceStatusChangeLog", jsonObject.toString());
    }
}
