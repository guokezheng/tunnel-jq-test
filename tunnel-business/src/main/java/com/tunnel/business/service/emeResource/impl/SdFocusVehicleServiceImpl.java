package com.tunnel.business.service.emeResource.impl;

import com.tunnel.business.domain.emeResource.SdFocusVehicle;
import com.tunnel.business.mapper.emeResource.SdFocusVehicleMapper;
import com.tunnel.business.mapper.emeResource.SdVehicleTypeMapper;
import com.tunnel.business.service.emeResource.SdFocusVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhai
 * @date 2024/1/24
 */
@Service
public class SdFocusVehicleServiceImpl implements SdFocusVehicleService {

    @Autowired
    private SdFocusVehicleMapper sdVehicleTypeMapper;

    @Override
    public List<SdFocusVehicle> selectList(SdFocusVehicle sdFocusVehicle) {
        List<SdFocusVehicle> sdFocusVehicles = sdVehicleTypeMapper.selectFocusVehicleList(sdFocusVehicle);
        return sdFocusVehicles;
    }

    @Override
    public List<SdFocusVehicle> exportData(SdFocusVehicle sdFocusVehicle) {
        return null;
    }
}
