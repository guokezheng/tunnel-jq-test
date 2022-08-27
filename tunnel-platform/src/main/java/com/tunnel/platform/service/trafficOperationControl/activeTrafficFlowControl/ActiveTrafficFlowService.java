package com.tunnel.platform.service.trafficOperationControl.activeTrafficFlowControl;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.dataInfo.SdTrafficStatistics;
import com.tunnel.platform.domain.intelligent.SdWeatherReport;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * describe: 主动交通流
 *
 * @author zs
 * @date 2022/2/28
 */
public interface ActiveTrafficFlowService {


    /**
     * 计算隧道的微波车检记录的所有车道的平均速度
     * @param flowList 微波车检记录
     * @return
     */
    BigDecimal computeAvgSpeed(List<SdTrafficStatistics> flowList);


    /**
     * 计算能见度
     * @param weatherList
     * @return
     */
    BigDecimal computeVisibility(List<SdWeatherReport> weatherList);


    /**
     * 获得满足条件的管控措施
     * @param roadConditionList 路面情况
     * @param avgSpeed 平均速度
     * @param visibilityNum 能见度
     * @return
     */
    JSONObject getSatisfiedConditionMeasure(List<String> roadConditionList,BigDecimal avgSpeed,BigDecimal visibilityNum);


    /**
     * 获取主动交通流推送管控措施
     * 查询管控等级配置措施-管控原因，得到实时能见度值、平均速度值、路面情况数据，进行匹配，返回对应的管控措施
     * @param tunnelId 隧道id
     * @return
     */
    JSONObject getActiveTrafficMeasure(String tunnelId);

    /**
     * 获取特定字典类型的字典数据，并转换为key:dictValue,value:dictLabel格式
     * @param dictType 字典类型
     * @return
     */
     Map<String,String> getDictMap(String dictType);


    /**
     * 获得多种类型的字典数据
     * @return
     */
    Map<String,Map<String,String>> getMultiDictMap();

    /**
     * 获取措施详情
     * @param list 措施列表
     * @return
     */
    String getCompleteMeasureDetail(List<SdControlConfigMeasure> list);


//    /**
//     * 拼接获取措施详情
//     * @param list 措施列表数据
//     * @param dictMap 多种字典数据
////     * @param roadControlDictMap 道路管制措施字典数据
////     * @param limitSpeedDictMap 限制速度字典数据
////     * @param limitTypeDictMap 限制类型字典数据
////     * @param limitCarTypeDictMap 车辆类型字典数据
//     * @return
//     */
//    String handleMeasureDetail(List<SdControlConfigMeasure> list, Map<String,Map<String,String>> dictMap);


    /**
     * 拼接获取措施详情【完整的措施详情】
     * @param list 措施列表数据
     * @param dictMap 多种字典数据
//     * @param measureTypeDictMap 措施类型字典数据
//     * @param roadControlDictMap 道路管制措施字典数据
//     * @param limitSpeedDictMap 限制速度字典数据
//     * @param limitTypeDictMap 限制类型字典数据
//     * @param limitCarTypeDictMap 车辆类型字典数据
     * @return
     */
    String handleCompleteMeasureDetail(List<SdControlConfigMeasure> list, Map<String,Map<String,String>> dictMap);


    /**
     * 获取列表数据的措施详情
     * @param list 事件列表数据
     * @return
     */
    List<Map> getIncidentListMeasure(List<Map> list);


    /**
     * 获取事件的措施详情
     * @param incidentId 事件id
     * @return
     */
    Map getIncidentInfoMeasure(Long incidentId);


    /**
     * 获取突发事件的主动交通流推送措施
     * @param incidentType 事件类型
     * @param incidentGrade 事件级别
     * @return
     */
    JSONObject getActiveMeasureWithEmergencyIncident(String incidentType,String incidentGrade);



//    /**
//     * 获取管控原因相关的多种类型的字典数据
//     * @return
//     */
//    Map<String,Map<String,String>> getMultiCauseDictMap();
//
//
//    /**
//     * 获取管控原因的描述信息
//     * @param causeList 管控原因列表
//     * @return
//     */
//    String getReasonCauseDesc(List<SdControlConfigCause> causeList);

}
