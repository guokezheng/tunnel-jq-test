package com.tunnel.platform.controller.dataInfo;

import java.util.List;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.tunnel.business.domain.dataInfo.SdMicrowaveRealData;
import com.tunnel.business.service.dataInfo.ISdMicrowaveRealDataService;
import io.swagger.annotations.Api;
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
 * 微波车检单车实时数据Controller
 * 
 * @author ruoyi
 * @date 2022-12-07
 */
@RestController
@RequestMapping("/microwave")
@Api(tags = "微波车检单车实时数据")
@ApiSupport(order = 16)
public class SdMicrowaveRealDataController extends BaseController
{
    @Autowired
    private ISdMicrowaveRealDataService sdMicrowaveRealDataService;

    /**
     * 查询微波车检单车实时数据列表
     */
    @ApiOperation("查询微波车检单车实时数据列表")
    @PreAuthorize("@ss.hasPermi('microwave:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdMicrowaveRealData sdMicrowaveRealData)
    {
        startPage();
        List<SdMicrowaveRealData> list = sdMicrowaveRealDataService.selectSdMicrowaveRealDataList(sdMicrowaveRealData);
        return getDataTable(list);
    }

    /**
     * 导出微波车检单车实时数据列表
     */
    @ApiOperation("导出微波车检单车实时数据列表")
    @PreAuthorize("@ss.hasPermi('microwave:data:export')")
    @Log(title = "微波车检单车实时数据", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdMicrowaveRealData sdMicrowaveRealData)
    {
        List<SdMicrowaveRealData> list = sdMicrowaveRealDataService.selectSdMicrowaveRealDataList(sdMicrowaveRealData);
        ExcelUtil<SdMicrowaveRealData> util = new ExcelUtil<SdMicrowaveRealData>(SdMicrowaveRealData.class);
        return util.exportExcel(list, "微波车检单车实时数据数据");
    }

    /**
     * 获取微波车检单车实时数据详细信息
     */
    @ApiOperation("获取微波车检单车实时数据详细信息")
    @PreAuthorize("@ss.hasPermi('microwave:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdMicrowaveRealDataService.selectSdMicrowaveRealDataById(id));
    }

    /**
     * 新增微波车检单车实时数据
     */
    @ApiOperation("新增微波车检单车实时数据")
    @PreAuthorize("@ss.hasPermi('microwave:data:add')")
    @Log(title = "微波车检单车实时数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdMicrowaveRealData sdMicrowaveRealData)
    {
        return toAjax(sdMicrowaveRealDataService.insertSdMicrowaveRealData(sdMicrowaveRealData));
    }

    /**
     * 修改微波车检单车实时数据
     */
    @ApiOperation("修改微波车检单车实时数据")
    @PreAuthorize("@ss.hasPermi('microwave:data:edit')")
    @Log(title = "微波车检单车实时数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdMicrowaveRealData sdMicrowaveRealData)
    {
        return toAjax(sdMicrowaveRealDataService.updateSdMicrowaveRealData(sdMicrowaveRealData));
    }

    /**
     * 删除微波车检单车实时数据
     */
    @ApiOperation("删除微波车检单车实时数据")
    @PreAuthorize("@ss.hasPermi('system:data:remove')")
    @Log(title = "微波车检单车实时数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdMicrowaveRealDataService.deleteSdMicrowaveRealDataByIds(ids));
    }
}
