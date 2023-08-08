package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.system.domain.SdAppVersion;
import com.ruoyi.system.service.ISdAppVersionService;
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
 * app版本管理Controller
 * 
 * @author guoz
 * @date 2023-08-08
 */
@RestController
@RequestMapping("/system/appVersion")
public class SdAppVersionController extends BaseController
{
    @Autowired
    private ISdAppVersionService sdAppVersionService;

    /**
     * 查询app版本管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdAppVersion sdAppVersion)
    {
        startPage();
        List<SdAppVersion> list = sdAppVersionService.selectSdAppVersionList(sdAppVersion);
        return getDataTable(list);
    }

    /**
     * 导出app版本管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:export')")
    @Log(title = "app版本管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdAppVersion sdAppVersion)
    {
        List<SdAppVersion> list = sdAppVersionService.selectSdAppVersionList(sdAppVersion);
        ExcelUtil<SdAppVersion> util = new ExcelUtil<SdAppVersion>(SdAppVersion.class);
        return util.exportExcel(list, "app版本管理数据");
    }

    /**
     * 获取app版本管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdAppVersionService.selectSdAppVersionById(id));
    }

    /**
     * 新增app版本管理
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:add')")
    @Log(title = "app版本管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdAppVersion sdAppVersion)
    {
        return toAjax(sdAppVersionService.insertSdAppVersion(sdAppVersion));
    }

    /**
     * 修改app版本管理
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:edit')")
    @Log(title = "app版本管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdAppVersion sdAppVersion)
    {
        return toAjax(sdAppVersionService.updateSdAppVersion(sdAppVersion));
    }

    /**
     * 删除app版本管理
     */
    @PreAuthorize("@ss.hasPermi('system:appVersion:remove')")
    @Log(title = "app版本管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdAppVersionService.deleteSdAppVersionByIds(ids));
    }
}
