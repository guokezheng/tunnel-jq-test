package com.tunnel.platform.controller.videoevents;

import com.tunnel.platform.domain.videoevents.SdkEventLane;
import com.tunnel.platform.service.videoevents.ISdkEventLaneService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车道信息Controller
 * 
 * @author ruoyi
 * @date 2021-04-26
 */
@RestController
@RequestMapping("/eventlane")
public class SdkEventLaneController extends BaseController
{
    @Autowired
    private ISdkEventLaneService sdkEventLaneService;

    /**
     * 查询车道信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:eventlane:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdkEventLane sdkEventLane)
    {
        startPage();
        List<SdkEventLane> list = sdkEventLaneService.selectSdkEventLaneList(sdkEventLane);
        return getDataTable(list);
    }

    /**
     * 导出车道信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:eventlane:export')")
    @Log(title = "车道信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdkEventLane sdkEventLane)
    {
        List<SdkEventLane> list = sdkEventLaneService.selectSdkEventLaneList(sdkEventLane);
        ExcelUtil<SdkEventLane> util = new ExcelUtil<SdkEventLane>(SdkEventLane.class);
        return util.exportExcel(list, "eventlane");
    }

    /**
     * 获取车道信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:eventlane:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sdkEventLaneService.selectSdkEventLaneById(id));
    }
    @GetMapping(value = "/getEventlaneByTaskId/{id}")
    public AjaxResult getInfoByTaskId(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(sdkEventLaneService.selectSdkEventLaneByTaskId(id));
    }

    /**
     * 新增车道信息
     */
    @PreAuthorize("@ss.hasPermi('system:eventlane:add')")
    @Log(title = "车道信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdkEventLane sdkEventLane)
    {
        return toAjax(sdkEventLaneService.insertSdkEventLane(sdkEventLane));
    }

    /**
     * 修改车道信息
     */
    @PreAuthorize("@ss.hasPermi('system:eventlane:edit')")
    @Log(title = "车道信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdkEventLane sdkEventLane)
    {
        return toAjax(sdkEventLaneService.updateSdkEventLane(sdkEventLane));
    }

    /**
     * 删除车道信息
     */
    @PreAuthorize("@ss.hasPermi('system:eventlane:remove')")
    @Log(title = "车道信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(sdkEventLaneService.deleteSdkEventLaneByIds(ids));
    }
}
