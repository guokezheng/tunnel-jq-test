package com.tunnel.deal.tcp.client.general.impl;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
     * @param deviceMap 设备信息缓存Map
     * @param ip 设备IP
     * @return
     */
    @Override
    public String getDeviceIdByIp(Map<String,Map> deviceMap,String ip){
        List<String> idList =  deviceMap.entrySet()
                .stream().filter(x-> Objects.equals(x.getValue().get("ip"), ip)).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if(idList.size() > 0){
            return idList.get(0);
        }
        return null;
    }

    /**
     * 根据设备IP筛选设备ID
     *
     * @param ip 设备IP
     * @return
     */
    @Override
    public String getDeviceIdByIp(String ip) {
        SdDevices sdDevices = new SdDevices();
        sdDevices.setIp(ip);
        List<SdDevices> list = sdDevicesService.selectDevicesByProtocol(sdDevices);
        if(list != null && list.size() > 0){
            sdDevices = list.get(0);
            return sdDevices.getEqId();
        }
        return null;
    }

    /**
     * 设备信息缓存
     * 缓存所有配置对应协议的设备
     * @param deviceMap 设备信息缓存Map
     * @param protocolCode 协议标识
     * @param eqType 设备类型
     */
    @Override
    public void deviceInfoCache(Map<String,Map> deviceMap,String protocolCode, Long eqType) {

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
            if(deviceMap.get(deviceId) == null){
                Map map = new HashMap();
                map.put("deviceId",deviceId);
                map.put("ip",device.getIp());
                map.put("port",device.getPort());
                deviceMap.put(deviceId,map);
            }else{
                Map map = deviceMap.get(deviceId);
                map.put("ip",device.getIp());
                map.put("port",device.getPort());
            }

        }
    }

    /**
     * 根据协议、设备类型查询设备
     * @param protocolCode
     * @param eqType
     * @return
     */
    @Override
    public List<SdDevices> getDevicesList(String protocolCode, Long eqType) {
        SdDevicesProtocol sdDevicesProtocol = new SdDevicesProtocol();
        sdDevicesProtocol.setProtocolCode(protocolCode);

        List<SdDevicesProtocol> protocolList = sdDevicesProtocolService.selectSdDevicesProtocolList(sdDevicesProtocol);
        if(protocolList == null || protocolList.size() == 0){
            log.error("缓存设备信息报错,未查询到对应的协议：协议标识protocolCode="+protocolCode+"，设备类型eqType="+eqType);
            return new ArrayList<>();
        }

        sdDevicesProtocol = protocolList.get(0);
        SdDevices sdDevices = new SdDevices();
        sdDevices.setProtocolId(sdDevicesProtocol.getId());
        sdDevices.setEqType(eqType);
        List<SdDevices> list = sdDevicesService.selectDevicesByProtocol(sdDevices);
        return list;
    }
}
