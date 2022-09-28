package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdCallRecord;

import java.util.List;
import java.util.Map;

/**
 * 紧急电话记录Mapper接口
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
public interface SdCallRecordMapper 
{
    /**
     * 查询紧急电话记录
     * 
     * @param id 紧急电话记录ID
     * @return 紧急电话记录
     */
    public SdCallRecord selectSdCallRecordById(Long id);

    /**
     * 查询紧急电话记录列表
     * 
     * @param sdCallRecord 紧急电话记录
     * @return 紧急电话记录集合
     */
    public List<SdCallRecord> selectSdCallRecordList(SdCallRecord sdCallRecord);

    /**
     * 新增紧急电话记录
     * 
     * @param sdCallRecord 紧急电话记录
     * @return 结果
     */
    public int insertSdCallRecord(SdCallRecord sdCallRecord);

    /**
     * 修改紧急电话记录
     * 
     * @param sdCallRecord 紧急电话记录
     * @return 结果
     */
    public int updateSdCallRecord(SdCallRecord sdCallRecord);

    /**
     * 删除紧急电话记录
     * 
     * @param id 紧急电话记录ID
     * @return 结果
     */
    public int deleteSdCallRecordById(Long id);

    /**
     * 批量删除紧急电话记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdCallRecordByIds(Long[] ids);
    /**
     * 查询紧急电话echart数据
     * @param sdCallRecord
     * @return
     */

	public List<Map<String, String>> selectSdCallRecordEcharts(SdCallRecord sdCallRecord);

	 /**
     * 批量插入记录
     * 
     * @return 结果
     */
	public int insertSdCallRecordList(List<SdCallRecord> sdCallRecordList);
}