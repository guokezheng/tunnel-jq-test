package com.tunnel.business.mapper.config;

import java.util.List;

import com.tunnel.business.domain.config.SdConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 数字孪生页面配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-04-10
 */
public interface SdConfigMapper 
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
     * 删除数字孪生页面配置
     * 
     * @param id 数字孪生页面配置主键
     * @return 结果
     */
    public int deleteSdConfigById(Long id);

    /**
     * 批量删除数字孪生页面配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdConfigByIds(Long[] ids);

    List<SdConfig> selectSdConfigByDeptId(SdConfig sdConfig);
}
