package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdEmergencyResponse;

import java.util.List;

/**
 * 应急处置Mapper接口
 * 
 * @author zhangweitian
 * @date 2020-11-18
 */
public interface SdEmergencyResponseMapper 
{
    /**
     * 查询应急处置
     * 
     * @param responseId 应急处置ID
     * @return 应急处置
     */
    public SdEmergencyResponse selectSdEmergencyResponseById(Long responseId);

    /**
     * 查询应急处置列表
     * 
     * @param sdEmergencyResponse 应急处置
     * @return 应急处置集合
     */
    public List<SdEmergencyResponse> selectSdEmergencyResponseList(SdEmergencyResponse sdEmergencyResponse);
    /**
     * 查询应急处置列表
     * @param sdEmergencyResponse
     * @return
     */
    public List<SdEmergencyResponse> selectDropSdEmergencyResponseList(SdEmergencyResponse sdEmergencyResponse);
    /**
     * 新增应急处置
     * 
     * @param sdEmergencyResponse 应急处置
     * @return 结果
     */
    public int insertSdEmergencyResponse(SdEmergencyResponse sdEmergencyResponse);

    /**
     * 修改应急处置
     * 
     * @param sdEmergencyResponse 应急处置
     * @return 结果
     */
    public int updateSdEmergencyResponse(SdEmergencyResponse sdEmergencyResponse);

    /**
     * 删除应急处置
     * 
     * @param responseId 应急处置ID
     * @return 结果
     */
    public int deleteSdEmergencyResponseById(Long responseId);

    /**
     * 批量删除应急处置
     * 
     * @param responseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEmergencyResponseByIds(Long[] responseIds);
}
