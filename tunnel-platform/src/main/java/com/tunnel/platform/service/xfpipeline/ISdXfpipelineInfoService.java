package com.tunnel.platform.service.xfpipeline;

import com.tunnel.platform.domain.xfpipeline.SdXfpipelineInfo;

import java.util.List;

/**
 * 消防管道监测Service接口
 * 
 * @author wangx
 * @date 2021-11-23
 *
 * 2022-02-12废除
 */
public interface ISdXfpipelineInfoService 
{
    /**
     * 查询消防管道监测
     * 
     * @param id 消防管道监测ID
     * @return 消防管道监测
     */
    public SdXfpipelineInfo selectSdXfpipelineInfoById(Long id);

    /**
     * 查询消防管道监测列表
     * 
     * @param sdXfpipelineInfo 消防管道监测
     * @return 消防管道监测集合
     */
    public List<SdXfpipelineInfo> selectSdXfpipelineInfoList(SdXfpipelineInfo sdXfpipelineInfo);
}
