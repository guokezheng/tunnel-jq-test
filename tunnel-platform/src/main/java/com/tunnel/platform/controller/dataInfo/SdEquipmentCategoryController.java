package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.SdEquipmentCategoryDto;
import com.ruoyi.common.core.domain.TreeCategorySelect;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdEquipmentCategory;
import com.tunnel.business.service.dataInfo.ISdEquipmentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备大类 Controller
 *
 * @author ruoyi
 * @date 2023-02-06
 */
@RestController
@RequestMapping("/bigType")
public class SdEquipmentCategoryController extends BaseController {
    @Autowired
    private ISdEquipmentCategoryService sdEquipmentCategoryService;

    /**
     * 查询设备类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdEquipmentCategory sdEquipmentCategory) {
        startPage();
        List<SdEquipmentCategory> list = sdEquipmentCategoryService.selectSdEquipmentCategoryList(sdEquipmentCategory);
        return getDataTable(list);
    }

    /**
     * 导出设备类型列表
     */
    @Log(title = "设备类型", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdEquipmentCategory sdEquipmentCategory) {
        List<SdEquipmentCategory> list = sdEquipmentCategoryService.selectSdEquipmentCategoryList(sdEquipmentCategory);
        ExcelUtil<SdEquipmentCategory> util = new ExcelUtil<SdEquipmentCategory>(SdEquipmentCategory.class);
        return util.exportExcel(list, "设备类型数据");
    }

    /**
     * 获取设备类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('platform:bigType:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sdEquipmentCategoryService.selectSdEquipmentCategoryById(id));
    }

    /**
     * 新增设备类型
     */
    @PreAuthorize("@ss.hasPermi('platform:bigType:add')")
    @Log(title = "设备类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEquipmentCategory sdEquipmentCategory) {
        return toAjax(sdEquipmentCategoryService.insertSdEquipmentCategory(sdEquipmentCategory));
    }

    /**
     * 修改设备类型
     */
    @PreAuthorize("@ss.hasPermi('platform:bigType:edit')")
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEquipmentCategory sdEquipmentCategory) {
        return toAjax(sdEquipmentCategoryService.updateSdEquipmentCategory(sdEquipmentCategory));
    }

    /**
     * 删除设备类型
     */
    @PreAuthorize("@ss.hasPermi('platform:bigType:remove')")
    @Log(title = "设备类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sdEquipmentCategoryService.deleteSdEquipmentCategoryByIds(ids));
    }

    /**
     * 设备类型树结构
     * 二级：大类---设备
     * @param tunnelId 隧道ID
     * @return
     */
    @GetMapping(value = "/getCategoryDeviceTree")
    public AjaxResult getCategoryDeviceTree(@RequestParam String tunnelId) {
        List<SdEquipmentCategoryDto> list = sdEquipmentCategoryService.getCategoryDeviceList(tunnelId);

        List<TreeCategorySelect> treeCategoryDevice = sdEquipmentCategoryService.buildCategoryTreeSelect(list);

        return AjaxResult.success(treeCategoryDevice);
    }


    /**
     * 设备类型树结构
     *
     * 二级：大类---小类
     * @param tunnelId 隧道ID
     * @return
     */
    @GetMapping(value = "/getCategoryTree")
    public AjaxResult getCategoryTree(@RequestParam String tunnelId) {
        List<SdEquipmentCategoryDto> list = sdEquipmentCategoryService.getCategoryList(tunnelId);

        List<TreeCategorySelect> treeCategory = sdEquipmentCategoryService.buildCategoryTreeSelect(list);

        return AjaxResult.success(treeCategory);
    }














}
