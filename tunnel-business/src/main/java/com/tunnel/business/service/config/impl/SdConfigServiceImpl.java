package com.tunnel.business.service.config.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.config.SdConfig;
import com.tunnel.business.mapper.config.SdConfigMapper;
import com.tunnel.business.service.config.ISdConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数字孪生页面配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-04-10
 */
@Service
public class SdConfigServiceImpl implements ISdConfigService
{
    @Autowired
    private SdConfigMapper sdConfigMapper;

    /**
     * 查询数字孪生页面配置
     * 
     * @param id 数字孪生页面配置主键
     * @return 数字孪生页面配置
     */
    @Override
    public SdConfig selectSdConfigById(Long id)
    {
        return sdConfigMapper.selectSdConfigById(id);
    }

    /**
     * 查询数字孪生页面配置列表
     * 
     * @param sdConfig 数字孪生页面配置
     * @return 数字孪生页面配置
     */
    @Override
    public List<SdConfig> selectSdConfigList(SdConfig sdConfig)
    {
        return sdConfigMapper.selectSdConfigList(sdConfig);
    }

    /**
     * 新增数字孪生页面配置
     * 
     * @param sdConfig 数字孪生页面配置
     * @return 结果
     */
    @Override
    public int insertSdConfig(SdConfig sdConfig)
    {
        sdConfig.setCreateTime(DateUtils.getNowDate());
        return sdConfigMapper.insertSdConfig(sdConfig);
    }

    /**
     * 修改数字孪生页面配置
     * 
     * @param sdConfig 数字孪生页面配置
     * @return 结果
     */
    @Override
    public int updateSdConfig(SdConfig sdConfig)
    {
        sdConfig.setUpdateTime(DateUtils.getNowDate());
        return sdConfigMapper.updateSdConfig(sdConfig);
    }

    /**
     * 批量删除数字孪生页面配置
     * 
     * @param ids 需要删除的数字孪生页面配置主键
     * @return 结果
     */
    @Override
    public int deleteSdConfigByIds(Long[] ids)
    {
        return sdConfigMapper.deleteSdConfigByIds(ids);
    }

    /**
     * 删除数字孪生页面配置信息
     * 
     * @param id 数字孪生页面配置主键
     * @return 结果
     */
    @Override
    public int deleteSdConfigById(Long id)
    {
        return sdConfigMapper.deleteSdConfigById(id);
    }


    @Override
    public List<SdConfig> selectSdConfigByDeptId(SdConfig sdConfig) {
        return sdConfigMapper.selectSdConfigByDeptId(sdConfig);
    }
}
