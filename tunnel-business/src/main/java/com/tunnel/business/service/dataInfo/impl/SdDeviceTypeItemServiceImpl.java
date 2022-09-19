package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import com.tunnel.business.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 设备类型数据项Service业务层处理
 *
 * @author ruoyi
 * @date 2022-09-01
 */
@Service
public class SdDeviceTypeItemServiceImpl implements ISdDeviceTypeItemService {
    @Autowired
    private SdDeviceTypeItemMapper sdDeviceTypeItemMapper;

    @Autowired
    private SdEquipmentTypeMapper sdEquipmentTypeMapper;

    /**
     * 查询设备类型数据项
     *
     * @param id 设备类型数据项主键
     * @return 设备类型数据项
     */
    @Override
    public SdDeviceTypeItem selectSdDeviceTypeItemById(Long id) {
        return sdDeviceTypeItemMapper.selectSdDeviceTypeItemById(id);
    }

    /**
     * 查询设备类型数据项列表
     *
     * @param sdDeviceTypeItem 设备类型数据项
     * @return 设备类型数据项
     */
    @Override
    public List<SdDeviceTypeItem> selectSdDeviceTypeItemList(SdDeviceTypeItem sdDeviceTypeItem) {
        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemMapper.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        for (SdDeviceTypeItem item : sdDeviceTypeItems) {
            SdEquipmentType sdEquipmentType = sdEquipmentTypeMapper.selectSdEquipmentTypeById(item.getDeviceTypeId());
            item.setTypeName(sdEquipmentType.getTypeName());
        }
        return sdDeviceTypeItems;
    }

    /**
     * 新增设备类型数据项
     *
     * @param sdDeviceTypeItem 设备类型数据项
     * @return 结果
     */
    @Override
    public int insertSdDeviceTypeItem(SdDeviceTypeItem sdDeviceTypeItem) {
        sdDeviceTypeItem.setCreateTime(DateUtils.getNowDate());
        return sdDeviceTypeItemMapper.insertSdDeviceTypeItem(sdDeviceTypeItem);
    }

    /**
     * 修改设备类型数据项
     *
     * @param sdDeviceTypeItem 设备类型数据项
     * @return 结果
     */
    @Override
    public int updateSdDeviceTypeItem(SdDeviceTypeItem sdDeviceTypeItem) {
        sdDeviceTypeItem.setUpdateTime(DateUtils.getNowDate());
        return sdDeviceTypeItemMapper.updateSdDeviceTypeItem(sdDeviceTypeItem);
    }

    /**
     * 批量删除设备类型数据项
     *
     * @param ids 需要删除的设备类型数据项主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceTypeItemByIds(Long[] ids) {
        return sdDeviceTypeItemMapper.deleteSdDeviceTypeItemByIds(ids);
    }

    /**
     * 删除设备类型数据项信息
     *
     * @param id 设备类型数据项主键
     * @return 结果
     */
    @Override
    public int deleteSdDeviceTypeItemById(Long id) {
        return sdDeviceTypeItemMapper.deleteSdDeviceTypeItemById(id);
    }
}
