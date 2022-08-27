package com.tunnel.platform.mapper.informationBoard;

import com.tunnel.platform.domain.informationBoard.SdReleaseRecord;

import java.util.List;

/**
 * 发布记录Mapper接口
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
public interface SdReleaseRecordMapper 
{
    /**
     * 查询发布记录
     * 
     * @param id 发布记录ID
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
     * @param id 发布记录ID
     * @return 结果
     */
    public int deleteSdReleaseRecordById(Long id);

    /**
     * 批量删除发布记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdReleaseRecordByIds(Long[] ids);
}
