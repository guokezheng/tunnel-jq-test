package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdLipowerDevice;

import java.util.List;

/**
 * 照明设备信息Service接口
 * 
 * @author wangx
 * @date 2021-03-15
 */
public interface ISdLipowerDeviceService 
{

    /**
     * 查询照明设备信息列表
     * 
     * @param sdLipowerDevice 照明设备信息
     * @return 照明设备信息集合
     */
    public List<SdLipowerDevice> selectSdLipowerDeviceList(SdLipowerDevice sdLipowerDevice);

    
    /**
     * 查询照明系统信息
     * @param groupId
     * @param sdLipowerDeviceList
     * @param lists
     * @return
     */
	public int getLightingSystemInfo(String groupId,List<SdLipowerDevice> lists);
}
