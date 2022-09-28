package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEquipmentFile;

import java.util.List;

/**
 * 设备档案文件Mapper接口
 * 
 * @author yanghousheng
 * @date 2020-11-20
 */
public interface SdEquipmentFileMapper 
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
     * 批量新增预案文件
     * 
     * @param List<SdEquipmentFile> 预案文件
     * @return 结果
     */
    public int brachInsertSdEquipmentFile(List<SdEquipmentFile> list);

    /**
     * 修改设备档案文件
     * 
     * @param sdEquipmentFile 设备档案文件
     * @return 结果
     */
    public int updateSdEquipmentFile(SdEquipmentFile sdEquipmentFile);

    /**
     * 删除设备档案文件
     * 
     * @param id 设备档案文件ID
     * @return 结果
     */
    public int deleteSdEquipmentFileById(Long id);

    /**
     * 批量删除设备档案文件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEquipmentFileByIds(Long[] ids);
    /**
     * 通过文件id批量预案文件页面信息
     * @param fileId
     * @return
     */
    public int deleteSdEquipmentFileByrlIds(String[] fileId);
}
