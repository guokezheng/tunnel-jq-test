package com.tunnel.platform.controller.xfpipeline;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.tunnel.business.domain.xfpipeline.SdXfpipelineInfo;
import com.tunnel.business.service.xfpipeline.ISdXfpipelineInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消防管道监测数据查询Controller
 * 
 * @author wangx
 * @date 2021-11-23
 *
 * 2022-02-12废除
 */
@RestController
@RequestMapping("/xfpipeline")
public class SdXfpipelineInfoController extends BaseController
{
    @Autowired
    private ISdXfpipelineInfoService sdXfpipelineInfoService;

    /**
     * 查询消防管道监测列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdXfpipelineInfo sdXfpipelineInfo)
    {
        startPage();
        List<SdXfpipelineInfo> list = sdXfpipelineInfoService.selectSdXfpipelineInfoList(sdXfpipelineInfo);
        return getDataTable(list);
    }

    /**
     * 获取消防管道监测详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdXfpipelineInfoService.selectSdXfpipelineInfoById(id));
    }

}
