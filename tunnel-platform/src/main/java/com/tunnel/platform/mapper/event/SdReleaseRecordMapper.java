package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdReleaseRecord;

import java.util.List;

/**
 * 发布记录Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
public interface SdReleaseRecordMapper 
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
     * 删除发布记录
     * 
     * @param id 发布记录主键
     * @return 结果
     */
    public int deleteSdReleaseRecordById(Long id);

    /**
     * 批量删除发布记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdReleaseRecordByIds(Long[] ids);
}
