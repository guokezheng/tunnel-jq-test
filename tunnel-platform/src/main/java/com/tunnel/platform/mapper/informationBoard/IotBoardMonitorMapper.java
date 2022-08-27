package com.tunnel.platform.mapper.informationBoard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监控台情报板Mapper接口
 * 
 * @author wangyaozong
 * @date 2020-03-25
 */
public interface IotBoardMonitorMapper
{
	/**
	 * 获取情报板设备树
	 * @return
	 */
    List<HashMap<String ,Object>>selectDeviceInfo();
  
    /**
     * 获取设备信息
     * @param deviceId
     * @return
     */
    Map getDeviceInfo(Long deviceId);
}
