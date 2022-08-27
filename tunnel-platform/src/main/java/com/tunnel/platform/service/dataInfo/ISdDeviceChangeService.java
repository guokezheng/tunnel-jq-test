package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdDeviceChange;

/**
 * 设备变更Service接口
 * 
 * @author 刘方堃
 * @date 2021-12-09
 */
public interface ISdDeviceChangeService 
{
    /**
     * 查询设备变更
     * 
     * @param id 设备变更主键
     * @return 设备变更
     */
    public SdDeviceChange selectSdDeviceChangeById(Long id);

    /**
     * 查询设备变更列表
     * 
     * @param sdDeviceChange 设备变更
     * @return 设备变更集合
     */
    public List<SdDeviceChange> selectSdDeviceChangeList(SdDeviceChange sdDeviceChange);

    /**
     * 新增设备变更
     * 
     * @param sdDeviceChange 设备变更
     * @return 结果
     */
    public int insertSdDeviceChange(SdDeviceChange sdDeviceChange);

    /**
     * 修改设备变更
     * 
     * @param sdDeviceChange 设备变更
     * @return 结果
     */
    public int updateSdDeviceChange(SdDeviceChange sdDeviceChange);

    /**
     * 批量删除设备变更
     * 
     * @param ids 需要删除的设备变更主键集合
     * @return 结果
     */
    public int deleteSdDeviceChangeByIds(Long[] ids);

    /**
     * 删除设备变更信息
     * 
     * @param id 设备变更主键
     * @return 结果
     */
    public int deleteSdDeviceChangeById(Long id);
}
