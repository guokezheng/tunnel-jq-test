package com.tunnel.business.service.videoevents;


import com.tunnel.business.domain.videoevents.SdkEventLane;

import java.util.List;


/**
 * 车道信息Service接口
 *
 * @author ruoyi
 * @date 2021-04-26
 */
public interface ISdkEventLaneService {
    /**
     * 查询车道信息
     *
     * @param id 车道信息ID
     * @return 车道信息
     */
    SdkEventLane selectSdkEventLaneById(Integer id);

    List<SdkEventLane> selectSdkEventLaneByTaskId(Integer id);

    /**
     * 查询车道信息列表
     *
     * @param sdkEventLane 车道信息
     * @return 车道信息集合
     */
    List<SdkEventLane> selectSdkEventLaneList(SdkEventLane sdkEventLane);

    /**
     * 新增车道信息
     *
     * @param sdkEventLane 车道信息
     * @return 结果
     */
    int insertSdkEventLane(SdkEventLane sdkEventLane);

    /**
     * 修改车道信息
     *
     * @param sdkEventLane 车道信息
     * @return 结果
     */
    int updateSdkEventLane(SdkEventLane sdkEventLane);

    /**
     * 批量删除车道信息
     *
     * @param ids 需要删除的车道信息ID
     * @return 结果
     */
    int deleteSdkEventLaneByIds(Integer[] ids);

    /**
     * 删除车道信息信息
     *
     * @param id 车道信息ID
     * @return 结果
     */
    int deleteSdkEventLaneById(Integer id);
}
