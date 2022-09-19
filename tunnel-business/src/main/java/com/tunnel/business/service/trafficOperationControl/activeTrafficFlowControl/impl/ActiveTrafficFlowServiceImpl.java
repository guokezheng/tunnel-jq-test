package com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.service.ISysDictTypeService;
import com.tunnel.business.datacenter.domain.enumeration.DictTypeEnum;
import com.tunnel.business.domain.dataInfo.SdIcyRoad;
import com.tunnel.business.domain.dataInfo.SdTrafficStatistics;
import com.tunnel.business.domain.intelligent.SdWeatherReport;
import com.tunnel.business.domain.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasure;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.*;
import com.tunnel.business.service.dataInfo.ISdIcyRoadService;
import com.tunnel.business.service.dataInfo.ISdTrafficStatisticsService;
import com.tunnel.business.service.intelligent.ISdWeatherReportService;
import com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.ActiveTrafficFlowService;
import com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.ISdTrafficIncidentMeasureService;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigCauseService;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigMeasureService;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlLevelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * describe: 主动交通流
 *
 * @author zs
 * @date 2022/2/28
 */
@Service
public class ActiveTrafficFlowServiceImpl implements ActiveTrafficFlowService {

    @Autowired
    private ISdControlLevelConfigService levelConfigService;

    @Autowired
    private ISdControlConfigCauseService configCauseService;

    @Autowired
    private ISdControlConfigMeasureService configMeasureService;

    @Autowired
    private ISdTrafficIncidentMeasureService incidentMeasureService;

    @Autowired
    private ISdIcyRoadService icyRoadService;

    @Autowired
    private ISdTrafficStatisticsService trafficStatisticsService;

    @Autowired
    private ISdWeatherReportService weatherReportService;

    @Autowired
    private ISysDictTypeService sysDictTypeService;

    /**
     * 计算隧道的微波车检记录的所有车道的平均速度
     *
     * @param flowList 微波车检记录
     * @return
     */
    @Override
    public BigDecimal computeAvgSpeed(List<SdTrafficStatistics> flowList) {
        //计算所有车道的平均速度  todo
        Long sum = 0L;
        int count = 0;
        for (SdTrafficStatistics flow : flowList) {
            //平均速度不应该是小数吗？ todo
            Long speed = flow.getBySpeed();
            sum += speed;
            count++;
        }
        if (count == 0) {
            return null;
        }
        //平均速度保留2位小数，四舍五入
        BigDecimal avgSpeed = new BigDecimal(sum).divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
        return avgSpeed;
    }

    /**
     * 计算实时能见度
     *
     * @param weatherList
     * @return
     */
    @Override
    public BigDecimal computeVisibility(List<SdWeatherReport> weatherList) {
        //暂时按照气象记录一条数据来计算,使用1分钟能见度
        BigDecimal visibilityNum = null;
        if (weatherList != null && weatherList.size() > 0) {
            String visibilityValue = weatherList.get(0).getOneMinuteVisibility();
            visibilityNum = new BigDecimal(visibilityValue);
        }
        return visibilityNum;
    }

    /**
     * 获得满足条件的管控措施
     *
     * @param roadConditionList 路面情况
     * @param avgSpeed          平均速度
     * @param visibilityNum     能见度
     * @return
     */
    @Override
    public JSONObject getSatisfiedConditionMeasure(List<String> roadConditionList, BigDecimal avgSpeed, BigDecimal visibilityNum) {

        //筛选出的管控等级id
        List<Long> levelConfigIdList = new ArrayList<>();

        //查询出所有可用的管控配置原因
        String status = LevelConfigStatusEnum.open_status.getCode();
        List<SdControlConfigCause> causeList = configCauseService.selectValidConfigCauseList(status);
        for (SdControlConfigCause configCause : causeList) {
            String causeType = configCause.getCauseType();
            String roadCondition = configCause.getRoadCondition();
            String visibilityMin = configCause.getVisibilityMin();
            String visibilityMax = configCause.getVisibilityMax();
            String congestionDegree = configCause.getCongestionDegree();
            if (visibilityNum != null) {
                if (ControlConfigCauseEnum.visibility.getCode().equals(causeType)) {
                    //能见度
                    BigDecimal min = new BigDecimal(visibilityMin);
                    BigDecimal max = new BigDecimal(visibilityMax);
                    if (visibilityNum.compareTo(BigDecimal.ZERO) > -1 && visibilityNum.compareTo(min) > -1 && visibilityNum.compareTo(max) < 1) {
                        //能见度有实际值并且在范围区间内
                        levelConfigIdList.add(configCause.getConfigLevelId());
                    }
                }
            }

            if (roadConditionList.size() > 0) {
                if (ControlConfigCauseEnum.road_condition.getCode().equals(causeType)) {
                    //路面情况 todo，实际接入设备监测值后修改字典值
                    String roadConditionName = RoadConditionEnum.findName(roadCondition);
                    if (roadConditionList.contains(roadConditionName)) {
                        //路面情况符合其中一个监测值
                        levelConfigIdList.add(configCause.getConfigLevelId());
                    }
                }
            }

            if (avgSpeed != null) {
                if (ControlConfigCauseEnum.congestion_degree.getCode().equals(causeType)) {
                    //拥挤度
                    CongestionDegreeEnum degreeEnum = CongestionDegreeEnum.findEnum(congestionDegree);
                    BigDecimal minSpeed = new BigDecimal(degreeEnum.getMinSpeed());
                    BigDecimal maxSpeed = new BigDecimal(degreeEnum.getMaxSpeed());
                    if (avgSpeed.compareTo(minSpeed) > -1 && avgSpeed.compareTo(maxSpeed) == -1) {
                        //大于等于minSpeed且小于maxSpeed
                        levelConfigIdList.add(configCause.getConfigLevelId());
                    }

                }
            }
        }

        JSONObject jsonObject = getConfigResultInfo(levelConfigIdList);

        return jsonObject;
    }


    /**
     * 获取满足条件的管控等级配置、管控原因、管控详情
     *
     * @param levelConfigIdList
     * @return
     */
    public JSONObject getConfigResultInfo(List<Long> levelConfigIdList) {
        JSONObject jsonObject = new JSONObject();

        //如果满足多个管控等级配置，先暂时取一个,todo
        if (levelConfigIdList.size() > 0) {
            Long levelConfigId = levelConfigIdList.get(0);

            SdControlLevelConfig levelConfig = levelConfigService.selectSdControlLevelConfigById(levelConfigId);
            List<SdControlConfigCause> configCauseList = configCauseService.getConfigCauseByLevelId(levelConfigId);
            String causeDetail = configCauseService.getControlCauseDescription(configCauseList);
            List<SdControlConfigMeasure> configMeasureList = configMeasureService.getConfigMeasureByLevelId(levelConfigId);
            String detail = getCompleteMeasureDetail(configMeasureList);
            jsonObject.put("levelConfig", levelConfig);
            jsonObject.put("measureList", configMeasureList);
            jsonObject.put("measureDetail", detail);
            jsonObject.put("controlReason", causeDetail);
        }
        return jsonObject;
    }


    /**
     * 获取主动交通流推送管控措施
     * 查询管控等级配置措施-管控原因，得到实时能见度值、平均速度值、路面情况数据，进行匹配，返回对应的管控措施
     *
     * @param tunnelId 隧道id
     * @return
     */
    @Override
    public JSONObject getActiveTrafficMeasure(String tunnelId) {
        JSONObject realData = new JSONObject();

        List<String> roadConditionList = new ArrayList<>();

        //道路结冰记录，一条隧道两个设备，两条记录
        List<SdIcyRoad> icyRoadList = icyRoadService.selectLatestIcyRoadList(tunnelId);
        icyRoadList.forEach(icyRoad -> {
            roadConditionList.add(icyRoad.getRoadCondition());
        });

        //微波车检记录，一条隧道四个设备，四条记录
        List<SdTrafficStatistics> flowList = trafficStatisticsService.selectLatestTrafficFlowList(tunnelId);
        //计算所有车道的平均速度
        BigDecimal avgSpeed = computeAvgSpeed(flowList);

        //气象记录
        List<SdWeatherReport> weatherList = weatherReportService.selectLatestWeatherList(tunnelId);
        //计算能见度
        BigDecimal visibilityNum = computeVisibility(weatherList);

        //获得满足条件的管控措施
        JSONObject measureData = getSatisfiedConditionMeasure(roadConditionList, avgSpeed, visibilityNum);

        realData.put("icyRoadList", icyRoadList);
        realData.put("flowList", flowList);
        realData.put("weatherList", weatherList);

        JSONObject resultObject = new JSONObject();
        resultObject.put("realData", realData);
        resultObject.put("measureData", measureData);
        return resultObject;
    }


    /**
     * 获取特定字典类型的字典数据，并转换为key:dictValue,value:dictLabel格式
     *
     * @param dictType 字典类型
     * @return
     */
    @Override
    public Map<String, String> getDictMap(String dictType) {
        List<SysDictData> list = sysDictTypeService.selectDictDataByType(dictType);
        return list.stream().collect(Collectors.toMap(SysDictData::getDictValue, SysDictData::getDictLabel));
    }

    /**
     * 获得多种类型的字典数据
     *
     * @return
     */
    @Override
    public Map<String, Map<String, String>> getMultiDictMap() {
        Map<String, Map<String, String>> dictMap = new HashMap<>();

        //措施类型
        String measureTypeCode = DictTypeEnum.sd_control_measure.getCode();
        Map<String, String> measureTypeDictMap = getDictMap(measureTypeCode);

        //道路管制措施
        String roadControlCode = DictTypeEnum.sd_road_control.getCode();
        Map<String, String> roadControlDictMap = getDictMap(roadControlCode);

        //限制类型
        String limitTypeCode = DictTypeEnum.sd_limit_type.getCode();
        Map<String, String> limitTypeDictMap = getDictMap(limitTypeCode);

        //限制车辆类型
        String limitCarTypeCode = DictTypeEnum.sd_limit_car_type.getCode();
        Map<String, String> limitCarTypeDictMap = getDictMap(limitCarTypeCode);

        //限制速度
        String limitSpeedCode = DictTypeEnum.sd_limit_speed.getCode();
        Map<String, String> limitSpeedDictMap = getDictMap(limitSpeedCode);

        dictMap.put("measureTypeDict", measureTypeDictMap);
        dictMap.put("roadControlDict", roadControlDictMap);
        dictMap.put("limitTypeDict", limitTypeDictMap);
        dictMap.put("limitCarTypeDict", limitCarTypeDictMap);
        dictMap.put("limitSpeedDict", limitSpeedDictMap);
        return dictMap;
    }


    /**
     * 获取措施详情
     *
     * @param list 措施列表
     * @return
     */
    @Override
    public String getCompleteMeasureDetail(List<SdControlConfigMeasure> list) {
        String measureDetail = "";

        Map<String, Map<String, String>> dictMap = getMultiDictMap();

        if (list != null && list.size() > 0) {
            measureDetail = handleCompleteMeasureDetail(list, dictMap);
        }
        return measureDetail;
    }


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
//    @Override
//    public String handleMeasureDetail(List<SdControlConfigMeasure> list, Map<String,Map<String,String>> dictMap){
//        StringBuffer detail = new StringBuffer();
//        for(SdControlConfigMeasure measure : list){
//            detail.append(dictMap.get("roadControlDict").get(measure.getControlMeasure()));
//
//            String limitSpeed = measure.getLimitSpeed();
//            if(limitSpeed !=null && !limitSpeed.isEmpty()){
//                detail.append(",").append("限制速度为").append(dictMap.get("limitSpeedDict").get(limitSpeed)).append("公里每小时");
//            }
//            String limitType = measure.getLimitType();
//            if(limitType != null && !limitType.isEmpty()){
//                detail.append(",").append(dictMap.get("limitTypeDict").get(limitType)).append(dictMap.get("limitCarTypeDict").get(measure.getCarType())).append("通行");
//            }
//            detail.append(";");
//        }
//        return detail.toString();
//    }

    /**
     * 拼接获取措施详情【完整的措施详情】
     *
     * @param list    措施列表数据
     * @param dictMap 多种字典数据
     *                //     * @param roadControlDictMap  道路管制措施字典数据
     *                //     * @param limitSpeedDictMap   限制速度字典数据
     *                //     * @param limitTypeDictMap    限制类型字典数据
     *                //     * @param limitCarTypeDictMap 车辆类型字典数据
     * @return
     */
    @Override
    public String handleCompleteMeasureDetail(List<SdControlConfigMeasure> list, Map<String, Map<String, String>> dictMap) {
        StringBuffer detail = new StringBuffer();
        for (SdControlConfigMeasure measure : list) {
            detail.append("采取").append(dictMap.get("measureTypeDict").get(measure.getMeasureType()));
            detail.append(",").append("管控范围：").append(measure.getControlRangeMin()).append("-").append(measure.getControlRangeMax()).append("公里");
            detail.append(",").append(dictMap.get("roadControlDict").get(measure.getControlMeasure()));

            String limitSpeed = measure.getLimitSpeed();
            if (limitSpeed != null && !limitSpeed.isEmpty()) {
                detail.append(",").append("限制速度为").append(dictMap.get("limitSpeedDict").get(limitSpeed)).append("公里每小时");
            }
            String limitType = measure.getLimitType();
            if (limitType != null && !limitType.isEmpty()) {
                detail.append(",").append(dictMap.get("limitTypeDict").get(limitType)).append(dictMap.get("limitCarTypeDict").get(measure.getCarType())).append("通行");
            }
            detail.append(";");
        }
        String result = "";
        if (detail.length() > 0) {
            result = detail.substring(0, detail.length() - 1);
        }
        if (result.length() > 0) {
            result = result + "。";
        }
        return result;
    }

    /**
     * 获取列表数据的措施详情
     *
     * @param list 事件列表数据
     * @return
     */
    @Override
    public List<Map> getIncidentListMeasure(List<Map> list) {
        //查询管控配置措施
        String status = LevelConfigStatusEnum.open_status.getCode();
        List<SdControlConfigMeasure> measureList = configMeasureService.selectValidConfigMeasureList(status);

        Map<Long, List<SdControlConfigMeasure>> measureMap = new HashMap<>();
        for (SdControlConfigMeasure measure : measureList) {
            Long levelId = measure.getConfigLevelId();
            measureMap.computeIfAbsent(levelId, k -> new ArrayList<>());
            measureMap.get(levelId).add(measure);
        }

        Map<String, Map<String, String>> dictMap = getMultiDictMap();

        if (list != null && list.size() > 0) {
            for (Map itemMap : list) {
                Long configLevelId = (Long) itemMap.get("configLevelId");
                List<SdControlConfigMeasure> measures = measureMap.get(configLevelId);
                if (measures == null || measures.size() == 0) {
                    continue;
                }
                String detail = handleCompleteMeasureDetail(measures, dictMap);

                itemMap.put("measureDetail", detail);
            }
        }
        return list;
    }

    /**
     * 获取事件的措施详情
     *
     * @param incidentId 事件id
     * @return
     */
    @Override
    public Map getIncidentInfoMeasure(Long incidentId) {
        Map map = new HashMap();

        //获取事件关联的管控措施
        List<SdTrafficIncidentMeasure> incidentMeasure = incidentMeasureService.getIncidentMeasureByIncidentId(incidentId);

        //获取管控措施列表、管控措施详情
        if (incidentMeasure != null && incidentMeasure.size() > 0) {
            Long configLevelId = incidentMeasure.get(0).getConfigLevelId();
            SdControlLevelConfig levelConfig = levelConfigService.selectSdControlLevelConfigById(configLevelId);
            List<SdControlConfigCause> causeList = configCauseService.getConfigCauseByLevelId(configLevelId);
            String causeDetail = configCauseService.getControlCauseDescription(causeList);
            List<SdControlConfigMeasure> list = configMeasureService.getConfigMeasureByLevelId(configLevelId);
            String measureDetail = getCompleteMeasureDetail(list);
            map.put("levelConfig", levelConfig);
            map.put("controlReason", causeDetail);
            map.put("measureList", list);
            map.put("measureDetail", measureDetail);
        }
        return map;
    }


    /**
     * 获取突发事件的主动交通流推送措施
     *
     * @param incidentType  事件类型
     * @param incidentGrade 事件级别
     * @return
     */
    @Override
    public JSONObject getActiveMeasureWithEmergencyIncident(String incidentType, String incidentGrade) {

        //筛选出的管控等级id
        List<Long> levelConfigIdList = new ArrayList<>();

        //查询出所有可用的管控配置原因
        String status = LevelConfigStatusEnum.open_status.getCode();
        List<SdControlConfigCause> causeList = configCauseService.selectValidConfigCauseList(status);
        for (SdControlConfigCause configCause : causeList) {
            String causeType = configCause.getCauseType();
            String incidentTypeConfig = configCause.getIncidentType();
            String incidentGradeConfig = configCause.getIncidentGrade();
            if (ControlConfigCauseEnum.emergency_incident.getCode().equals(causeType)) {
                //管控原因：突发事件
                if (incidentTypeConfig != null && incidentGradeConfig != null) {
                    if (incidentTypeConfig.equals(incidentType) && incidentGradeConfig.equals(incidentGrade)) {
                        //事件类型、事件级别符合
                        levelConfigIdList.add(configCause.getConfigLevelId());
                    }
                }
            }
        }

        JSONObject jsonObject = getConfigResultInfo(levelConfigIdList);

        return jsonObject;
    }

//
//    /**
//     * 获取管控原因相关的多种类型的字典数据
//     *
//     * @return
//     */
//    @Override
//    public Map<String, Map<String, String>> getMultiCauseDictMap() {
//        Map<String, Map<String, String>> dictMap = new HashMap<>();
//
//        //路面情况
//        String roadConditionCode = DictTypeEnum.sd_road_condition.getCode();
//        Map<String,String> roadConditionDictMap = getDictMap(roadConditionCode);
//
//        //拥挤度
//        String congestionCode = DictTypeEnum.sd_congestion_degree.getCode();
//        Map<String,String> congestionDictMap = getDictMap(congestionCode);
//
//        //事件类型
//        String incidentTypeCode = DictTypeEnum.incident_type.getCode();
//        Map<String,String> incidentTypeDictMap = getDictMap(incidentTypeCode);
//
//        //事件级别
//        String incidentLevelCode = DictTypeEnum.sd_incident_level.getCode();
//        Map<String,String> incidentLevelDictMap = getDictMap(incidentLevelCode);
//
//        dictMap.put("roadConditionDict",roadConditionDictMap);
//        dictMap.put("congestionDict",congestionDictMap);
//        dictMap.put("incidentTypeDict",incidentTypeDictMap);
//        dictMap.put("incidentLevelDict",incidentLevelDictMap);
//
//        return dictMap;
//
//    }
//
//
//
//    /**
//     * 获取管控原因的描述信息
//     *
//     * @param causeList 管控原因列表
//     * @return
//     */
//    @Override
//    public String getReasonCauseDesc(List<SdControlConfigCause> causeList) {
//
//        StringBuffer result = new StringBuffer();
//        if(causeList != null && causeList.size() > 0){
//            //获取字典数据
//            Map<String, Map<String, String>> dictMap = getMultiCauseDictMap();
//
//            //todo 暂时取一个管控原因
//           SdControlConfigCause cause = causeList.get(0);
//           String causeType = cause.getCauseType();
//            if(ControlConfigCauseEnum.visibility.getCode().equals(causeType)){
//                //能见度
//                result.append("能见度：").append(cause.getVisibilityMin()).append("-").append(cause.getVisibilityMax()).append("米");
//            }
//            if(ControlConfigCauseEnum.road_condition.getCode().equals(causeType)){
//                //路面情况
//                String roadCondition = cause.getRoadCondition();
//                result.append("路面").append(dictMap.get("roadConditionDict").get(roadCondition));
//            }
//            if(ControlConfigCauseEnum.congestion_degree.getCode().equals(causeType)){
//                //拥挤度
//                String congestion = cause.getCongestionDegree();
//                result.append("道路").append(dictMap.get("congestionDict").get(congestion));
//            }
//            if(ControlConfigCauseEnum.emergency_incident.getCode().equals(causeType)){
//                //突发事件
//                String incidentType = cause.getIncidentType();
//                String incidentLevel = cause.getIncidentGrade();
//                result.append("事件类型：").append(dictMap.get("incidentTypeDict").get(incidentType)).append(",")
//                        .append("事件级别：").append(dictMap.get("incidentLevelDict").get(incidentLevel));
//
//            }
//        }
//        return result.toString();
//    }
}
