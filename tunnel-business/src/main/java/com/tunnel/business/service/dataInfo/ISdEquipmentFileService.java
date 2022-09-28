package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEquipmentFile;

import java.util.List;

/**
 * 设备档案文件Service接口
 *
 * @author yanghousheng
 * @date 2020-11-20
 */
public interface ISdEquipmentFileService {
    /**
     * 查询设备档案文件
     *
     * @param id 设备档案文件ID
     * @return 设备档案文件
     */
    SdEquipmentFile selectSdEquipmentFileById(Long id);

    /**
     * 查询设备档案文件列表
     *
     * @param sdEquipmentFile 设备档案文件
     * @return 设备档案文件集合
     */
    List<SdEquipmentFile> selectSdEquipmentFileList(SdEquipmentFile sdEquipmentFile);

    /**
     * 新增设备档案文件
     *
     * @param sdEquipmentFile 设备档案文件
     * @return 结果
     */
    int insertSdEquipmentFile(SdEquipmentFile sdEquipmentFile);

    /**
     * 修改设备档案文件
     *
     * @param sdEquipmentFile 设备档案文件
     * @return 结果
     */
    int updateSdEquipmentFile(SdEquipmentFile sdEquipmentFile);

    /**
     * 批量删除设备档案文件
     *
     * @param ids 需要删除的设备档案文件ID
     * @return 结果
     */
    int deleteSdEquipmentFileByIds(Long[] ids);

    /**
     * 删除设备档案文件信息
     *
     * @param id 设备档案文件ID
     * @return 结果
     */
    int deleteSdEquipmentFileById(Long id);
}