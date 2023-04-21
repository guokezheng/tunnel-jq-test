package com.tunnel.platform.controller.enhancedLighting;

import java.util.List;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.service.enhancedLighting.ISdEnhancedLightingConfigService;
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
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 【加强照明配置】Controller
 * 
 * @author ruoyi
 * @date 2023-03-27
 */
@RestController
@RequestMapping("/enhancedLighting/config")
public class SdEnhancedLightingConfigController extends BaseController
{
    @Autowired
    private ISdEnhancedLightingConfigService sdEnhancedLightingConfigService;

    /**
     * 查询【加强照明配置】列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        startPage();
        List<SdEnhancedLightingConfig> list = sdEnhancedLightingConfigService.selectSdEnhancedLightingConfigList(sdEnhancedLightingConfig);
        return getDataTable(list);
    }

    /**
     * 导出【加强照明配置】列表
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        List<SdEnhancedLightingConfig> list = sdEnhancedLightingConfigService.selectSdEnhancedLightingConfigList(sdEnhancedLightingConfig);
        ExcelUtil<SdEnhancedLightingConfig> util = new ExcelUtil<SdEnhancedLightingConfig>(SdEnhancedLightingConfig.class);
        return util.exportExcel(list, "【加强照明配置】数据");
    }

    /**
     * 获取【加强照明配置】详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEnhancedLightingConfigService.selectSdEnhancedLightingConfigById(id));
    }

    /**
     * 新增【加强照明配置】
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        return toAjax(sdEnhancedLightingConfigService.insertSdEnhancedLightingConfig(sdEnhancedLightingConfig));
    }

    /**
     * 修改【加强照明配置】
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        return toAjax(sdEnhancedLightingConfigService.updateSdEnhancedLightingConfig(sdEnhancedLightingConfig));
    }

    /**
     * 删除【加强照明配置】
     */
    @Log(title = "【加强照明配置】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEnhancedLightingConfigService.deleteSdEnhancedLightingConfigByIds(ids));
    }
}
