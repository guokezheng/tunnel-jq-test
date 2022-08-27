package com.tunnel.platform.mapper.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdMaintenanceManagement;

/**
 * 养护管理Mapper接口
 * 
 * @author ruoyi
 * @date 2022-02-14
 */
public interface SdMaintenanceManagementMapper 
{
    /**
     * 查询养护管理
     * 
     * @param id 养护管理主键
     * @return 养护管理
     */
    public SdMaintenanceManagement selectSdMaintenanceManagementById(Long id);

    /**
     * 查询养护管理列表
     * 
     * @param sdMaintenanceManagement 养护管理
     * @return 养护管理集合
     */
    public List<SdMaintenanceManagement> selectSdMaintenanceManagementList(SdMaintenanceManagement sdMaintenanceManagement);

    /**
     * 新增养护管理
     * 
     * @param sdMaintenanceManagement 养护管理
     * @return 结果
     */
    public int insertSdMaintenanceManagement(SdMaintenanceManagement sdMaintenanceManagement);

    /**
     * 修改养护管理
     * 
     * @param sdMaintenanceManagement 养护管理
     * @return 结果
     */
    public int updateSdMaintenanceManagement(SdMaintenanceManagement sdMaintenanceManagement);

    /**
     * 删除养护管理
     * 
     * @param id 养护管理主键
     * @return 结果
     */
    public int deleteSdMaintenanceManagementById(Long id);

    /**
     * 批量删除养护管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdMaintenanceManagementByIds(Long[] ids);
}
