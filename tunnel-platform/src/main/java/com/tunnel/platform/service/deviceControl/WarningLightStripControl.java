package com.tunnel.platform.service.deviceControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.CommonControlService;
import com.tunnel.deal.warninglightstrip.WarningLightStripHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * describe: 警示灯带控制类
 *
 * @author zs
 * @date 2023/5/11
 */
@Component
public class WarningLightStripControl implements GeneralControlBean {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;


    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

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

        //警示灯带控制方法
        controlState = controlWarningLightStripDevice(map,devId, state, sdDevices);

        //生成日志
        addOperationLog(map,sdDevices,controlState);

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

        String state = map.get("state").toString();

        //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
        sdDevices.setEqStatus("1");
        sdDevices.setEqStatusTime(new Date());
        sdDevicesService.updateSdDevices(sdDevices);
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        if (sdDeviceTypeItems.size() == 0) {
            throw new RuntimeException("当前设备没有设备类型数据项数据");
        }
        SdDeviceTypeItem typeItem = sdDeviceTypeItems.get(0);
        sdDeviceDataService.updateDeviceData(sdDevices, state, typeItem.getId());
       Integer controlState = 1;
       return controlState;
    }


    public void addOperationLog(Map<String, Object> map,SdDevices sdDevices,Integer controlState){

        String devId = map.get("devId").toString();
        String state = map.get("state").toString();
        String controlType = map.get("controlType").toString();

        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(devId);
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);

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

        //警示灯带设备类型数据项目前只有一个状态state
        if (data.size() > 0) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }

        sdOperationLog.setState(String.valueOf(controlState));
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
    }

    /**
     * 警示灯带控制方法
     * @param devId
     * @param state
     * @param sdDevices
     * @return
     */
    private int controlWarningLightStripDevice(Map<String, Object> map, String devId, String state, SdDevices sdDevices) {
        Integer controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();

        if (!isopen) {
            //连接设备进行控制
            controlState = WarningLightStripHandle.getInstance().toControlDev(devId, Integer.parseInt(state), sdDevices);
        } else {
            //模拟控制
            controlState = analogControl(map,sdDevices);
        }
        return controlState;
    }
}
