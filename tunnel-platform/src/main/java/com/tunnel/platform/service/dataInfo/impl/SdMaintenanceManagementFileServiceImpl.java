package com.tunnel.platform.service.dataInfo.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tunnel.platform.mapper.dataInfo.SdMaintenanceManagementFileMapper;
import com.tunnel.platform.domain.dataInfo.SdMaintenanceManagementFile;
import com.tunnel.platform.service.dataInfo.ISdMaintenanceManagementFileService;

/**
 * 养护文件Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-02-15
 */
@Service
public class SdMaintenanceManagementFileServiceImpl implements ISdMaintenanceManagementFileService 
{
    @Autowired
    private SdMaintenanceManagementFileMapper sdMaintenanceManagementFileMapper;

    /**
     * 查询养护文件
     * 
     * @param id 养护文件主键
     * @return 养护文件
     */
    @Override
    public SdMaintenanceManagementFile selectSdMaintenanceManagementFileById(Long id)
    {
        return sdMaintenanceManagementFileMapper.selectSdMaintenanceManagementFileById(id);
    }

    /**
     * 查询养护文件列表
     * 
     * @param sdMaintenanceManagementFile 养护文件
     * @return 养护文件
     */
    @Override
    public List<SdMaintenanceManagementFile> selectSdMaintenanceManagementFileList(SdMaintenanceManagementFile sdMaintenanceManagementFile)
    {
        return sdMaintenanceManagementFileMapper.selectSdMaintenanceManagementFileList(sdMaintenanceManagementFile);
    }

    /**
     * 新增养护文件
     * 
     * @param sdMaintenanceManagementFile 养护文件
     * @return 结果
     */
    @Override
    public int insertSdMaintenanceManagementFile(SdMaintenanceManagementFile sdMaintenanceManagementFile)
    {
        sdMaintenanceManagementFile.setCreateTime(DateUtils.getNowDate());
        return sdMaintenanceManagementFileMapper.insertSdMaintenanceManagementFile(sdMaintenanceManagementFile);
    }

    /**
     * 修改养护文件
     * 
     * @param sdMaintenanceManagementFile 养护文件
     * @return 结果
     */
    @Override
    public int updateSdMaintenanceManagementFile(SdMaintenanceManagementFile sdMaintenanceManagementFile)
    {
        sdMaintenanceManagementFile.setUpdateTime(DateUtils.getNowDate());
        return sdMaintenanceManagementFileMapper.updateSdMaintenanceManagementFile(sdMaintenanceManagementFile);
    }

    /**
     * 批量删除养护文件
     * 
     * @param ids 需要删除的养护文件主键
     * @return 结果
     */
    @Override
    public int deleteSdMaintenanceManagementFileByIds(Long[] ids)
    {
        return sdMaintenanceManagementFileMapper.deleteSdMaintenanceManagementFileByIds(ids);
    }

    /**
     * 删除养护文件信息
     * 
     * @param id 养护文件主键
     * @return 结果
     */
    @Override
    public int deleteSdMaintenanceManagementFileById(Long id)
    {
        return sdMaintenanceManagementFileMapper.deleteSdMaintenanceManagementFileById(id);
    }
}
