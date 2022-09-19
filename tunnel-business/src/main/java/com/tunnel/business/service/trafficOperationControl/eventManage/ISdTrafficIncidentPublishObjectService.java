package com.tunnel.business.service.trafficOperationControl.eventManage;


import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentPublishObject;

import java.util.List;

/**
 * 交通事件-发布对象Service接口
 *
 * @author ruoyi
 * @date 2022-02-18
 */
public interface ISdTrafficIncidentPublishObjectService {
    /**
     * 查询交通事件-发布对象
     *
     * @param objectId 交通事件-发布对象主键
     * @return 交通事件-发布对象
     */
    SdTrafficIncidentPublishObject selectSdTrafficIncidentPublishObjectByObjectId(Long objectId);

    /**
     * 查询交通事件-发布对象列表
     *
     * @param sdTrafficIncidentPublishObject 交通事件-发布对象
     * @return 交通事件-发布对象集合
     */
    List<SdTrafficIncidentPublishObject> selectSdTrafficIncidentPublishObjectList(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject);

    /**
     * 新增交通事件-发布对象
     *
     * @param sdTrafficIncidentPublishObject 交通事件-发布对象
     * @return 结果
     */
    int insertSdTrafficIncidentPublishObject(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject);

    /**
     * 修改交通事件-发布对象
     *
     * @param sdTrafficIncidentPublishObject 交通事件-发布对象
     * @return 结果
     */
    int updateSdTrafficIncidentPublishObject(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject);

    /**
     * 批量删除交通事件-发布对象
     *
     * @param objectIds 需要删除的交通事件-发布对象主键集合
     * @return 结果
     */
    int deleteSdTrafficIncidentPublishObjectByObjectIds(Long[] objectIds);

    /**
     * 删除交通事件-发布对象信息
     *
     * @param objectId 交通事件-发布对象主键
     * @return 结果
     */
    int deleteSdTrafficIncidentPublishObjectByObjectId(Long objectId);


    /**
     * 根据事件信息incidentId获取发布对象数据
     *
     * @param incidentId
     * @return
     */
    List<SdTrafficIncidentPublishObject> getPublishObjectByIncidentId(Long incidentId);


    /**
     * 添加多条发布对象
     *
     * @param list 发布对象list
     * @return
     */
    int insertMultiPublishObject(List<SdTrafficIncidentPublishObject> list);

    /**
     * 根据事件信息incidentId删除发布对象数据
     *
     * @param incidentId
     * @return
     */
    int delMultiPublishObject(Long incidentId);

    /**
     * 保存发布对象记录
     *
     * @param list       发布对象信息
     * @param incidentId 关联事件id
     * @return
     */
    int savePublishObject(List<String> list, Long incidentId);
}
