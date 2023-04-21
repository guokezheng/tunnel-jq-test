package com.tunnel.business.service.electromechanicalPatrol;


import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 故障清单Service接口
 * 
 * @author ruoyi
 * @date 2022-11-02
 */
public interface ISdFaultListService 
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
    public int insertSdFaultList(MultipartFile[] file,SdFaultList sdFaultList);

    /**
     * 修改故障清单
     * 
     * @param sdFaultList 故障清单
     * @return 结果
     */
    public int updateSdFaultList(MultipartFile[] file,SdFaultList sdFaultList,String[] removeIds);

    /**
     * 批量删除故障清单
     * 
     * @param ids 需要删除的故障清单主键集合
     * @return 结果
     */
    public int deleteSdFaultListByIds(String[] ids);

    /**
     * 删除故障清单信息
     * 
     * @param id 故障清单主键
     * @return 结果
     */
    public int deleteSdFaultListById(String id);

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
    List<SdFaultList> getFaultList(String tunnelId, String faultLevel);

    /**
     * 根据故障id查设备
     * @param eqFaultId
     * @return
     */
    String selectSdFaultEqById(String eqFaultId);
}
