package com.tunnel.platform.mapper.dataInfo;


import com.tunnel.platform.domain.dataInfo.SdEquipmentStateIconFile;

import java.util.List;

/**
 * 设备类型图标文件Mapper接口
 * 
 * @author yujieying
 * @date 2021-1-23
 */
public interface SdEquipmentIconFileMapper 
{
    /**
     * 查询设备状态图标文件
     * 
     * @param id 设备状态图标ID
     * @return 设备状态图标文件
     */
    public SdEquipmentStateIconFile selectStateIconFileById(Long id);

    /**
     * 查询设备状态图标文件
     * 
     * @param sdEquipmentStateIconFile 设备状态图标文件
     * @return 设备状态图标文件集合
     */
    public List<SdEquipmentStateIconFile> selectStateIconFileList(SdEquipmentStateIconFile sdEquipmentStateIconFile);

    /**
     * 新增设备状态图标文件
     * 
     * @param sdEquipmentStateIconFile 设备状态图标文件
     * @return 结果
     */
    public int insertStateIconFile(SdEquipmentStateIconFile sdEquipmentStateIconFile);
    /**
     * 批量新增设备状态图标文件
     * 
     * @param List<SdEquipmentStateIconFile> 图标文件
     * @return 结果
     */
    public int brachInsertStateIconFile(List<SdEquipmentStateIconFile> list);

    /**
     * 批量新增设备状态图标文件(返回id)
     *
     * @param List<SdEquipmentStateIconFile> 图标文件
     * @return 结果
     */
    public int brachInsertStateIconFiles(List<SdEquipmentStateIconFile> list);

    /**
     * 修改设备设备状态图标文件
     * 
     * @param SdEquipmentStateIconFile 设备状态图标文件
     * @return 结果
     */
    public int updateStateIconFile(SdEquipmentStateIconFile iconFile);

    /**
     * 修改设备设备状态图标文件根据Id
     *
     * @param SdEquipmentStateIconFile 设备状态图标文件
     * @return 结果
     */
    public int updateStateIconFileById(SdEquipmentStateIconFile iconFile);

    /**
     * 删除设备状态图标文件
     * 
     * @param id 设备档案文件ID
     * @return 结果
     */
    public int deleteStateIconFileById(Long id);

    /**
     * 批量删除设备状态图标文件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteStateIconFileByIds(Long[] ids);
    /**
     * 通过文件id批量设备状态图标文件页面信息
     * @param fileId
     * @return
     */
    public int deleteStateIconFileByrlIds(String[] fileId);
}
