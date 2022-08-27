package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdEventControlConfiguration;

/**
 * 事件管控类型配置Service接口
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdEventControlConfigurationService 
{
    /**
     * 查询事件管控类型配置
     * 
     * @param id 事件管控类型配置主键
     * @return 事件管控类型配置
     */
    public SdEventControlConfiguration selectSdEventControlConfigurationById(Long id);

    /**
     * 查询事件管控类型配置列表
     * 
     * @param sdEventControlConfiguration 事件管控类型配置
     * @return 事件管控类型配置集合
     */
    public List<SdEventControlConfiguration> selectSdEventControlConfigurationList(SdEventControlConfiguration sdEventControlConfiguration);

    /**
     * 新增事件管控类型配置
     * 
     * @param sdEventControlConfiguration 事件管控类型配置
     * @return 结果
     */
    public int insertSdEventControlConfiguration(SdEventControlConfiguration sdEventControlConfiguration);

    /**
     * 修改事件管控类型配置
     * 
     * @param sdEventControlConfiguration 事件管控类型配置
     * @return 结果
     */
    public int updateSdEventControlConfiguration(SdEventControlConfiguration sdEventControlConfiguration);

    /**
     * 批量删除事件管控类型配置
     * 
     * @param ids 需要删除的事件管控类型配置主键集合
     * @return 结果
     */
    public int deleteSdEventControlConfigurationByIds(Long[] ids);

    /**
     * 删除事件管控类型配置信息
     * 
     * @param id 事件管控类型配置主键
     * @return 结果
     */
    public int deleteSdEventControlConfigurationById(Long id);
}
