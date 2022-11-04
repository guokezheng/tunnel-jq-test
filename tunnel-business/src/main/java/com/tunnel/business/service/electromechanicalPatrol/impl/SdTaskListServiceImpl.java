package com.tunnel.business.service.electromechanicalPatrol.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.mapper.electromechanicalPatrol.SdTaskListMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 巡查任务Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class SdTaskListServiceImpl implements ISdTaskListService
{
    @Autowired
    private SdTaskListMapper sdTaskListMapper;

    /**
     * 查询巡查任务
     * 
     * @param id 巡查任务主键
     * @return 巡查任务
     */
    @Override
    public SdTaskList selectSdTaskListById(String id)
    {
        return sdTaskListMapper.selectSdTaskListById(id);
    }

    /**
     * 查询巡查任务列表
     * 
     * @param sdTaskList 巡查任务
     * @return 巡查任务
     */
    @Override
    public List<SdTaskList> selectSdTaskListList(SdTaskList sdTaskList)
    {
        return sdTaskListMapper.selectSdTaskListList(sdTaskList);
    }

    /**
     * 新增巡查任务
     * 
     * @param sdTaskList 巡查任务
     * @return 结果
     */
    @Override
    public int insertSdTaskList(SdTaskList sdTaskList)
    {
        sdTaskList.setCreateTime(DateUtils.getNowDate());
        return sdTaskListMapper.insertSdTaskList(sdTaskList);
    }

    /**
     * 修改巡查任务
     * 
     * @param sdTaskList 巡查任务
     * @return 结果
     */
    @Override
    public int updateSdTaskList(SdTaskList sdTaskList)
    {
        sdTaskList.setUpdateTime(DateUtils.getNowDate());
        return sdTaskListMapper.updateSdTaskList(sdTaskList);
    }

    /**
     * 批量删除巡查任务
     * 
     * @param ids 需要删除的巡查任务主键
     * @return 结果
     */
    @Override
    public int deleteSdTaskListByIds(String[] ids)
    {
        return sdTaskListMapper.deleteSdTaskListByIds(ids);
    }

    /**
     * 删除巡查任务信息
     * 
     * @param id 巡查任务主键
     * @return 结果
     */
    @Override
    public int deleteSdTaskListById(String id)
    {
        return sdTaskListMapper.deleteSdTaskListById(id);
    }
}
