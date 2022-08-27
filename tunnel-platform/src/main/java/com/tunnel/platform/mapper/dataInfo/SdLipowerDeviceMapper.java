package com.tunnel.platform.mapper.dataInfo;


import com.tunnel.platform.domain.dataInfo.SdLipowerDevice;

import java.util.List;

/**
 * 照明设备信息Mapper接口
 * 
 * @author wangx
 * @date 2021-03-15
 */
public interface SdLipowerDeviceMapper 
{

    /**
     * 查询照明设备信息列表
     * 
     * @param sdLipowerDevice 照明设备信息
     * @return 照明设备信息集合
     */
    public List<SdLipowerDevice> selectSdLipowerDeviceList(SdLipowerDevice sdLipowerDevice);

    /**
     * 批量添加照明设备信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int insertSdLipowerDeviceList(List<SdLipowerDevice> list);
}
