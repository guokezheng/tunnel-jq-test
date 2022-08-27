package com.tunnel.platform.service.dataInfo;


import com.tunnel.platform.domain.dataInfo.SdEmergencyResponse;

import java.util.List;

/**
 * 应急处置Service接口
 * 
 * @author zhangweitian
 * @date 2020-11-18
 */
public interface ISdEmergencyResponseService 
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
     * 批量删除应急处置
     * 
     * @param responseIds 需要删除的应急处置ID
     * @return 结果
     */
    public int deleteSdEmergencyResponseByIds(Long[] responseIds);

    /**
     * 删除应急处置信息
     * 
     * @param responseId 应急处置ID
     * @return 结果
     */
    public int deleteSdEmergencyResponseById(Long responseId);
}
