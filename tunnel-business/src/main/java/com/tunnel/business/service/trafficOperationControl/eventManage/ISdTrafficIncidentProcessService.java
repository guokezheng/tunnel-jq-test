package com.tunnel.business.service.trafficOperationControl.eventManage;


import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentProcess;

import java.util.List;

/**
 * 交通事件-处理流程Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdTrafficIncidentProcessService {
    /**
     * 查询交通事件-处理流程
     *
     * @param processId 交通事件-处理流程主键
     * @return 交通事件-处理流程
     */
    SdTrafficIncidentProcess selectSdTrafficIncidentProcessByProcessId(Long processId);

    /**
     * 查询交通事件-处理流程列表
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 交通事件-处理流程集合
     */
    List<SdTrafficIncidentProcess> selectSdTrafficIncidentProcessList(SdTrafficIncidentProcess sdTrafficIncidentProcess);

    /**
     * 新增交通事件-处理流程
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 结果
     */
    int insertSdTrafficIncidentProcess(SdTrafficIncidentProcess sdTrafficIncidentProcess);

    /**
     * 修改交通事件-处理流程
     *
     * @param sdTrafficIncidentProcess 交通事件-处理流程
     * @return 结果
     */
    int updateSdTrafficIncidentProcess(SdTrafficIncidentProcess sdTrafficIncidentProcess);

    /**
     * 批量删除交通事件-处理流程
     *
     * @param processIds 需要删除的交通事件-处理流程主键集合
     * @return 结果
     */
    int deleteSdTrafficIncidentProcessByProcessIds(Long[] processIds);

    /**
     * 删除交通事件-处理流程信息
     *
     * @param processId 交通事件-处理流程主键
     * @return 结果
     */
    int deleteSdTrafficIncidentProcessByProcessId(Long processId);


    /**
     * 获取交通事件-处理流程列表
     *
     * @param incidentId
     * @return
     */
    List<SdTrafficIncidentProcess> getProcessList(Long incidentId);

    /**
     * 删除事件关联的流程
     *
     * @param incidentId 事件id
     * @return
     */
    int delProcessByIncidentId(Long incidentId);

    /**
     * 查询事件关联的所有流程id
     *
     * @param incidentId 事件id
     * @return
     */
    Long[] queryProcessIdByIncidentId(Long incidentId);


    /**
     * 删除事件关联的流程以及图片信息
     *
     * @param incidentId 事件id
     * @return
     */
    int delProcessAndImage(Long incidentId);
}
