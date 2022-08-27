package com.tunnel.platform.mapper.trafficOperationControl.activeTrafficFlowControl;

import com.tunnel.platform.domain.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasure;

import java.util.List;

/**
 * 交通事件推送措施Mapper接口
 *
 * @author ruoyi
 * @date 2022-03-01
 */
public interface SdTrafficIncidentMeasureMapper
{
    /**
     * 查询交通事件推送措施
     *
     * @param id 交通事件推送措施主键
     * @return 交通事件推送措施
     */
    public SdTrafficIncidentMeasure selectSdTrafficIncidentMeasureById(Long id);

    /**
     * 查询交通事件推送措施列表
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 交通事件推送措施集合
     */
    public List<SdTrafficIncidentMeasure> selectSdTrafficIncidentMeasureList(SdTrafficIncidentMeasure sdTrafficIncidentMeasure);

    /**
     * 新增交通事件推送措施
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 结果
     */
    public int insertSdTrafficIncidentMeasure(SdTrafficIncidentMeasure sdTrafficIncidentMeasure);

    /**
     * 修改交通事件推送措施
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 结果
     */
    public int updateSdTrafficIncidentMeasure(SdTrafficIncidentMeasure sdTrafficIncidentMeasure);

    /**
     * 删除交通事件推送措施
     *
     * @param id 交通事件推送措施主键
     * @return 结果
     */
    public int deleteSdTrafficIncidentMeasureById(Long id);

    /**
     * 批量删除交通事件推送措施
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTrafficIncidentMeasureByIds(Long[] ids);

    /**
     * 删除事件关联的交通管控措施
     * @param incidentId 事件id
     * @return
     */
    int delMultiIncidentMeasure(Long incidentId);


    /**
     * 获取事件关联的管控措施
     * @param incidentId 事件id
     * @return
     */
    List<SdTrafficIncidentMeasure> getIncidentMeasureByIncidentId(Long incidentId);
}
