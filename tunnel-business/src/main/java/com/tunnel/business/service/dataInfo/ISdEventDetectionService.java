package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEventDetection;

import java.util.List;

/**
 * 事件监测记录Service接口
 *
 * @author liubaokui
 * @date 2021-04-21
 */
public interface ISdEventDetectionService {
    /**
     * 查询事件监测记录
     *
     * @param id 事件监测记录ID
     * @return 事件监测记录
     */
    SdEventDetection selectSdEventDetectionById(Long id);

    /**
     * 查询事件监测记录列表
     *
     * @param sdEventDetection 事件监测记录
     * @return 事件监测记录集合
     */
    List<SdEventDetection> selectSdEventDetectionList(SdEventDetection sdEventDetection);

    /**
     * 新增事件监测记录
     *
     * @param sdEventDetection 事件监测记录
     * @return 结果
     */
    int insertSdEventDetection(SdEventDetection sdEventDetection);

    /**
     * 修改事件监测记录
     *
     * @param sdEventDetection 事件监测记录
     * @return 结果
     */
    int updateSdEventDetection(SdEventDetection sdEventDetection);

    /**
     * 批量删除事件监测记录
     *
     * @param ids 需要删除的事件监测记录ID
     * @return 结果
     */
    int deleteSdEventDetectionByIds(Long[] ids);

    /**
     * 删除事件监测记录信息
     *
     * @param id 事件监测记录ID
     * @return 结果
     */
    int deleteSdEventDetectionById(Long id);
}
