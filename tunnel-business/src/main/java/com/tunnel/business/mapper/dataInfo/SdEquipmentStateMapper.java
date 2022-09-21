package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备类型状态关系Mapper接口
 *
 * @author gongfanfei
 * @date 2020-08-28
 */
public interface SdEquipmentStateMapper
{
    /**
     * 查询设备类型状态关系
     *
     * @param id 设备类型状态关系ID
     * @return 设备类型状态关系
     */
    public SdEquipmentState selectSdEquipmentStateById(Long id);

    /**
     * 查询设备类型状态关系列表
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 设备类型状态关系集合
     */
    public List<SdEquipmentState> selectSdEquipmentStateList(SdEquipmentState sdEquipmentState);

    /**
     * 查询设备类型状态关系列表(按设备类型分组查询)
     * @param sdEquipmentState
     * @return
     */
    public List<Map> getGroupList(SdEquipmentState sdEquipmentState);

    /**
     * 查询设备类型状态关系列表
     *
     * @param sdEquipmentState 设备类型状态关系（可控制选项）
     * @return 设备类型状态关系集合
     */
    public List<SdEquipmentState> selectIsControlList(SdEquipmentState sdEquipmentState);

    /**
     * 查询设备类型状态关系列表
     * @param sdEquipmentState
     * @return
     */
    public List<SdEquipmentState> selectDropSdEquipmentStateList(SdEquipmentState sdEquipmentState);

    /**
     * 查询设备类型状态关系列表 根据状态类型分组
     * @param sdEquipmentState
     * @return
     */
    public List<SdEquipmentState> selectSdEquipmentStateListGroupByStateType(SdEquipmentState sdEquipmentState);

    /**
     * 根据设备类型查询设备类型状态关系列表
     * @param sdEquipmentState
     * @return
     */
    public List<SdEquipmentState> selectDropSdEquipmentStateListByTypeId(SdEquipmentState sdEquipmentState);
    /**
     * 新增设备类型状态关系
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 结果
     */
    public int insertSdEquipmentState(SdEquipmentState sdEquipmentState);

    /**
     * 批量添加设备状态关系(没有图片)
     * @param list
     * @return
     */
    public int insertSdEquipmentStateList(@Param("list") List<SdEquipmentState> list);

    /**
     * 修改设备类型状态关系
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 结果
     */
    public int updateSdEquipmentState(SdEquipmentState sdEquipmentState);

    /**
     * 批量修改设备类型状态关系
     * @param sdEquipmentStates
     * @return
     */
    public int updateSdEquipmentStates(@Param("sdEquipmentStates") List<SdEquipmentState> sdEquipmentStates);

    /**
     * 删除设备类型状态关系
     *
     * @param id 设备类型状态关系ID
     * @return 结果
     */
    public int deleteSdEquipmentStateById(Long id);

    /**
     * 批量删除设备类型状态关系
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEquipmentStateByIds(Long[] ids);

    /**
     * 根据设备类型查询设备状态
     * @param typeId
     * @return
     */
    public List<SdEquipmentState> selectSdEquipmentStateByTypeId(Long typeId);

    /**
     * 批量删除
     * @param typeId
     * @return
     */
    public int deleteSdEquipmentStateByTypeId(Long[] typeId);
}
