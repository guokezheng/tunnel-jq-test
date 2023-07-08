package com.tunnel.deal.generalcontrol.service.impl;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.product.ISdProductService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.generalcontrol.service.GeneralControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * describe: 通用控制业务类实现类
 *
 * @author zs
 * @date 2023/4/6
 */
@Service
public class GeneralControlServiceImpl implements GeneralControlService {


    @Autowired
    private ISdDevicesService sdDevicesService;


    @Autowired
    private ISdProductService sdProductService;


    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;


//    /**
//     * 根据设备ID获得对应的协议类对象
//     *
//     * @param deviceId 设备ID
//     * @return
//     */
//    @Override
//    public GeneralControlBean getProtocolBean(String deviceId) {
//        //查询设备关联的产品ID
////        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
//////        String productId = sdDevices.getProductId();
////        //查询产品关联的协议信息
////        SdProduct sdProduct = sdProductService.selectSdProductById(Long.valueOf(productId));
////        Long protocolId = sdProduct.getProtocolId();
////        SdDevicesProtocol devicesProtocol = sdDevicesProtocolService.selectSdDevicesProtocolById(protocolId);
////        String className = devicesProtocol.getClassName();
////        //获取协议类对象
////        GeneralControlBean generalControlDevice = getBeanOfDeviceProtocol(className);
////        return generalControlDevice;
//        return null;
//    }

    /**
     * 根据设备ID获得对应的协议类对象
     *
     * @param sdDevices 设备对象
     * @return
     */
    @Override
    public GeneralControlBean getProtocolBean(SdDevices sdDevices) {
//        String deviceId = sdDevices.getEqId();
        Long protocolId = sdDevices.getProtocolId();
//        String fEqId = sdDevices.getfEqId();
//        if(fEqId == null || "".equals(fEqId)){
////            //查询设备关联的产品ID
////            sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
////            fEqId = sdDevices.getfEqId();
//            return null;
//        }
//        //查询父设备的产品ID
//        SdDevices fDevice = sdDevicesService.selectSdDevicesById(fEqId);
//        String productId = fDevice.getProductId();
//        if(productId == null || "".equals(productId)){
//           return null;
//        }
//        //查询产品关联的协议信息
//        SdProduct sdProduct = sdProductService.selectSdProductById(Long.valueOf(productId));
//        Long protocolId = sdProduct.getProtocolId();
        SdDevicesProtocol devicesProtocol = sdDevicesProtocolService.selectSdDevicesProtocolById(protocolId);
        if(devicesProtocol == null){
            return null;
        }
        String className = devicesProtocol.getClassName();
        //获取协议类对象
        GeneralControlBean generalControlDevice = getBeanOfDeviceProtocol(className);
        return generalControlDevice;
    }

    /**
     * 从Spring容器中获取设备协议中配置的Class对象
     *
     * @param className
     * @return
     */
    public GeneralControlBean getBeanOfDeviceProtocol(String className) {
        GeneralControlBean generalControlDevice = null;
        try {
            Class<?> aClass = Class.forName(className);
            Object object = aClass.newInstance();
            if (object instanceof GeneralControlBean) {
                generalControlDevice = (GeneralControlBean) SpringContextUtils.getBean(object.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generalControlDevice;
    }
}
