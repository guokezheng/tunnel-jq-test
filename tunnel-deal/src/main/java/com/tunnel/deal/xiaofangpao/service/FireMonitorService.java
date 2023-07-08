package com.tunnel.deal.xiaofangpao.service;

/**
 * describe: 消防炮service
 *
 * @author tjw
 * @date 2023/6/13
 */
public interface FireMonitorService {

    /**
     * 更新消防炮设备状态
     * @param deviceAddress
     * @param eqStatus
     * @param dataStatus
     * @return
     */
    int updateFireMonitorStatus(String deviceAddress, String eqStatus, String dataStatus);
}
