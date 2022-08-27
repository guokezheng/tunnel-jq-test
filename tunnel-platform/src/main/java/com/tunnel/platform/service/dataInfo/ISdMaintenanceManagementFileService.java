package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdMaintenanceManagementFile;

/**
 * 养护文件Service接口
 * 
 * @author ruoyi
 * @date 2022-02-15
 */
public interface ISdMaintenanceManagementFileService 
{
    /**
     * 查询养护文件
     * 
     * @param id 养护文件主键
     * @return 养护文件
     */
    public SdMaintenanceManagementFile selectSdMaintenanceManagementFileById(Long id);

    /**
     * 查询养护文件列表
     * 
     * @param sdMaintenanceManagementFile 养护文件
     * @return 养护文件集合
     */
    public List<SdMaintenanceManagementFile> selectSdMaintenanceManagementFileList(SdMaintenanceManagementFile sdMaintenanceManagementFile);

    /**
     * 新增养护文件
     * 
     * @param sdMaintenanceManagementFile 养护文件
     * @return 结果
     */
    public int insertSdMaintenanceManagementFile(SdMaintenanceManagementFile sdMaintenanceManagementFile);

    /**
     * 修改养护文件
     * 
     * @param sdMaintenanceManagementFile 养护文件
     * @return 结果
     */
    public int updateSdMaintenanceManagementFile(SdMaintenanceManagementFile sdMaintenanceManagementFile);

    /**
     * 批量删除养护文件
     * 
     * @param ids 需要删除的养护文件主键集合
     * @return 结果
     */
    public int deleteSdMaintenanceManagementFileByIds(Long[] ids);

    /**
     * 删除养护文件信息
     * 
     * @param id 养护文件主键
     * @return 结果
     */
    public int deleteSdMaintenanceManagementFileById(Long id);
}
