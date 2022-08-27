package com.tunnel.platform.mapper.dataInfo;


import com.tunnel.platform.domain.dataInfo.SdRepair;

import java.util.List;

/**
 * 设备巡检修Mapper接口
 * 
 * @author liubaokui
 * @date 2021-05-12
 */
public interface SdRepairMapper 
{
    /**
     * 查询设备巡检修
     * 
     * @param repairId 设备巡检修ID
     * @return 设备巡检修
     */
    public SdRepair selectSdRepairById(Long repairId);

    /**
     * 查询设备巡检修列表
     * 
     * @param sdRepair 设备巡检修
     * @return 设备巡检修集合
     */
    public List<SdRepair> selectSdRepairList(SdRepair sdRepair);

    /**
     * 新增设备巡检修
     * 
     * @param sdRepair 设备巡检修
     * @return 结果
     */
    public int insertSdRepair(SdRepair sdRepair);

    /**
     * 修改设备巡检修
     * 
     * @param sdRepair 设备巡检修
     * @return 结果
     */
    public int updateSdRepair(SdRepair sdRepair);

    /**
     * 删除设备巡检修
     * 
     * @param repairId 设备巡检修ID
     * @return 结果
     */
    public int deleteSdRepairById(Long repairId);

    /**
     * 批量删除设备巡检修
     * 
     * @param repairIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdRepairByIds(Long[] repairIds);
}
