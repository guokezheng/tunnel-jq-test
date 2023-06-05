package com.tunnel.platform.service.deviceControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import com.tunnel.deal.generalcontrol.service.CommonControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * describe: 诱导灯控制类
 *
 * @author zs
 * @date 2023/5/10
 */
@Component
public class InductionLampControl implements GeneralControlBean {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        return null;
    }

    /**
     * 设备控制
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {
        int controlState = 0;

        if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的亮度信息");
        } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的频率信息");
        }
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();

        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        //控制器不在工作台展示，传进来的子设备需要关联查父级控制器的信息
        String fEqId = sdDevices.getFEqId();
        SdDevices fDevices = sdDevicesService.selectSdDevicesById(fEqId);

        //诱导灯控制方法
        //实际控制的是父设备
        controlState = controlGuidanceLamp(map,fEqId, state,fDevices);

        //生成所控制的子设备ID日志
        SdOperationLog sdOperationLog = getOperationLog(map,sdDevices,controlState);
        sdOperationLogService.insertSdOperationLog(sdOperationLog);

        //通过websocket推送到前端
        String[] states = new String[4];
        states[0] = state;
        states[1] = brightness;
        states[2] = frequency;
        //父设备信息
        commonControlService.sendNowDeviceStatusByWebsocket(fDevices,states,sdOperationLog,"ydd");

        return controlState;
    }


    /**
     * 模拟控制方法
     *
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
        String devId = sdDevices.getEqId();
        String state = map.get("state").toString();
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();


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
        sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
        sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()));
        sdDeviceDataService.updateDeviceData(sdDevices, frequency, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()));
        //子级设备变更
        SdDevices dev = new SdDevices();
        dev.setFEqId(devId);
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
        for (int i = 0;i < list.size();i++) {
            SdDevices devo = list.get(i);
            sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
            sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_BRIGHNESS.getCode()));
            sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_FREQUENCY.getCode()));
        }
       Integer controlState = 1;
        return controlState;
    }


    /**
     * 组装操作日志
     * @param map
     * @param sdDevices
     * @param controlState
     * @return
     */
    public SdOperationLog getOperationLog(Map<String, Object> map, SdDevices sdDevices,Integer controlState){
        String devId = sdDevices.getEqId();
        String state = map.get("state").toString();
        String controlType = map.get("controlType").toString();

        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();

        //部份设备未接入，无法正确获取设备控制结果，默认失败
        sdOperationLog.setState("0");
        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType(controlType);
        if (null != map.get("operIp")) {
            sdOperationLog.setOperIp(map.get("operIp").toString());
        }
        if (null != map.get("controlTime")) {
            sdOperationLog.setCreateTime(DateUtils.parseDate(map.get("controlTime")));
        }else{
            sdOperationLog.setCreateTime(new Date());
        }
        if (null != map.get("eventId")) {
            sdOperationLog.setEventId(map.get("eventId").toString());
        }

        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.GUIDANCE_LAMP_CONTROL_MODE.getCode()));
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (data.size() > 0) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }

        sdOperationLog.setState(String.valueOf(controlState));
        return sdOperationLog;
    }


    /**
     * 诱导灯控制方法
     * @param devId
     * @param state
     * @param sdDevices 父设备信息
     * @return
     */
    public int controlGuidanceLamp(Map<String, Object> map, String devId, String state, SdDevices sdDevices) {

        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();

        boolean isopen = commonControlService.queryAnalogControlConfig();

        int controlState = 0;
        if (!isopen) {
            //连接设备进行控制
            if (sdDevices.getBrandId() != null && sdDevices.getBrandId().equals("0057")) {
                controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, null);
            } else {
                controlState = GuidanceLampHandle.getInstance().toControlXianKeDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency);
            }
        } else {
            //模拟控制
          controlState = analogControl(map,sdDevices);
        }
        return controlState;
    }
}
