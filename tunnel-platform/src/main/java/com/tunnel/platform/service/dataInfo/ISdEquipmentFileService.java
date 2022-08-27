package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdEquipmentFile;

import java.util.List;

/**
 * 设备档案文件Service接口
 * 
 * @author yanghousheng
 * @date 2020-11-20
 */
public interface ISdEquipmentFileService 
{
    /**
     * 查询设备档案文件
     * 
     * @param id 设备档案文件ID
     * @return 设备档案文件
     */
    public SdEquipmentFile selectSdEquipmentFileById(Long id);

    /**
     * 查询设备档案文件列表
     * 
     * @param sdEquipmentFile 设备档案文件
     * @return 设备档案文件集合
     */
    public List<SdEquipmentFile> selectSdEquipmentFileList(SdEquipmentFile sdEquipmentFile);

    /**
     * 新增设备档案文件
     * 
     * @param sdEquipmentFile 设备档案文件
     * @return 结果
     */
    public int insertSdEquipmentFile(SdEquipmentFile sdEquipmentFile);

    /**
     * 修改设备档案文件
     * 
     * @param sdEquipmentFile 设备档案文件
     * @return 结果
     */
    public int updateSdEquipmentFile(SdEquipmentFile sdEquipmentFile);

    /**
     * 批量删除设备档案文件
     * 
     * @param ids 需要删除的设备档案文件ID
     * @return 结果
     */
    public int deleteSdEquipmentFileByIds(Long[] ids);

    /**
     * 删除设备档案文件信息
     * 
     * @param id 设备档案文件ID
     * @return 结果
     */
    public int deleteSdEquipmentFileById(Long id);
}