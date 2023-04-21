package com.tunnel.business.service.config;

import java.util.List;

import com.tunnel.business.domain.config.SdConfig;

/**
 * 数字孪生页面配置Service接口
 * 
 * @author ruoyi
 * @date 2023-04-10
 */
public interface ISdConfigService 
{
    /**
     * 查询数字孪生页面配置
     * 
     * @param id 数字孪生页面配置主键
     * @return 数字孪生页面配置
     */
    public SdConfig selectSdConfigById(Long id);

    /**
     * 查询数字孪生页面配置列表
     * 
     * @param sdConfig 数字孪生页面配置
     * @return 数字孪生页面配置集合
     */
    public List<SdConfig> selectSdConfigList(SdConfig sdConfig);

    /**
     * 新增数字孪生页面配置
     * 
     * @param sdConfig 数字孪生页面配置
     * @return 结果
     */
    public int insertSdConfig(SdConfig sdConfig);

    /**
     * 修改数字孪生页面配置
     * 
     * @param sdConfig 数字孪生页面配置
     * @return 结果
     */
    public int updateSdConfig(SdConfig sdConfig);

    /**
     * 批量删除数字孪生页面配置
     * 
     * @param ids 需要删除的数字孪生页面配置主键集合
     * @return 结果
     */
    public int deleteSdConfigByIds(Long[] ids);

    /**
     * 删除数字孪生页面配置信息
     * 
     * @param id 数字孪生页面配置主键
     * @return 结果
     */
    public int deleteSdConfigById(Long id);

    List<SdConfig> selectSdConfigByDeptId(String deptId,String code);
}
