package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdReleaseRecord;

import java.util.List;

/**
 * 发布记录Service接口
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
public interface ISdReleaseRecordService 
{
    /**
     * 查询发布记录
     * 
     * @param id 发布记录主键
     * @return 发布记录
     */
    public SdReleaseRecord selectSdReleaseRecordById(Long id);

    /**
     * 查询发布记录列表
     * 
     * @param sdReleaseRecord 发布记录
     * @return 发布记录集合
     */
    public List<SdReleaseRecord> selectSdReleaseRecordList(SdReleaseRecord sdReleaseRecord);

    /**
     * 新增发布记录
     * 
     * @param sdReleaseRecord 发布记录
     * @return 结果
     */
    public int insertSdReleaseRecord(SdReleaseRecord sdReleaseRecord);

    /**
     * 修改发布记录
     * 
     * @param sdReleaseRecord 发布记录
     * @return 结果
     */
    public int updateSdReleaseRecord(SdReleaseRecord sdReleaseRecord);

    /**
     * 批量删除发布记录
     * 
     * @param ids 需要删除的发布记录主键集合
     * @return 结果
     */
    public int deleteSdReleaseRecordByIds(Long[] ids);

    /**
     * 删除发布记录信息
     * 
     * @param id 发布记录主键
     * @return 结果
     */
    public int deleteSdReleaseRecordById(Long id);
}
