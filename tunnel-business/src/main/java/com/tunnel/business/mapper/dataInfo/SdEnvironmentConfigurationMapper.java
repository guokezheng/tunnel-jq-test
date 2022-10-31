package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdEnvironmentConfiguration;

import java.util.List;

/**
 * 隧道环境配置Mapper接口
 * 
 * @author 刘方堃
 * @date 2021-12-13
 */
public interface SdEnvironmentConfigurationMapper 
{
    /**
     * 查询隧道环境配置
     * 
     * @param id 隧道环境配置主键
     * @return 隧道环境配置
     */
    public SdEnvironmentConfiguration selectSdEnvironmentConfigurationById(Long id);

    /**
     * 查询隧道环境配置列表
     * 
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 隧道环境配置集合
     */
    public List<SdEnvironmentConfiguration> selectSdEnvironmentConfigurationList(SdEnvironmentConfiguration sdEnvironmentConfiguration);

    /**
     * 导出隧道环境配置列表
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 隧道环境配置集合
     */
    public List<SdEnvironmentConfiguration> selectSdEnvironmentList(SdEnvironmentConfiguration sdEnvironmentConfiguration);

    /**
     * 新增隧道环境配置
     * 
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 结果
     */
    public int insertSdEnvironmentConfiguration(SdEnvironmentConfiguration sdEnvironmentConfiguration);

    /**
     * 修改隧道环境配置
     * 
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 结果
     */
    public int updateSdEnvironmentConfiguration(SdEnvironmentConfiguration sdEnvironmentConfiguration);

    /**
     * 删除隧道环境配置
     * 
     * @param id 隧道环境配置主键
     * @return 结果
     */
    public int deleteSdEnvironmentConfigurationById(Long id);

    /**
     * 批量删除隧道环境配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdEnvironmentConfigurationByIds(Long[] ids);

    public List<SdEnvironmentConfiguration> selectSdEnvironmentConfigurationList_exp(SdEnvironmentConfiguration sdEnvironmentConfiguration);
}
