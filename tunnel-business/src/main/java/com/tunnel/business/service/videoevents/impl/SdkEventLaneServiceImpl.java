package com.tunnel.business.service.videoevents.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.videoevents.SdkEventLane;
import com.tunnel.business.mapper.videoevents.SdkEventLaneMapper;
import com.tunnel.business.service.videoevents.ISdkEventLaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 车道信息Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-26
 */
@Service
public class SdkEventLaneServiceImpl implements ISdkEventLaneService {
    @Autowired
    private SdkEventLaneMapper sdkEventLaneMapper;

    /**
     * 查询车道信息
     *
     * @param id 车道信息ID
     * @return 车道信息
     */
    @Override
    public SdkEventLane selectSdkEventLaneById(Integer id) {
        return sdkEventLaneMapper.selectSdkEventLaneById(id);
    }

    @Override
    public List<SdkEventLane> selectSdkEventLaneByTaskId(Integer id) {
        return sdkEventLaneMapper.selectSdkEventLaneByTaskId(id);
    }

    /**
     * 查询车道信息列表
     *
     * @param sdkEventLane 车道信息
     * @return 车道信息
     */
    @Override
    public List<SdkEventLane> selectSdkEventLaneList(SdkEventLane sdkEventLane) {
        return sdkEventLaneMapper.selectSdkEventLaneList(sdkEventLane);
    }

    /**
     * 新增车道信息
     *
     * @param sdkEventLane 车道信息
     * @return 结果
     */
    @Override
    public int insertSdkEventLane(SdkEventLane sdkEventLane) {
        sdkEventLane.setCreateTime(DateUtils.getNowDate());
        return sdkEventLaneMapper.insertSdkEventLane(sdkEventLane);
    }

    /**
     * 修改车道信息
     *
     * @param sdkEventLane 车道信息
     * @return 结果
     */
    @Override
    public int updateSdkEventLane(SdkEventLane sdkEventLane) {
        sdkEventLane.setUpdateTime(DateUtils.getNowDate());
        return sdkEventLaneMapper.updateSdkEventLane(sdkEventLane);
    }

    /**
     * 批量删除车道信息
     *
     * @param ids 需要删除的车道信息ID
     * @return 结果
     */
    @Override
    public int deleteSdkEventLaneByIds(Integer[] ids) {
        return sdkEventLaneMapper.deleteSdkEventLaneByIds(ids);
    }

    /**
     * 删除车道信息信息
     *
     * @param id 车道信息ID
     * @return 结果
     */
    @Override
    public int deleteSdkEventLaneById(Integer id) {
        return sdkEventLaneMapper.deleteSdkEventLaneById(id);
    }
}
