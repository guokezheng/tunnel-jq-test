package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdReserveProcess;

import java.util.List;

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
}
