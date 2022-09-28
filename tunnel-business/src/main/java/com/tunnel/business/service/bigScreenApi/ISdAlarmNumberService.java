package com.tunnel.business.service.bigScreenApi;

import java.util.Map;

public interface ISdAlarmNumberService {

    Map<String, Object> getTodayWarningmsg();

    Map<String, Object> getTodayAccidentmsg();

}
