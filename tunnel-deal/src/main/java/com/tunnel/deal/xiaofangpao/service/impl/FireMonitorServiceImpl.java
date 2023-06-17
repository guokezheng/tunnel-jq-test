package com.tunnel.deal.xiaofangpao.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.deal.xiaofangpao.service.FireMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * describe: 消防炮service实现类
 *
 * @author tjw
 * @date 2023/6/13
 */
@Service
public class FireMonitorServiceImpl implements FireMonitorService {

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdDeviceDataMapper sdDeviceDataMapper;

    private static final String FireMonitorType = "33";

    /**
     * 更新消防炮设备状态
     * @param deviceAddress
     * @param eqStatus
     * @param dataStatus
     * @return
     */
    @Override
    public int updateFireMonitorStatus(String deviceAddress, String eqStatus, String dataStatus) {
        SdDevices sdDevices = new SdDevices();
        sdDevices.setIp(deviceAddress);
        sdDevices.setEqStatus(eqStatus);
        sdDevices.setUpdateTime(DateUtils.getNowDate());
        int result =  sdDevicesMapper.updateFireMonitorStatus(sdDevices);
        if(result>0){
            //查询消防炮设备的id和数据项id
            String deviceId = sdDevicesMapper.getFireMonitorId(deviceAddress);
            Long deviceTypeItem = sdDevicesMapper.getFireMonitorTypeItem(FireMonitorType);
            SdDeviceData sdDeviceData = new SdDeviceData();
            sdDeviceData.setDeviceId(deviceId);
            sdDeviceData.setItemId(deviceTypeItem);
            //判断数据库中是否存在
            List<SdDeviceData> list = sdDeviceDataMapper.selectSdDeviceDataInfo(sdDeviceData);
            //有，则更新  无，则添加
            if(list.size()>0){
                sdDeviceData.setDeviceId(deviceId);
                sdDeviceData.setItemId(deviceTypeItem);
                sdDeviceData.setUpdateTime(DateUtils.getNowDate());
                sdDeviceData.setData(dataStatus);
                sdDeviceDataMapper.updateFireMonitorData(sdDeviceData);
            }else{
                sdDeviceData.setDeviceId(deviceId);
                sdDeviceData.setItemId(deviceTypeItem);
                sdDeviceData.setCreateTime(DateUtils.getNowDate());
                sdDeviceData.setData(dataStatus);
                sdDeviceDataMapper.insertFireMonitorData(sdDeviceData);
            }

        }
        return result;

    }
}
