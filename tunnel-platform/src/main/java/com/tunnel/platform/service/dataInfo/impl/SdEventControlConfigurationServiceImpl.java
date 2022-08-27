package com.tunnel.platform.service.dataInfo.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tunnel.platform.mapper.dataInfo.SdEventControlConfigurationMapper;
import com.tunnel.platform.domain.dataInfo.SdEventControlConfiguration;
import com.tunnel.platform.service.dataInfo.ISdEventControlConfigurationService;

/**
 * 事件管控类型配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdEventControlConfigurationServiceImpl implements ISdEventControlConfigurationService 
{
    @Autowired
    private SdEventControlConfigurationMapper sdEventControlConfigurationMapper;

    /**
     * 查询事件管控类型配置
     * 
     * @param id 事件管控类型配置主键
     * @return 事件管控类型配置
     */
    @Override
    public SdEventControlConfiguration selectSdEventControlConfigurationById(Long id)
    {
        return sdEventControlConfigurationMapper.selectSdEventControlConfigurationById(id);
    }

    /**
     * 查询事件管控类型配置列表
     * 
     * @param sdEventControlConfiguration 事件管控类型配置
     * @return 事件管控类型配置
     */
    @Override
    public List<SdEventControlConfiguration> selectSdEventControlConfigurationList(SdEventControlConfiguration sdEventControlConfiguration)
    {
        return sdEventControlConfigurationMapper.selectSdEventControlConfigurationList(sdEventControlConfiguration);
    }

    /**
     * 新增事件管控类型配置
     * 
     * @param sdEventControlConfiguration 事件管控类型配置
     * @return 结果
     */
    @Override
    public int insertSdEventControlConfiguration(SdEventControlConfiguration sdEventControlConfiguration)
    {
        sdEventControlConfiguration.setCreateTime(DateUtils.getNowDate());
        return sdEventControlConfigurationMapper.insertSdEventControlConfiguration(sdEventControlConfiguration);
    }

    /**
     * 修改事件管控类型配置
     * 
     * @param sdEventControlConfiguration 事件管控类型配置
     * @return 结果
     */
    @Override
    public int updateSdEventControlConfiguration(SdEventControlConfiguration sdEventControlConfiguration)
    {
        sdEventControlConfiguration.setUpdateTime(DateUtils.getNowDate());
        return sdEventControlConfigurationMapper.updateSdEventControlConfiguration(sdEventControlConfiguration);
    }

    /**
     * 批量删除事件管控类型配置
     * 
     * @param ids 需要删除的事件管控类型配置主键
     * @return 结果
     */
    @Override
    public int deleteSdEventControlConfigurationByIds(Long[] ids)
    {
        return sdEventControlConfigurationMapper.deleteSdEventControlConfigurationByIds(ids);
    }

    /**
     * 删除事件管控类型配置信息
     * 
     * @param id 事件管控类型配置主键
     * @return 结果
     */
    @Override
    public int deleteSdEventControlConfigurationById(Long id)
    {
        return sdEventControlConfigurationMapper.deleteSdEventControlConfigurationById(id);
    }
}
