package com.tunnel.platform.service.trafficOperationControl.controlConfig.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.platform.mapper.trafficOperationControl.controlConfig.SdControlConfigMeasureMapper;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlConfigMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 管控等级配置措施-管控措施Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-15
 */
@Service
public class SdControlConfigMeasureServiceImpl implements ISdControlConfigMeasureService
{
    @Autowired
    private SdControlConfigMeasureMapper sdControlConfigMeasureMapper;

    /**
     * 查询管控等级配置措施-管控措施
     *
     * @param id 管控等级配置措施-管控措施主键
     * @return 管控等级配置措施-管控措施
     */
    @Override
    public SdControlConfigMeasure selectSdControlConfigMeasureById(Long id)
    {
        return sdControlConfigMeasureMapper.selectSdControlConfigMeasureById(id);
    }

    /**
     * 查询管控等级配置措施-管控措施列表
     *
     * @param sdControlConfigMeasure 管控等级配置措施-管控措施
     * @return 管控等级配置措施-管控措施
     */
    @Override
    public List<SdControlConfigMeasure> selectSdControlConfigMeasureList(SdControlConfigMeasure sdControlConfigMeasure)
    {
        return sdControlConfigMeasureMapper.selectSdControlConfigMeasureList(sdControlConfigMeasure);
    }

    /**
     * 新增管控等级配置措施-管控措施
     *
     * @param sdControlConfigMeasure 管控等级配置措施-管控措施
     * @return 结果
     */
    @Override
    public int insertSdControlConfigMeasure(SdControlConfigMeasure sdControlConfigMeasure)
    {
        sdControlConfigMeasure.setCreateTime(DateUtils.getNowDate());
        return sdControlConfigMeasureMapper.insertSdControlConfigMeasure(sdControlConfigMeasure);
    }

    /**
     * 修改管控等级配置措施-管控措施
     *
     * @param sdControlConfigMeasure 管控等级配置措施-管控措施
     * @return 结果
     */
    @Override
    public int updateSdControlConfigMeasure(SdControlConfigMeasure sdControlConfigMeasure)
    {
        sdControlConfigMeasure.setUpdateTime(DateUtils.getNowDate());
        return sdControlConfigMeasureMapper.updateSdControlConfigMeasure(sdControlConfigMeasure);
    }

    /**
     * 批量删除管控等级配置措施-管控措施
     *
     * @param ids 需要删除的管控等级配置措施-管控措施主键
     * @return 结果
     */
    @Override
    public int deleteSdControlConfigMeasureByIds(Long[] ids)
    {
        return sdControlConfigMeasureMapper.deleteSdControlConfigMeasureByIds(ids);
    }

    /**
     * 删除管控等级配置措施-管控措施信息
     *
     * @param id 管控等级配置措施-管控措施主键
     * @return 结果
     */
    @Override
    public int deleteSdControlConfigMeasureById(Long id)
    {
        return sdControlConfigMeasureMapper.deleteSdControlConfigMeasureById(id);
    }

    /**
     * 根据等级配置id删除管控措施记录
     *
     * @param configLevelId 等级配置id
     * @return
     */
    @Override
    public int delConfigMeasureByLevelId(Long configLevelId) {
        return sdControlConfigMeasureMapper.delConfigMeasureByLevelId(configLevelId);
    }

    /**
     * 删除管控等级关联的管控措施
     *
     * @param levelIds 等级id数组
     * @return
     */
    @Override
    public int delConfigMeasureByLevelIds(Long[] levelIds) {
        return sdControlConfigMeasureMapper.delConfigMeasureByLevelIds(levelIds);
    }

    /**
     * 根据等级配置id查询管控措施记录
     *
     * @param configLevelId 等级配置id
     * @return
     */
    @Override
    public List<SdControlConfigMeasure> getConfigMeasureByLevelId(Long configLevelId) {
        return sdControlConfigMeasureMapper.getConfigMeasureByLevelId(configLevelId);
    }


    /**
     * 根据管控类别、管控级别查询配置的管控措施
     *
     * @param controlType  管控类别
     * @param controlLevel 管控级别
     * @return
     */
    @Override
    public List<SdControlConfigMeasure> getControlMeasureByTypeLevel(String controlType, String controlLevel) {
        return sdControlConfigMeasureMapper.getControlMeasureByTypeLevel(controlType,controlLevel);
    }

    /**
     * 查询可用的管控等级配置的管控措施
     *
     * @param status 可用状态
     * @return
     */
    @Override
    public List<SdControlConfigMeasure> selectValidConfigMeasureList(String status) {
        return sdControlConfigMeasureMapper.selectValidConfigMeasureList(status);
    }
}
