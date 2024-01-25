package com.tunnel.business.service.emeResource;

import com.tunnel.business.domain.emeResource.SdFocusVehicle;

import java.util.List;

/**
 * @author zhai
 * @date 2024/1/24
 */
public interface SdFocusVehicleService {

    /**
     * 查询列表
     * @param sdFocusVehicle
     * @return
     */
    List<SdFocusVehicle> selectList(SdFocusVehicle sdFocusVehicle);

    List<SdFocusVehicle> exportData(SdFocusVehicle sdFocusVehicle);
}
