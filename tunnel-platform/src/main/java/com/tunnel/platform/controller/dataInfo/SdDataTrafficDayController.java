package com.tunnel.platform.controller.dataInfo;

import java.text.ParseException;
import java.util.List;

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
import com.tunnel.platform.domain.dataInfo.SdDataTrafficDay;
import com.tunnel.platform.service.dataInfo.ISdDataTrafficDayService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 各路段日车流量统计Controller
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/trafficFlowData/day")
public class SdDataTrafficDayController extends BaseController
{
    @Autowired
    private ISdDataTrafficDayService sdDataTrafficDayService;

    /**
     * 查询各路段日车流量统计列表
     */
    @PreAuthorize("@ss.hasPermi('trafficFlowData:day:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdDataTrafficDay sdDataTrafficDay)
    {
        startPage();
        List<SdDataTrafficDay> list = sdDataTrafficDayService.selectSdDataTrafficDayList(sdDataTrafficDay);
        return getDataTable(list);
    }

    /**
     * 导出各路段日车流量统计列表
     */
    @PreAuthorize("@ss.hasPermi('trafficFlowData:day:export')")
    @Log(title = "各路段日车流量统计", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDataTrafficDay sdDataTrafficDay)
    {
        List<SdDataTrafficDay> list = sdDataTrafficDayService.selectSdDataTrafficDayList(sdDataTrafficDay);
        ExcelUtil<SdDataTrafficDay> util = new ExcelUtil<SdDataTrafficDay>(SdDataTrafficDay.class);
        return util.exportExcel(list, "各路段日车流量统计数据");
    }

    /**
     * 获取各路段日车流量统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('trafficFlowData:day:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDataTrafficDayService.selectSdDataTrafficDayById(id));
    }

    /**
     * 新增各路段日车流量统计
     */
    @PreAuthorize("@ss.hasPermi('trafficFlowData:day:add')")
    @Log(title = "各路段日车流量统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDataTrafficDay sdDataTrafficDay)
    {
        return toAjax(sdDataTrafficDayService.insertSdDataTrafficDay(sdDataTrafficDay));
    }

    /**
     * 修改各路段日车流量统计
     */
    @PreAuthorize("@ss.hasPermi('trafficFlowData:day:edit')")
    @Log(title = "各路段日车流量统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDataTrafficDay sdDataTrafficDay)
    {
        return toAjax(sdDataTrafficDayService.updateSdDataTrafficDay(sdDataTrafficDay));
    }

    /**
     * 删除各路段日车流量统计
     */
    @PreAuthorize("@ss.hasPermi('trafficFlowData:day:remove')")
    @Log(title = "各路段日车流量统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDataTrafficDayService.deleteSdDataTrafficDayByIds(ids));
    }

    /**
     * 按日、隧道查每日每个车型的数量
     */
    @PostMapping("/getCarNumberByDay")
    public AjaxResult getCarNumberByDay(@RequestBody SdDataTrafficDay sdDataTrafficDay) throws ParseException {
        return AjaxResult.success(sdDataTrafficDayService.getCarNumberByDay(sdDataTrafficDay));
    }

    /**
     * 今日各隧道车流量
     */
    @GetMapping("/getCarFlowNumberOfTodayGroupByTunnel")
    public AjaxResult getCarFlowNumberOfTodayGroupByTunnel() {
        return AjaxResult.success(sdDataTrafficDayService.getCarFlowNumberOfTodayGroupByTunnel());
    }

    /**
     * 今日车型占比
     */
    @GetMapping ("/getCarTypeRatioOfToday")
    public AjaxResult getCarTypeOfToday(SdDataTrafficDay sdDataTrafficDay) {
        return AjaxResult.success(sdDataTrafficDayService.getCarTypeOfToday(sdDataTrafficDay));
    }
}
