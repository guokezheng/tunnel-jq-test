package com.tunnel.business.mapper.electromechanicalPatrol;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskOpt;
import org.apache.ibatis.annotations.Param;

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
     * @param
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
    public int deleteSdTaskListByIds(String ids);

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
    List<SysDept> selectTableBzDataInfo(String deptId);

    /**
     * 废纸巡查任务
     * @param id
     * @return
     */
    public int abolishSdTaskList(String id);

    /**
     * app 端接收任务
     * @param id
     * @return
     */
    int acceptSdTaskList(String id);

    /**
     * app  查询巡查列表
     * @param tunnelName
     * @param sdTaskList
     * @return
     */
    List<SdTaskList> getTaskList(@Param("taskStatus")String taskStatus,@Param("taskName")String taskName,@Param("startTime") String startTime, @Param("endTime")String endTime );

    /**
     * 操作记录
     * @param taskId
     * @return
     */
    List<SdTaskOpt> getTaskOpt(String taskId);

    /**
     * app端  暂存本地
     * @param sdTaskList
     * @return
     */
    int saveLocal(SdTaskList sdTaskList);

    /**
     * 添加操作记录
     * @param sdTaskOpt
     * @return
     */
    int insertTaskOpt(SdTaskOpt sdTaskOpt);

    /**
     * 获取任务状态
     * @return
     */
    List<SdTaskList> getTaskStatus();

    /**
     * app端首页待处理任务单
     * @param deptId
     * @return
     */
    List<SdTaskList> getTaskToDo(String deptId);

    /**
     * 根据隧道查询班组
     * @param tunnelId
     * @return
     */
    String selectBzByTunnel(String tunnelId);

    /**
     * 查询超时任务列表
     * @param sdTaskList
     * @return
     */
    List<SdTaskList> selectChaoshiSdTaskListList(SdTaskList sdTaskList);
}
