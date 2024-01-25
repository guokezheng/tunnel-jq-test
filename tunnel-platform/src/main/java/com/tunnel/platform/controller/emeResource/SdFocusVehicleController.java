package com.tunnel.platform.controller.emeResource;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.emeResource.SdFocusVehicle;
import com.tunnel.business.domain.emeResource.SdVehicleType;
import com.tunnel.business.service.emeResource.SdFocusVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhai
 * @date 2024/1/24
 */
@RestController
@RequestMapping("/system/focusVehicle")
public class SdFocusVehicleController extends BaseController {

    @Autowired
    private SdFocusVehicleService sdFocusVehicleService;

    /**
     * 查询重点车辆列表
     */
    @PreAuthorize("@ss.hasPermi('system:focuscar:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdFocusVehicle sdFocusVehicle){
        startPage();
        List<SdFocusVehicle> list = sdFocusVehicleService.selectList(sdFocusVehicle);
        return getDataTable(list);
    }

    /**
     * 导出
     * @param sdFocusVehicle
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:focuscar:export')")
    @Log(title = "重点车辆", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdFocusVehicle sdFocusVehicle){
        List<SdFocusVehicle> list = sdFocusVehicleService.selectList(sdFocusVehicle);
        ExcelUtil<SdFocusVehicle> util = new ExcelUtil<SdFocusVehicle>(SdFocusVehicle.class);
        return util.exportExcel(list, "重点车辆");
    }
}
