package com.zc.iot.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.zc.iot.domain.IotFunction;
import com.zc.iot.service.IIotFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能定义Controller
 * 
 * @author YangChao
 * @date 2021-10-27
 */
@Api(tags = "功能定义")
@RestController
@RequestMapping("/iot/function")
public class IotFunctionController extends BaseController
{
    @Autowired
    private IIotFunctionService iotFunctionService;

    /**
     * 查询功能定义列表
     */
    @ApiOperation("查询功能定义列表")
    @PreAuthorize("@ss.hasPermi('iot:function:list')")
    @GetMapping("/list")
    public TableDataInfo<List<IotFunction>> list(IotFunction iotFunction)
    {
        startPage();
        List<IotFunction> list = iotFunctionService.selectIotFunctionList(iotFunction);
        return getDataTable(list);
    }

    /**
     * 导出功能定义列表
     */
    @PreAuthorize("@ss.hasPermi('iot:function:export')")
    @Log(title = "功能定义", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotFunction iotFunction)
    {
        List<IotFunction> list = iotFunctionService.selectIotFunctionList(iotFunction);
        ExcelUtil<IotFunction> util = new ExcelUtil<IotFunction>(IotFunction.class);
        return util.exportExcel(list, "功能定义数据");
    }

    /**
     * 获取功能定义详细信息
     */
    @ApiOperation("获取功能定义详细信息")
    @PreAuthorize("@ss.hasPermi('iot:function:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return Result.success(iotFunctionService.selectIotFunctionById(id));
    }

    /**
     * 新增功能定义
     */
    @ApiOperation("新增功能定义")
    @PreAuthorize("@ss.hasPermi('iot:function:add')")
    @Log(title = "功能定义", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody IotFunction iotFunction)
    {
        return Result.toResult(iotFunctionService.insertIotFunction(iotFunction));
    }

    /**
     * 修改功能定义
     */
    @ApiOperation("修改功能定义")
    @PreAuthorize("@ss.hasPermi('iot:function:edit')")
    @Log(title = "功能定义", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody IotFunction iotFunction)
    {
        return Result.toResult(iotFunctionService.updateIotFunction(iotFunction));
    }

    /**
     * 删除功能定义
     */
    @ApiOperation("删除功能定义")
    @PreAuthorize("@ss.hasPermi('iot:function:remove')")
    @Log(title = "功能定义", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return Result.toResult(iotFunctionService.deleteIotFunctionByIds(ids));
    }
}
