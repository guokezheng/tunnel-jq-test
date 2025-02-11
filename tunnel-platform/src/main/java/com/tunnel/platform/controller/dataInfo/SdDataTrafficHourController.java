package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdDataTrafficHour;
import com.tunnel.business.service.dataInfo.ISdDataTrafficHourService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * 各路段小时车流量统计Controller
 *
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/trafficFlowData/hour")
@Api(tags = "各路段小时车流量统计")
@ApiSupport(order = 16)
public class SdDataTrafficHourController extends BaseController
{
    @Autowired
    private ISdDataTrafficHourService sdDataTrafficHourService;

    /**
     * 查询各路段小时车流量统计列表
     */
    @ApiOperation("查询各路段小时车流量统计列表")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:hour:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdDataTrafficHour sdDataTrafficHour)
    {
        startPage();
        List<SdDataTrafficHour> list = sdDataTrafficHourService.selectSdDataTrafficHourList(sdDataTrafficHour);
        return getDataTable(list);
    }

    /**
     * 导出各路段小时车流量统计列表
     */
    @ApiOperation("导出各路段小时车流量统计列表")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:hour:export')")
    @Log(title = "各路段小时车流量统计", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDataTrafficHour sdDataTrafficHour)
    {
        List<SdDataTrafficHour> list = sdDataTrafficHourService.selectSdDataTrafficHourList(sdDataTrafficHour);
        ExcelUtil<SdDataTrafficHour> util = new ExcelUtil<SdDataTrafficHour>(SdDataTrafficHour.class);
        return util.exportExcel(list, "各路段小时车流量统计数据");
    }

    /**
     * 获取各路段小时车流量统计详细信息
     */
    @ApiOperation("获取各路段小时车流量统计详细信息")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:hour:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDataTrafficHourService.selectSdDataTrafficHourById(id));
    }

    /**
     * 新增各路段小时车流量统计
     */
    @ApiOperation("新增各路段小时车流量统计")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:hour:add')")
    @Log(title = "各路段小时车流量统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDataTrafficHour sdDataTrafficHour)
    {
        return toAjax(sdDataTrafficHourService.insertSdDataTrafficHour(sdDataTrafficHour));
    }

    /**
     * 修改各路段小时车流量统计
     */
    @ApiOperation("修改各路段小时车流量统计")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:hour:edit')")
    @Log(title = "各路段小时车流量统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDataTrafficHour sdDataTrafficHour)
    {
        return toAjax(sdDataTrafficHourService.updateSdDataTrafficHour(sdDataTrafficHour));
    }

    /**
     * 删除各路段小时车流量统计
     */
    @ApiOperation("删除各路段小时车流量统计")
    @PreAuthorize("@ss.hasPermi('trafficFlowData:hour:remove')")
    @Log(title = "各路段小时车流量统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDataTrafficHourService.deleteSdDataTrafficHourByIds(ids));
    }

    /**
     * 按小时、隧道查每小时每个车型的数量
     */
    @ApiOperation("按小时、隧道查每小时每个车型的数量")
    @PostMapping("/getCarNumberByHour")
    public AjaxResult getCarNumberByHour(@RequestBody SdDataTrafficHour sdDataTrafficHour) throws ParseException {
        return AjaxResult.success(sdDataTrafficHourService.getCarNumberByHour(sdDataTrafficHour));
    }

    /**
     * 查询redis缓存中的实时车流量信息
     * */
    @ApiOperation("查询redis缓存中的实时车流量信息")
    @PostMapping("/obtainNowTrafficFlowInformation")
    public AjaxResult obtainNowTrafficFlowInformation() {
        return AjaxResult.success(sdDataTrafficHourService.getCacheTrafficFlowInformation());
    }
}
