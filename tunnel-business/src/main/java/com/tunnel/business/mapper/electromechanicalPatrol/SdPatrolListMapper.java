package com.tunnel.business.mapper.electromechanicalPatrol;

import java.util.List;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPointList;

/**
 * 巡查点清单Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-11
 */
public interface SdPatrolListMapper 
{
    /**
     * 查询巡查点清单
     * 
     * @param id 巡查点清单主键
     * @return 巡查点清单
     */
    public SdPatrolList selectSdPatrolListById(String id);

    /**
     * 查询巡查点清单列表
     * 
     * @param sdPatrolList 巡查点清单
     * @return 巡查点清单集合
     */
    public List<SdPatrolList> selectSdPatrolListList(SdPatrolList sdPatrolList);

    /**
     * 新增巡查点清单
     * 
     * @param sdPatrolList 巡查点清单
     * @return 结果
     */
    public int insertSdPatrolList(SdPatrolList sdPatrolList);

    /**
     * 修改巡查点清单
     * 
     * @param sdPatrolList 巡查点清单
     * @return 结果
     */
    public int updateSdPatrolList(SdPatrolList sdPatrolList);

    /**
     * 删除巡查点清单
     * 
     * @param id 巡查点清单主键
     * @return 结果
     */
    public int deleteSdPatrolListById(String id);

    /**
     * 批量删除巡查点清单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdPatrolListByIds(String[] ids);

    /**
     * 批量添加巡查点信息
     * @param sdPatrolList
     * @param id
     * @return
     */
    int batchInsertPatrolList(List<SdPatrolList> sdPatrolList, String id);


    /**
     * 批量添加巡查点信息
     * @param sdPatrolList
     * @param
     * @return
     */
    int batchInsertPatrol(List<SdPatrolList> sdPatrolList);

    /**
     * 根据任务id删除巡查点
     * @param id
     * @return
     */
    int batchDeletePatrolListByTaskId(String id);


    /**
     * 批量删除巡查点
     * @param ids
     * @return
     */
    int batchDeletePatrolListByTaskIds(String ids);

    /**
     * 根据任务id查询关联巡检点详细信息
     * @param task_id
     * @return
     */
    List<SdPatrolList> getPatrolListsInfo(String task_id);
    /**
     *
     * @param id
     * @return
     */
    int countPatrolNum(String id);

    /**
     * app 获取巡检点清单
     * @param taskId
     * @return
     */
    List<SdPatrolList> getPatrolInfo(String taskId);

    /**
     * 获取任务关联的巡查点处理状态
     * @param taskId
     * @return
     */
    List<SdPatrolList> isFaultEnd(String taskId);

    /**
     * 修改，，获取巡检点信息
     * @param id
     * @return
     */
    List<SdPointList> getDevicesPatrolLists(String id);

    /**
     * 修改，，获取故障点信息
     * @param id
     * @return
     */
    List<SdPointList> getFaultPatrolLists(String id);
}
