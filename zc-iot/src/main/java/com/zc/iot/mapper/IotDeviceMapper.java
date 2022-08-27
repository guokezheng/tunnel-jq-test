package com.zc.iot.mapper;

import java.util.List;
import com.zc.iot.domain.IotDevice;

/**
 * 设备Mapper接口
 * 
 * @author Athena-xiepufeng
 * @date 2021-11-04
 */
public interface IotDeviceMapper 
{
    /**
     * 查询设备
     * 
     * @param id 设备主键
     * @return 设备
     */
    public IotDevice selectIotDeviceById(Long id);
    /**
     * 查询设备
     *
     * @param deviceCode 设备编号
     * @return 设备
     */
    public IotDevice selectIotDeviceByCode(String deviceCode);

    /**
     * 查询设备列表
     * 
     * @param iotDevice 设备
     * @return 设备集合
     */
    public List<IotDevice> selectIotDeviceList(IotDevice iotDevice);

    /**
     * 新增设备
     * 
     * @param iotDevice 设备
     * @return 结果
     */
    public int insertIotDevice(IotDevice iotDevice);

    /**
     * 修改设备
     * 
     * @param iotDevice 设备
     * @return 结果
     */
    public int updateIotDevice(IotDevice iotDevice);

    /**
     * 删除设备
     * 
     * @param id 设备主键
     * @return 结果
     */
    public int deleteIotDeviceById(Long id);

    /**
     * 批量删除设备
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteIotDeviceByIds(Long[] ids);
}
