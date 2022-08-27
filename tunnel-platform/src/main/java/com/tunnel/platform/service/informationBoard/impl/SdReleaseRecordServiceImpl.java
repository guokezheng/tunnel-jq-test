package com.tunnel.platform.service.informationBoard.impl;

import com.tunnel.platform.domain.informationBoard.SdReleaseRecord;
import com.tunnel.platform.mapper.informationBoard.SdReleaseRecordMapper;
import com.tunnel.platform.service.informationBoard.ISdReleaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 发布记录Service业务层处理
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
@Service
public class SdReleaseRecordServiceImpl implements ISdReleaseRecordService
{
    @Autowired
    private SdReleaseRecordMapper sdReleaseRecordMapper;

    /**
     * 查询发布记录
     * 
     * @param id 发布记录ID
     * @return 发布记录
     */
    @Override
    public SdReleaseRecord selectSdReleaseRecordById(Long id)
    {
        return sdReleaseRecordMapper.selectSdReleaseRecordById(id);
    }

    /**
     * 查询发布记录列表
     * 
     * @param sdReleaseRecord 发布记录
     * @return 发布记录
     */
    @Override
    public List<SdReleaseRecord> selectSdReleaseRecordList(SdReleaseRecord sdReleaseRecord)
    {
        return sdReleaseRecordMapper.selectSdReleaseRecordList(sdReleaseRecord);
    }

    /**
     * 新增发布记录
     * 
     * @param sdReleaseRecord 发布记录
     * @return 结果
     */
    @Override
    public int insertSdReleaseRecord(SdReleaseRecord sdReleaseRecord)
    {
        return sdReleaseRecordMapper.insertSdReleaseRecord(sdReleaseRecord);
    }

    /**
     * 修改发布记录
     * 
     * @param sdReleaseRecord 发布记录
     * @return 结果
     */
    @Override
    public int updateSdReleaseRecord(SdReleaseRecord sdReleaseRecord)
    {
        return sdReleaseRecordMapper.updateSdReleaseRecord(sdReleaseRecord);
    }

    /**
     * 批量删除发布记录
     * 
     * @param ids 需要删除的发布记录ID
     * @return 结果
     */
    @Override
    public int deleteSdReleaseRecordByIds(Long[] ids)
    {
        return sdReleaseRecordMapper.deleteSdReleaseRecordByIds(ids);
    }

    /**
     * 删除发布记录信息
     * 
     * @param id 发布记录ID
     * @return 结果
     */
    @Override
    public int deleteSdReleaseRecordById(Long id)
    {
        return sdReleaseRecordMapper.deleteSdReleaseRecordById(id);
    }
}
