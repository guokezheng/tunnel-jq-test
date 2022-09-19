package com.tunnel.platform.controller.trafficOperationControl.activeTrafficFlowControl;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.trafficOperationControl.activeTrafficFlowControl.SdTrafficIncidentMeasure;
import com.tunnel.business.service.trafficOperationControl.activeTrafficFlowControl.ISdTrafficIncidentMeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交通事件推送措施Controller
 *
 * @author ruoyi
 * @date 2022-03-01
 */
@RestController
@RequestMapping("/trafficIncident/measure")
public class SdTrafficIncidentMeasureController extends BaseController
{
    @Autowired
    private ISdTrafficIncidentMeasureService sdTrafficIncidentMeasureService;

    /**
     * 查询交通事件推送措施列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:measure:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficIncidentMeasure sdTrafficIncidentMeasure)
    {
        startPage();
        List<SdTrafficIncidentMeasure> list = sdTrafficIncidentMeasureService.selectSdTrafficIncidentMeasureList(sdTrafficIncidentMeasure);
        return getDataTable(list);
    }

    /**
     * 导出交通事件推送措施列表
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:measure:export')")
    @Log(title = "交通事件推送措施", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTrafficIncidentMeasure sdTrafficIncidentMeasure)
    {
        List<SdTrafficIncidentMeasure> list = sdTrafficIncidentMeasureService.selectSdTrafficIncidentMeasureList(sdTrafficIncidentMeasure);
        ExcelUtil<SdTrafficIncidentMeasure> util = new ExcelUtil<SdTrafficIncidentMeasure>(SdTrafficIncidentMeasure.class);
        return util.exportExcel(list, "交通事件推送措施数据");
    }

    /**
     * 获取交通事件推送措施详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:measure:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdTrafficIncidentMeasureService.selectSdTrafficIncidentMeasureById(id));
    }

    /**
     * 新增交通事件推送措施
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:measure:add')")
    @Log(title = "交通事件推送措施", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficIncidentMeasure sdTrafficIncidentMeasure)
    {
        return toAjax(sdTrafficIncidentMeasureService.insertSdTrafficIncidentMeasure(sdTrafficIncidentMeasure));
    }

    /**
     * 修改交通事件推送措施
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:measure:edit')")
    @Log(title = "交通事件推送措施", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficIncidentMeasure sdTrafficIncidentMeasure)
    {
        return toAjax(sdTrafficIncidentMeasureService.updateSdTrafficIncidentMeasure(sdTrafficIncidentMeasure));
    }

    /**
     * 删除交通事件推送措施
     */
    @PreAuthorize("@ss.hasPermi('trafficIncident:measure:remove')")
    @Log(title = "交通事件推送措施", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdTrafficIncidentMeasureService.deleteSdTrafficIncidentMeasureByIds(ids));
    }
}
