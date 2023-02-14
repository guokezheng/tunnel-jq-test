package com.tunnel.business.service.dataInfo;

import com.ruoyi.common.core.domain.SdEquipmentCategoryDto;
import com.ruoyi.common.core.domain.TreeCategorySelect;
import com.tunnel.business.domain.dataInfo.SdEquipmentCategory;

import java.util.List;

/**
 * 设备类型Service接口
 *
 * @author ruoyi
 * @date 2023-02-06
 */
public interface ISdEquipmentCategoryService {
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
     * 批量删除设备类型
     *
     * @param ids 需要删除的设备类型主键集合
     * @return 结果
     */
    public int deleteSdEquipmentCategoryByIds(Long[] ids);

    /**
     * 删除设备类型信息
     *
     * @param id 设备类型主键
     * @return 结果
     */
    public int deleteSdEquipmentCategoryById(Long id);

    List<SdEquipmentCategoryDto> getCategoryDeviceList(String tunnelId);

    List<SdEquipmentCategoryDto> getCategoryList();


    /**
     * 构建前端所需要树结构
     *
     * @param categoryList 设备类型集合
     * @return 树结构列表
     */
    List<SdEquipmentCategoryDto> buildCategoryTree(List<SdEquipmentCategoryDto> categoryList);

    List<TreeCategorySelect> buildCategoryTreeSelect(List<SdEquipmentCategoryDto> CategoryDtoList);
}
