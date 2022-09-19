package com.tunnel.business.mapper.bigScreenApi;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SdControlRecordMapper {

    public List<Map<String, Object>> getRecentControlRecordMsg(@Param("tunnelId") String tunnelId);

}
