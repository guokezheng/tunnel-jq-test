package com.tunnel.platform.service.dataInfo.impl;


import com.tunnel.platform.domain.dataInfo.SdEmergencyResponse;
import com.tunnel.platform.mapper.dataInfo.SdEmergencyResponseMapper;
import com.tunnel.platform.service.dataInfo.ISdEmergencyResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 应急处置Service业务层处理
 * 
 * @author zhangweitian
 * @date 2020-11-18
 */
@Service
public class SdEmergencyResponseServiceImpl implements ISdEmergencyResponseService 
{
    @Autowired
    private SdEmergencyResponseMapper sdEmergencyResponseMapper;

    /**
     * 查询应急处置
     * 
     * @param responseId 应急处置ID
     * @return 应急处置
     */
    @Override
    public SdEmergencyResponse selectSdEmergencyResponseById(Long responseId)
    {
        return sdEmergencyResponseMapper.selectSdEmergencyResponseById(responseId);
    }

    /**
     * 查询应急处置列表
     * 
     * @param sdEmergencyResponse 应急处置
     * @return 应急处置
     */
    @Override
    public List<SdEmergencyResponse> selectSdEmergencyResponseList(SdEmergencyResponse sdEmergencyResponse)
    {
        return sdEmergencyResponseMapper.selectDropSdEmergencyResponseList(sdEmergencyResponse);
    }

    /**
     * 新增应急处置
     * 
     * @param sdEmergencyResponse 应急处置
     * @return 结果
     */
    @Override
    public int insertSdEmergencyResponse(SdEmergencyResponse sdEmergencyResponse)
    {
        return sdEmergencyResponseMapper.insertSdEmergencyResponse(sdEmergencyResponse);
    }

    /**
     * 修改应急处置
     * 
     * @param sdEmergencyResponse 应急处置
     * @return 结果
     */
    @Override
    public int updateSdEmergencyResponse(SdEmergencyResponse sdEmergencyResponse)
    {
        return sdEmergencyResponseMapper.updateSdEmergencyResponse(sdEmergencyResponse);
    }

    /**
     * 批量删除应急处置
     * 
     * @param responseIds 需要删除的应急处置ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyResponseByIds(Long[] responseIds)
    {
        return sdEmergencyResponseMapper.deleteSdEmergencyResponseByIds(responseIds);
    }

    /**
     * 删除应急处置信息
     * 
     * @param responseId 应急处置ID
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyResponseById(Long responseId)
    {
        return sdEmergencyResponseMapper.deleteSdEmergencyResponseById(responseId);
    }
}
