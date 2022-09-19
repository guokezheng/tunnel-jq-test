package com.tunnel.business.service.informationBoard;


import com.tunnel.business.domain.informationBoard.IotDeviceType;
import com.tunnel.business.utils.core.domain.Ztree;

import java.util.List;

/**
 * 设备类型编号Service接口
 *
 * @author yangqichao
 * @date 2020-03-26
 */
public interface IIotDeviceTypeService {
    /**
     * 查询设备类型编号
     *
     * @param deviceTypeId 设备类型编号ID
     * @return 设备类型编号
     */
    IotDeviceType selectIotDeviceTypeById(Long deviceTypeId);

    /**
     * 查询设备类型编号列表
     *
     * @param iotDeviceType 设备类型编号
     * @return 设备类型编号集合
     */
    List<IotDeviceType> selectIotDeviceTypeList(IotDeviceType iotDeviceType);

    /**
     * 新增设备类型编号
     *
     * @param iotDeviceType 设备类型编号
     * @return 结果
     */
    int insertIotDeviceType(IotDeviceType iotDeviceType);

    /**
     * 修改设备类型编号
     *
     * @param iotDeviceType 设备类型编号
     * @return 结果
     */
    int updateIotDeviceType(IotDeviceType iotDeviceType);

    /**
     * 批量删除设备类型编号
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteIotDeviceTypeByIds(String ids);

    /**
     * 删除设备类型编号信息
     *
     * @param deviceTypeId 设备类型编号ID
     * @return 结果
     */
    int deleteIotDeviceTypeById(Long deviceTypeId);

    /**
     * 查询设备类型发管理树
     *
     * @return 结果
     */
    List<Ztree> selectDeviceTypeTree();
}
