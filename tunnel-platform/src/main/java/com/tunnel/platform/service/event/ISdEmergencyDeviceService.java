package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdEmergencyDevice;

import java.util.List;

/**
 * 应急物资信息Service接口
 * 
 * @author ruoyi
 * @date 2021-05-12
 */
public interface ISdEmergencyDeviceService
{
    /**
     * 查询应急物资信息
     * 
     * @param id 应急物资信息ID
     * @return 应急物资信息
     */
    public SdEmergencyDevice selectSdEmergencyDeviceById(Long id);

    /**
     * 查询应急物资信息列表
     * 
     * @param SdEmergencyDevice 应急物资信息
     * @return 应急物资信息集合
     */
    public List<SdEmergencyDevice> selectSdEmergencyDeviceList(SdEmergencyDevice SdEmergencyDevice);

    /**
     * 新增应急物资信息
     * 
     * @param SdEmergencyDevice 应急物资信息
     * @return 结果
     */
    public int insertSdEmergencyDevice(SdEmergencyDevice SdEmergencyDevice);

    /**
     * 修改应急物资信息
     * 
     * @param SdEmergencyDevice 应急物资信息
     * @return 结果
     */
    public int updateSdEmergencyDevice(SdEmergencyDevice SdEmergencyDevice);

    /**
     * 批量删除应急物资信息
     * 
     * @param ids 需要删除的应急物资信息ID
     * @return 结果
     */
    public int deleteSdEmergencyDeviceByIds(Long[] ids);

    /**
     * 删除应急物资信息信息
     * 
     * @param id 应急物资信息ID
     * @return 结果
     */
    public int deleteSdEmergencyDeviceById(Long id);
}
