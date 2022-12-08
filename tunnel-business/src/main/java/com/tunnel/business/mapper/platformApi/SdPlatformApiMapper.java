package com.tunnel.business.mapper.platformApi;


import com.tunnel.business.domain.platformApi.SdPlatformApi;

import java.util.List;

/**
 * 操作日志Mapper接口
 *
 * @author yanghousheng
 * @date 2020-09-03
 */
public interface SdPlatformApiMapper
{
    int insertSdPushHistory(SdPlatformApi sdPlatformApi);

    int updateSdPushHistory(SdPlatformApi sdPlatformApi);

    int deleteSdPushHistoryById(Long id);

    int deleteSdPushHistoryByIds(String[] strings);

    List<SdPlatformApi> selectList();
}
