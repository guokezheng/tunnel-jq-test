package com.tunnel.platform.service.trafficOperationControl.eventManage;

import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigCause;
import com.tunnel.platform.domain.trafficOperationControl.controlConfig.SdControlConfigMeasure;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficControlInfo;
import com.tunnel.platform.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;

import java.util.List;
import java.util.Map;

/**
 * 交通管制信息Service接口
 *
 * @author ruoyi
 * @date 2022-02-16
 */
public interface ISdTrafficControlInfoService
{
    /**
     * 查询交通管制信息
     *
     * @param id 交通管制信息主键
     * @return 交通管制信息
     */
    public SdTrafficControlInfo selectSdTrafficControlInfoById(Long id);

    /**
     * 查询交通管制信息列表
     *
     * @param sdTrafficControlInfo 交通管制信息
     * @return 交通管制信息集合
     */
    public List<SdTrafficControlInfo> selectSdTrafficControlInfoList(SdTrafficControlInfo sdTrafficControlInfo);

    /**
     * 查询交通管制列表
     * @param incidentInfo 事件信息
     * @return
     */
    List<Map> selectControlInfoList(SdTrafficIncidentInfo incidentInfo);

    /**
     * 新增交通管制信息
     *
     * @param sdTrafficControlInfo 交通管制信息
     * @return 结果
     */
    public int insertSdTrafficControlInfo(SdTrafficControlInfo sdTrafficControlInfo);

    /**
     * 修改交通管制信息
     *
     * @param sdTrafficControlInfo 交通管制信息
     * @return 结果
     */
    public int updateSdTrafficControlInfo(SdTrafficControlInfo sdTrafficControlInfo);

    /**
     * 批量删除交通管制信息
     *
     * @param ids 需要删除的交通管制信息主键集合
     * @return 结果
     */
    public int deleteSdTrafficControlInfoByIds(Long[] ids);

    /**
     * 删除交通管制信息信息
     *
     * @param id 交通管制信息主键
     * @return 结果
     */
    public int deleteSdTrafficControlInfoById(Long id);

    /**
     * 根据incidentId字段查询交通管制事件信息
     * @param incidentId
     * @return
     */
    SdTrafficControlInfo getControlInfoByIncidentId(Long incidentId);

    /**
     * 根据事件信息id删除交通管制事件信息
     * @param incidentId
     * @return
     */
    int delControlInfoByIncidentId(Long incidentId);

    /**
     * 拼接发布内容
     * @param tunnelName 隧道名称
     * @param reportTime 统计时间
     * @param causeList 原因列表
     * @param list 措施列表
     * @return
     */
    String joinPublishContent(String tunnelName, String reportTime, List<SdControlConfigCause> causeList, List<SdControlConfigMeasure> list);
}
