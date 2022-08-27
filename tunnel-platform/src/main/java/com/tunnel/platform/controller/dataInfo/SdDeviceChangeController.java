package com.tunnel.platform.controller.dataInfo;

import java.util.List;

import com.ruoyi.common.core.page.Result;
import com.tunnel.platform.domain.dataInfo.SdDeviceChange;
import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.service.dataInfo.ISdDeviceChangeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
 * 设备变更Controller
 * 
 * @author 刘方堃
 * @date 2021-12-09
 */
@RestController
@RequestMapping("/system/change")
@Api(tags = "设备变更")
public class SdDeviceChangeController extends BaseController
{
    @Autowired
    private ISdDeviceChangeService sdDeviceChangeService;

    /**
     * 查询设备变更列表
     */
    @PreAuthorize("@ss.hasPermi('system:change:list')")
    @GetMapping("/list")
    @ApiOperation("查询设备变更列表")
    public TableDataInfo<List<SdDeviceChange>> list(SdDeviceChange sdDeviceChange)
    {
        startPage();
        List<SdDeviceChange> list = sdDeviceChangeService.selectSdDeviceChangeList(sdDeviceChange);
        return getDataTable(list);
    }

    /**
     * 导出设备变更列表
     */
    @PreAuthorize("@ss.hasPermi('system:change:export')")
    @Log(title = "设备变更", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDeviceChange sdDeviceChange)
    {
        List<SdDeviceChange> list = sdDeviceChangeService.selectSdDeviceChangeList(sdDeviceChange);
        ExcelUtil<SdDeviceChange> util = new ExcelUtil<SdDeviceChange>(SdDeviceChange.class);
        return util.exportExcel(list, "设备变更数据");
    }

    /**
     * 获取设备变更详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:change:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取设备变更详细信息")
    @ApiImplicitParam(name = "id", value = "设备变更主键", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result<SdDeviceChange> getInfo(@PathVariable("id") Long id)
    {
        return Result.success(sdDeviceChangeService.selectSdDeviceChangeById(id));
    }

    /**
     * 新增设备变更
     */
    @PreAuthorize("@ss.hasPermi('system:change:add')")
    @Log(title = "设备变更", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增设备变更")
    public Result add(@RequestBody SdDeviceChange sdDeviceChange)
    {
        return Result.toResult(sdDeviceChangeService.insertSdDeviceChange(sdDeviceChange));
    }

    /**
     * 修改设备变更
     */
    @PreAuthorize("@ss.hasPermi('system:change:edit')")
    @Log(title = "设备变更", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改设备变更")
    public Result edit(@RequestBody SdDeviceChange sdDeviceChange)
    {
        return Result.toResult(sdDeviceChangeService.updateSdDeviceChange(sdDeviceChange));
    }

    /**
     * 删除设备变更
     */
    @PreAuthorize("@ss.hasPermi('system:change:remove')")
    @Log(title = "设备变更", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除设备变更")
    @ApiImplicitParam(name = "ids", value = "需要删除的设备变更主键集合", required = true, dataType = "Long", paramType = "path",dataTypeClass = Long.class)
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(sdDeviceChangeService.deleteSdDeviceChangeByIds(ids));
    }
}
