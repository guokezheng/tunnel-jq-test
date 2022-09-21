package com.tunnel.business.service.videoevents;


import com.tunnel.business.domain.videoevents.SdkEventAnalysis;

import java.util.List;


/**
 * 车道事件Service接口
 *
 * @author ruoyi
 * @date 2021-04-26
 */
public interface ISdkEventAnalysisService {
    /**
     * 查询车道事件
     *
     * @param id 车道事件ID
     * @return 车道事件
     */
    SdkEventAnalysis selectSdkEventAnalysisById(Integer id);

    List<SdkEventAnalysis> selectSdkEventAnalysisByTaskId(Integer id);

    /**
     * 查询车道事件列表
     *
     * @param sdkEventAnalysis 车道事件
     * @return 车道事件集合
     */
    List<SdkEventAnalysis> selectSdkEventAnalysisList(SdkEventAnalysis sdkEventAnalysis);

    /**
     * 新增车道事件
     *
     * @param sdkEventAnalysis 车道事件
     * @return 结果
     */
    int insertSdkEventAnalysis(SdkEventAnalysis sdkEventAnalysis);

    /**
     * 修改车道事件
     *
     * @param sdkEventAnalysis 车道事件
     * @return 结果
     */
    int updateSdkEventAnalysis(SdkEventAnalysis sdkEventAnalysis);

    /**
     * 批量删除车道事件
     *
     * @param ids 需要删除的车道事件ID
     * @return 结果
     */
    int deleteSdkEventAnalysisByIds(Integer[] ids);

    /**
     * 删除车道事件信息
     *
     * @param id 车道事件ID
     * @return 结果
     */
    int deleteSdkEventAnalysisById(Integer id);
}
