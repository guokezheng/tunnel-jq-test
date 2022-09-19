package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdMaintenanceManagementFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 养护文件Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-15
 */
public interface SdMaintenanceManagementFileMapper
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
     * 删除养护文件
     *
     * @param id 养护文件主键
     * @return 结果
     */
    public int deleteSdMaintenanceManagementFileById(Long id);

    /**
     * 批量删除养护文件
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdMaintenanceManagementFileByIds(Long[] ids);

    public int brachInsertSdMaintenanceManagementFile(List<SdMaintenanceManagementFile> list);

    public int updateFileId(@Param("planfileid") String planfileid, @Param("oldid") String oldid);

    public int deleteFileByFileIds(@Param("pFileId") String pFileId);
}
