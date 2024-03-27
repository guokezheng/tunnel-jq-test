package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdDataTrafficMonth;
import com.tunnel.business.service.dataInfo.ISdDataTrafficMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 各路段月车流量统计Controller
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/trafficFlowData/month")
@Api(tags = "各路段月车流量统计")
@ApiSupport(order = 16)
public class SdDataTrafficMonthController extends BaseController
{
    @Autowired
    private ISdDataTrafficMonthService sdDataTrafficMonthService;

    /**
     * 查询各路段月车流量统计列表
     */
    @ApiOperation("查询各路段月车流量统计列表")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:month:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdDataTrafficMonth sdDataTrafficMonth)
    {
        startPage();
        List<SdDataTrafficMonth> list = sdDataTrafficMonthService.selectSdDataTrafficMonthList(sdDataTrafficMonth);
        return getDataTable(list);
    }

    /**
     * 导出各路段月车流量统计列表
     */
    @ApiOperation("导出各路段月车流量统计列表")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:month:export')")
    @Log(title = "各路段月车流量统计", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDataTrafficMonth sdDataTrafficMonth)
    {
        List<SdDataTrafficMonth> list = sdDataTrafficMonthService.selectSdDataTrafficMonthList(sdDataTrafficMonth);
        ExcelUtil<SdDataTrafficMonth> util = new ExcelUtil<SdDataTrafficMonth>(SdDataTrafficMonth.class);
        return util.exportExcel(list, "各路段月车流量统计数据");
    }

    /**
     * 获取各路段月车流量统计详细信息
     */
    @ApiOperation("获取各路段月车流量统计详细信息")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:month:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDataTrafficMonthService.selectSdDataTrafficMonthById(id));
    }

    /**
     * 新增各路段月车流量统计
     */
    @ApiOperation("新增各路段月车流量统计")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:month:add')")
    @Log(title = "各路段月车流量统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDataTrafficMonth sdDataTrafficMonth)
    {
        return toAjax(sdDataTrafficMonthService.insertSdDataTrafficMonth(sdDataTrafficMonth));
    }

    /**
     * 修改各路段月车流量统计
     */
    @ApiOperation("修改各路段月车流量统计")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:month:edit')")
    @Log(title = "各路段月车流量统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDataTrafficMonth sdDataTrafficMonth)
    {
        return toAjax(sdDataTrafficMonthService.updateSdDataTrafficMonth(sdDataTrafficMonth));
    }

    /**
     * 删除各路段月车流量统计
     */
    @ApiOperation("删除各路段月车流量统计")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:month:remove')")
    @Log(title = "各路段月车流量统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDataTrafficMonthService.deleteSdDataTrafficMonthByIds(ids));
    }

    /**
     * 按月、隧道查每月每个车型的数量
     */
    @ApiOperation("按月、隧道查每月每个车型的数量")
    @PostMapping("/getCarNumberByMonth")
    public AjaxResult getCarNumberByMonth(@RequestBody SdDataTrafficMonth sdDataTrafficMonth) throws ParseException {
        return AjaxResult.success(sdDataTrafficMonthService.getCarNumberByMonth(sdDataTrafficMonth));
    }
}
