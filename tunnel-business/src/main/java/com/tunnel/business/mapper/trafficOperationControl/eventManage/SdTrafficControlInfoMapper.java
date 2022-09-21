package com.tunnel.business.mapper.trafficOperationControl.eventManage;


import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficControlInfo;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;

import java.util.List;
import java.util.Map;

/**
 * 交通管制信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-16
 */
public interface SdTrafficControlInfoMapper
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
     * 删除交通管制信息
     *
     * @param id 交通管制信息主键
     * @return 结果
     */
    public int deleteSdTrafficControlInfoById(Long id);

    /**
     * 批量删除交通管制信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTrafficControlInfoByIds(Long[] ids);


    /**
     * 根据incidentId字段查询交通管制事件信息
     * @param incidentId
     * @return
     */
    List<SdTrafficControlInfo> getControlInfoByIncidentId(Long incidentId);

    /**
     * 根据事件信息id删除交通管制事件信息
     * @param incidentId
     * @return
     */
    int delControlInfoByIncidentId(Long incidentId);


//    /**
//     * 根据交通管制事件字段incidentId查询事件信息
//     * @param incidentId
//     * @return
//     */
//    SdTrafficIncidentInfo findIncidentInfo(String incidentId);
}
