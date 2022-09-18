package com.tunnel.platform.service.bigScreenApi;

import java.util.List;
import java.util.Map;

public interface ISdControlRecordService {

    public List<Map<String, Object>> getRecentControlRecordMsg(String tunnelId);

}
