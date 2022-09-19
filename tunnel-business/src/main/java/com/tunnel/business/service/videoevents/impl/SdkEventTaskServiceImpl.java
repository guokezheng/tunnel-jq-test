package com.tunnel.business.service.videoevents.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.videoevents.SdkEventTask;
import com.tunnel.business.mapper.videoevents.SdkEventTaskMapper;
import com.tunnel.business.service.videoevents.ISdkEventTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 事件任务表Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-24
 */
@Service
public class SdkEventTaskServiceImpl implements ISdkEventTaskService {
    @Autowired
    private SdkEventTaskMapper sdkEventTaskMapper;

    /**
     * 查询事件任务表
     *
     * @param id 事件任务表ID
     * @return 事件任务表
     */
    @Override
    public SdkEventTask selectSdkEventTaskById(Integer id) {
        return sdkEventTaskMapper.selectSdkEventTaskById(id);
    }

    @Override
    public SdkEventTask selectSdkEventTaskByChannelId(String id) {
        return sdkEventTaskMapper.selectSdkEventTaskByChannelId(id);
    }

    /**
     * 查询事件任务表列表
     *
     * @param sdkEventTask 事件任务表
     * @return 事件任务表
     */
    @Override
    public List<SdkEventTask> selectSdkEventTaskList(SdkEventTask sdkEventTask) {
        return sdkEventTaskMapper.selectSdkEventTaskList(sdkEventTask);
    }

    /**
     * 新增事件任务表
     *
     * @param sdkEventTask 事件任务表
     * @return 结果
     */
    @Override
    public int insertSdkEventTask(SdkEventTask sdkEventTask) {
        sdkEventTask.setCreateTime(DateUtils.getNowDate());
        return sdkEventTaskMapper.insertSdkEventTask(sdkEventTask);
    }

    /**
     * 修改事件任务表
     *
     * @param sdkEventTask 事件任务表
     * @return 结果
     */
    @Override
    public int updateSdkEventTask(SdkEventTask sdkEventTask) {
        return sdkEventTaskMapper.updateSdkEventTask(sdkEventTask);
    }

    /**
     * 批量删除事件任务表
     *
     * @param ids 需要删除的事件任务表ID
     * @return 结果
     */
    @Override
    public int deleteSdkEventTaskByIds(Integer[] ids) {
        return sdkEventTaskMapper.deleteSdkEventTaskByIds(ids);
    }

    /**
     * 删除事件任务表信息
     *
     * @param id 事件任务表ID
     * @return 结果
     */
    @Override
    public int deleteSdkEventTaskById(Integer id) {
        return sdkEventTaskMapper.deleteSdkEventTaskById(id);
    }
}
