package com.tunnel.platform.mapper.bigScreenApi;

import java.util.List;
import java.util.Map;

public interface SdAlarmNumberMapper {

    public List<Map<String, Object>> getTodayWarningmsg();

    public List<Map<String, Object>> getTodayWarningmsgStatus();

    public List<Map<String, Object>> getTodayAccidentmsg();

    public List<Map<String, Object>> getTodayAccidentmsgStatus();

}
