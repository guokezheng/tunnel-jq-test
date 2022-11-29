package com.tunnel.platform.service;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysDictDataService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.event.SdDeviceNowState;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
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

import java.math.BigDecimal;
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
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;


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
            return sdOptDeviceService.optSingleDevice(map);
        }

        //根据字典中配置的设备模拟控制值进行模拟状态展示
        List<SysDictData> isopenList = sysDictDataService.getSysDictDataByDictType("sys_analog_control_isopen");
        if (isopenList.size() == 0) {
            throw new RuntimeException("设备模拟控制是否开启字典值不存在，请联系管理员添加后重试");
        }
        SysDictData sysDictData = isopenList.get(0);
        String isopen = sysDictData.getDictValue();

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
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType(controlType);
        if (null != map.get("operIp")) {
            sdOperationLog.setOperIp(map.get("operIp").toString());
        }
//        if (null != map.get("controlTime")) {
//            sdOperationLog.setCreateTime(DateUtils.parseDate(map.get("controlTime")));
//        }else{
        sdOperationLog.setCreateTime(new Date());
        //}
        if (null != map.get("eventId")) {
            sdOperationLog.setEventId(map.get("eventId").toString());
        }

        int controlState = 0;
        String fireMark = "";
        //控制车指
        if (sdDevices != null && sdDevices.getEqType().longValue() == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode().longValue()) {
            if (data.size() > 0) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }

            if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
                //连接设备进行控制
                controlState = ModbusTcpHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
            } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
                //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
                sdDevices.setEqStatus("1");
                sdDevices.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(sdDevices);
                SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
                sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
                List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
                if (sdDeviceTypeItems.size() == 0) {
                    throw new RuntimeException("当前设备没有设备类型数据项数据，请添加后重试！");
                }
                SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
                updateDeviceData(sdDevices, state, Integer.parseInt(typeItem.getId().toString()));
                controlState = 1;
            }

            sdOperationLog.setState(String.valueOf(controlState));
            //通过websocket推送到前端
            String[] states = new String[4];
            states[0] = state;
            sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"cz");
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
            data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
            if (data.size() > 0) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }
            //控制器不在工作台展示，传进来的子设备需要关联查父级控制器的信息
            sdDevices = sdDevicesService.selectSdDevicesById(sdDevices.getFEqId());
            devId = sdDevices.getEqId();
            if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
                //连接设备进行控制
                controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, null);
            } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
                //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
                sdDevices.setEqStatus("1");
                sdDevices.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(sdDevices);
                if (devId != null && !"".equals(devId)) {
                    SdDevices devices = new SdDevices();
                    devices.setEqStatus("1");
                    devices.setEqStatusTime(new Date());
                    devices.setFEqId(devId);
                    sdDevicesService.updateSdDevicesByFEqId(sdDevices);
                }
                //父级设备变更
                updateDeviceData(sdDevices, state, DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode());
                updateDeviceData(sdDevices, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode());
                updateDeviceData(sdDevices, frequency, DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode());
                //子级设备变更
                SdDevices dev = new SdDevices();
                dev.setFEqId(devId);
                List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
                for (int i = 0;i < list.size();i++) {
                    SdDevices devo = list.get(i);
                    updateDeviceData(devo, state, DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode());
                    updateDeviceData(devo, brightness, DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode());
                    updateDeviceData(devo, frequency, DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode());
                }
                controlState = 1;
            }

            sdOperationLog.setState(String.valueOf(controlState));
            //通过websocket推送到前端
            String[] states = new String[4];
            states[0] = state;
            states[1] = brightness;
            states[2] = frequency;
            sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"ydd");
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
            data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
            if (data.size() > 0) {
                sdOperationLog.setBeforeState(data.get(0).getData());
            }

            //控制器不在工作台展示，传进来的子设备需要关联查父级控制器的信息
            sdDevices = sdDevicesService.selectSdDevicesById(sdDevices.getFEqId());
            devId = sdDevices.getEqId();
            if (isopen != null && !isopen.equals("") && isopen.equals("0")) {
                //连接设备进行控制
                controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
            } else if (isopen != null && !isopen.equals("") && isopen.equals("1")) {
                //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
                sdDevices.setEqStatus("1");
                sdDevices.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(sdDevices);
                if (devId != null && !"".equals(devId)) {
                    SdDevices devices = new SdDevices();
                    devices.setEqStatus("1");
                    devices.setEqStatusTime(new Date());
                    devices.setFEqId(devId);
                    sdDevicesService.updateSdDevicesByFEqId(sdDevices);
                }
                //父级设备变更
                updateDeviceData(sdDevices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                updateDeviceData(sdDevices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                updateDeviceData(sdDevices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                updateDeviceData(sdDevices, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                //子级设备变更
                SdDevices dev = new SdDevices();
                dev.setFEqId(devId);
                List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
                if (!list.isEmpty()) {
                    //疏散标志关灯
                    if (fireMark.equals("0") && !fireMark.equals("255")) {
                        state = "1";
                        for (int i = 0;i < list.size();i++) {
                            SdDevices devo = list.get(i);
                            updateDeviceData(devo, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                            updateDeviceData(devo, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                            updateDeviceData(devo, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                            updateDeviceData(devo, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                        }
                        //疏散标志报警点更新
                    } else if (!fireMark.equals("0") && !fireMark.equals("255")) {
                        BigDecimal fMark = new BigDecimal(fireMark);
                        for (int i = 0;i < list.size();i++) {
                            SdDevices devices = list.get(i);
                            BigDecimal addressMark = new BigDecimal(devices.getQueryPointAddress());
                            if (fMark.compareTo(addressMark) < 0) {
                                state = "6";
                            } else if (fMark.compareTo(addressMark) == 0) {
                                state = "5";
                            } else if (fMark.compareTo(addressMark) > 0) {
                                state = "4";
                            }
                            updateDeviceData(devices, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                            updateDeviceData(devices, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                            updateDeviceData(devices, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                            updateDeviceData(devices, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                        }
                    } else {
                        //疏散标志开灯无报警点
                        state = "2";
                        for (int i = 0;i < list.size();i++) {
                            SdDevices devo = list.get(i);
                            updateDeviceData(devo, state, DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode());
                            updateDeviceData(devo, brightness, DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode());
                            updateDeviceData(devo, frequency, DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode());
                            updateDeviceData(devo, fireMark, DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode());
                        }
                    }
                }
                controlState = 1;
            }

            sdOperationLog.setState(String.valueOf(controlState));
            //通过websocket推送到前端
            String[] states = new String[4];
            states[0] = state;
            states[1] = brightness;
            states[2] = frequency;
            states[3] = fireMark;
            sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"ydd");
        }
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return controlState;
    }

    private void updateDeviceData(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            sdDeviceDataService.updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            sdDeviceDataService.insertSdDeviceData(sdDeviceData);
        }
    }

    public void sendNowDeviceStatusByWebsocket(SdDevices sdDevices, String[] state,SdOperationLog sdOperationLog, String type) {
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
        sdDeviceNowState.setExecuteResult(sdOperationLog.getState());
        if(null != sdOperationLog.getEventId()) {
            sdDeviceNowState.setEventId(sdOperationLog.getEventId());
        }
        sdDeviceNowState.setExecuteTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,sdOperationLog.getCreateTime()));
        dataList.add(sdDeviceNowState);
        jsonObject.put("deviceStatusChangeLog", dataList);
        WebSocketService.broadcast("deviceStatusChangeLog", jsonObject.toString());
    }
}
