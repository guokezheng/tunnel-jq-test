package com.tunnel.platform.mapper.emeDrill;


import com.tunnel.platform.domain.emeDrill.SdEmergencyDrill;

import java.util.List;

/**
 * 应急演练Mapper接口
 * 
 * @author ruoyi
 * @date 2021-11-22
 */
public interface SdEmergencyDrillMapper 
{
    /**
     * 查询应急演练
     * 
     * @param id 应急演练ID
     * @return 应急演练
     */
    public SdEmergencyDrill selectSdEmergencyDrillById(Long id);

    /**
     * 查询应急演练列表
     * 
     * @param sdEmergencyDrill 应急演练
     * @return 应急演练集合
     */
    public List<SdEmergencyDrill> selectSdEmergencyDrillList(SdEmergencyDrill sdEmergencyDrill);

    /**
     * 新增应急演练
     * 
     * @param sdEmergencyDrill 应急演练
     * @return 结果
     */
    public int insertSdEmergencyDrill(SdEmergencyDrill sdEmergencyDrill);

    /**
     * 修改应急演练
     * 
     * @param sdEmergencyDrill 应急演练
     * @return 结果
     */
    public int updateSdEmergencyDrill(SdEmergencyDrill sdEmergencyDrill);

    /**
     * 删除应急演练
     * 
     * @param id 应急演练ID
     * @return 结果
     */
    public int deleteSdEmergencyDrillById(Long id);

    /**
     * 批量删除应急演练
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEmergencyDrillByIds(Long[] ids);
}
