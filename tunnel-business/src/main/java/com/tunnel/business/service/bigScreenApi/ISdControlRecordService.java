package com.tunnel.business.service.bigScreenApi;

import java.util.List;
import java.util.Map;

public interface ISdControlRecordService {

    List<Map<String, Object>> getRecentControlRecordMsg(String tunnelId);

}
