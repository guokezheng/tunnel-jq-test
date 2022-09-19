package com.tunnel.platform.controller.intelligent;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.intelligent.SdWeatherReport;
import com.tunnel.business.service.intelligent.ISdWeatherReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 气象采集器信息Controller
 *
 * @author ruoyi
 * @date 2021-11-22
 */
@RestController
@RequestMapping("/intelligent/report")
public class SdWeatherReportController extends BaseController
{
    @Autowired
    private ISdWeatherReportService sdWeatherReportService;

    /**
     * 查询气象采集器信息列表
     */
    @PreAuthorize("@ss.hasPermi('intelligent:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdWeatherReport sdWeatherReport)
    {
        startPage();
        List<SdWeatherReport> list = sdWeatherReportService.selectSdWeatherReportList(sdWeatherReport);
        return getDataTable(list);
    }

    /**
     * 导出气象采集器信息列表
     */
    @PreAuthorize("@ss.hasPermi('intelligent:report:export')")
    @Log(title = "气象采集器信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdWeatherReport sdWeatherReport)
    {
        List<SdWeatherReport> list = sdWeatherReportService.selectSdWeatherReportList(sdWeatherReport);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0;i < list.size();i++) {
            SdWeatherReport weatherReport = list.get(i);
            String format = dateFormat.format(weatherReport.getCreateTime());
            weatherReport.setCollectionTime(format);
        }
        ExcelUtil<SdWeatherReport> util = new ExcelUtil<SdWeatherReport>(SdWeatherReport.class);
        return util.exportExcel(list, "气象采集器信息");
    }

    /**
     * 获取气象采集器信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('intelligent:report:query')")
    @GetMapping(value = "/{reportId}")
    public AjaxResult getInfo(@PathVariable("reportId") Long reportId)
    {
        return AjaxResult.success(sdWeatherReportService.selectSdWeatherReportById(reportId));
    }

    /**
     * 新增气象采集器信息
     */
    @PreAuthorize("@ss.hasPermi('intelligent:report:add')")
    @Log(title = "气象采集器信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdWeatherReport sdWeatherReport)
    {
        return toAjax(sdWeatherReportService.insertSdWeatherReport(sdWeatherReport));
    }

    /**
     * 修改气象采集器信息
     */
    @PreAuthorize("@ss.hasPermi('intelligent:report:edit')")
    @Log(title = "气象采集器信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdWeatherReport sdWeatherReport)
    {
        return toAjax(sdWeatherReportService.updateSdWeatherReport(sdWeatherReport));
    }

    /**
     * 删除气象采集器信息
     */
    @PreAuthorize("@ss.hasPermi('intelligent:report:remove')")
    @Log(title = "气象采集器信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reportIds}")
    public AjaxResult remove(@PathVariable Long[] reportIds)
    {
        return toAjax(sdWeatherReportService.deleteSdWeatherReportByIds(reportIds));
    }
}
