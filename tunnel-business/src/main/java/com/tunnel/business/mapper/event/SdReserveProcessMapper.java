package com.tunnel.business.mapper.event;

import com.tunnel.business.domain.event.SdReserveProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预案流程节点Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
public interface SdReserveProcessMapper 
{
    /**
     * 查询预案流程节点
     * 
     * @param id 预案流程节点主键
     * @return 预案流程节点
     */
    public SdReserveProcess selectSdReserveProcessById(Long id);

    /**
     * 查询预案流程节点列表
     * 
     * @param sdReserveProcess 预案流程节点
     * @return 预案流程节点集合
     */
    public List<SdReserveProcess> selectSdReserveProcessList(SdReserveProcess sdReserveProcess);

    /**
     * 新增预案流程节点
     * 
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    public int insertSdReserveProcess(SdReserveProcess sdReserveProcess);

    /**
     * 批量新增预案流程节点
     * @param sdReserveProcesses
     * @return
     */
    public int batchSdReserveProcess(@Param("sdReserveProcesses") List<SdReserveProcess> sdReserveProcesses);
    /**
     * 修改预案流程节点
     * 
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    public int updateSdReserveProcess(SdReserveProcess sdReserveProcess);

    /**
     * 删除预案流程节点
     * 
     * @param id 预案流程节点主键
     * @return 结果
     */
    public int deleteSdReserveProcessById(Long id);

    /**
     * 批量删除预案流程节点
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdReserveProcessByIds(Long[] ids);

    /**
     * 根据预案id删除预案流程节点信息
     * @param id
     * @return
     */
    public int deleteSdReserveProcessByPlanId(Long id);

    /**
     * 根据预案id获得预案流程节点信息
     * @param reserveId
     * @return
     */
    public List<SdReserveProcess> selectSdReserveProcessByRid(Long reserveId);

    public List<Map> getReservePlanProcess(Long reserveId);
}
