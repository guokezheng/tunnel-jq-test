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
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.guidancelamp.control.util.GuidanceLampHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * describe: 疏散标志控制类
 *
 * @author zs
 * @date 2023/5/11
 */
@Component
public class EvacuationSignControl implements GeneralControlBean {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private CommonControlService commonControlService;

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

        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
//        String controlType = map.get("controlType").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        if (map.get("brightness") == null || map.get("brightness").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的亮度信息");
        } else if (map.get("frequency") == null || map.get("frequency").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的频率信息");
        } else if (map.get("fireMark") == null || map.get("fireMark").toString().equals("")) {
            throw new RuntimeException("未指定设备需要变更的标号位置信息");
        }
        String fireMark = map.get("fireMark").toString();
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();


        //控制器不在工作台展示，传进来的子设备需要关联查父级控制器的信息
        String fEqId = sdDevices.getFEqId();
        SdDevices fDevices = sdDevicesService.selectSdDevicesById(fEqId);
        //疏散标志控制方法
        //实际控制的是父设备
        controlState = controlEvacuationSign(map, fEqId, state, fDevices);

        //生成所控制的子设备ID日志
        SdOperationLog sdOperationLog = getOperationLog(map,sdDevices,controlState);
        sdOperationLogService.insertSdOperationLog(sdOperationLog);

        //通过websocket推送到前端
        String[] states = new String[4];
        states[0] = state;
        states[1] = brightness;
        states[2] = frequency;
        states[3] = fireMark;
        commonControlService.sendNowDeviceStatusByWebsocket(sdDevices,states,sdOperationLog,"ydd");

        return controlState;
    }


    /**
     * 模拟控制方法
     *
     * @param map
     * @param sdDevices
     * @return
     */
//    @Override
    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String fireMark = map.get("fireMark").toString();
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();

        //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
        sdDevices.setEqStatus("1");
        sdDevices.setEqStatusTime(new Date());
        sdDevicesService.updateSdDevices(sdDevices);
        if (sdDevices.getEqId() != null && !"".equals(sdDevices.getEqId())) {
            SdDevices devices = new SdDevices();
            devices.setEqStatus("1");
            devices.setEqStatusTime(new Date());
            devices.setFEqId(sdDevices.getEqId());
            sdDevicesService.updateSdDevicesByFEqId(devices);
        }
        //父级设备变更
        sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
        sdDeviceDataService.updateDeviceData(sdDevices, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
        sdDeviceDataService.updateDeviceData(sdDevices, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
        sdDeviceDataService.updateDeviceData(sdDevices, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
        //子级设备变更
        SdDevices dev = new SdDevices();
        dev.setFEqId(sdDevices.getEqId());
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(dev);
        if (!list.isEmpty()) {
            //疏散标志关灯
            if (fireMark.equals("0") && !fireMark.equals("255")) {
                state = "1";
                for (int i = 0;i < list.size();i++) {
                    SdDevices devo = list.get(i);
                    sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                    sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                    sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                    sdDeviceDataService.updateDeviceData(devo, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
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
                    sdDeviceDataService.updateDeviceData(devices, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
                    sdDeviceDataService.updateDeviceData(devices, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                    sdDeviceDataService.updateDeviceData(devices, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                    sdDeviceDataService.updateDeviceData(devices, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                }
            } else {
                //疏散标志开灯无报警点
                state = "2";
                for (int i = 0;i < list.size();i++) {
                    SdDevices devo = list.get(i);
                    sdDeviceDataService.updateDeviceData(devo, state, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
                    sdDeviceDataService.updateDeviceData(devo, brightness, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_BRIGHNESS.getCode()));
                    sdDeviceDataService.updateDeviceData(devo, frequency, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FREQUENCY.getCode()));
                    sdDeviceDataService.updateDeviceData(devo, fireMark, Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_FIREMARK.getCode()));
                }
            }
        }
        Integer controlState = 1;
        return controlState;
    }

    public SdOperationLog getOperationLog(Map<String, Object> map, SdDevices sdDevices,Integer controlState){

        String state = map.get("state").toString();
        String controlType = map.get("controlType").toString();

        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.EVACUATION_SIGN_CONTROL_MODE.getCode()));
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);


        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();

        //部份设备未接入，无法正确获取设备控制结果，默认失败
        sdOperationLog.setState("0");

        if (data.size() > 0) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }

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

        sdOperationLog.setState(String.valueOf(controlState));
        return sdOperationLog;
    }


    /**
     * 疏散标志控制方法
     * @param devId 父设备ID
     * @param state
     * @param sdDevices 父设备信息
     * @return
     */
    private int controlEvacuationSign(Map<String, Object> map, String devId, String state, SdDevices sdDevices) {
        Integer controlState = 0;

        String fireMark = map.get("fireMark").toString();
        String brightness = map.get("brightness").toString();
        String frequency = map.get("frequency").toString();

        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (!isopen) {
            //连接设备进行控制
            controlState = GuidanceLampHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices, brightness, frequency, fireMark);
        } else{
            //模拟控制
            controlState = analogControl(map,sdDevices);
        }

        return controlState;
    }

}
