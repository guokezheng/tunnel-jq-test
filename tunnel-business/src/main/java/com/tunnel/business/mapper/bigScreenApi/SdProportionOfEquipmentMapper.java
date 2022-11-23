package com.tunnel.business.mapper.bigScreenApi;

import com.tunnel.business.domain.bigScreenApi.SdProportionOfEquipment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SdProportionOfEquipmentMapper {

    public List<Map<String, Object>> getProportionOfEquipment(@Param("tunnelId") String tunnelId);

    /**
     * 获取设备运行状态
     * @param tunnelId
     * @return
     */
    List<SdProportionOfEquipment> getEquipmentOperationStatus(@Param("tunnelId") String tunnelId);

}
