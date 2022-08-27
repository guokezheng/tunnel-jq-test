package com.tunnel.platform.mapper.trafficOperationControl.eventManage;

import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentProcess;

import java.util.List;

/**
 * 交通事件-处理流程Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface SdTrafficIncidentProcessMapper
{
    /**
     * 查询交通事件-处理流程
     *
     * @param processId 交通事件-处理流程主键
     * @return 交通事件-处理流程
     */
    public SdTrafficIncidentProcess selectSdTrafficIncidentProcessByProcessId(Long processId);

    /**
     * 查询交通事件-处理流程列表
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 交通事件-处理流程集合
     */
    public List<SdTrafficIncidentProcess> selectSdTrafficIncidentProcessList(SdTrafficIncidentProcess sdTrafficIncidentProcess);

    /**
     * 新增交通事件-处理流程
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 结果
     */
    public int insertSdTrafficIncidentProcess(SdTrafficIncidentProcess sdTrafficIncidentProcess);

    /**
     * 修改交通事件-处理流程
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 结果
     */
    public int updateSdTrafficIncidentProcess(SdTrafficIncidentProcess sdTrafficIncidentProcess);

    /**
     * 删除交通事件-处理流程
     *
     * @param processId 交通事件-处理流程主键
     * @return 结果
     */
    public int deleteSdTrafficIncidentProcessByProcessId(Long processId);

    /**
     * 批量删除交通事件-处理流程
     *
     * @param processIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTrafficIncidentProcessByProcessIds(Long[] processIds);

    /**
     * 获取交通事件-处理流程列表
     * @param incidentId
     * @return
     */
    List<SdTrafficIncidentProcess> getProcessList(Long incidentId);

    /**
     * 删除事件关联的流程
     * @param incidentId 事件id
     * @return
     */
    int delProcessByIncidentId(Long incidentId);

    /**
     * 查询事件关联的所有流程id
     * @param incidentId 事件id
     * @return
     */
    Long[] queryProcessIdByIncidentId(Long incidentId);
}
