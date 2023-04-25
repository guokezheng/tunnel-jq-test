package com.tunnel.business.mapper.protocol;

import java.util.List;
import com.tunnel.business.domain.protocol.SdDevicePointState;

/**
 * 设备点位状态Mapper接口
 *
 * @author ruoyi
 * @date 2023-04-11
 */
public interface SdDevicePointStateMapper
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
     * 删除设备点位状态
     *
     * @param id 设备点位状态主键
     * @return 结果
     */
    public int deleteSdDevicePointStateById(Long id);

    /**
     * 批量删除设备点位状态
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdDevicePointStateByIds(Long[] ids);
}
