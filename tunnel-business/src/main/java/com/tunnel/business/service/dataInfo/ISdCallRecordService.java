package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdCallRecord;

import java.util.List;
import java.util.Map;

/**
 * 紧急电话记录Service接口
 *
 * @author yanghousheng
 * @date 2020-11-10
 */
public interface ISdCallRecordService {
    /**
     * 查询紧急电话记录
     *
     * @param id 紧急电话记录ID
     * @return 紧急电话记录
     */
    SdCallRecord selectSdCallRecordById(Long id);

    /**
     * 查询紧急电话记录列表
     *
     * @param sdCallRecord 紧急电话记录
     * @return 紧急电话记录集合
     */
    List<SdCallRecord> selectSdCallRecordList(SdCallRecord sdCallRecord);

    /**
     * 新增紧急电话记录
     *
     * @param sdCallRecord 紧急电话记录
     * @return 结果
     */
    int insertSdCallRecord(SdCallRecord sdCallRecord);

    /**
     * 修改紧急电话记录
     *
     * @param sdCallRecord 紧急电话记录
     * @return 结果
     */
    int updateSdCallRecord(SdCallRecord sdCallRecord);

    /**
     * 批量删除紧急电话记录
     *
     * @param ids 需要删除的紧急电话记录ID
     * @return 结果
     */
    int deleteSdCallRecordByIds(Long[] ids);

    /**
     * 删除紧急电话记录信息
     *
     * @param id 紧急电话记录ID
     * @return 结果
     */
    int deleteSdCallRecordById(Long id);

    /**
     * 查询紧急电话echart信息
     *
     * @param sdCallRecord
     * @return
     */

    List<Map<String, String>> selectSdCallRecordEcharts(SdCallRecord sdCallRecord);

    /**
     * 批量插入记录
     *
     * @param sdCallRecordList
     * @return
     */
    int insertSdCallRecordList(List<SdCallRecord> sdCallRecordList);
}