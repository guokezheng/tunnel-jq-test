package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;

import java.util.List;

/**
 * 设备类型数据项Service接口
 *
 * @author ruoyi
 * @date 2022-09-01
 */
public interface ISdDeviceTypeItemService {
    /**
     * 查询设备类型数据项
     *
     * @param id 设备类型数据项主键
     * @return 设备类型数据项
     */
    SdDeviceTypeItem selectSdDeviceTypeItemById(Long id);

    /**
     * 查询设备类型数据项列表
     *
     * @param sdDeviceTypeItem 设备类型数据项
     * @return 设备类型数据项集合
     */
    List<SdDeviceTypeItem> selectSdDeviceTypeItemList(SdDeviceTypeItem sdDeviceTypeItem);

    /**
     * 新增设备类型数据项
     *
     * @param sdDeviceTypeItem 设备类型数据项
     * @return 结果
     */
    int insertSdDeviceTypeItem(SdDeviceTypeItem sdDeviceTypeItem);

    /**
     * 修改设备类型数据项
     *
     * @param sdDeviceTypeItem 设备类型数据项
     * @return 结果
     */
    int updateSdDeviceTypeItem(SdDeviceTypeItem sdDeviceTypeItem);

    /**
     * 批量删除设备类型数据项
     *
     * @param ids 需要删除的设备类型数据项主键集合
     * @return 结果
     */
    int deleteSdDeviceTypeItemByIds(Long[] ids);

    /**
     * 删除设备类型数据项信息
     *
     * @param id 设备类型数据项主键
     * @return 结果
     */
    int deleteSdDeviceTypeItemById(Long id);
}
