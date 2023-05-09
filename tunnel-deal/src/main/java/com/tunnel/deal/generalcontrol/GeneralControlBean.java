package com.tunnel.deal.generalcontrol;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;

import java.util.Map;

/**
 * describe: 通用设备控制类
 *
 * 设备控制通用接口
 * 所有的子类：测控执行器、PLC、鸿蒙控制器（杭山东接口）、三晶照明
 * @author zs
 * @date 2023/4/4
 */
public interface GeneralControlBean {

//    /**
//     * 控制方法
//     * @param sdDevices 设备信息
//     * @param state 控制状态
//     * @return
//     */
//    AjaxResult control(SdDevices sdDevices, String state);
//
//
//    AjaxResult control(Map<String, Object> map);

    AjaxResult control(Map<String, Object> map,SdDevices sdDevices);
}
