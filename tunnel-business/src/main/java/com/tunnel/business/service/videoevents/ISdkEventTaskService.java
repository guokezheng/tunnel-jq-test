package com.tunnel.business.service.videoevents;


import com.tunnel.business.domain.videoevents.SdkEventTask;

import java.util.List;

/**
 * 事件任务表Service接口
 *
 * @author ruoyi
 * @date 2021-04-24
 */
public interface ISdkEventTaskService {
    /**
     * 查询事件任务表
     *
     * @param id 事件任务表ID
     * @return 事件任务表
     */
    SdkEventTask selectSdkEventTaskById(Integer id);

    SdkEventTask selectSdkEventTaskByChannelId(String id);

    /**
     * 查询事件任务表列表
     *
     * @param sdkEventTask 事件任务表
     * @return 事件任务表集合
     */
    List<SdkEventTask> selectSdkEventTaskList(SdkEventTask sdkEventTask);

    /**
     * 新增事件任务表
     *
     * @param sdkEventTask 事件任务表
     * @return 结果
     */
    int insertSdkEventTask(SdkEventTask sdkEventTask);

    /**
     * 修改事件任务表
     *
     * @param sdkEventTask 事件任务表
     * @return 结果
     */
    int updateSdkEventTask(SdkEventTask sdkEventTask);

    /**
     * 批量删除事件任务表
     *
     * @param ids 需要删除的事件任务表ID
     * @return 结果
     */
    int deleteSdkEventTaskByIds(Integer[] ids);

    /**
     * 删除事件任务表信息
     *
     * @param id 事件任务表ID
     * @return 结果
     */
    int deleteSdkEventTaskById(Integer id);
}
