package com.tunnel.platform.controller.emeResource;

import java.util.List;

import com.tunnel.business.domain.emeResource.SdVehicleType;
import com.tunnel.business.service.emeResource.ISdVehicleTypeService;
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
 * 车辆类型配置Controller
 * 
 * @author ruoyi
 * @date 2022-12-01
 */
@RestController
@RequestMapping("/system/type")
public class SdVehicleTypeController extends BaseController
{
    @Autowired
    private ISdVehicleTypeService sdVehicleTypeService;

    /**
     * 查询车辆类型配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdVehicleType sdVehicleType)
    {
        startPage();
        List<SdVehicleType> list = sdVehicleTypeService.selectSdVehicleTypeList(sdVehicleType);
        return getDataTable(list);
    }

    /**
     * 导出车辆类型配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "车辆类型配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdVehicleType sdVehicleType)
    {
        List<SdVehicleType> list = sdVehicleTypeService.exportSdVehicleTypeList(sdVehicleType);
        ExcelUtil<SdVehicleType> util = new ExcelUtil<SdVehicleType>(SdVehicleType.class);
        return util.exportExcel(list, "车辆类型");
    }

    /**
     * 获取车辆类型配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sdVehicleTypeService.selectSdVehicleTypeById(id));
    }

    /**
     * 新增车辆类型配置
     */
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "车辆类型配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdVehicleType sdVehicleType)
    {
        return toAjax(sdVehicleTypeService.insertSdVehicleType(sdVehicleType));
    }

    /**
     * 修改车辆类型配置
     */
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "车辆类型配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdVehicleType sdVehicleType)
    {
        return toAjax(sdVehicleTypeService.updateSdVehicleType(sdVehicleType));
    }

    /**
     * 删除车辆类型配置
     */
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "车辆类型配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sdVehicleTypeService.deleteSdVehicleTypeByIds(ids));
    }

    /**
     * 校验数据是否重复
     */
    @PostMapping("/checkData")
    public AjaxResult checkData(@RequestBody SdVehicleType sdVehicleType)
    {
        return sdVehicleTypeService.checkData(sdVehicleType);
    }

    /**
     * 获取车辆类型下拉列表
     * @param sdVehicleType
     * @return
     */
    @PostMapping("/getVehicleSelectList")
    public List<SdVehicleType> getVehicleSelectList(@RequestBody SdVehicleType sdVehicleType){
        return sdVehicleTypeService.selectSdVehicleTypeList(sdVehicleType);
    }
}
