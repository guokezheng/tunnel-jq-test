package com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasure;

import java.util.List;

/**
 * 交通事件推送措施Service接口
 *
 * @author ruoyi
 * @date 2022-03-01
 */
public interface ISdTrafficIncidentMeasureService {
    /**
     * 查询交通事件推送措施
     *
     * @param id 交通事件推送措施主键
     * @return 交通事件推送措施
     */
    SdTrafficIncidentMeasure selectSdTrafficIncidentMeasureById(Long id);

    /**
     * 查询交通事件推送措施列表
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 交通事件推送措施集合
     */
    List<SdTrafficIncidentMeasure> selectSdTrafficIncidentMeasureList(SdTrafficIncidentMeasure sdTrafficIncidentMeasure);

    /**
     * 新增交通事件推送措施
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 结果
     */
    int insertSdTrafficIncidentMeasure(SdTrafficIncidentMeasure sdTrafficIncidentMeasure);

    /**
     * 修改交通事件推送措施
     *
     * @param sdTrafficIncidentMeasure 交通事件推送措施
     * @return 结果
     */
    int updateSdTrafficIncidentMeasure(SdTrafficIncidentMeasure sdTrafficIncidentMeasure);

    /**
     * 批量删除交通事件推送措施
     *
     * @param ids 需要删除的交通事件推送措施主键集合
     * @return 结果
     */
    int deleteSdTrafficIncidentMeasureByIds(Long[] ids);

    /**
     * 删除交通事件推送措施信息
     *
     * @param id 交通事件推送措施主键
     * @return 结果
     */
    int deleteSdTrafficIncidentMeasureById(Long id);


    /**
     * 删除事件关联的交通管控措施
     *
     * @param incidentId 事件id
     * @return
     */
    int delMultiIncidentMeasure(Long incidentId);

    /**
     * 获取事件关联的管控措施
     *
     * @param incidentId 事件id
     * @return
     */
    List<SdTrafficIncidentMeasure> getIncidentMeasureByIncidentId(Long incidentId);


    /**
     * 保存管控措施数据
     *
     * @param configInfo 管控等级配置数据
     * @param incidentId 事件id
     * @return
     */
    //AjaxResult saveIncidentMeasure(SdControlLevelConfig configInfo, Long incidentId);


}
