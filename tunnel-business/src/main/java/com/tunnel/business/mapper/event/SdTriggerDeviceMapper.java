package com.tunnel.business.mapper.event;


import com.tunnel.business.domain.event.SdTriggerDevice;

import java.util.List;

/**
 * 触发器关联设备Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
public interface SdTriggerDeviceMapper 
{
    /**
     * 查询触发器关联设备
     * 
     * @param id 触发器关联设备主键
     * @return 触发器关联设备
     */
    public SdTriggerDevice selectSdTriggerDeviceById(Long id);

    /**
     * 查询触发器关联设备列表
     * 
     * @param sdTriggerDevice 触发器关联设备
     * @return 触发器关联设备集合
     */
    public List<SdTriggerDevice> selectSdTriggerDeviceList(SdTriggerDevice sdTriggerDevice);

    /**
     * 新增触发器关联设备
     * 
     * @param sdTriggerDevice 触发器关联设备
     * @return 结果
     */
    public int insertSdTriggerDevice(SdTriggerDevice sdTriggerDevice);

    /**
     * 修改触发器关联设备
     * 
     * @param sdTriggerDevice 触发器关联设备
     * @return 结果
     */
    public int updateSdTriggerDevice(SdTriggerDevice sdTriggerDevice);

    /**
     * 删除触发器关联设备
     * 
     * @param id 触发器关联设备主键
     * @return 结果
     */
    public int deleteSdTriggerDeviceById(Long id);

    /**
     * 批量删除触发器关联设备
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTriggerDeviceByIds(Long[] ids);

    /**
     * 根据触发器id删除触发器关联设备
     * @param tid
     * @return
     */
    public int deleteSdTriggerDeviceByTriggerId(Long tid);
}
