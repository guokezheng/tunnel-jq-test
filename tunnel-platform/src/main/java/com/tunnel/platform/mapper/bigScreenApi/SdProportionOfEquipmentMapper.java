package com.tunnel.platform.mapper.bigScreenApi;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SdProportionOfEquipmentMapper {

    public List<Map<String, Object>> getProportionOfEquipment(@Param("tunnelId") String tunnelId);

}
