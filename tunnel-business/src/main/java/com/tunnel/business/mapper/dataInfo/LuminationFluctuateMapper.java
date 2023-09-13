package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.LightConfiguration;

import java.util.List;

public interface LuminationFluctuateMapper {


    /**
     * 查询所有照明设备
     *
     * @return 外部系统
     */
    public List<LightConfiguration> getlightConfiguration();
}
