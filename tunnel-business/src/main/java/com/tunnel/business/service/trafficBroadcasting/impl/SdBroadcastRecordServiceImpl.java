package com.tunnel.business.service.trafficBroadcasting.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.trafficBroadcasting.SdBroadcastRecord;
import com.tunnel.business.mapper.trafficBroadcasting.SdBroadcastRecordMapper;
import com.tunnel.business.service.trafficBroadcasting.ISdBroadcastRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 广播记录Service业务层处理
 *
 * @author ruoyi
 * @date 2021-11-25
 */
@Service
public class SdBroadcastRecordServiceImpl implements ISdBroadcastRecordService {
    @Autowired
    private SdBroadcastRecordMapper sdBroadcastRecordMapper;

    /**
     * 查询广播记录
     *
     * @param id 广播记录ID
     * @return 广播记录
     */
    @Override
    public SdBroadcastRecord selectSdBroadcastRecordById(Long id) {
        return sdBroadcastRecordMapper.selectSdBroadcastRecordById(id);
    }

    /**
     * 查询广播记录列表
     *
     * @param sdBroadcastRecord 广播记录
     * @return 广播记录
     */
    @Override
    public List<SdBroadcastRecord> selectSdBroadcastRecordList(SdBroadcastRecord sdBroadcastRecord) {
        return sdBroadcastRecordMapper.selectSdBroadcastRecordList(sdBroadcastRecord);
    }

    /**
     * 新增广播记录
     *
     * @param sdBroadcastRecord 广播记录
     * @return 结果
     */
    @Override
    public int insertSdBroadcastRecord(SdBroadcastRecord sdBroadcastRecord) {
        sdBroadcastRecord.setCreateTime(DateUtils.getNowDate());
        sdBroadcastRecord.setPublishResults("发布成功");
        return sdBroadcastRecordMapper.insertSdBroadcastRecord(sdBroadcastRecord);
    }

    /**
     * 修改广播记录
     *
     * @param sdBroadcastRecord 广播记录
     * @return 结果
     */
    @Override
    public int updateSdBroadcastRecord(SdBroadcastRecord sdBroadcastRecord) {
        return sdBroadcastRecordMapper.updateSdBroadcastRecord(sdBroadcastRecord);
    }

    /**
     * 批量删除广播记录
     *
     * @param ids 需要删除的广播记录ID
     * @return 结果
     */
    @Override
    public int deleteSdBroadcastRecordByIds(Long[] ids) {
        return sdBroadcastRecordMapper.deleteSdBroadcastRecordByIds(ids);
    }

    /**
     * 删除广播记录信息
     *
     * @param id 广播记录ID
     * @return 结果
     */
    @Override
    public int deleteSdBroadcastRecordById(Long id) {
        return sdBroadcastRecordMapper.deleteSdBroadcastRecordById(id);
    }
}
