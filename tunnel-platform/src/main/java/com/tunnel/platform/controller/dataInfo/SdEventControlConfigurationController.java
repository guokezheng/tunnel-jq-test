package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdEventControlConfiguration;
import com.tunnel.business.service.dataInfo.ISdEventControlConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 事件管控类型配置Controller
 * 
 * @author ruoyi
 * @date 2022-02-18
 */
@RestController
@RequestMapping("/eventControl/configuration")
@Api(tags = "事件管控类型配置")
@ApiSupport(order = 16)
public class SdEventControlConfigurationController extends BaseController
{
    @Autowired
    private ISdEventControlConfigurationService sdEventControlConfigurationService;

    /**
     * 查询事件管控类型配置列表
     */
    @ApiOperation("查询事件管控类型配置列表")
    @PreAuthorize("@ss.hasPermi('eventControl:configuration:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdEventControlConfiguration sdEventControlConfiguration)
    {
        startPage();
        List<SdEventControlConfiguration> list = sdEventControlConfigurationService.selectSdEventControlConfigurationList(sdEventControlConfiguration);
        return getDataTable(list);
    }

    /**
     * 导出事件管控类型配置列表
     */
    @ApiOperation("导出事件管控类型配置列表")
    @PreAuthorize("@ss.hasPermi('eventControl:configuration:export')")
    @Log(title = "事件管控类型配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEventControlConfiguration sdEventControlConfiguration)
    {
        List<SdEventControlConfiguration> list = sdEventControlConfigurationService.selectSdEventControlConfigurationList(sdEventControlConfiguration);
        ExcelUtil<SdEventControlConfiguration> util = new ExcelUtil<SdEventControlConfiguration>(SdEventControlConfiguration.class);
        return util.exportExcel(list, "事件管控类型配置数据");
    }

    /**
     * 获取事件管控类型配置详细信息
     */
    @ApiOperation("获取事件管控类型配置详细信息")
    @PreAuthorize("@ss.hasPermi('eventControl:configuration:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEventControlConfigurationService.selectSdEventControlConfigurationById(id));
    }

    /**
     * 新增事件管控类型配置
     */
    @ApiOperation("新增事件管控类型配置")
    @PreAuthorize("@ss.hasPermi('eventControl:configuration:add')")
    @Log(title = "事件管控类型配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEventControlConfiguration sdEventControlConfiguration)
    {
        return toAjax(sdEventControlConfigurationService.insertSdEventControlConfiguration(sdEventControlConfiguration));
    }

    /**
     * 修改事件管控类型配置
     */
    @ApiOperation("修改事件管控类型配置")
    @PreAuthorize("@ss.hasPermi('eventControl:configuration:edit')")
    @Log(title = "事件管控类型配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEventControlConfiguration sdEventControlConfiguration)
    {
        return toAjax(sdEventControlConfigurationService.updateSdEventControlConfiguration(sdEventControlConfiguration));
    }

    /**
     * 删除事件管控类型配置
     */
    @ApiOperation("删除事件管控类型配置")
    @PreAuthorize("@ss.hasPermi('eventControl:configuration:remove')")
    @Log(title = "事件管控类型配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEventControlConfigurationService.deleteSdEventControlConfigurationByIds(ids));
    }
}
