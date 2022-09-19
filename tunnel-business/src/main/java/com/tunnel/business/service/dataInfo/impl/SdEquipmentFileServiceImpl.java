package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.SdEquipmentFile;
import com.tunnel.business.mapper.dataInfo.SdEquipmentFileMapper;
import com.tunnel.business.service.dataInfo.ISdEquipmentFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备档案文件Service业务层处理
 *
 * @author yanghousheng
 * @date 2020-11-20
 */
@Service
public class SdEquipmentFileServiceImpl implements ISdEquipmentFileService {
    @Autowired
    private SdEquipmentFileMapper sdEquipmentFileMapper;

    /**
     * 查询设备档案文件
     *
     * @param id 设备档案文件ID
     * @return 设备档案文件
     */
    @Override
    public SdEquipmentFile selectSdEquipmentFileById(Long id) {
        return sdEquipmentFileMapper.selectSdEquipmentFileById(id);
    }

    /**
     * 查询设备档案文件列表
     *
     * @param sdEquipmentFile 设备档案文件
     * @return 设备档案文件
     */
    @Override
    public List<SdEquipmentFile> selectSdEquipmentFileList(SdEquipmentFile sdEquipmentFile) {
        return sdEquipmentFileMapper.selectSdEquipmentFileList(sdEquipmentFile);
    }

    /**
     * 新增设备档案文件
     *
     * @param sdEquipmentFile 设备档案文件
     * @return 结果
     */
    @Override
    public int insertSdEquipmentFile(SdEquipmentFile sdEquipmentFile) {
        sdEquipmentFile.setCreateTime(DateUtils.getNowDate());
        return sdEquipmentFileMapper.insertSdEquipmentFile(sdEquipmentFile);
    }

    /**
     * 修改设备档案文件
     *
     * @param sdEquipmentFile 设备档案文件
     * @return 结果
     */
    @Override
    public int updateSdEquipmentFile(SdEquipmentFile sdEquipmentFile) {
        sdEquipmentFile.setUpdateTime(DateUtils.getNowDate());
        return sdEquipmentFileMapper.updateSdEquipmentFile(sdEquipmentFile);
    }

    /**
     * 批量删除设备档案文件
     *
     * @param ids 需要删除的设备档案文件ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentFileByIds(Long[] ids) {
        return sdEquipmentFileMapper.deleteSdEquipmentFileByIds(ids);
    }

    /**
     * 删除设备档案文件信息
     *
     * @param id 设备档案文件ID
     * @return 结果
     */
    @Override
    public int deleteSdEquipmentFileById(Long id) {
        return sdEquipmentFileMapper.deleteSdEquipmentFileById(id);
    }
}
