package com.tunnel.platform.controller.emeResource;

import java.util.List;

import com.tunnel.platform.domain.emeResource.SdEmergencyOrg;
import com.tunnel.platform.service.emeResource.ISdEmergencyOrgService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 应急机构Controller
 * 
 * @author ruoyi
 * @date 2022-08-09
 */
@RestController
@RequestMapping("/system/org")
public class SdEmergencyOrgController extends BaseController
{
    @Autowired
    private ISdEmergencyOrgService sdEmergencyOrgService;

    /**
     * 查询应急机构列表
     */
    @PreAuthorize("@ss.hasPermi('system:org:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEmergencyOrg sdEmergencyOrg)
    {
        startPage();
        List<SdEmergencyOrg> list = sdEmergencyOrgService.selectSdEmergencyOrgList(sdEmergencyOrg);
        return getDataTable(list);
    }

    /**
     * 导出应急机构列表
     */
    @PreAuthorize("@ss.hasPermi('system:org:export')")
    @Log(title = "应急机构", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEmergencyOrg sdEmergencyOrg)
    {
        List<SdEmergencyOrg> list = sdEmergencyOrgService.selectSdEmergencyOrgList(sdEmergencyOrg);
        ExcelUtil<SdEmergencyOrg> util = new ExcelUtil<SdEmergencyOrg>(SdEmergencyOrg.class);
        return util.exportExcel(list, "应急机构数据");
    }

    /**
     * 获取应急机构详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:org:query')")
    @GetMapping(value = "/{orgId}")
    public AjaxResult getInfo(@PathVariable("orgId") Long orgId)
    {
        return AjaxResult.success(sdEmergencyOrgService.selectSdEmergencyOrgByOrgId(orgId));
    }

    /**
     * 新增应急机构
     */
    @PreAuthorize("@ss.hasPermi('system:org:add')")
    @Log(title = "应急机构", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(SdEmergencyOrg sdEmergencyOrg)
    {
        return toAjax(sdEmergencyOrgService.insertSdEmergencyOrg(sdEmergencyOrg));
    }

    /**
     * 修改应急机构
     */
    @PreAuthorize("@ss.hasPermi('system:org:edit')")
    @Log(title = "应急机构", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEmergencyOrg sdEmergencyOrg)
    {
        return toAjax(sdEmergencyOrgService.updateSdEmergencyOrg(sdEmergencyOrg));
    }

    /**
     * 删除应急机构
     */
    @PreAuthorize("@ss.hasPermi('system:org:remove')")
    @Log(title = "应急机构", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orgIds}")
    public AjaxResult remove(@PathVariable Long[] orgIds)
    {
        return toAjax(sdEmergencyOrgService.deleteSdEmergencyOrgByOrgIds(orgIds));
    }
}
