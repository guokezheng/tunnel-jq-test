package com.tunnel.business.service.trafficOperationControl.eventManage.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.EmergencyIncidentTypeEnum;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigCause;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.business.domain.trafficOperationControl.eventManage.PublishStatusEnum;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficIncidentInfoMapper;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigCauseService;
import com.tunnel.business.service.trafficOperationControl.controlConfig.ISdControlConfigMeasureService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficAccidentInfoService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficIncidentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 交通运行管控-事件信息管理Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-14
 */
@Service
public class SdTrafficIncidentInfoServiceImpl implements ISdTrafficIncidentInfoService {
    @Autowired
    private SdTrafficIncidentInfoMapper sdTrafficIncidentInfoMapper;

    @Autowired
    private ISdControlConfigCauseService causeService;

    @Autowired
    private ISdControlConfigMeasureService measureService;

    @Autowired
    private ISdTrafficAccidentInfoService accidentInfoService;

    /**
     * 查询交通运行管控-事件信息管理
     *
     * @param id 交通运行管控-事件信息管理主键
     * @return 交通运行管控-事件信息管理
     */
    @Override
    public SdTrafficIncidentInfo selectSdTrafficIncidentInfoById(Long id) {
        return sdTrafficIncidentInfoMapper.selectSdTrafficIncidentInfoById(id);
    }

    /**
     * 查询交通运行管控-事件信息管理列表
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 交通运行管控-事件信息管理
     */
    @Override
    public List<SdTrafficIncidentInfo> selectSdTrafficIncidentInfoList(SdTrafficIncidentInfo sdTrafficIncidentInfo) {
        return sdTrafficIncidentInfoMapper.selectSdTrafficIncidentInfoList(sdTrafficIncidentInfo);
    }

    /**
     * 新增交通运行管控-事件信息管理
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 结果
     */
    @Override
    public int insertSdTrafficIncidentInfo(SdTrafficIncidentInfo sdTrafficIncidentInfo) {
        sdTrafficIncidentInfo.setCreateTime(DateUtils.getNowDate());
        return sdTrafficIncidentInfoMapper.insertSdTrafficIncidentInfo(sdTrafficIncidentInfo);
    }

    /**
     * 修改交通运行管控-事件信息管理
     *
     * @param sdTrafficIncidentInfo 交通运行管控-事件信息管理
     * @return 结果
     */
    @Override
    public int updateSdTrafficIncidentInfo(SdTrafficIncidentInfo sdTrafficIncidentInfo) {
        sdTrafficIncidentInfo.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficIncidentInfoMapper.updateSdTrafficIncidentInfo(sdTrafficIncidentInfo);
    }

    /**
     * 批量删除交通运行管控-事件信息管理
     *
     * @param ids 需要删除的交通运行管控-事件信息管理主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentInfoByIds(Long[] ids) {
        return sdTrafficIncidentInfoMapper.deleteSdTrafficIncidentInfoByIds(ids);
    }

    /**
     * 删除交通运行管控-事件信息管理信息
     *
     * @param id 交通运行管控-事件信息管理主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentInfoById(Long id) {
        return sdTrafficIncidentInfoMapper.deleteSdTrafficIncidentInfoById(id);
    }

    /**
     * 获取已发布的交通事件信息
     * //     * @param pageNum 页码
     * //     * @param pageSize 页面大小
     *
     * @return
     */
    @Override
    public List<Map> getPublishIncidentInfo() {
        String status = PublishStatusEnum.publish.getCode();
        List<Map> list = sdTrafficIncidentInfoMapper.getPublishIncidentInfo(status);

        Map<Long, List<SdControlConfigCause>> causeMap = new HashMap<>();
        List<SdControlConfigCause> causeList = causeService.selectSdControlConfigCauseList(new SdControlConfigCause());
        for (SdControlConfigCause cause : causeList) {
            Long configLevelId = cause.getConfigLevelId();
            List<SdControlConfigCause> itemList = causeMap.computeIfAbsent(configLevelId, k -> new ArrayList<>());
            itemList.add(cause);
        }

        Map<Long, List<SdControlConfigMeasure>> measureMap = new HashMap<>();
        List<SdControlConfigMeasure> measureList = measureService.selectSdControlConfigMeasureList(new SdControlConfigMeasure());
        for (SdControlConfigMeasure measure : measureList) {
            Long configLevelId = measure.getConfigLevelId();
            List<SdControlConfigMeasure> itemList = measureMap.computeIfAbsent(configLevelId, k -> new ArrayList<>());
            itemList.add(measure);
        }

        for (Map itemMap : list) {
            String configLevelId = Optional.ofNullable(itemMap.get("configLevelId")).orElse("").toString();
            Long levelId = Long.valueOf(configLevelId);
            String incidentType = Optional.ofNullable(itemMap.get("incidentType")).orElse("").toString();

            String content = "";
            if (EmergencyIncidentTypeEnum.barrier_clear.getCode().equals(incidentType)
                    || EmergencyIncidentTypeEnum.traffic_accident.getCode().equals(incidentType)) {
                //交通突发事件
                content = getEmergencyPublishContent(itemMap, measureMap.get(levelId));
            } else {
                //交通管制事件
                content = getControlPublishContent(itemMap, causeMap.get(levelId), measureMap.get(levelId));
            }

            itemMap.put("content", content);
        }
        return list;
    }

    /**
     * 获取突发事件的发布内容
     *
     * @param itemMap
     * @param measureList
     * @return
     */
    public String getEmergencyPublishContent(Map itemMap, List<SdControlConfigMeasure> measureList) {
        String tunnelName = Optional.ofNullable(itemMap.get("tunnelName")).orElse("").toString();
        String occurTime = Optional.ofNullable(itemMap.get("occurTime")).orElse("").toString();
        String incidentType = Optional.ofNullable(itemMap.get("incidentType")).orElse("").toString();
        String content = accidentInfoService.joinPublishContent(tunnelName, occurTime, incidentType, measureList);
        return content;
    }

    /**
     * 获取管制事件的发布内容
     *
     * @param itemMap
     * @param causeList
     * @param measureList
     * @return
     */
    public String getControlPublishContent(Map itemMap, List<SdControlConfigCause> causeList, List<SdControlConfigMeasure> measureList) {

        String tunnelName = Optional.ofNullable(itemMap.get("tunnelName")).orElse("").toString();
        String occurTime = Optional.ofNullable(itemMap.get("occurTime")).orElse("").toString();
        return "content";
    }
}
