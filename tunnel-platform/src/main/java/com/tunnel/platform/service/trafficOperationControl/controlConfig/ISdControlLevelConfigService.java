package com.tunnel.platform.service.trafficOperationControl.controlConfig;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlLevelConfig;

import java.util.List;
import java.util.Map;

/**
 * 管控等级配置Service接口
 *
 * @author ruoyi
 * @date 2022-02-12
 */
public interface ISdControlLevelConfigService
{
    /**
     * 查询管控等级配置
     *
     * @param id 管控等级配置主键
     * @return 管控等级配置
     */
    public SdControlLevelConfig selectSdControlLevelConfigById(Long id);

    /**
     * 查询管控等级配置列表
     *
     * @param sdControlLevelConfig 管控等级配置
     * @return 管控等级配置集合
     */
    public List<SdControlLevelConfig> selectSdControlLevelConfigList(SdControlLevelConfig sdControlLevelConfig);

    /**
     * 新增管控等级配置
     *
     * @param sdControlLevelConfig 管控等级配置
     * @return 结果
     */
    public int insertSdControlLevelConfig(SdControlLevelConfig sdControlLevelConfig);

    /**
     * 修改管控等级配置
     *
     * @param sdControlLevelConfig 管控等级配置
     * @return 结果
     */
    public int updateSdControlLevelConfig(SdControlLevelConfig sdControlLevelConfig);

    /**
     * 批量删除管控等级配置
     *
     * @param ids 需要删除的管控等级配置主键集合
     * @return 结果
     */
    public int deleteSdControlLevelConfigByIds(Long[] ids);

    /**
     * 删除管控等级配置信息
     *
     * @param id 管控等级配置主键
     * @return 结果
     */
    public int deleteSdControlLevelConfigById(Long id);


    /**
     * 保存配置措施
     * @param jsonObject 参数
     * @return
     */
    void saveControlMeasure(JSONObject jsonObject);

    /**
     * 查询是否存在相同的管控类别、管控级别的数据
     * @param sdControlLevelConfig 管控等级配置
     * @return
     */
    int querySameLevelConfig(SdControlLevelConfig sdControlLevelConfig);


    /**
     * 查询配置的管控类别
     * @return
     */
    List<Map> getControlTypeList();


    /**
     * 根据管控类别查询配置的管控级别
     * @param controlType 管控类别
     * @return
     */
    List<Map> getControlLevelByType(String controlType);


    /**
     * 根据管控类别、管控级别查询配置信息
     * @param config
     * @return
     */
    SdControlLevelConfig getConfigInfo(SdControlLevelConfig config);
}
