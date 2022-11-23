package com.tunnel.business.service.bigScreenApi;

import com.tunnel.business.domain.bigScreenApi.SdProportionOfEquipment;

import java.util.List;
import java.util.Map;

public interface ISdProportionOfEquipmentServcie {

    List<Map<String, Object>> getProportionOfEquipment(String tunnelId);

    /**
     * 获取设备运行状态
     * @param tunnelId
     * @return
     */
    List<SdProportionOfEquipment> getEquipmentOperationStatus(String tunnelId);
}
