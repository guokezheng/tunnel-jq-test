package com.tunnel.business.service.electromechanicalPatrol;


import com.ruoyi.common.core.domain.DeptTunnelTreeSelect;
import com.ruoyi.common.core.domain.SysDeptTunnel;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;

import java.util.List;
import java.util.Map;

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
    public int insertSdTaskList(SdTaskList sdTaskList);

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
    public int deleteSdTaskListByIds(String ids);

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

    /**
     * 查询班组信息
     * @param deptId
     * @return
     */
    List<SysDept> selectTableBzDataInfo(Long deptId);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param deptTunnels 部门列表
     * @return 下拉树结构列表
     */
    public List<DeptTunnelTreeSelect> buildDeptTunnelTreeSelect(List<SysDeptTunnel> deptTunnels);
    /**
     * 废止巡查任务
     */
    public int abolishSdTaskList(String  id);

    /**
     * app  查询巡查任务列表
     * @param tunnelName
     * @param sdTaskList
     * @return
     */
    List<SdTaskList> getTaskList(String tunnelName, SdTaskList sdTaskList);

    /**
     * 查询任务关联的巡查点数量
     * @param id
     * @return
     */
    int countPatrolNum(String id);

    /**
     * app端接收任务
     * @param id
     * @return
     */
    int acceptSdTaskList(String id);

    /**
     * app端  巡查点清单
     * @param taskId
     * @return
     */
    List<SdPatrolList> getPatrolInfo(String taskId);

    /**
     * app端  获取任务现场情况
     * @param taskId
     * @return
     */
    List<SdTaskList> getTaskSiteCondition(String taskId);

    /**
     * 获取任务关联巡检点信息
     * @param id
     * @return
     */
    Map getUpdatePatrolLists(String id);
}
