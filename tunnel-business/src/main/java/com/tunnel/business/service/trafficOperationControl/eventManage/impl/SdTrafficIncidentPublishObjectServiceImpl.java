package com.tunnel.business.service.trafficOperationControl.eventManage.impl;

import com.tunnel.business.domain.trafficOperationControl.eventManage.SdTrafficIncidentPublishObject;
import com.tunnel.business.mapper.trafficOperationControl.eventManage.SdTrafficIncidentPublishObjectMapper;
import com.tunnel.business.service.trafficOperationControl.eventManage.ISdTrafficIncidentPublishObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 交通事件-发布对象Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@Service
public class SdTrafficIncidentPublishObjectServiceImpl implements ISdTrafficIncidentPublishObjectService {
    @Autowired
    private SdTrafficIncidentPublishObjectMapper sdTrafficIncidentPublishObjectMapper;

    /**
     * 查询交通事件-发布对象
     *
     * @param objectId 交通事件-发布对象主键
     * @return 交通事件-发布对象
     */
    @Override
    public SdTrafficIncidentPublishObject selectSdTrafficIncidentPublishObjectByObjectId(Long objectId) {
        return sdTrafficIncidentPublishObjectMapper.selectSdTrafficIncidentPublishObjectByObjectId(objectId);
    }

    /**
     * 查询交通事件-发布对象列表
     *
     * @param sdTrafficIncidentPublishObject 交通事件-发布对象
     * @return 交通事件-发布对象
     */
    @Override
    public List<SdTrafficIncidentPublishObject> selectSdTrafficIncidentPublishObjectList(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject) {
        return sdTrafficIncidentPublishObjectMapper.selectSdTrafficIncidentPublishObjectList(sdTrafficIncidentPublishObject);
    }

    /**
     * 新增交通事件-发布对象
     *
     * @param sdTrafficIncidentPublishObject 交通事件-发布对象
     * @return 结果
     */
    @Override
    public int insertSdTrafficIncidentPublishObject(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject) {
        return sdTrafficIncidentPublishObjectMapper.insertSdTrafficIncidentPublishObject(sdTrafficIncidentPublishObject);
    }

    /**
     * 修改交通事件-发布对象
     *
     * @param sdTrafficIncidentPublishObject 交通事件-发布对象
     * @return 结果
     */
    @Override
    public int updateSdTrafficIncidentPublishObject(SdTrafficIncidentPublishObject sdTrafficIncidentPublishObject) {
        return sdTrafficIncidentPublishObjectMapper.updateSdTrafficIncidentPublishObject(sdTrafficIncidentPublishObject);
    }

    /**
     * 批量删除交通事件-发布对象
     *
     * @param objectIds 需要删除的交通事件-发布对象主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentPublishObjectByObjectIds(Long[] objectIds) {
        return sdTrafficIncidentPublishObjectMapper.deleteSdTrafficIncidentPublishObjectByObjectIds(objectIds);
    }

    /**
     * 删除交通事件-发布对象信息
     *
     * @param objectId 交通事件-发布对象主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficIncidentPublishObjectByObjectId(Long objectId) {
        return sdTrafficIncidentPublishObjectMapper.deleteSdTrafficIncidentPublishObjectByObjectId(objectId);
    }

    /**
     * 根据事件信息incidentId获取发布对象数据
     *
     * @param incidentId
     * @return
     */
    @Override
    public List<SdTrafficIncidentPublishObject> getPublishObjectByIncidentId(Long incidentId) {
        return sdTrafficIncidentPublishObjectMapper.getPublishObjectByIncidentId(incidentId);
    }

    /**
     * 添加多条发布对象
     *
     * @param list 发布对象list
     * @return
     */
    @Override
    public int insertMultiPublishObject(List<SdTrafficIncidentPublishObject> list) {
        return sdTrafficIncidentPublishObjectMapper.insertMultiPublishObject(list);
    }

    /**
     * 根据事件信息incidentId删除发布对象数据
     *
     * @param incidentId
     * @return
     */
    @Override
    public int delMultiPublishObject(Long incidentId) {
        return sdTrafficIncidentPublishObjectMapper.delMultiPublishObject(incidentId);
    }

    /**
     * 保存发布对象记录
     *
     * @param list       发布对象信息
     * @param incidentId 关联事件id
     * @return
     */
    @Override
    public int savePublishObject(List<String> list, Long incidentId) {
        List<SdTrafficIncidentPublishObject> incidentPublishObjectList = new ArrayList<>();
        list.forEach(publishObject -> {
            SdTrafficIncidentPublishObject incidentPublishObject = new SdTrafficIncidentPublishObject();
            incidentPublishObject.setIncidentId(incidentId);
            incidentPublishObject.setPublishObject(publishObject);
            incidentPublishObjectList.add(incidentPublishObject);
        });
        int rows = sdTrafficIncidentPublishObjectMapper.insertMultiPublishObject(incidentPublishObjectList);
        return rows;
    }
}
