package com.tunnel.business.mapper.electromechanicalPatrol;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;

import java.util.List;

/**
 * 巡查任务Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface SdTaskListMapper 
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
    public List<SdTaskList>  selectSdTaskListList(SdTaskList sdTaskList);

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
    public int updateSdTaskList(SdTaskList sdTaskList);

    /**
     * 删除巡查任务
     * 
     * @param id 巡查任务主键
     * @return 结果
     */
    public int deleteSdTaskListById(String id);

    /**
     * 批量删除巡查任务
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTaskListByIds(String[] ids);

    /**
     * 查询任务详情
     * @param task_id
     * @return
     */
    List<SdTaskList> getTaskInfoList(String task_id);

    /**
     * 查询班组信息
     * @param deptId
     * @return
     */
    List<SysDept> selectTableBzDataInfo(Long deptId);
}
