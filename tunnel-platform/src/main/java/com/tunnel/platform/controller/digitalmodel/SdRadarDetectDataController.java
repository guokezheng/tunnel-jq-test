package com.tunnel.platform.controller.digitalmodel;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdRadarDetectData;
import com.tunnel.business.service.digitalmodel.ISdRadarDetectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 雷达监测感知数据Controller
 * 
 * @author ruoyi
 * @date 2022-09-04
 */
@RestController
@RequestMapping("/radar/data")
public class SdRadarDetectDataController extends BaseController
{
    @Autowired
    private ISdRadarDetectDataService sdRadarDetectDataService;

    /**
     * 查询雷达监测感知数据列表
     */
    @PreAuthorize("@ss.hasPermi('radar:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdRadarDetectData sdRadarDetectData)
    {
        startPage();
        List<Map<String,String>> list = sdRadarDetectDataService.selectSdRadarDetectDataList(sdRadarDetectData);
        return getDataTable(list);
    }

    /**
     * 导出雷达监测感知数据列表
     */
    @PreAuthorize("@ss.hasPermi('radar:data:export')")
    @Log(title = "雷达监测感知数据", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdRadarDetectData sdRadarDetectData)
    {
        List<Map<String,String>> list = sdRadarDetectDataService.selectSdRadarDetectDataList(sdRadarDetectData);
        ExcelUtil<SdRadarDetectData> util = new ExcelUtil<SdRadarDetectData>(SdRadarDetectData.class);
        return util.exportExcel(null, "雷达监测感知数据数据");
    }

    /**
     * 获取雷达监测感知数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('radar:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sdRadarDetectDataService.selectSdRadarDetectDataById(id));
    }

    /**
     * 新增雷达监测感知数据
     */
    @PreAuthorize("@ss.hasPermi('radar:data:add')")
    @Log(title = "雷达监测感知数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdRadarDetectData sdRadarDetectData)
    {
        return toAjax(sdRadarDetectDataService.insertSdRadarDetectData(sdRadarDetectData));
    }

    /**
     * 修改雷达监测感知数据
     */
    @PreAuthorize("@ss.hasPermi('radar:data:edit')")
    @Log(title = "雷达监测感知数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdRadarDetectData sdRadarDetectData)
    {
        return toAjax(sdRadarDetectDataService.updateSdRadarDetectData(sdRadarDetectData));
    }

    /**
     * 删除雷达监测感知数据
     */
    @PreAuthorize("@ss.hasPermi('radar:data:remove')")
    @Log(title = "雷达监测感知数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sdRadarDetectDataService.deleteSdRadarDetectDataByIds(ids));
    }

    /**
     * 根据隧道id 查询24小时 感知数据
     * @param tunnelId
     * @return
     */
    @GetMapping("/byId")
    public AjaxResult eventById(@RequestParam("tunnelId") String tunnelId)
    {
        return AjaxResult.success(sdRadarDetectDataService.eventById(tunnelId));
    }
}
