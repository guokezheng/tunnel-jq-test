package com.tunnel.business.mapper.videoevents;


import com.tunnel.business.domain.videoevents.SdkEventTask;

import java.util.List;

/**
 * 事件任务表Mapper接口
 * 
 * @author ruoyi
 * @date 2021-04-24
 */
public interface SdkEventTaskMapper 
{
    /**
     * 查询事件任务表
     * 
     * @param id 事件任务表ID
     * @return 事件任务表
     */
    public SdkEventTask selectSdkEventTaskById(Integer id);
    public SdkEventTask selectSdkEventTaskByChannelId(String id);

    /**
     * 查询事件任务表列表
     * 
     * @param sdkEventTask 事件任务表
     * @return 事件任务表集合
     */
    public List<SdkEventTask> selectSdkEventTaskList(SdkEventTask sdkEventTask);

    /**
     * 新增事件任务表
     * 
     * @param sdkEventTask 事件任务表
     * @return 结果
     */
    public int insertSdkEventTask(SdkEventTask sdkEventTask);

    /**
     * 修改事件任务表
     * 
     * @param sdkEventTask 事件任务表
     * @return 结果
     */
    public int updateSdkEventTask(SdkEventTask sdkEventTask);

    /**
     * 删除事件任务表
     * 
     * @param id 事件任务表ID
     * @return 结果
     */
    public int deleteSdkEventTaskById(Integer id);

    /**
     * 批量删除事件任务表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdkEventTaskByIds(Integer[] ids);
}
