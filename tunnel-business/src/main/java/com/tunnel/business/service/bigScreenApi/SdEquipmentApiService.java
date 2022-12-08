package com.tunnel.business.service.bigScreenApi;

import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 万集接口
 * @author zhai
 * @date 2022/11/15
 */
public interface SdEquipmentApiService {

    /**
     * 设备故障状况统计
     * @param tunnelId
     * @return
     */
    AjaxResult getEquipmentFault(String tunnelId);

    /**
     * 未处理故障统计
     * @param tunnelId
     * @return
     */
    AjaxResult getUnhandledFault(String tunnelId);

    /**
     * 设备类型
     * @param tunnelId
     * @return
     */
    AjaxResult getEquipmentType(String tunnelId);
}
