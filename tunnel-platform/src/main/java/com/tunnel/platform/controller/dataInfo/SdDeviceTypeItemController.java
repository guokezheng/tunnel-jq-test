package com.tunnel.platform.controller.dataInfo;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.service.dataInfo.ISdDeviceTypeItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备类型数据项Controller
 * 
 * @author ruoyi
 * @date 2022-09-01
 */
@RestController
@RequestMapping("/eqType/item")
@Api(tags = "设备类型数据项")
public class SdDeviceTypeItemController extends BaseController
{
    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;


    /**
     * 查询设备类型数据项列表
     */
    @GetMapping("/allList")
    @ApiOperation("查询设备类型数据项列表")
    public AjaxResult allList(SdDeviceTypeItem sdDeviceTypeItem)
    {
        return AjaxResult.success(sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem));
    }

    /**
     * 查询设备类型数据项列表
     */
 /*   @PreAuthorize("@ss.hasPermi('eqType:item:list')")*/
    @GetMapping("/list")
    @ApiOperation("查询设备类型数据项列表")
    public TableDataInfo list(SdDeviceTypeItem sdDeviceTypeItem)
    {
        startPage();
        List<SdDeviceTypeItem> list = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        return getDataTable(list);
    }

    /**
     * 导出设备类型数据项列表
     */
    @PreAuthorize("@ss.hasPermi('eqType:item:export')")
    @Log(title = "设备类型数据项", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出设备类型数据项列表")
    public AjaxResult export(SdDeviceTypeItem sdDeviceTypeItem)
    {
        List<SdDeviceTypeItem> list = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        ExcelUtil<SdDeviceTypeItem> util = new ExcelUtil<SdDeviceTypeItem>(SdDeviceTypeItem.class);
        return util.exportExcel(list, "设备类型数据项");
    }

    /**
     * 获取设备类型数据项详细信息
     */
    @PreAuthorize("@ss.hasPermi('eqType:item:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取设备类型数据项详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdDeviceTypeItemService.selectSdDeviceTypeItemById(id));
    }

    /**
     * 新增设备类型数据项
     */
    @PreAuthorize("@ss.hasPermi('eqType:item:add')")
    @Log(title = "设备类型数据项", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增设备类型数据项")
    public AjaxResult add(@RequestBody SdDeviceTypeItem sdDeviceTypeItem)
    {
        return toAjax(sdDeviceTypeItemService.insertSdDeviceTypeItem(sdDeviceTypeItem));
    }

    /**
     * 修改设备类型数据项
     */
    @PreAuthorize("@ss.hasPermi('eqType:item:edit')")
    @Log(title = "设备类型数据项", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("设备类型数据项")
    public AjaxResult edit(@RequestBody SdDeviceTypeItem sdDeviceTypeItem)
    {
        return toAjax(sdDeviceTypeItemService.updateSdDeviceTypeItem(sdDeviceTypeItem));
    }

    /**
     * 删除设备类型数据项
     */
    @PreAuthorize("@ss.hasPermi('eqType:item:remove')")
    @Log(title = "设备类型数据项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除设备类型数据项")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdDeviceTypeItemService.deleteSdDeviceTypeItemByIds(ids));
    }
}
