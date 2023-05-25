package com.tunnel.business.mapper.bigScreenApiN;

import java.util.List;
import java.util.Map;

public interface SdOperateDataMapper {

    /* 获取隧道昨日运营日流量 */
    Integer[] getYesterdayFlowDataTunnelsList();

    /* 获取隧道昨日周环比运营日流量增长率 */
    Integer[] getYeasterDayFlowDataTunnelsQoQList();

    /* 获取今日流量总数 */
    Integer getIndexDataTodayFlowCount();

    /* 获取今日流量增长率 */
    Integer getIndexDataTodayFolwPercent();

    /* 获取情报版总数 */
    Integer getIndexDataBoardCount();

    /* 获取7天情报板发布总数 */
    Integer getIndexDataSevenDaysBoardPushCount();

    /* 获取情报版发布总数 */
    Integer getIndexDataBoardPushCount();

    /* 获取路况信息 */
    List<Map> getEventDataList();
}
