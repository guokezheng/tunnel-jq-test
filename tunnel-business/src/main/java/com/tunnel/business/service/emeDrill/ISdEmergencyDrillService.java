package com.tunnel.business.service.emeDrill;


import com.tunnel.business.domain.emeDrill.SdEmergencyDrill;

import java.util.List;

/**
 * 应急演练Service接口
 *
 * @author ruoyi
 * @date 2021-11-22
 */
public interface ISdEmergencyDrillService {
    /**
     * 查询应急演练
     *
     * @param id 应急演练ID
     * @return 应急演练
     */
    SdEmergencyDrill selectSdEmergencyDrillById(Long id);

    /**
     * 查询应急演练列表
     *
     * @param sdEmergencyDrill 应急演练
     * @return 应急演练集合
     */
    List<SdEmergencyDrill> selectSdEmergencyDrillList(SdEmergencyDrill sdEmergencyDrill);

    /**
     * 新增应急演练
     *
     * @param sdEmergencyDrill 应急演练
     * @return 结果
     */
    int insertSdEmergencyDrill(SdEmergencyDrill sdEmergencyDrill);

    /**
     * 修改应急演练
     *
     * @param sdEmergencyDrill 应急演练
     * @return 结果
     */
    int updateSdEmergencyDrill(SdEmergencyDrill sdEmergencyDrill);

    /**
     * 批量删除应急演练
     *
     * @param ids 需要删除的应急演练ID
     * @return 结果
     */
    int deleteSdEmergencyDrillByIds(Long[] ids);

    /**
     * 删除应急演练信息
     *
     * @param id 应急演练ID
     * @return 结果
     */
    int deleteSdEmergencyDrillById(Long id);
}
