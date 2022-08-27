package com.tunnel.platform.service.trafficOperationControl.eventManage.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigCause;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficControlInfo;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;
import com.tunnel.platform.mapper.trafficOperationControl.controlConfig.SdControlConfigMeasureMapper;
import com.tunnel.platform.mapper.trafficOperationControl.eventManage.SdTrafficControlInfoMapper;
import com.tunnel.platform.service.trafficOperationControl.activeTrafficFlowControl.ActiveTrafficFlowService;
import com.tunnel.platform.service.trafficOperationControl.controlConfig.ISdControlConfigCauseService;
import com.tunnel.platform.service.trafficOperationControl.eventManage.ISdTrafficControlInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 交通管制信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-16
 */
@Service
public class SdTrafficControlInfoServiceImpl implements ISdTrafficControlInfoService
{
    @Autowired
    private SdTrafficControlInfoMapper sdTrafficControlInfoMapper;

    @Autowired
    private SdControlConfigMeasureMapper configMeasureMapper;

    @Autowired
    private ActiveTrafficFlowService activeTrafficFlowService;

    @Autowired
    private ISdControlConfigCauseService configCauseService;

    /**
     * 查询交通管制信息
     *
     * @param id 交通管制信息主键
     * @return 交通管制信息
     */
    @Override
    public SdTrafficControlInfo selectSdTrafficControlInfoById(Long id)
    {
        return sdTrafficControlInfoMapper.selectSdTrafficControlInfoById(id);
    }

    /**
     * 查询交通管制信息列表
     *
     * @param sdTrafficControlInfo 交通管制信息
     * @return 交通管制信息
     */
    @Override
    public List<SdTrafficControlInfo> selectSdTrafficControlInfoList(SdTrafficControlInfo sdTrafficControlInfo)
    {
        return sdTrafficControlInfoMapper.selectSdTrafficControlInfoList(sdTrafficControlInfo);
    }

    /**
     * 查询交通管制列表
     *
     * @param incidentInfo 事件信息
     * @return
     */
    @Override
    public List<Map> selectControlInfoList(SdTrafficIncidentInfo incidentInfo) {
        Long deptId = SecurityUtils.getDeptId();
        incidentInfo.getParams().put("deptId",deptId);
        List<Map> list = sdTrafficControlInfoMapper.selectControlInfoList(incidentInfo);
        //获取列表数据的措施详情
        list = activeTrafficFlowService.getIncidentListMeasure(list);
        return list;
    }

    /**
     * 新增交通管制信息
     *
     * @param sdTrafficControlInfo 交通管制信息
     * @return 结果
     */
    @Override
    public int insertSdTrafficControlInfo(SdTrafficControlInfo sdTrafficControlInfo)
    {
        sdTrafficControlInfo.setCreateTime(DateUtils.getNowDate());
        return sdTrafficControlInfoMapper.insertSdTrafficControlInfo(sdTrafficControlInfo);
    }

    /**
     * 修改交通管制信息
     *
     * @param sdTrafficControlInfo 交通管制信息
     * @return 结果
     */
    @Override
    public int updateSdTrafficControlInfo(SdTrafficControlInfo sdTrafficControlInfo)
    {
        sdTrafficControlInfo.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficControlInfoMapper.updateSdTrafficControlInfo(sdTrafficControlInfo);
    }

    /**
     * 批量删除交通管制信息
     *
     * @param ids 需要删除的交通管制信息主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficControlInfoByIds(Long[] ids)
    {
        return sdTrafficControlInfoMapper.deleteSdTrafficControlInfoByIds(ids);
    }

    /**
     * 删除交通管制信息信息
     *
     * @param id 交通管制信息主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficControlInfoById(Long id)
    {
        return sdTrafficControlInfoMapper.deleteSdTrafficControlInfoById(id);
    }

    /**
     * 根据incidentId字段查询交通管制事件信息
     *
     * @param incidentId
     * @return
     */
    @Override
    public SdTrafficControlInfo getControlInfoByIncidentId(Long incidentId) {
        List<SdTrafficControlInfo> list = sdTrafficControlInfoMapper.getControlInfoByIncidentId(incidentId);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据事件信息id删除交通管制事件信息
     *
     * @param incidentId
     * @return
     */
    @Override
    public int delControlInfoByIncidentId(Long incidentId) {
        return sdTrafficControlInfoMapper.delControlInfoByIncidentId(incidentId);
    }

    /**
     * 拼接发布内容
     *
     * @param tunnelName 隧道名称
     * @param reportTime 统计时间
     * @param causeList  原因列表
     * @param list       措施列表
     * @return
     */
    @Override
    public String joinPublishContent(String tunnelName, String reportTime, List<SdControlConfigCause> causeList, List<SdControlConfigMeasure> list) {
        //获取管控原因的描述信息
        String causeDesc = configCauseService.getControlCauseDescription(causeList);

        StringBuffer content = new StringBuffer();
        content.append(tunnelName).append( ",");
        content.append(reportTime).append(",");
        content.append(causeDesc).append(",");

        String measureDetail = activeTrafficFlowService.getCompleteMeasureDetail(list);
        content.append(measureDetail);
        return content.toString();
    }
}
