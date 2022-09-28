package com.tunnel.business.mapper.trafficOperationControl.eventManage;


import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficAccidentInfo;
import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentInfo;

import java.util.List;
import java.util.Map;

/**
 * 交通事故和清障信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface SdTrafficAccidentInfoMapper
{
    /**
     * 查询交通事故和清障信息
     *
     * @param id 交通事故和清障信息主键
     * @return 交通事故和清障信息
     */
    public SdTrafficAccidentInfo selectSdTrafficAccidentInfoById(Long id);

    /**
     * 查询交通事故和清障信息列表
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 交通事故和清障信息集合
     */
    public List<SdTrafficAccidentInfo> selectSdTrafficAccidentInfoList(SdTrafficAccidentInfo sdTrafficAccidentInfo);

    /**
     * 新增交通事故和清障信息
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 结果
     */
    public int insertSdTrafficAccidentInfo(SdTrafficAccidentInfo sdTrafficAccidentInfo);

    /**
     * 修改交通事故和清障信息
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 结果
     */
    public int updateSdTrafficAccidentInfo(SdTrafficAccidentInfo sdTrafficAccidentInfo);

    /**
     * 删除交通事故和清障信息
     *
     * @param id 交通事故和清障信息主键
     * @return 结果
     */
    public int deleteSdTrafficAccidentInfoById(Long id);

    /**
     * 批量删除交通事故和清障信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdTrafficAccidentInfoByIds(Long[] ids);

    /**
     * 根据incidentId字段查询交通事故、清障事件信息
     * @param incidentId
     * @return
     */
    List<SdTrafficAccidentInfo> getAccidentInfoByIncidentId(String incidentId);

    /**
     * 根据事件信息incidentId删除交通事故、清障事件信息
     * @param incidentId
     * @return
     */
    int delAccidentInfoByIncidentId(Long incidentId);


    /**
     * 查询交通事故、清障事件信息列表
     * @param incidentInfo 事件信息
     * @return
     */
    List<Map> selectAccidentInfoList(SdTrafficIncidentInfo incidentInfo);

    /**
     * 根据incidentId字段查询交通事故、清障事件信息
     * @param incidentId 事件id
     * @return
     */
    List<Map> getAccident(Long incidentId);

    /**
     * 根据事件incidentId修改交通事故和清障信息
     *
     * @param sdTrafficAccidentInfo 交通事故和清障信息
     * @return 结果
     */
    int updateAccidentInfo(SdTrafficAccidentInfo sdTrafficAccidentInfo);
}
