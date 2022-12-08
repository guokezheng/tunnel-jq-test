package com.tunnel.business.service.bigScreenApi.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesSystemEnum;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.FaultStatusEnum;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.service.bigScreenApi.SdEquipmentApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhai
 * @date 2022/11/15
 */
@Service
public class SdEquipmentApiServiceImpl implements SdEquipmentApiService {

    @Autowired
    private SdFaultListMapper sdFaultListMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Override
    public AjaxResult getEquipmentFault(String tunnelId) {
        //监控系统
        List<Map<String, Object>> monitoring = getEqSystem(DevicesSystemEnum.DEVICE_MONITORING.getCode(),tunnelId);
        //通风系统
        List<Map<String, Object>> ventilation = getEqSystem(DevicesSystemEnum.DEVICE_VENTILATION.getCode(),tunnelId);
        //照明系统
        List<Map<String, Object>> lighting = getEqSystem(DevicesSystemEnum.DEVICE_LIGHTING.getCode(),tunnelId);
        //能源系统
        List<Map<String, Object>> energy = getEqSystem(DevicesSystemEnum.DEVICE_ENERGY.getCode(),tunnelId);
        //消防系统
        List<Map<String, Object>> fireFighting = getEqSystem(DevicesSystemEnum.DEVICE_FIREFIGHTING.getCode(),tunnelId);
        Map<String,Object> map = new HashMap<>();
        map.put("monitoringList",monitoring);
        map.put("ventilationList",ventilation);
        map.put("lightingList",lighting);
        map.put("fireFightingList",fireFighting);
        map.put("energyList",energy);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getUnhandledFault(String tunnelId) {
        List<Map<String, Object>> equipmentFault = sdFaultListMapper.getEquipmentFaultStatistics(DictTypeEnum.eq_system.getCode(), tunnelId,
                                                                                                 FaultStatusEnum.DEVICE_RELEASE.getCode(),
                                                                                                 FaultStatusEnum.DEVICE_NO_REMOVE.getCode());
        Map<String, Object> map = new HashMap<>();
        map.put("eqUnhandledFaultList",equipmentFault);
        return AjaxResult.success(map);
    }

    @Override
    public AjaxResult getEquipmentType(String tunnelId) {
        List<Map<String, Object>> equipmentType = sdDevicesMapper.getEquipmentType(tunnelId, DevicesStatusEnum.DEVICE_ON_LINE.getCode(),
                                                                                   FaultStatusEnum.DEVICE_RELEASE.getCode(),
                                                                                   FaultStatusEnum.DEVICE_NO_REMOVE.getCode());
        return AjaxResult.success(equipmentType);
    }

    /**
     * 查询设备系统集合
     * @param eqSystemType
     * @param tunnelId
     * @return
     */
    public List<Map<String, Object>> getEqSystem(String eqSystemType, String tunnelId){
        List<Map<String, Object>> equipmentFault = sdFaultListMapper.getEquipmentFault(DictTypeEnum.eq_system.getCode(), eqSystemType, tunnelId,
                                                                                       FaultStatusEnum.DEVICE_RELEASE.getCode());
        String value = DevicesSystemEnum.getValue(eqSystemType);
        equipmentFault.stream().forEach(item -> {
            item.put("eqSystemName",value);
            item.put("eqSystemCode",eqSystemType);
        });
        return equipmentFault;
    }
}
