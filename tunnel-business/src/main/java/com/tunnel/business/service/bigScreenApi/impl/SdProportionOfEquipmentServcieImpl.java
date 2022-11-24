package com.tunnel.business.service.bigScreenApi.impl;

import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.domain.bigScreenApi.SdProportionOfEquipment;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.mapper.bigScreenApi.SdProportionOfEquipmentMapper;
import com.tunnel.business.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.business.service.bigScreenApi.ISdProportionOfEquipmentServcie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class SdProportionOfEquipmentServcieImpl implements ISdProportionOfEquipmentServcie {

    private static final Logger log = LoggerFactory.getLogger(SdProportionOfEquipmentServcieImpl.class);

    @Autowired
    private SdProportionOfEquipmentMapper sdProportionOfEquipmentMapper;
    @Autowired
    private SdTunnelsMapper sdTunnelsMapper;

    @Override
    public List<Map<String, Object>> getProportionOfEquipment(String tunnelId) {
        log.info("调用设备占比查询接口成功");
        if (tunnelId == null || tunnelId.equals("")) {
            SdTunnels sdTunnels = new SdTunnels();
            List<SdTunnels> sdTunnelsList = sdTunnelsMapper.selectSdTunnelsList(sdTunnels);
            tunnelId = sdTunnelsList.get(0).getTunnelId();
        }
        return sdProportionOfEquipmentMapper.getProportionOfEquipment(tunnelId);
    }

    @Override
    public List<SdProportionOfEquipment> getEquipmentOperationStatus(String tunnelId) {
        List<SdProportionOfEquipment> list = sdProportionOfEquipmentMapper.getEquipmentOperationStatus(tunnelId);
        List<SdProportionOfEquipment> equipmentOperationStatus = checkEqStatus(list);
        return equipmentOperationStatus;
    }

    /**
     * 判断是否缺少状态
     * @param equipmentOperationStatus
     * @return
     */
    public List<SdProportionOfEquipment> checkEqStatus(List<SdProportionOfEquipment> equipmentOperationStatus){
        if(equipmentOperationStatus.size() < 3){
            int zx = 0;
            int lx = 0;
            int gz = 0;
            for(SdProportionOfEquipment item : equipmentOperationStatus){
                if(DevicesStatusEnum.DEVICE_ON_LINE.getCode().equals(item.getEqStatus())){
                    zx = 1;
                }
                if(DevicesStatusEnum.DEVICE_OFF_LINE.getCode().equals(item.getEqStatus())){
                    lx = 1;
                }
                if(DevicesStatusEnum.DEVICE_ERROR.getCode().equals(item.getEqStatus())){
                    gz = 1;
                }
            }
            if(zx == 0){
                SdProportionOfEquipment sdProportionOfEquipment = new SdProportionOfEquipment();
                sdProportionOfEquipment.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                sdProportionOfEquipment.setNumber(0);
                sdProportionOfEquipment.setPercentage("0%");
                equipmentOperationStatus.add(sdProportionOfEquipment);
            }
            if(lx == 0){
                SdProportionOfEquipment sdProportionOfEquipment = new SdProportionOfEquipment();
                sdProportionOfEquipment.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                sdProportionOfEquipment.setNumber(0);
                sdProportionOfEquipment.setPercentage("0%");
                equipmentOperationStatus.add(sdProportionOfEquipment);
            }
            if(gz == 0){
                SdProportionOfEquipment sdProportionOfEquipment = new SdProportionOfEquipment();
                sdProportionOfEquipment.setEqStatus(DevicesStatusEnum.DEVICE_ERROR.getCode());
                sdProportionOfEquipment.setNumber(0);
                sdProportionOfEquipment.setPercentage("0%");
                equipmentOperationStatus.add(sdProportionOfEquipment);
            }
        }
        Collections.sort(equipmentOperationStatus, new Comparator<SdProportionOfEquipment>() {
            @Override
            public int compare(SdProportionOfEquipment o1, SdProportionOfEquipment o2) {
                Integer integer1 = Integer.valueOf(o1.getEqStatus());
                Integer integer2 = Integer.valueOf(o2.getEqStatus());
                if (integer1 > integer2) {
                    return 1;
                }
                if (integer1 == integer2) {
                    return 0;
                }
                return -1;
            }
        });
        return equipmentOperationStatus;
    }
}
