package com.tunnel.business.service.trafficOperationControl.eventManage.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.EmergencyIncidentTypeEnum;
import com.tunnel.business.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficAccidentInfo;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficAccidentInfoMapper;
import com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.ActiveTrafficFlowService;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficAccidentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 交通事故和清障信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdTrafficAccidentInfoServiceImpl implements ISdTrafficAccidentInfoService {
    @Autowired
    private SdTrafficAccidentInfoMapper sdTrafficAccidentInfoMapper;


    @Autowired
    private ActiveTrafficFlowService activeTrafficFlowService;

    /**
     * 查询交通事故和清障信息
     *
     * @param id 交通事故和清障信息主键
     * @return 交通事故和清障信息
     */
    @Override
    public SdTrafficAccidentInfo selectSdTrafficAccidentInfoById(Long id) {
        return sdTrafficAccidentInfoMapper.selectSdTrafficAccidentInfoById(id);
    }

    /**
     * 查询交通事故和清障信息列表
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 交通事故和清障信息
     */
    @Override
    public List<SdTrafficAccidentInfo> selectSdTrafficAccidentInfoList(SdTrafficAccidentInfo sdTrafficAccidentInfo) {
        return sdTrafficAccidentInfoMapper.selectSdTrafficAccidentInfoList(sdTrafficAccidentInfo);
    }

    /**
     * 新增交通事故和清障信息
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 结果
     */
    @Override
    public int insertSdTrafficAccidentInfo(SdTrafficAccidentInfo sdTrafficAccidentInfo) {
        sdTrafficAccidentInfo.setCreateTime(DateUtils.getNowDate());
        return sdTrafficAccidentInfoMapper.insertSdTrafficAccidentInfo(sdTrafficAccidentInfo);
    }

    /**
     * 修改交通事故和清障信息
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 结果
     */
    @Override
    public int updateSdTrafficAccidentInfo(SdTrafficAccidentInfo sdTrafficAccidentInfo) {
        sdTrafficAccidentInfo.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficAccidentInfoMapper.updateSdTrafficAccidentInfo(sdTrafficAccidentInfo);
    }

    /**
     * 批量删除交通事故和清障信息
     *
     * @param ids 需要删除的交通事故和清障信息主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficAccidentInfoByIds(Long[] ids) {
        return sdTrafficAccidentInfoMapper.deleteSdTrafficAccidentInfoByIds(ids);
    }

    /**
     * 删除交通事故和清障信息信息
     *
     * @param id 交通事故和清障信息主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficAccidentInfoById(Long id) {
        return sdTrafficAccidentInfoMapper.deleteSdTrafficAccidentInfoById(id);
    }

    /**
     * 根据incidentId字段查询交通事故、清障事件信息
     *
     * @param incidentId
     * @return
     */
    @Override
    public SdTrafficAccidentInfo getAccidentInfoByIncidentId(String incidentId) {
        List<SdTrafficAccidentInfo> list = sdTrafficAccidentInfoMapper.getAccidentInfoByIncidentId(incidentId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据事件信息incidentId删除交通事故、清障事件信息
     *
     * @param incidentId
     * @return
     */
    @Override
    public int delAccidentInfoByIncidentId(Long incidentId) {
        return sdTrafficAccidentInfoMapper.delAccidentInfoByIncidentId(incidentId);
    }

    /**
     * 查询交通事故、清障事件信息列表
     *
     * @param incidentInfo 事件信息
     * @return
     */
    @Override
    public List<Map> selectAccidentInfoList(SdTrafficIncidentInfo incidentInfo) {
        Long deptId = SecurityUtils.getDeptId();
        incidentInfo.getParams().put("deptId", deptId);
        List<Map> list = sdTrafficAccidentInfoMapper.selectAccidentInfoList(incidentInfo);
        //获取列表数据的措施详情
        list = activeTrafficFlowService.getIncidentListMeasure(list);
        return list;
    }

    /**
     * 根据incidentId字段查询交通事故、清障事件信息
     *
     * @param incidentId 事件id
     * @return
     */
    @Override
    public Map getAccident(Long incidentId) {
        List<Map> list = sdTrafficAccidentInfoMapper.getAccident(incidentId);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据事件incidentId修改交通事故和清障信息
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 结果
     */
    @Override
    public int updateAccidentInfo(SdTrafficAccidentInfo sdTrafficAccidentInfo) {
        sdTrafficAccidentInfo.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficAccidentInfoMapper.updateAccidentInfo(sdTrafficAccidentInfo);
    }

    /**
     * 拼接发布内容
     *
     * @param tunnelName   隧道名称
     * @param occurTime    发生时间
     * @param incidentType 事件类型
     * @param list         措施列表
     * @return
     */
    @Override
    public String joinPublishContent(String tunnelName, String occurTime, String incidentType, List<SdControlConfigMeasure> list) {

        StringBuffer content = new StringBuffer();
        String measureDetail = activeTrafficFlowService.getCompleteMeasureDetail(list);
        content.append(tunnelName).append(",");

        content.append(occurTime).append(",");

        if (EmergencyIncidentTypeEnum.traffic_accident.getCode().equals(incidentType)) {
            //交通事故
            content.append("发生交通事故").append(",");
        }
        if (EmergencyIncidentTypeEnum.barrier_clear.getCode().equals(incidentType)) {
            //道路清障
            content.append("实施道路清障").append(",");
        }

        content.append(measureDetail);
        return content.toString();
    }
}
