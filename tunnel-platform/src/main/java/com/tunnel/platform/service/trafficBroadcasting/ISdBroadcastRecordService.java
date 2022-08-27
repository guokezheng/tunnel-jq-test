package com.tunnel.platform.service.trafficBroadcasting;

import com.tunnel.platform.domain.trafficBroadcasting.SdBroadcastRecord;

import java.util.List;

/**
 * 广播记录Service接口
 * 
 * @author ruoyi
 * @date 2021-11-25
 */
public interface ISdBroadcastRecordService 
{
    /**
     * 查询广播记录
     * 
     * @param id 广播记录ID
     * @return 广播记录
     */
    public SdBroadcastRecord selectSdBroadcastRecordById(Long id);

    /**
     * 查询广播记录列表
     * 
     * @param sdBroadcastRecord 广播记录
     * @return 广播记录集合
     */
    public List<SdBroadcastRecord> selectSdBroadcastRecordList(SdBroadcastRecord sdBroadcastRecord);

    /**
     * 新增广播记录
     * 
     * @param sdBroadcastRecord 广播记录
     * @return 结果
     */
    public int insertSdBroadcastRecord(SdBroadcastRecord sdBroadcastRecord);

    /**
     * 修改广播记录
     * 
     * @param sdBroadcastRecord 广播记录
     * @return 结果
     */
    public int updateSdBroadcastRecord(SdBroadcastRecord sdBroadcastRecord);

    /**
     * 批量删除广播记录
     * 
     * @param ids 需要删除的广播记录ID
     * @return 结果
     */
    public int deleteSdBroadcastRecordByIds(Long[] ids);

    /**
     * 删除广播记录信息
     * 
     * @param id 广播记录ID
     * @return 结果
     */
    public int deleteSdBroadcastRecordById(Long id);
}
