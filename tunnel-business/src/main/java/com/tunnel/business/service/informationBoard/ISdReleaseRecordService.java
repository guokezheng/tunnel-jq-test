package com.tunnel.business.service.informationBoard;


import com.tunnel.business.domain.informationBoard.SdReleaseRecord;

import java.util.List;

/**
 * 发布记录Service接口
 *
 * @author 刘方堃
 * @date 2021-11-29
 */
public interface ISdReleaseRecordService {
    /**
     * 查询发布记录
     *
     * @param id 发布记录ID
     * @return 发布记录
     */
    SdReleaseRecord selectSdReleaseRecordById(Long id);

    /**
     * 查询发布记录列表
     *
     * @param sdReleaseRecord 发布记录
     * @return 发布记录集合
     */
    List<SdReleaseRecord> selectSdReleaseRecordList(SdReleaseRecord sdReleaseRecord);

    /**
     * 新增发布记录
     *
     * @param sdReleaseRecord 发布记录
     * @return 结果
     */
    int insertSdReleaseRecord(SdReleaseRecord sdReleaseRecord);

    /**
     * 修改发布记录
     *
     * @param sdReleaseRecord 发布记录
     * @return 结果
     */
    int updateSdReleaseRecord(SdReleaseRecord sdReleaseRecord);

    /**
     * 批量删除发布记录
     *
     * @param ids 需要删除的发布记录ID
     * @return 结果
     */
    int deleteSdReleaseRecordByIds(Long[] ids);

    /**
     * 删除发布记录信息
     *
     * @param id 发布记录ID
     * @return 结果
     */
    int deleteSdReleaseRecordById(Long id);
}
