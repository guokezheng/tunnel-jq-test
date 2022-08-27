package com.tunnel.platform.controller.event;

import com.tunnel.platform.domain.event.SdStrategyBack;
import com.tunnel.platform.service.event.ISdStrategyBackService;
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
 * 策略还原Controller
 * 
 * @author ruoyi
 * @date 2021-06-08
 */
@RestController
@RequestMapping("/system/SdStrategyBack")
public class SdStrategyBackController extends BaseController
{
    @Autowired
    private ISdStrategyBackService sdStrategyBackService;

    /**
     * 查询策略还原列表
     */
    @PreAuthorize("@ss.hasPermi('system:SdStrategyBack:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdStrategyBack SdStrategyBack)
    {
        startPage();
        List<SdStrategyBack> list = sdStrategyBackService.selectSdStrategyBackList(SdStrategyBack);
        return getDataTable(list);
    }

    /**
     * 导出策略还原列表
     */
    @PreAuthorize("@ss.hasPermi('system:SdStrategyBack:export')")
    @Log(title = "策略还原", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdStrategyBack SdStrategyBack)
    {
        List<SdStrategyBack> list = sdStrategyBackService.selectSdStrategyBackList(SdStrategyBack);
        ExcelUtil<SdStrategyBack> util = new ExcelUtil<SdStrategyBack>(SdStrategyBack.class);
        return util.exportExcel(list, "策略还原");
    }

    /**
     * 获取策略还原详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:SdStrategyBack:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdStrategyBackService.selectSdStrategyBackById(id));
    }

    /**
     * 新增策略还原
     */
    @PreAuthorize("@ss.hasPermi('system:SdStrategyBack:add')")
    @Log(title = "策略还原", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdStrategyBack SdStrategyBack)
    {
        return toAjax(sdStrategyBackService.insertSdStrategyBack(SdStrategyBack));
    }

    /**
     * 修改策略还原
     */
    @PreAuthorize("@ss.hasPermi('system:SdStrategyBack:edit')")
    @Log(title = "策略还原", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdStrategyBack SdStrategyBack)
    {
        return toAjax(sdStrategyBackService.updateSdStrategyBack(SdStrategyBack));
    }

    /**
     * 删除策略还原
     */
    @PreAuthorize("@ss.hasPermi('system:SdStrategyBack:remove')")
    @Log(title = "策略还原", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdStrategyBackService.deleteSdStrategyBackByIds(ids));
    }
}
