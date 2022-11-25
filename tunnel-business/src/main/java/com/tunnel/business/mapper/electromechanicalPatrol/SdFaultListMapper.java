package com.tunnel.business.mapper.electromechanicalPatrol;


import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * 故障清单Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-02
 */
public interface SdFaultListMapper 
{
    /**
     * 查询故障清单
     * 
     * @param id 故障清单主键
     * @return 故障清单
     */
    public SdFaultList selectSdFaultListById(String id);

    /**
     * 查询故障清单列表
     * 
     * @param sdFaultList 故障清单
     * @return 故障清单集合
     */
    public List<SdFaultList> selectSdFaultListList(SdFaultList sdFaultList);

    /**
     * 新增故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    public int insertSdFaultList(SdFaultList sdFaultList);

    /**
     * 修改故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    public int updateSdFaultList(SdFaultList sdFaultList);

    /**
     * 删除故障清单
     * 
     * @param id 故障清单主键
     * @return 结果
     */
    public int deleteSdFaultListById(String id);

    /**
     * 批量删除故障清单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdFaultListByIds(String[] ids);

    /**
     * 查询故障检修记录
     * @param faultId
     * @return
     */
    public List<SdPatrolList> getFaultRepairInfo(String faultId);

    /**
     * 根据隧道、故障类型获取故障列表
     * @param tunnelId
     * @param faultLevel
     * @return
     */
    List<SdFaultList> getFaultList1(@Param("tunnelId")String tunnelId, @Param("faultLevel")String faultLevel);

    /**
     * 批量查询故障信息
     * @param faultIds
     * @return
     */
    List<SdFaultList> batchGetFaultList(String[] faultIds);
}
