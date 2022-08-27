package com.zc.iot.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.zc.iot.domain.IotFunctionValue;
import com.zc.iot.service.IIotFunctionValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能值定义Controller
 * 
 * @author YangChao
 * @date 2021-10-27
 */
@Api(tags = "功能值定义")
@RestController
@RequestMapping("/iot/value")
public class IotFunctionValueController extends BaseController
{
    @Autowired
    private IIotFunctionValueService iotFunctionValueService;

    /**
     * 查询功能值定义列表
     */
    @ApiOperation("查询功能值定义列表")
    @PreAuthorize("@ss.hasPermi('iot:value:list')")
    @GetMapping("/list")
    public TableDataInfo<List<IotFunctionValue>> list(IotFunctionValue iotFunctionValue)
    {
        startPage();
        List<IotFunctionValue> list = iotFunctionValueService.selectIotFunctionValueList(iotFunctionValue);
        return getDataTable(list);
    }

    /**
     * 导出功能值定义列表
     */
    @PreAuthorize("@ss.hasPermi('iot:value:export')")
    @Log(title = "功能值定义", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotFunctionValue iotFunctionValue)
    {
        List<IotFunctionValue> list = iotFunctionValueService.selectIotFunctionValueList(iotFunctionValue);
        ExcelUtil<IotFunctionValue> util = new ExcelUtil<IotFunctionValue>(IotFunctionValue.class);
        return util.exportExcel(list, "功能值定义数据");
    }

    /**
     * 获取功能值定义详细信息
     */
    @ApiOperation("获取功能值定义详细信息")
    @PreAuthorize("@ss.hasPermi('iot:value:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return Result.success(iotFunctionValueService.selectIotFunctionValueById(id));
    }

    /**
     * 新增功能值定义
     */
    @ApiOperation("新增功能值定义")
    @PreAuthorize("@ss.hasPermi('iot:value:add')")
    @Log(title = "功能值定义", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody IotFunctionValue iotFunctionValue)
    {
        return Result.toResult(iotFunctionValueService.insertIotFunctionValue(iotFunctionValue));
    }

    /**
     * 修改功能值定义
     */
    @ApiOperation("修改功能值定义")
    @PreAuthorize("@ss.hasPermi('iot:value:edit')")
    @Log(title = "功能值定义", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody IotFunctionValue iotFunctionValue)
    {
        return Result.toResult(iotFunctionValueService.updateIotFunctionValue(iotFunctionValue));
    }

    /**
     * 删除功能值定义
     */
    @ApiOperation("删除功能值定义")
    @PreAuthorize("@ss.hasPermi('iot:value:remove')")
    @Log(title = "功能值定义", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(iotFunctionValueService.deleteIotFunctionValueByIds(ids));
    }

    /**
     * 保存抽屉数据
     */
    @ApiOperation("保存抽屉数据")
    @PostMapping("/saveFvData")
    public Result saveFvData(@RequestBody IotFunctionValue iotFunctionValue)
    {
        return Result.toResult(iotFunctionValueService.saveFvData(iotFunctionValue));
    }
}
