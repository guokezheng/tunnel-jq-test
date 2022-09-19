package com.tunnel.business.service.videoevents.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.videoevents.SdkEventAnalysis;
import com.tunnel.business.mapper.videoevents.SdkEventAnalysisMapper;
import com.tunnel.business.service.videoevents.ISdkEventAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 车道事件Service业务层处理
 *
 * @author ruoyi
 * @date 2021-04-26
 */
@Service
public class SdkEventAnalysisServiceImpl implements ISdkEventAnalysisService {
    @Autowired
    private SdkEventAnalysisMapper sdkEventAnalysisMapper;

    /**
     * 查询车道事件
     *
     * @param id 车道事件ID
     * @return 车道事件
     */
    @Override
    public SdkEventAnalysis selectSdkEventAnalysisById(Integer id) {
        return sdkEventAnalysisMapper.selectSdkEventAnalysisById(id);
    }

    @Override
    public List<SdkEventAnalysis> selectSdkEventAnalysisByTaskId(Integer id) {
        return sdkEventAnalysisMapper.selectSdkEventAnalysisByTaskId(id);
    }

    /**
     * 查询车道事件列表
     *
     * @param sdkEventAnalysis 车道事件
     * @return 车道事件
     */
    @Override
    public List<SdkEventAnalysis> selectSdkEventAnalysisList(SdkEventAnalysis sdkEventAnalysis) {
        return sdkEventAnalysisMapper.selectSdkEventAnalysisList(sdkEventAnalysis);
    }

    /**
     * 新增车道事件
     *
     * @param sdkEventAnalysis 车道事件
     * @return 结果
     */
    @Override
    public int insertSdkEventAnalysis(SdkEventAnalysis sdkEventAnalysis) {
        sdkEventAnalysis.setCreateTime(DateUtils.getNowDate());
        return sdkEventAnalysisMapper.insertSdkEventAnalysis(sdkEventAnalysis);
    }

    /**
     * 修改车道事件
     *
     * @param sdkEventAnalysis 车道事件
     * @return 结果
     */
    @Override
    public int updateSdkEventAnalysis(SdkEventAnalysis sdkEventAnalysis) {
        sdkEventAnalysis.setUpdateTime(DateUtils.getNowDate());
        return sdkEventAnalysisMapper.updateSdkEventAnalysis(sdkEventAnalysis);
    }

    /**
     * 批量删除车道事件
     *
     * @param ids 需要删除的车道事件ID
     * @return 结果
     */
    @Override
    public int deleteSdkEventAnalysisByIds(Integer[] ids) {
        return sdkEventAnalysisMapper.deleteSdkEventAnalysisByIds(ids);
    }

    /**
     * 删除车道事件信息
     *
     * @param id 车道事件ID
     * @return 结果
     */
    @Override
    public int deleteSdkEventAnalysisById(Integer id) {
        return sdkEventAnalysisMapper.deleteSdkEventAnalysisById(id);
    }
}
