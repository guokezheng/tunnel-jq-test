package com.zc.iot.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.zc.iot.domain.IotCategory;
import com.zc.iot.service.IIotCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品类Controller
 * 
 * @author YangChao
 * @date 2021-10-29
 */
@RestController
@RequestMapping("/iot/category")
public class IotCategoryController extends BaseController
{
    @Autowired
    private IIotCategoryService iotCategoryService;

    /**
     * 查询品类列表
     */
    @PreAuthorize("@ss.hasPermi('iot:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotCategory iotCategory)
    {
        startPage();
        List<IotCategory> list = iotCategoryService.selectIotCategoryList(iotCategory);
        return getDataTable(list);
    }

    /**
     * 导出品类列表
     */
    @PreAuthorize("@ss.hasPermi('iot:category:export')")
    @Log(title = "品类", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotCategory iotCategory)
    {
        List<IotCategory> list = iotCategoryService.selectIotCategoryList(iotCategory);
        ExcelUtil<IotCategory> util = new ExcelUtil<IotCategory>(IotCategory.class);
        return util.exportExcel(list, "品类数据");
    }

    /**
     * 获取品类详细信息
     */
    @PreAuthorize("@ss.hasPermi('iot:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotCategoryService.selectIotCategoryById(id));
    }

    /**
     * 新增品类
     */
    @PreAuthorize("@ss.hasPermi('iot:category:add')")
    @Log(title = "品类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotCategory iotCategory)
    {
        return toAjax(iotCategoryService.insertIotCategory(iotCategory));
    }

    /**
     * 修改品类
     */
    @PreAuthorize("@ss.hasPermi('iot:category:edit')")
    @Log(title = "品类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotCategory iotCategory)
    {
        return toAjax(iotCategoryService.updateIotCategory(iotCategory));
    }

    /**
     * 删除品类
     */
    @PreAuthorize("@ss.hasPermi('iot:category:remove')")
    @Log(title = "品类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotCategoryService.deleteIotCategoryByIds(ids));
    }
}
