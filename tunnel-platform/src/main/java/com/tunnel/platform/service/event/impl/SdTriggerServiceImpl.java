package com.tunnel.platform.service.event.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.event.SdTrigger;
import com.tunnel.platform.mapper.event.SdTriggerMapper;
import com.tunnel.platform.service.event.ISdTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 触发器Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
@Service
public class SdTriggerServiceImpl implements ISdTriggerService
{
    @Autowired
    private SdTriggerMapper sdTriggerMapper;

    /**
     * 查询触发器
     * 
     * @param id 触发器主键
     * @return 触发器
     */
    @Override
    public SdTrigger selectSdTriggerById(Long id)
    {
        return sdTriggerMapper.selectSdTriggerById(id);
    }

    /**
     * 查询触发器列表
     * 
     * @param sdTrigger 触发器
     * @return 触发器
     */
    @Override
    public List<SdTrigger> selectSdTriggerList(SdTrigger sdTrigger)
    {
        return sdTriggerMapper.selectSdTriggerList(sdTrigger);
    }

    /**
     * 新增触发器
     * 
     * @param sdTrigger 触发器
     * @return 结果
     */
    @Override
    public int insertSdTrigger(SdTrigger sdTrigger)
    {
        sdTrigger.setCreateTime(DateUtils.getNowDate());
        return sdTriggerMapper.insertSdTrigger(sdTrigger);
    }

    /**
     * 修改触发器
     * 
     * @param sdTrigger 触发器
     * @return 结果
     */
    @Override
    public int updateSdTrigger(SdTrigger sdTrigger)
    {
        sdTrigger.setUpdateTime(DateUtils.getNowDate());
        return sdTriggerMapper.updateSdTrigger(sdTrigger);
    }

    /**
     * 批量删除触发器
     * 
     * @param ids 需要删除的触发器主键
     * @return 结果
     */
    @Override
    public int deleteSdTriggerByIds(Long[] ids)
    {
        return sdTriggerMapper.deleteSdTriggerByIds(ids);
    }

    /**
     * 删除触发器信息
     * 
     * @param id 触发器主键
     * @return 结果
     */
    @Override
    public int deleteSdTriggerById(Long id)
    {
        return sdTriggerMapper.deleteSdTriggerById(id);
    }
}
