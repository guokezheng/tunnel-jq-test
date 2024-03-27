package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * 设备大类 Controller
 *
 * @author ruoyi
 * @date 2023-02-06
 */
@RestController
@RequestMapping("/bigType")
@Api(tags = "设备大类")
@ApiSupport(order = 16)
public class SdEquipmentCategoryController extends BaseController {
    @Autowired
    private ISdEquipmentCategoryService sdEquipmentCategoryService;

    /**
     * 查询设备类型列表
     */
    @ApiOperation("查询设备类型列表")
    @GetMapping("/list")
    public TableDataInfo list(SdEquipmentCategory sdEquipmentCategory) {
        startPage();
        List<SdEquipmentCategory> list = sdEquipmentCategoryService.selectSdEquipmentCategoryList(sdEquipmentCategory);
        return getDataTable(list);
    }

    /**
     * 导出设备类型列表
     */
    @ApiOperation("导出设备类型列表")
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
    @ApiOperation("获取设备类型详细信息")
    @PreAuthorize("@ss.hasPermi('device:bigType:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sdEquipmentCategoryService.selectSdEquipmentCategoryById(id));
    }

    /**
     * 新增设备类型
     */
    @ApiOperation("新增设备类型")
    @PreAuthorize("@ss.hasPermi('device:bigType:add')")
    @Log(title = "设备类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEquipmentCategory sdEquipmentCategory) {
        return toAjax(sdEquipmentCategoryService.insertSdEquipmentCategory(sdEquipmentCategory));
    }

    /**
     * 修改设备类型
     */
    @ApiOperation("修改设备类型")
    @PreAuthorize("@ss.hasPermi('device:bigType:edit')")
    @Log(title = "设备类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEquipmentCategory sdEquipmentCategory) {
        return toAjax(sdEquipmentCategoryService.updateSdEquipmentCategory(sdEquipmentCategory));
    }

    /**
     * 删除设备类型
     */
    @ApiOperation("删除设备类型")
    @PreAuthorize("@ss.hasPermi('device:bigType:remove')")
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
    @ApiOperation("设备类型树结构")
    @GetMapping(value = "/getCategoryDeviceTree")
    public AjaxResult getCategoryDeviceTree(@RequestParam String tunnelId) {
        List<SdEquipmentCategoryDto> list = sdEquipmentCategoryService.getCategoryDeviceList(tunnelId);

        List<TreeCategorySelect> treeCategoryDevice = sdEquipmentCategoryService.buildCategoryTreeSelect(list);
        //过滤没有子节点的节点
        Iterator<TreeCategorySelect> iterator = treeCategoryDevice.iterator();
        while (iterator.hasNext()) {
            TreeCategorySelect entity = iterator.next();
            if (entity.getChildren().size() == 0) {
                iterator.remove();
            }
        }
        return AjaxResult.success(treeCategoryDevice);
    }


    /**
     * 设备类型树结构
     * 二级：大类---小类
     * @return
     */
    @ApiOperation("设备类型树结构")
    @GetMapping(value = "/getCategoryTree")
    public AjaxResult getCategoryTree() {
        List<SdEquipmentCategoryDto> list = sdEquipmentCategoryService.getCategoryList();

        List<TreeCategorySelect> treeCategory = sdEquipmentCategoryService.buildCategoryTreeSelect(list);

        return AjaxResult.success(treeCategory);
    }

    /**
     * 查询全部设备类型
     * @return
     */
    @ApiOperation("查询全部设备类型")
    @GetMapping(value = "/getCategoryAllTree")
    public AjaxResult getCategoryAllTree() {
        List<SdEquipmentCategoryDto> list = sdEquipmentCategoryService.getCategoryAllList();

        List<TreeCategorySelect> treeCategory = sdEquipmentCategoryService.buildCategoryTreeSelect(list);

        return AjaxResult.success(treeCategory);
    }














}
