package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdDevicesBrand;
import com.tunnel.business.service.dataInfo.ISdDevicesBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物联设备厂商Controller
 *
 * @author ruoyi
 * @date 2022-12-02
 */
@RestController
@RequestMapping("/device/brand")
public class SdDevicesBrandController extends BaseController {
    @Autowired
    private ISdDevicesBrandService sdDevicesBrandService;

    /**
     * 查询物联设备厂商列表
     */
    // @PreAuthorize("@ss.hasPermi('device:brand:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdDevicesBrand sdDevicesBrand) {
        startPage();
        List<SdDevicesBrand> list = sdDevicesBrandService.selectSdDevicesBrandList(sdDevicesBrand);
        return getDataTable(list);
    }

    /**
     * 导出物联设备厂商列表
     */
    @PreAuthorize("@ss.hasPermi('device:brand:export')")
    @Log(title = "物联设备厂商", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDevicesBrand sdDevicesBrand) {
        List<SdDevicesBrand> list = sdDevicesBrandService.selectSdDevicesBrandList(sdDevicesBrand);
        ExcelUtil<SdDevicesBrand> util = new ExcelUtil<SdDevicesBrand>(SdDevicesBrand.class);
        return util.exportExcel(list, "物联设备厂商数据");
    }

    /**
     * 获取物联设备厂商详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:brand:query')")
    @GetMapping(value = "/{supplierId}")
    public AjaxResult getInfo(@PathVariable("supplierId") String supplierId) {
        return AjaxResult.success(sdDevicesBrandService.selectSdDevicesBrandBySupplierId(supplierId));
    }

    /**
     * 新增物联设备厂商
     */
    @PreAuthorize("@ss.hasPermi('device:brand:add')")
    @Log(title = "物联设备厂商", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDevicesBrand sdDevicesBrand) {
        return toAjax(sdDevicesBrandService.insertSdDevicesBrand(sdDevicesBrand));
    }

    /**
     * 修改物联设备厂商
     */
    // @PreAuthorize("@ss.hasPermi('device:brand:edit')")
    @Log(title = "物联设备厂商", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDevicesBrand sdDevicesBrand) {
        return toAjax(sdDevicesBrandService.updateSdDevicesBrand(sdDevicesBrand));
    }

    /**
     * 删除物联设备厂商
     */
    @PreAuthorize("@ss.hasPermi('device:brand:remove')")
    @Log(title = "物联设备厂商", businessType = BusinessType.DELETE)
    @DeleteMapping("/{supplierIds}")
    public AjaxResult remove(@PathVariable String[] supplierIds) {
        return toAjax(sdDevicesBrandService.deleteSdDevicesBrandBySupplierIds(supplierIds));
    }
}
