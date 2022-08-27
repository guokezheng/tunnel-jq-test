package com.tunnel.platform.service.trafficOperationControl.activeTrafficFlowControl.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasure;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlLevelConfig;
import com.tunnel.platform.mapper.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasureMapper;
import com.tunnel.platform.service.trafficOperationControl.activeTrafficFlowControl.ISdTrafficIncidentMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交通事件推送措施Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-01
 */
@Service
public class SdTrafficIncidentMeasureServiceImpl implements ISdTrafficIncidentMeasureService
{
    @Autowired
    private SdTrafficIncidentMeasureMapper sdTrafficIncidentMeasureMapper;



    /**
     * 查询交通事件推送措施
     *
     * @param id 交通事件推送措施主键
     * @return 交通事件推送措施
     */
    @Override
    public SdTrafficIncidentMeasure selectSdTrafficIncidentMeasureById(Long id)
    {
        return sdTrafficIncidentMeasureMapper.selectSdTrafficIncidentMeasureById(id);
    }

    /**
     * 查询交通事件推送措施列表
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 交通事件推送措施
     */
    @Override
    public List<SdTrafficIncidentMeasure> selectSdTrafficIncidentMeasureList(SdTrafficIncidentMeasure sdTrafficIncidentMeasure)
    {
        return sdTrafficIncidentMeasureMapper.selectSdTrafficIncidentMeasureList(sdTrafficIncidentMeasure);
    }

    /**
     * 新增交通事件推送措施
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 结果
     */
    @Override
    public int insertSdTrafficIncidentMeasure(SdTrafficIncidentMeasure sdTrafficIncidentMeasure)
    {
        sdTrafficIncidentMeasure.setCreateTime(DateUtils.getNowDate());
        return sdTrafficIncidentMeasureMapper.insertSdTrafficIncidentMeasure(sdTrafficIncidentMeasure);
    }

    /**
     * 修改交通事件推送措施
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 结果
     */
    @Override
    public int updateSdTrafficIncidentMeasure(SdTrafficIncidentMeasure sdTrafficIncidentMeasure)
    {
        sdTrafficIncidentMeasure.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficIncidentMeasureMapper.updateSdTrafficIncidentMeasure(sdTrafficIncidentMeasure);
    }

    /**
     * 批量删除交通事件推送措施
     *
     * @param ids 需要删除的交通事件推送措施主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentMeasureByIds(Long[] ids)
    {
        return sdTrafficIncidentMeasureMapper.deleteSdTrafficIncidentMeasureByIds(ids);
    }

    /**
     * 删除交通事件推送措施信息
     *
     * @param id 交通事件推送措施主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentMeasureById(Long id)
    {
        return sdTrafficIncidentMeasureMapper.deleteSdTrafficIncidentMeasureById(id);
    }

    /**
     * 删除事件关联的交通管控措施
     *
     * @param incidentId 事件id
     * @return
     */
    @Override
    public int delMultiIncidentMeasure(Long incidentId) {
        return sdTrafficIncidentMeasureMapper.delMultiIncidentMeasure(incidentId);
    }

    /**
     * 获取事件关联的管控措施
     *
     * @param incidentId 事件id
     * @return
     */
    @Override
    public List<SdTrafficIncidentMeasure> getIncidentMeasureByIncidentId(Long incidentId) {
        return sdTrafficIncidentMeasureMapper.getIncidentMeasureByIncidentId(incidentId);
    }

    /**
     * 保存管控措施数据
     *
     * @param configInfo 管控等级配置数据
     * @param incidentId 事件id
     * @return
     */
    @Override
    public AjaxResult saveIncidentMeasure(SdControlLevelConfig configInfo,Long incidentId) {
        Long levelConfigId = configInfo.getId();
        SdTrafficIncidentMeasure incidentMeasure = new SdTrafficIncidentMeasure();
        incidentMeasure.setIncidentId(incidentId);
        incidentMeasure.setConfigLevelId(levelConfigId);
        sdTrafficIncidentMeasureMapper.insertSdTrafficIncidentMeasure(incidentMeasure);
        return AjaxResult.success();
    }
}
