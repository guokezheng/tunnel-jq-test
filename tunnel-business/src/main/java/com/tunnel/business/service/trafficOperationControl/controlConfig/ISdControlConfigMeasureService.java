package com.tunnel.business.service.trafficOperationControl.controlConfig;


import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;

import java.util.List;

/**
 * 管控等级配置措施-管控措施Service接口
 *
 * @author ruoyi
 * @date 2022-02-15
 */
public interface ISdControlConfigMeasureService {
    /**
     * 查询管控等级配置措施-管控措施
     *
     * @param id 管控等级配置措施-管控措施主键
     * @return 管控等级配置措施-管控措施
     */
    SdControlConfigMeasure selectSdControlConfigMeasureById(Long id);

    /**
     * 查询管控等级配置措施-管控措施列表
     *
     * @param sdControlConfigMeasure 管控等级配置措施-管控措施
     * @return 管控等级配置措施-管控措施集合
     */
    List<SdControlConfigMeasure> selectSdControlConfigMeasureList(SdControlConfigMeasure sdControlConfigMeasure);

    /**
     * 新增管控等级配置措施-管控措施
     *
     * @param sdControlConfigMeasure 管控等级配置措施-管控措施
     * @return 结果
     */
    int insertSdControlConfigMeasure(SdControlConfigMeasure sdControlConfigMeasure);

    /**
     * 修改管控等级配置措施-管控措施
     *
     * @param sdControlConfigMeasure 管控等级配置措施-管控措施
     * @return 结果
     */
    int updateSdControlConfigMeasure(SdControlConfigMeasure sdControlConfigMeasure);

    /**
     * 批量删除管控等级配置措施-管控措施
     *
     * @param ids 需要删除的管控等级配置措施-管控措施主键集合
     * @return 结果
     */
    int deleteSdControlConfigMeasureByIds(Long[] ids);

    /**
     * 删除管控等级配置措施-管控措施信息
     *
     * @param id 管控等级配置措施-管控措施主键
     * @return 结果
     */
    int deleteSdControlConfigMeasureById(Long id);


    /**
     * 根据等级配置id删除管控措施记录
     *
     * @param configLevelId 等级配置id
     * @return
     */
    int delConfigMeasureByLevelId(Long configLevelId);

    /**
     * 删除管控等级关联的管控措施
     *
     * @param levelIds 等级id数组
     * @return
     */
    int delConfigMeasureByLevelIds(Long[] levelIds);

    /**
     * 根据等级配置id查询管控措施记录
     *
     * @param configLevelId 等级配置id
     * @return
     */
    List<SdControlConfigMeasure> getConfigMeasureByLevelId(Long configLevelId);


    /**
     * 根据管控类别、管控级别查询配置的管控措施
     *
     * @param controlType  管控类别
     * @param controlLevel 管控级别
     * @return
     */
    List<SdControlConfigMeasure> getControlMeasureByTypeLevel(String controlType, String controlLevel);

    /**
     * 查询可用的管控等级配置的管控措施
     *
     * @param status 可用状态
     * @return
     */
    List<SdControlConfigMeasure> selectValidConfigMeasureList(String status);
}
