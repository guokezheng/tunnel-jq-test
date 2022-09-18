package com.tunnel.platform.mapper.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdEquipmentType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备类型Mapper接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface SdEquipmentTypeMapper
{
    /**
     * 查询设备类型
     *
     * @param typeId 设备类型ID
     * @return 设备类型
     */
    public SdEquipmentType selectSdEquipmentTypeById(Long typeId);

    /**
     * 查询设备类型列表
     *
     * @param sdEquipmentType 设备类型
     * @return 设备类型集合
     */
    public List<SdEquipmentType> selectSdEquipmentTypeList(SdEquipmentType sdEquipmentType);

    /**
     *
     * @param sdEquipmentType
     * @return
     */
    public List<SdEquipmentType> selectHasItemEqTypeList(SdEquipmentType sdEquipmentType);

    /**
     * 查询是否存在重复的设备类型
     * @param sdEquipmentType 设备类型
     * @return
     */
    Integer selectExistSameType(SdEquipmentType sdEquipmentType);

    /**
     * 新增设备类型
     *
     * @param sdEquipmentType 设备类型
     * @return 结果
     */
    public int insertSdEquipmentType(SdEquipmentType sdEquipmentType);

    /**
     * 修改设备类型
     *
     * @param sdEquipmentType 设备类型
     * @return 结果
     */
    public int updateSdEquipmentType(SdEquipmentType sdEquipmentType);

    /**
     * 删除设备类型
     *
     * @param typeId 设备类型ID
     * @return 结果
     */
    public int deleteSdEquipmentTypeById(Long typeId);

    /**
     * 批量删除设备类型
     *
     * @param typeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEquipmentTypeByIds(Long[] typeIds);

    /**
     * 根据隧道id查询可控设备类型
     * @param tunnelId
     * @return
     */
	public List<SdEquipmentType> selectSdEquipmentTypeHasList(@Param("tunnelId")String tunnelId);

    /**
     * 根据设备类型查设备类型
     * @param bigType
     * @return
     */
	public List<SdEquipmentType> selectSdEquipmentTypeByBigType(@Param("bigType")String bigType);

    /**
     *
     * @return
     */
    public List<SdEquipmentType> selectSdEquipmentTypeGroupByBigType();

    /**
     * 查询所有
     * @return
     */
    List<SdEquipmentType> selectList();
}
