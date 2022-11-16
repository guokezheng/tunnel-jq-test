package com.tunnel.business.service.electromechanicalPatrol;


import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;

import java.util.List;

/**
 * 巡查任务Service接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface ISdTaskListService 
{
    /**
     * 查询巡查任务
     * 
     * @param id 巡查任务主键
     * @return 巡查任务
     */
    public SdTaskList selectSdTaskListById(String id);

    /**
     * 查询巡查任务列表
     * 
     * @param sdTaskList 巡查任务
     * @return 巡查任务集合
     */
    public List<SdTaskList> selectSdTaskListList(SdTaskList sdTaskList);

    /**
     * 新增巡查任务
     * 
     * @param sdTaskList 巡查任务
     * @return 结果
     */
    public int insertSdTaskList(SdTaskList sdTaskList, List<SdPatrolList>sdPatrolList);

    /**
     * 修改巡查任务
     * 
     * @param sdTaskList 巡查任务
     * @return 结果
     */
    public int updateSdTaskList(SdTaskList sdTaskList,List<SdPatrolList>sdPatrolList);

    /**
     * 批量删除巡查任务
     * 
     * @param ids 需要删除的巡查任务主键集合
     * @return 结果
     */
    public int deleteSdTaskListByIds(String[] ids);

    /**
     * 删除巡查任务信息
     * 
     * @param id 巡查任务主键
     * @return 结果
     */
    public int deleteSdTaskListById(String id);

    /**
     * 查询巡查任务详情
     * @param task_id
     * @return
     */
    List<SdTaskList> getTaskInfoList(String task_id);

    /**
     * 根据任务id查询关联巡检点详细信息
     * @param task_id
     * @return
     */
    List<SdPatrolList> getPatrolListsInfo(String task_id);
}
