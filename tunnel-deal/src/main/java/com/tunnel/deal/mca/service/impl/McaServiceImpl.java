package com.tunnel.deal.mca.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicePointControlTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DeviceStateTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.TunnelEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.protocol.SdDevicePoint;
import com.tunnel.business.domain.protocol.SdDevicePointState;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdEquipmentStateService;
import com.tunnel.business.service.protocol.ISdDevicePointService;
import com.tunnel.business.service.protocol.ISdDevicePointStateService;
import com.tunnel.deal.mca.config.DeviceManager;
import com.tunnel.deal.mca.service.McaService;
import com.tunnel.deal.mca.service.McaCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe: MCA控制类的实现类
 *
 * @author zs
 * @date 2023/3/30
 */
@Service
public class McaServiceImpl implements McaService {

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdDevicePointService devicePointService;

    @Autowired
    private ISdEquipmentStateService equipmentStateService;


    @Autowired
    private ISdDevicePointStateService devicePointStateService;

    @Autowired
    private McaCmd mcaCmd;

    /**
     * 控制设备
     *
     * @param deviceId 设备Id
     * @param state    设备状态
     */
    @Override
    public AjaxResult control(String deviceId, String state) {
        //点位类型：控制点位
        Long pointType = DevicePointControlTypeEnum.control_enable.getCode();
        //数据状态
        String runState = DeviceStateTypeEnum.data_status.getCode();

        //设备信息
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);

        //查询设备状态
        SdEquipmentState sdEquipmentState = new SdEquipmentState();
        sdEquipmentState.setStateTypeId(sdDevices.getEqType());
        sdEquipmentState.setStateType(runState);
        sdEquipmentState.setDeviceState(state);
        List<SdEquipmentState> stateList = equipmentStateService.selectSdEquipmentStateList(sdEquipmentState);
        if(stateList == null || stateList.size() == 0){
           return AjaxResult.error("未配置设备状态");
        }
        sdEquipmentState = stateList.get(0);

        //查询设备点位
        SdDevicePoint devicePoint = new SdDevicePoint();
        devicePoint.setEqId(deviceId);
        devicePoint.setIsReserved(pointType);
        List<SdDevicePoint> devicePointList = devicePointService.selectSdDevicePointList(devicePoint);
        if(devicePointList == null || devicePointList.size() == 0){
            return AjaxResult.error("未配置设备点位");
        }
        devicePoint = devicePointList.get(0);
        //查询对应的设备控制状态
        SdDevicePointState sdDevicePointState = new SdDevicePointState();
        sdDevicePointState.setDevicePointId(devicePoint.getId());
        sdDevicePointState.setDeviceStateId(sdEquipmentState.getId());
        List<SdDevicePointState> pointStateList = devicePointStateService.selectSdDevicePointStateList(sdDevicePointState);
        if(pointStateList == null || pointStateList.size() == 0){
            return AjaxResult.error("未配置设备点位");
        }

        sdDevicePointState = pointStateList.get(0);
        //下发状态
        String controlState = sdDevicePointState.getControlState();
        //控制点位
        String address = devicePoint.getAddress();
        //功能码
        String functionCode = devicePoint.getFunctionCode();

        //通过控制父设备测控执行器控制设备
        String fEqId = sdDevices.getfEqId();
        if(fEqId == null || "".equals(fEqId)){
            return AjaxResult.error("未配置设备关联的测控执行器");
        }

        //发送指令
      return mcaCmd.sendControlCommand(fEqId,functionCode,address,controlState);
    }


    /**
     * 缓存测控执行器设备信息
     */
    @Override
    public void getDeviceList() {
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(DevicesTypeEnum.CE_KONG_ZHI_XING_QI.getCode());
        List<String> typeList = new ArrayList<>();
        //todo 测试暂时指定隧道
        String tunnelId = TunnelEnum.JIN_JIA_LOU.getCode();
        typeList.add(String.valueOf(DevicesTypeEnum.CE_KONG_ZHI_XING_QI.getCode()));
        List<SdDevices> devicesList = sdDevicesService.selectDeviceList(tunnelId,typeList);
        for(SdDevices device : devicesList){
            String deviceId = device.getEqId();
            Map map = new HashMap();
            map.put("deviceId",deviceId);
            map.put("ip",device.getIp());
            map.put("port",device.getPort());
            DeviceManager.deviceMap.put(deviceId,map);
        }
    }
}
