package com.tunnel.deal.tcp.client.general.impl;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.tcp.client.config.DeviceManager;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * describe: tcp协议通用业务实现类
 *
 * @author zs
 * @date 2023/6/26
 */
@Service
public class TcpClientGeneralImpl implements TcpClientGeneralService {

    private static final Logger log = LoggerFactory.getLogger(TcpClientGeneralImpl.class);

    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    /**
     * 根据设备ID获得对应的协议类对象
     *
     * @param sdDevices 设备对象
     * @return
     */
    @Override
    public TcpClientGeneralBean getProtocolBean(SdDevices sdDevices) {
        Long protocolId = sdDevices.getProtocolId();
        SdDevicesProtocol devicesProtocol = sdDevicesProtocolService.selectSdDevicesProtocolById(protocolId);
        if(devicesProtocol == null){
            return null;
        }
        String className = devicesProtocol.getClassName();
        //获取协议类对象
        TcpClientGeneralBean generalControlDevice = getBeanOfDeviceProtocol(className);
        return generalControlDevice;
    }


    /**
     * 从Spring容器中获取设备协议中配置的Class对象
     *
     * @param className
     * @return
     */
    public TcpClientGeneralBean getBeanOfDeviceProtocol(String className) {
        TcpClientGeneralBean tcpClientGeneralBean = null;
        try {
            Class<?> aClass = Class.forName(className);
            Object object = aClass.newInstance();
            if (object instanceof TcpClientGeneralBean) {
                tcpClientGeneralBean = (TcpClientGeneralBean) SpringContextUtils.getBean(object.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tcpClientGeneralBean;
    }


    /**
     * 根据设备IP筛选设备ID
     * @param ip 设备IP
     * @return
     */
    @Override
    public String getDeviceIdByIp(String ip){
        List<String> idList =  DeviceManager.deviceMap.entrySet()
                .stream().filter(x-> Objects.equals(x.getValue().get("ip"), ip)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if(idList.size() > 0){
            return idList.get(0);
        }
        return null;
    }

    /**
     * 设备信息缓存
     * 缓存所有配置测控执行器协议的设备（类型为测控执行器）
     *
     * @param protocolCode 协议标识
     * @param eqType 设备类型
     */
    @Override
    public void deviceInfoCache(String protocolCode, Long eqType) {

        SdDevicesProtocol sdDevicesProtocol = new SdDevicesProtocol();
        sdDevicesProtocol.setProtocolCode(protocolCode);

        List<SdDevicesProtocol> protocolList = sdDevicesProtocolService.selectSdDevicesProtocolList(sdDevicesProtocol);
        if(protocolList == null || protocolList.size() == 0){
            log.error("缓存设备信息报错,未查询到对应的协议：协议标识protocolCode="+protocolCode+"，设备类型eqType="+eqType);
            return;
        }

        sdDevicesProtocol = protocolList.get(0);
        SdDevices sdDevices = new SdDevices();
        sdDevices.setProtocolId(sdDevicesProtocol.getId());
        sdDevices.setEqType(eqType);
        List<SdDevices> list = sdDevicesService.selectDevicesByProtocol(sdDevices);
        for(SdDevices device : list){
            String deviceId = device.getEqId();
            Map map = new HashMap();
            map.put("deviceId",deviceId);
            map.put("ip",device.getIp());
            map.put("port",device.getPort());
            DeviceManager.deviceMap.put(deviceId,map);
        }
    }
}
