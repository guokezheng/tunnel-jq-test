package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 设备类型状态关系Service接口
 *
 * @author gongfanfei
 * @date 2020-08-28
 */
public interface ISdEquipmentStateService {
    /**
     * 查询设备类型状态关系
     *
     * @param id 设备类型状态关系ID
     * @return 设备类型状态关系
     */
    SdEquipmentState selectSdEquipmentStateById(Long id);

    /**
     * 查询设备类型状态关系列表
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 设备类型状态关系集合
     */
    List<SdEquipmentState> selectSdEquipmentStateList(SdEquipmentState sdEquipmentState);

    /**
     * 查询可控设备类型状态关系列表根据状态类型分组
     *
     * @param sdEquipmentState
     * @return
     */
    List<SdEquipmentState> selectSdEquipmentStateListGroupByStateType(SdEquipmentState sdEquipmentState);

    /**
     * 查询设备关系状态关系表(按设备类型分组查询)
     *
     * @param sdEquipmentState
     * @return
     */
    List<Map> getGroupList(SdEquipmentState sdEquipmentState);

    /**
     * 根据设备类型查询设备类型状态关系列表
     *
     * @param sdEquipmentState
     * @return
     */
    List<SdEquipmentState> selectDropSdEquipmentStateListByTypeId(SdEquipmentState sdEquipmentState);

    /**
     * 查询设备类型状态关系列表（可控制选项）
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 设备类型状态关系集合
     */
    List<SdEquipmentState> selectIsControlList(SdEquipmentState sdEquipmentState);

    /**
     * 新增设备类型状态关系
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 结果
     */
    int insertSdEquipmentState(MultipartFile[] file, SdEquipmentState sdEquipmentState);

    /**
     * 批量新增设备类型图片文件(返回设备图标关联id)
     *
     * @param file
     * @return
     */
    String insertSdEquipmentStateIconFile(MultipartFile[] file);

    /**
     * 删除设备类型图片文件
     *
     * @param id
     * @return
     */
    int delSdEquipmentStateIconFileById(Long id);

    /**
     * 修改设备类型状态关系
     *
     * @param sdEquipmentState 设备类型状态关系
     * @return 结果
     */
    int updateSdEquipmentState(MultipartFile[] file, SdEquipmentState sdEquipmentState, Long[] ids);

    /**
     * 批量删除设备类型状态关系
     *
     * @param ids 需要删除的设备类型状态关系ID
     * @return 结果
     */
    int deleteSdEquipmentStateByIds(Long[] ids);

    /**
     * 删除设备类型状态关系信息
     *
     * @param id 设备类型状态关系ID
     * @return 结果
     */
    int deleteSdEquipmentStateById(Long id);

    /**
     * 批量添加设备状态信息(没有图片)
     *
     * @param sdEquipmentStates
     * @return
     */
    int insertSdEquipmentStateList(List<SdEquipmentState> sdEquipmentStates);

    /**
     * 批量修改设备状态信息
     *
     * @param sdEquipmentStates
     * @return
     */
    int updateSdEquipmentStates(List<SdEquipmentState> sdEquipmentStates);

    /**
     * 根据设备类型删除设备状态信息
     *
     * @param typeId
     * @return
     */
    int deleteSdEquipmentStateByTypeId(Long[] typeId);

    /**
     * 批量删除状态图片
     * @param iconFileIds
     * @return
     */
    int batchDeletePic(String[] iconFileIds);
}
