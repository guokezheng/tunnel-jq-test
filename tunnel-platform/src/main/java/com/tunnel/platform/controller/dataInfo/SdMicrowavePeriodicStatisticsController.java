package com.tunnel.platform.controller.dataInfo;

import java.util.List;

import com.tunnel.business.domain.dataInfo.SdMicrowavePeriodicStatistics;
import com.tunnel.business.service.dataInfo.ISdMicrowavePeriodicStatisticsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车流量信息Controller
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
@RestController
@RequestMapping("/microwave/statistics")
public class SdMicrowavePeriodicStatisticsController extends BaseController
{
    @Autowired
    private ISdMicrowavePeriodicStatisticsService sdMicrowavePeriodicStatisticsService;

    /**
     * 查询车流量信息列表
     */
    @ApiOperation("查询车流量信息列表")
    @PreAuthorize("@ss.hasPermi('microwave:statistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics)
    {
        startPage();
        List<SdMicrowavePeriodicStatistics> list = sdMicrowavePeriodicStatisticsService.selectSdMicrowavePeriodicStatisticsList(sdMicrowavePeriodicStatistics);
        return getDataTable(list);
    }

    /**
     * 导出车流量信息列表
     */
    @ApiOperation("导出车流量信息列表")
    @PreAuthorize("@ss.hasPermi('microwave:statistics:export')")
    @Log(title = "车流量信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics)
    {
        List<SdMicrowavePeriodicStatistics> list = sdMicrowavePeriodicStatisticsService.selectSdMicrowavePeriodicStatisticsList(sdMicrowavePeriodicStatistics);
        ExcelUtil<SdMicrowavePeriodicStatistics> util = new ExcelUtil<SdMicrowavePeriodicStatistics>(SdMicrowavePeriodicStatistics.class);
        return util.exportExcel(list, "车流量信息数据");
    }

    /**
     * 获取车流量信息详细信息
     */
    @ApiOperation("获取车流量信息详细信息")
    @PreAuthorize("@ss.hasPermi('microwave:statistics:query')")
    @GetMapping(value = "/{statisticsId}")
    public AjaxResult getInfo(@PathVariable("statisticsId") Long statisticsId)
    {
        return AjaxResult.success(sdMicrowavePeriodicStatisticsService.selectSdMicrowavePeriodicStatisticsByStatisticsId(statisticsId));
    }

    /**
     * 新增车流量信息
     */
    @ApiOperation("新增车流量信息")
    @PreAuthorize("@ss.hasPermi('microwave:statistics:add')")
    @Log(title = "车流量信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics)
    {
        return toAjax(sdMicrowavePeriodicStatisticsService.insertSdMicrowavePeriodicStatistics(sdMicrowavePeriodicStatistics));
    }

    /**
     * 修改车流量信息
     */
    @ApiOperation("修改车流量信息")
    @PreAuthorize("@ss.hasPermi('microwave:statistics:edit')")
    @Log(title = "车流量信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdMicrowavePeriodicStatistics sdMicrowavePeriodicStatistics)
    {
        return toAjax(sdMicrowavePeriodicStatisticsService.updateSdMicrowavePeriodicStatistics(sdMicrowavePeriodicStatistics));
    }

    /**
     * 删除车流量信息
     */
    @ApiOperation("删除车流量信息")
    @PreAuthorize("@ss.hasPermi('microwave:statistics:remove')")
    @Log(title = "车流量信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{statisticsIds}")
    public AjaxResult remove(@PathVariable Long[] statisticsIds)
    {
        return toAjax(sdMicrowavePeriodicStatisticsService.deleteSdMicrowavePeriodicStatisticsByStatisticsIds(statisticsIds));
    }

    /**
     * 获取最新车流量信息
     *
     * @param statistics
     * @return
     */
    @ApiOperation("获取最新车流量信息")
    @GetMapping("/getStatisticsNewList")
    public AjaxResult getStatisticsNewList(SdMicrowavePeriodicStatistics statistics){
        return AjaxResult.success(sdMicrowavePeriodicStatisticsService.getStatisticsNewList(statistics));
    }

    /**
     * 查询24小时之内车流量信息
     *
     * @param statistics
     * @return
     */
    @ApiOperation("查询24小时之内车流量信息")
    @GetMapping("/getStatisticsRealList")
    public AjaxResult getStatisticsRealList(SdMicrowavePeriodicStatistics statistics){
        return AjaxResult.success(sdMicrowavePeriodicStatisticsService.getStatisticsRealList(statistics));
    }
}
