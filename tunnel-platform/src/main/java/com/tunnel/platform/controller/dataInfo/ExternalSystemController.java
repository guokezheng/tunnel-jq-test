package com.tunnel.platform.controller.dataInfo;

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
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 外部系统Controller
 */
@RestController
@RequestMapping("/system/system")
public class ExternalSystemController extends BaseController
{
    @Autowired
    private IExternalSystemService externalSystemService;

    /**
     * 查询外部系统列表
     */
    @PreAuthorize("@ss.hasPermi('system:system:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExternalSystem externalSystem)
    {
        startPage();
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(externalSystem);
        return getDataTable(list);
    }

    /**
     * 导出外部系统列表
     */
    @PreAuthorize("@ss.hasPermi('system:system:export')")
    @Log(title = "外部系统", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ExternalSystem externalSystem)
    {
        List<ExternalSystem> list = externalSystemService.selectExternalSystemList(externalSystem);
        ExcelUtil<ExternalSystem> util = new ExcelUtil<ExternalSystem>(ExternalSystem.class);
        return util.exportExcel(list, "外部系统数据");
    }

    /**
     * 获取外部系统详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:system:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(externalSystemService.selectExternalSystemById(id));
    }

    /**
     * 新增外部系统
     */
    @PreAuthorize("@ss.hasPermi('system:system:add')")
    @Log(title = "外部系统", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExternalSystem externalSystem)
    {
        return toAjax(externalSystemService.insertExternalSystem(externalSystem));
    }

    /**
     * 修改外部系统
     */
    @PreAuthorize("@ss.hasPermi('system:system:edit')")
    @Log(title = "外部系统", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExternalSystem externalSystem)
    {
        return toAjax(externalSystemService.updateExternalSystem(externalSystem));
    }

    /**
     * 删除外部系统
     */
    @PreAuthorize("@ss.hasPermi('system:system:remove')")
    @Log(title = "外部系统", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(externalSystemService.deleteExternalSystemByIds(ids));
    }
}
