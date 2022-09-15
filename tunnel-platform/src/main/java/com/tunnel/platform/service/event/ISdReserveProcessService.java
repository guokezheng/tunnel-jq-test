package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdReserveProcess;
import com.tunnel.platform.domain.event.SdReserveProcessModel;

import java.util.List;
import java.util.Map;

/**
 * 预案流程节点Service接口
 * 
 * @author ruoyi
 * @date 2022-09-02
 */
public interface ISdReserveProcessService 
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
     * 批量添加预案流程节点
     *
     * @param sdReserveProcesses
     * @return
     */
    public int batchSdReserveProcessed(SdReserveProcessModel sdReserveProcesses);

    /**
     * 修改预案流程节点
     * 
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    public int updateSdReserveProcess(SdReserveProcess sdReserveProcess);

    /**
     * 批量删除预案流程节点
     * 
     * @param ids 需要删除的预案流程节点主键集合
     * @return 结果
     */
    public int deleteSdReserveProcessByIds(Long[] ids);

    /**
     * 删除预案流程节点信息
     * 
     * @param id 预案流程节点主键
     * @return 结果
     */
    public int deleteSdReserveProcessById(Long id);

    /**
     * 根据预案id删除预案流程节点信息
     * @param id
     * @return
     */
    public int deleteSdReserveProcessByPlanId(Long id);

    /**
     * 根据预案id查找预案流程节点信息
     * @param RId
     * @return
     */
    public List<SdReserveProcess> selectSdReserveProcessByRId(Long RId);

    /**
     * 查询预览展示
     *
     * @param reserveId
     * @return
     */
    public List<Map> selectPreviewDisplay(Long reserveId);
}
