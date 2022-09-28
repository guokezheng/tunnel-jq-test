package com.tunnel.business.mapper.video;


import com.tunnel.business.domain.video.SdVedioRecord;

import java.util.List;

/**
 * 历史视频信息Mapper接口
 * 
 * @author xuebi
 * @date 2020-11-20
 */
public interface SdVedioRecordMapper 
{
    /**
     * 查询历史视频信息
     * 
     * @param id 历史视频信息ID
     * @return 历史视频信息
     */
    public SdVedioRecord selectSdVedioRecordById(Long id);

    /**
     * 查询历史视频信息列表
     * 
     * @param sdVedioRecord 历史视频信息
     * @return 历史视频信息集合
     */
    public List<SdVedioRecord> selectSdVedioRecordList(SdVedioRecord sdVedioRecord);

    /**
     * 新增历史视频信息
     * 
     * @param sdVedioRecord 历史视频信息
     * @return 结果
     */
    public int insertSdVedioRecord(SdVedioRecord sdVedioRecord);

    /**
     * 修改历史视频信息
     * 
     * @param sdVedioRecord 历史视频信息
     * @return 结果
     */
    public int updateSdVedioRecord(SdVedioRecord sdVedioRecord);

    /**
     * 删除历史视频信息
     * 
     * @param id 历史视频信息ID
     * @return 结果
     */
    public int deleteSdVedioRecordById(Long id);

    /**
     * 批量删除历史视频信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdVedioRecordByIds(Long[] ids);
}