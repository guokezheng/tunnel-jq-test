package com.tunnel.platform.controller.informationBoard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.informationBoard.SdEventOperating;
import com.tunnel.business.service.informationBoard.ISdEventOperatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 交通事件Controller
 * 
 * @author 刘方堃
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/system/operating")
public class SdEventOperatingController extends BaseController
{
    @Autowired
    private ISdEventOperatingService sdEventOperatingService;

    /**
     * 查询交通事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:operating:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEventOperating sdEventOperating)
    {
        startPage();
        List<SdEventOperating> list = sdEventOperatingService.selectSdEventOperatingList(sdEventOperating);
        return getDataTable(list);
    }

    /**
     * 导出交通事件列表
     */
    @PreAuthorize("@ss.hasPermi('system:operating:export')")
    @Log(title = "交通事件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEventOperating sdEventOperating)
    {
        List<SdEventOperating> list = sdEventOperatingService.selectSdEventOperatingList(sdEventOperating);
        ExcelUtil<SdEventOperating> util = new ExcelUtil<SdEventOperating>(SdEventOperating.class);
        return util.exportExcel(list, "交通事件列表");
    }

    /**
     * 获取交通事件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:operating:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEventOperatingService.selectSdEventOperatingById(id));
    }

    /**
     * 新增交通事件
     */
    @PreAuthorize("@ss.hasPermi('system:operating:add')")
    @Log(title = "交通事件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEventOperating sdEventOperating)
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdEventOperating.setCreatTime(formatter.format(currentTime));
        return toAjax(sdEventOperatingService.insertSdEventOperating(sdEventOperating));
    }

    /**
     * 修改交通事件
     */
    @PreAuthorize("@ss.hasPermi('system:operating:edit')")
    @Log(title = "交通事件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEventOperating sdEventOperating)
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdEventOperating.setUpdataTime(formatter.format(currentTime));
        return toAjax(sdEventOperatingService.updateSdEventOperating(sdEventOperating));
    }

    /**
     * 删除交通事件
     */
    @PreAuthorize("@ss.hasPermi('system:operating:remove')")
    @Log(title = "交通事件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEventOperatingService.deleteSdEventOperatingByIds(ids));
    }
}
