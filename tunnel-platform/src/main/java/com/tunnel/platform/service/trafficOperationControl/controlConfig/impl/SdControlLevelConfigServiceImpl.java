package com.tunnel.platform.service.trafficOperationControl.controlConfig.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.*;
import com.tunnel.platform.mapper.trafficOperationControl.controlConfig.SdControlConfigCauseMapper;
import com.tunnel.platform.mapper.trafficOperationControl.controlConfig.SdControlConfigMeasureMapper;
import com.tunnel.platform.mapper.trafficOperationControl.controlConfig.SdControlLevelConfigMapper;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlLevelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 管控等级配置Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-12
 */
@Service
public class SdControlLevelConfigServiceImpl implements ISdControlLevelConfigService
{
    @Autowired
    private SdControlLevelConfigMapper sdControlLevelConfigMapper;

    @Autowired
    private SdControlConfigCauseMapper configCauseMapper;

    @Autowired
    private SdControlConfigMeasureMapper configMeasureMapper;

    /**
     * 查询管控等级配置
     *
     * @param id 管控等级配置主键
     * @return 管控等级配置
     */
    @Override
    public SdControlLevelConfig selectSdControlLevelConfigById(Long id)
    {
        return sdControlLevelConfigMapper.selectSdControlLevelConfigById(id);
    }

    /**
     * 查询管控等级配置列表
     *
     * @param sdControlLevelConfig 管控等级配置
     * @return 管控等级配置
     */
    @Override
    public List<SdControlLevelConfig> selectSdControlLevelConfigList(SdControlLevelConfig sdControlLevelConfig)
    {
        return sdControlLevelConfigMapper.selectSdControlLevelConfigList(sdControlLevelConfig);
    }

    /**
     * 新增管控等级配置
     *
     * @param sdControlLevelConfig 管控等级配置
     * @return 结果
     */
    @Override
    public int insertSdControlLevelConfig(SdControlLevelConfig sdControlLevelConfig)
    {
        sdControlLevelConfig.setCreateTime(DateUtils.getNowDate());
        return sdControlLevelConfigMapper.insertSdControlLevelConfig(sdControlLevelConfig);
    }

    /**
     * 修改管控等级配置
     *
     * @param sdControlLevelConfig 管控等级配置
     * @return 结果
     */
    @Override
    public int updateSdControlLevelConfig(SdControlLevelConfig sdControlLevelConfig)
    {
        sdControlLevelConfig.setUpdateTime(DateUtils.getNowDate());
        return sdControlLevelConfigMapper.updateSdControlLevelConfig(sdControlLevelConfig);
    }

    /**
     * 批量删除管控等级配置
     *
     * @param ids 需要删除的管控等级配置主键
     * @return 结果
     */
    @Override
    public int deleteSdControlLevelConfigByIds(Long[] ids)
    {
        return sdControlLevelConfigMapper.deleteSdControlLevelConfigByIds(ids);
    }

    /**
     * 删除管控等级配置信息
     *
     * @param id 管控等级配置主键
     * @return 结果
     */
    @Override
    public int deleteSdControlLevelConfigById(Long id)
    {
        return sdControlLevelConfigMapper.deleteSdControlLevelConfigById(id);
    }

    /**
     * 保存配置措施
     *
     * @param jsonObject 参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveControlMeasure(JSONObject jsonObject){
        JSONArray causeList = jsonObject.getJSONArray("causeList");
        JSONArray measureList = jsonObject.getJSONArray("measureList");
        Long controlLevelId = Long.valueOf(jsonObject.getString("id"));

        //管控原因
        List<SdControlConfigCause> configCauseList = handleControlCauseList(causeList,controlLevelId);
        //管控措施
        List<SdControlConfigMeasure> configMeasureList = handleControlMeasureList(measureList,controlLevelId);

        //删除关联的管控原因、管控措施记录
        configCauseMapper.delConfigCauseByLevelId(controlLevelId);
        configMeasureMapper.delConfigMeasureByLevelId(controlLevelId);

        //添加新的管控原因、管控措施记录
        if(configCauseList != null && configCauseList.size() > 0){
            configCauseMapper.insertMultiConfigCause(configCauseList);
        }

        if(configMeasureList != null && configMeasureList.size() > 0){
            configMeasureMapper.insertMultiConfigMeasure(configMeasureList);
        }
    }

    /**
     * 组装管控原因列表数据
     * @param causeList 管控原因数据
     * @param controlLevelId 等级配置id
     * @return
     */
    private List<SdControlConfigCause> handleControlCauseList(JSONArray causeList,Long controlLevelId){
        List<SdControlConfigCause> configCauseList = new ArrayList<>();
        //管控原因
        causeList.forEach(itemObject->{
            SdControlConfigCause configCause = new SdControlConfigCause();
            configCause.setConfigLevelId(controlLevelId);
            Map itemMap = (Map) itemObject;
            String causeType = Optional.ofNullable(itemMap.get("causeType")).orElse("").toString();
            configCause.setCauseType(causeType);

            String roadCondition = Optional.ofNullable(itemMap.get("roadCondition")).orElse("").toString();
            configCause.setRoadCondition(roadCondition);

            String visibilityMin = Optional.ofNullable(itemMap.get("visibilityMin")).orElse("").toString();
            configCause.setVisibilityMin(visibilityMin);

            String visibilityMax = Optional.ofNullable(itemMap.get("visibilityMax")).orElse("").toString();
            configCause.setVisibilityMax(visibilityMax);

            String congestionDegree = Optional.ofNullable(itemMap.get("congestionDegree")).orElse("").toString();
            configCause.setCongestionDegree(congestionDegree);

            String incidentType = Optional.ofNullable(itemMap.get("incidentType")).orElse("").toString();
            configCause.setIncidentType(incidentType);

            String incidentGrade = Optional.ofNullable(itemMap.get("incidentGrade")).orElse("").toString();
            configCause.setIncidentGrade(incidentGrade);

            configCause.setCreateTime(DateUtils.getNowDate());
            configCauseList.add(configCause);

        });

        return configCauseList;
    }


    /**
     * 组装管控措施列表数据
     * @param measureList 管控措施数据
     * @param controlLevelId 等级配置id
     * @return
     */
    private List<SdControlConfigMeasure> handleControlMeasureList(JSONArray measureList,Long controlLevelId){
        List<SdControlConfigMeasure> configMeasureList = new ArrayList<>();

        //管控措施
        measureList.forEach(itemObject ->{
            Map itemMap = (Map) itemObject;
            SdControlConfigMeasure configMeasure = new SdControlConfigMeasure();
            configMeasure.setConfigLevelId(controlLevelId);

            //管控范围
            String controlRangeMin =  Optional.ofNullable(itemMap.get("controlRangeMin")).orElse("").toString();
            String controlRangeMax = Optional.ofNullable(itemMap.get("controlRangeMax")).orElse("").toString();
            configMeasure.setControlRangeMin(controlRangeMin);
            configMeasure.setControlRangeMax(controlRangeMax);

            //措施类型
            String measureType = Optional.ofNullable(itemMap.get("measureType")).orElse("").toString();
            configMeasure.setMeasureType(measureType);
            //具体措施
            String controlMeasure = Optional.ofNullable(itemMap.get("controlMeasure")).orElse("").toString();
            configMeasure.setControlMeasure(controlMeasure);
//            //措施值
//            String measureValue = Optional.ofNullable(itemMap.get("measureValue")).orElse("").toString();
//            configMeasure.setMeasureValue(measureValue);

            //后台做判断，避免将前台传过来多余的数据保存
            if(ControlMeasureEnum.limitspeed.getCode().equals(controlMeasure) || ControlMeasureEnum.emergencyLine.getCode().equals(controlMeasure)){
                //限制速度 或 开放应急车道
                String limitSpeed = Optional.ofNullable(itemMap.get("limitSpeed")).orElse("").toString();
                configMeasure.setLimitSpeed(limitSpeed);
            }
            if(ControlMeasureEnum.limitcar.getCode().equals(controlMeasure)){
                //限行车辆
                //限制类型
                String limitType = Optional.ofNullable(itemMap.get("limitType")).orElse("").toString();
                configMeasure.setLimitType(limitType);
                //车辆类型
                String carType = Optional.ofNullable(itemMap.get("carType")).orElse("").toString();
                configMeasure.setCarType(carType);
            }

            configMeasure.setCreateTime(DateUtils.getNowDate());
            configMeasureList.add(configMeasure);
        });

        return configMeasureList;
    }


    /**
     * 查询是否存在相同的管控类别、管控级别的数据
     * @param sdControlLevelConfig 管控等级配置
     * @return
     */
    @Override
    public int querySameLevelConfig(SdControlLevelConfig sdControlLevelConfig) {
        return sdControlLevelConfigMapper.querySameLevelConfig(sdControlLevelConfig);
    }

    /**
     * 查询配置的管控类别
     *
     * @return
     */
    @Override
    public List<Map> getControlTypeList() {
        String status = LevelConfigStatusEnum.open_status.getCode();
        String dictType = DictTypeEnum.sd_control_type.getCode();
        return sdControlLevelConfigMapper.getControlTypeList(status,dictType);
    }

    /**
     * 根据管控类别查询配置的管控级别
     *
     * @param controlType 管控类别
     * @return
     */
    @Override
    public List<Map> getControlLevelByType(String controlType) {
        String status = LevelConfigStatusEnum.open_status.getCode();
        String dictType = DictTypeEnum.sd_control_level.getCode();
        return sdControlLevelConfigMapper.getControlLevelByType(controlType,status,dictType);
    }

    /**
     * 根据管控类别、管控级别查询配置信息
     *
     * @param config
     * @return
     */
    @Override
    public SdControlLevelConfig getConfigInfo(SdControlLevelConfig config) {
        return sdControlLevelConfigMapper.getConfigInfo(config);
    }

}
