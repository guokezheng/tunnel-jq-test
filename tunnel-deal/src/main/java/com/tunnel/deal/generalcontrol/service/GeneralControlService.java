package com.tunnel.deal.generalcontrol.service;

import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import sun.java2d.loops.FillRect;

/**
 * describe: 通用控制业务类
 *
 * @author zs
 * @date 2023/4/6
 */
public interface GeneralControlService {


//    /**
//     * 根据设备ID获得对应的协议类对象
//     * @param deviceId 设备ID
//     * @return
//     */
//    GeneralControlBean getProtocolBean(String deviceId);


    /**
     * 根据设备ID获得对应的协议类对象
     * @param sdDevices 设备对象
     * @return
     */
    GeneralControlBean getProtocolBean(SdDevices sdDevices);

}
