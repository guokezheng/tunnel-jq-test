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


    /**
     * 设备控制--工作台单个设备控制
     * @param map
     * @param sdDevices
     * @return
     */
    AjaxResult control(Map<String, Object> map,SdDevices sdDevices);


    /**
     * 设备控制+模拟控制--其他模块调用的统一控制方法
     * @param map
     * @return
     */
    Integer controlDevices(Map<String, Object> map);


    /**
     * 模拟控制方法
     * @param map
     * @param sdDevices
     * @return
     */
    Integer analogControl(Map<String, Object> map, SdDevices sdDevices);


}
