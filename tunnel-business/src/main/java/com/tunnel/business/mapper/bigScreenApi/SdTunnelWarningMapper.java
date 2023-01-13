package com.tunnel.business.mapper.bigScreenApi;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SdTunnelWarningMapper {

    public List<Long> getTunnelWarningNumber(@Param("tunnelId") String tunnelId);

}
