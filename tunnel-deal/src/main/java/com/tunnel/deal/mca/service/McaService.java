package com.tunnel.deal.mca.service;

import com.ruoyi.common.core.domain.AjaxResult;

/**
 * describe: MCA控制类
 *
 * @author zs
 * @date 2023/3/30
 */
public interface McaService {

    /**
     * 控制设备
     * @param deviceId 设备Id
     * @param state 设备状态
     */
    AjaxResult control(String deviceId, String state);


    /**
     * 缓存测控执行器设备信息
     */
    void getDeviceList();

}
