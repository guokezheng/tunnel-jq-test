package com.tunnel.business.mapper.dataInfo;

import com.ruoyi.common.core.domain.SdEquipmentCategoryDto;
import com.tunnel.business.domain.dataInfo.SdEquipmentCategory;

import java.util.List;
import java.util.Map;

/**
 * 设备类型Mapper接口
 * 
 * @author ruoyi
 * @date 2023-02-06
 */
public interface SdEquipmentCategoryMapper 
{
    /**
     * 查询设备类型
     * 
     * @param id 设备类型主键
     * @return 设备类型
     */
    public SdEquipmentCategory selectSdEquipmentCategoryById(Long id);

    /**
     * 查询设备类型列表
     * 
     * @param sdEquipmentCategory 设备类型
     * @return 设备类型集合
     */
    public List<SdEquipmentCategory> selectSdEquipmentCategoryList(SdEquipmentCategory sdEquipmentCategory);

    /**
     * 新增设备类型
     * 
     * @param sdEquipmentCategory 设备类型
     * @return 结果
     */
    public int insertSdEquipmentCategory(SdEquipmentCategory sdEquipmentCategory);

    /**
     * 修改设备类型
     * 
     * @param sdEquipmentCategory 设备类型
     * @return 结果
     */
    public int updateSdEquipmentCategory(SdEquipmentCategory sdEquipmentCategory);

    /**
     * 删除设备类型
     * 
     * @param id 设备类型主键
     * @return 结果
     */
    public int deleteSdEquipmentCategoryById(Long id);

    /**
     * 批量删除设备类型
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdEquipmentCategoryByIds(Long[] ids);

    List<SdEquipmentCategoryDto> getCategoryDeviceList(String tunnelId);

    List<SdEquipmentCategoryDto> getCategoryList(Map<String,Object> paramMap);

}
