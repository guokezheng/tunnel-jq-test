package com.tunnel.platform.service.xfpipeline.impl;

import com.tunnel.platform.domain.xfpipeline.SdXfpipelineInfo;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.platform.mapper.xfpipeline.SdXfpipelineInfoMapper;
import com.tunnel.platform.service.xfpipeline.ISdXfpipelineInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消防管道监测Service业务层处理
 * 
 * @author wangx
 * @date 2021-11-23
 *
 * 2022-02-12废除
 */
@Service
public class SdXfpipelineInfoServiceImpl implements ISdXfpipelineInfoService
{
    @Autowired
    private SdXfpipelineInfoMapper sdXfpipelineInfoMapper;

    /**
     * 查询消防管道监测
     * 
     * @param id 消防管道监测ID
     * @return 消防管道监测
     */
    @Override
    public SdXfpipelineInfo selectSdXfpipelineInfoById(Long id)
    {
        return sdXfpipelineInfoMapper.selectSdXfpipelineInfoById(id);
    }

    /**
     * 查询消防管道监测列表
     * 
     * @param sdXfpipelineInfo 消防管道监测
     * @return 消防管道监测
     */
    @Override
    public List<SdXfpipelineInfo> selectSdXfpipelineInfoList(SdXfpipelineInfo sdXfpipelineInfo)
    {
        return sdXfpipelineInfoMapper.selectSdXfpipelineInfoList(sdXfpipelineInfo);
    }
}
