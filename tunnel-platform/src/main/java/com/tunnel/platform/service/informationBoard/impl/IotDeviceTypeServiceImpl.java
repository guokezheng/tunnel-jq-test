package com.tunnel.platform.service.informationBoard.impl;

import com.tunnel.platform.domain.informationBoard.IotDeviceType;
import com.tunnel.platform.mapper.informationBoard.IotDeviceTypeMapper;
import com.tunnel.platform.service.informationBoard.IIotDeviceTypeService;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.utils.core.domain.Ztree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备类型编号Service业务层处理
 *
 * @author yangqichao
 * @date 2020-03-26
 */
@Service
public class IotDeviceTypeServiceImpl implements IIotDeviceTypeService {
    @Autowired
    private IotDeviceTypeMapper iotDeviceTypeMapper;

    /**
     * 查询设备类型编号
     *
     * @param deviceTypeId 设备类型编号ID
     * @return 设备类型编号
     */
    @Override
    public IotDeviceType selectIotDeviceTypeById(Long deviceTypeId) {
        return iotDeviceTypeMapper.selectIotDeviceTypeById(deviceTypeId);
    }

    /**
     * 查询设备类型编号列表
     *
     * @param iotDeviceType 设备类型编号
     * @return 设备类型编号
     */
    @Override
    public List<IotDeviceType> selectIotDeviceTypeList(IotDeviceType iotDeviceType) {
        return iotDeviceTypeMapper.selectIotDeviceTypeList(iotDeviceType);
    }

    /**
     * 新增设备类型编号
     *
     * @param iotDeviceType 设备类型编号
     * @return 结果
     */
    @Override
    public int insertIotDeviceType(IotDeviceType iotDeviceType) {
        iotDeviceType.setCreateTime(DateUtils.getNowDate());
        return iotDeviceTypeMapper.insertIotDeviceType(iotDeviceType);
    }

    /**
     * 修改设备类型编号
     *
     * @param iotDeviceType 设备类型编号
     * @return 结果
     */
    @Override
    public int updateIotDeviceType(IotDeviceType iotDeviceType) {
        iotDeviceType.setUpdateTime(DateUtils.getNowDate());
        return iotDeviceTypeMapper.updateIotDeviceType(iotDeviceType);
    }

    /**
     * 删除设备类型编号对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceTypeByIds(String ids) {
        return iotDeviceTypeMapper.deleteIotDeviceTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除设备类型编号信息
     *
     * @param deviceTypeId 设备类型编号ID
     * @return 结果
     */
    @Override
    public int deleteIotDeviceTypeById(Long deviceTypeId) {
        return iotDeviceTypeMapper.deleteIotDeviceTypeById(deviceTypeId);
    }

    /**
     * 查询设备类型发管理树
     *
     * @return 结果
     */
    @Override
    public List<Ztree> selectDeviceTypeTree() {
        IotDeviceType iotDeviceType = new IotDeviceType();
        iotDeviceType.setVisible(0);
        List<IotDeviceType> iotDeviceTypeList = iotDeviceTypeMapper.selectIotDeviceTypeList(iotDeviceType);
        List<Ztree> ztrees = new ArrayList<Ztree>();
        for (IotDeviceType deviceType : iotDeviceTypeList) {
            Ztree ztree = new Ztree();
            ztree.setId(deviceType.getDeviceTypeId());
            ztree.setName(deviceType.getDeviceTypeName());
            ztree.setTitle(deviceType.getDeviceTypeName());
            ztree.setText(deviceType.getDeviceTypeName());
            ztrees.add(ztree);
        }
        return ztrees;
    }
}
