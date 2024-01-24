package com.tunnel.deal.borad;

import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * describe: 情报板-定时任务类
 *
 * @author gz
 * @date 2023/5/29
 */
@Component("BoradTask")
public class BoradTask {

    private static final Logger log = LoggerFactory.getLogger(BoradTask.class);

    @Autowired
    private ISdDevicesService sdDevicesService;


    @Autowired
    private SdDevicesMapper devicesMapper;


    public void selectDeviceStatus(){
        List<SdDevices> deviceList = selectBoradDeviceList();
        deviceList.stream().forEach(item -> {
            boolean reachable = true;
            try {
                //更新设备状态
                reachable = InetAddress.getByName(item.getIp()).isReachable(100);
                if(reachable != true){
                    devicesMapper.updateSdDevicesBatch(item.getEqId(),DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                }else {
                    devicesMapper.updateSdDevicesBatch(item.getEqId(),DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取情报板设备列表
     * @return
     */
    public List<SdDevices> selectBoradDeviceList(){
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(DevicesTypeEnum.VMS.getCode());
        List<SdDevices> devicesList = sdDevicesService.selectSdDevicesList(sdDevices);
        sdDevices.setEqType(DevicesTypeEnum.MEN_JIA_VMS.getCode());
        List<SdDevices> devicesList1 = sdDevicesService.selectSdDevicesList(sdDevices);
        devicesList.addAll(devicesList1);
        return devicesList;
    }
}
