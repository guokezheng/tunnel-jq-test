package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdEquipmentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 设备类型Service接口
 * 
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface ISdEquipmentTypeService 
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
    public int insertSdEquipmentType( MultipartFile[] file,SdEquipmentType sdEquipmentType);

    /**
     * 修改设备类型
     * 
     * @param sdEquipmentType 设备类型
     * @return 结果
     */
    public int updateSdEquipmentType(MultipartFile[] file,SdEquipmentType sdEquipmentType,Long[] removeIds);

    /**
     * 批量删除设备类型
     * 
     * @param typeIds 需要删除的设备类型ID
     * @return 结果
     */
    public int deleteSdEquipmentTypeByIds(Long[] typeIds);

    /**
     * 删除设备类型信息
     * 
     * @param typeId 设备类型ID
     * @return 结果
     */
    public int deleteSdEquipmentTypeById(Long typeId);

	public List<SdEquipmentType> selectSdEquipmentTypeHasList(String tunnelId);
	public List<SdEquipmentType> selectSdEquipmentTypeByBigType(String bigType);
	public List<SdEquipmentType> selectSdEquipmentTypeGroupByBigType();

    List<SdEquipmentType> selectList();
}