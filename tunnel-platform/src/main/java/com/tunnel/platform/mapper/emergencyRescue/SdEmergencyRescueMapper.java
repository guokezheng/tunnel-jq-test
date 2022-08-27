package com.tunnel.platform.mapper.emergencyRescue;


import com.tunnel.platform.domain.emergencyRescue.SdEmergencyRescue;

import java.util.List;

/**
 * 应急救援Mapper接口
 * 
 * @author ruoyi
 * @date 2021-11-23
 */
public interface SdEmergencyRescueMapper 
{
    /**
     * 查询应急救援
     * 
     * @param id 应急救援ID
     * @return 应急救援
     */
    public SdEmergencyRescue selectSdEmergencyRescueById(Long id);

    /**
     * 查询应急救援列表
     * 
     * @param sdEmergencyRescue 应急救援
     * @return 应急救援集合
     */
    public List<SdEmergencyRescue> selectSdEmergencyRescueList(SdEmergencyRescue sdEmergencyRescue);

    /**
     * 新增应急救援
     * 
     * @param sdEmergencyRescue 应急救援
     * @return 结果
     */
    public int insertSdEmergencyRescue(SdEmergencyRescue sdEmergencyRescue);

    /**
     * 修改应急救援
     * 
     * @param sdEmergencyRescue 应急救援
     * @return 结果
     */
    public int updateSdEmergencyRescue(SdEmergencyRescue sdEmergencyRescue);

    /**
     * 删除应急救援
     * 
     * @param id 应急救援ID
     * @return 结果
     */
    public int deleteSdEmergencyRescueById(Long id);

    /**
     * 批量删除应急救援
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEmergencyRescueByIds(Long[] ids);
}
