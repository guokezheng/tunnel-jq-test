package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdEquipmentType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 设备类型Service接口
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
public interface ISdEquipmentTypeService {
    /**
     * 查询设备类型
     *
     * @param typeId 设备类型ID
     * @return 设备类型
     */
    SdEquipmentType selectSdEquipmentTypeById(Long typeId);

    /**
     * 查询设备类型列表
     *
     * @param sdEquipmentType 设备类型
     * @return 设备类型集合
     */
    List<SdEquipmentType> selectSdEquipmentTypeList(SdEquipmentType sdEquipmentType);

    /**
     * 查询数据项中拥有的设备类型
     *
     * @param sdEquipmentType
     * @return
     */
    List<SdEquipmentType> selectHasItemEqTypeList(SdEquipmentType sdEquipmentType);


    /**
     * 查询是否存在重复的设备类型
     *
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
    int insertSdEquipmentType(MultipartFile[] file, SdEquipmentType sdEquipmentType);

    /**
     * 修改设备类型
     *
     * @param sdEquipmentType 设备类型
     * @return 结果
     */
    int updateSdEquipmentType(MultipartFile[] file, SdEquipmentType sdEquipmentType, Long[] removeIds);

    /**
     * 批量删除设备类型
     *
     * @param typeIds 需要删除的设备类型ID
     * @return 结果
     */
    int deleteSdEquipmentTypeByIds(Long[] typeIds);

    /**
     * 删除设备类型信息
     *
     * @param typeId 设备类型ID
     * @return 结果
     */
    int deleteSdEquipmentTypeById(Long typeId);

    List<SdEquipmentType> selectSdEquipmentTypeHasList(String tunnelId);

    List<SdEquipmentType> selectSdEquipmentTypeByBigType(String bigType);

    List<SdEquipmentType> selectSdEquipmentTypeGroupByBigType();

    List<SdEquipmentType> selectList();

    /**
     * 查找可控的设备类型和手动控制的控制策略
     *
     * @param sdEquipmentType
     * @return
     */
    List<Map> selectTypeAndStrategy(SdEquipmentType sdEquipmentType);

    /**
     * 查询设备类型
     * @return
     */
    List<SdEquipmentType> getDevicesType();
}