package com.tunnel.business.mapper.informationBoard;


import com.tunnel.business.domain.informationBoard.IotDeviceType;

import java.util.List;

/**
 * 设备类型编号Mapper接口
 * 
 * @author yangqichao
 * @date 2020-03-26
 */
public interface IotDeviceTypeMapper 
{
    /**
     * 查询设备类型编号
     * 
     * @param deviceTypeId 设备类型编号ID
     * @return 设备类型编号
     */
    public IotDeviceType selectIotDeviceTypeById(Long deviceTypeId);

    /**
     * 查询设备类型编号列表
     * 
     * @param iotDeviceType 设备类型编号
     * @return 设备类型编号集合
     */
    public List<IotDeviceType> selectIotDeviceTypeList(IotDeviceType iotDeviceType);

    /**
     * 新增设备类型编号
     * 
     * @param iotDeviceType 设备类型编号
     * @return 结果
     */
    public int insertIotDeviceType(IotDeviceType iotDeviceType);

    /**
     * 修改设备类型编号
     * 
     * @param iotDeviceType 设备类型编号
     * @return 结果
     */
    public int updateIotDeviceType(IotDeviceType iotDeviceType);

    /**
     * 删除设备类型编号
     * 
     * @param deviceTypeId 设备类型编号ID
     * @return 结果
     */
    public int deleteIotDeviceTypeById(Long deviceTypeId);

    /**
     * 批量删除设备类型编号
     * 
     * @param deviceTypeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteIotDeviceTypeByIds(String[] deviceTypeIds);
}
