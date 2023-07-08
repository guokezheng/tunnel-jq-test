package com.tunnel.platform.controller.config;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.config.SdConfig;
import com.tunnel.business.service.config.ISdConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;

import java.util.List;

/**
 * 数字孪生页面配置Controller
 * 
 * @author ruoyi
 * @date 2023-04-10
 */
@RestController
@RequestMapping("/config")
public class SdConfigController extends BaseController
{
    @Autowired
    private ISdConfigService sdConfigService;


    /**
     * 查询数字孪生页面配置列表
     */
    @GetMapping("/configPage")
    public AjaxResult deptList(SdConfig sdConfig)
    {

        if (sdConfig.getDeptId() == null || sdConfig.getDeptId().isEmpty()) {
            throw new RuntimeException("部门信息为空");
        }

        return AjaxResult.success(sdConfigService.selectSdConfigByDeptId(sdConfig));
    }


    /**
     * 查询数字孪生页面配置列表
     */
    @PreAuthorize("@ss.hasPermi('config:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdConfig sdConfig)
    {
        startPage();
        List<SdConfig> list = sdConfigService.selectSdConfigList(sdConfig);
        return getDataTable(list);
    }

    /**
     * 导出数字孪生页面配置列表
     */
    @PreAuthorize("@ss.hasPermi('config:config:export')")
    @Log(title = "数字孪生页面配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdConfig sdConfig)
    {
        List<SdConfig> list = sdConfigService.selectSdConfigList(sdConfig);
        ExcelUtil<SdConfig> util = new ExcelUtil<SdConfig>(SdConfig.class);
        return util.exportExcel(list, "数字孪生页面配置数据");
    }

    /**
     * 获取数字孪生页面配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('config:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdConfigService.selectSdConfigById(id));
    }

    /**
     * 新增数字孪生页面配置
     */
    @PreAuthorize("@ss.hasPermi('config:config:add')")
    @Log(title = "数字孪生页面配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdConfig sdConfig)
    {
        return toAjax(sdConfigService.insertSdConfig(sdConfig));
    }

    /**
     * 修改数字孪生页面配置
     */
    @PreAuthorize("@ss.hasPermi('config:config:edit')")
    @Log(title = "数字孪生页面配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdConfig sdConfig)
    {
        return toAjax(sdConfigService.updateSdConfig(sdConfig));
    }

    /**
     * 删除数字孪生页面配置
     */
    @PreAuthorize("@ss.hasPermi('config:config:remove')")
    @Log(title = "数字孪生页面配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdConfigService.deleteSdConfigByIds(ids));
    }
}
