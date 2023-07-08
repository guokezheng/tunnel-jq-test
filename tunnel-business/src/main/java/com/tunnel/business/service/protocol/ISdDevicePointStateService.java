package com.tunnel.business.service.protocol;

import com.tunnel.business.domain.protocol.SdDevicePointState;

import java.util.List;

/**
 * 设备点位状态Service接口
 *
 * @author ruoyi
 * @date 2023-04-11
 */
public interface ISdDevicePointStateService
{
    /**
     * 查询设备点位状态
     *
     * @param id 设备点位状态主键
     * @return 设备点位状态
     */
    public SdDevicePointState selectSdDevicePointStateById(Long id);

    /**
     * 查询设备点位状态列表
     *
     * @param sdDevicePointState 设备点位状态
     * @return 设备点位状态集合
     */
    public List<SdDevicePointState> selectSdDevicePointStateList(SdDevicePointState sdDevicePointState);

    /**
     * 新增设备点位状态
     *
     * @param sdDevicePointState 设备点位状态
     * @return 结果
     */
    public int insertSdDevicePointState(SdDevicePointState sdDevicePointState);

    /**
     * 修改设备点位状态
     *
     * @param sdDevicePointState 设备点位状态
     * @return 结果
     */
    public int updateSdDevicePointState(SdDevicePointState sdDevicePointState);

    /**
     * 批量删除设备点位状态
     *
     * @param ids 需要删除的设备点位状态主键集合
     * @return 结果
     */
    public int deleteSdDevicePointStateByIds(Long[] ids);

    /**
     * 删除设备点位状态信息
     *
     * @param id 设备点位状态主键
     * @return 结果
     */
    public int deleteSdDevicePointStateById(Long id);
}
