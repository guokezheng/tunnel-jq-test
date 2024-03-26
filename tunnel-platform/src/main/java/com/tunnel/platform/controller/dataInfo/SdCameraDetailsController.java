package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdCameraDetails;
import com.tunnel.business.service.dataInfo.ISdCameraDetailsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 摄像机详情Controller
 *
 * @author liubaokui
 * @date 2021-05-08
 */
@RestController
@RequestMapping("/system/details")
public class SdCameraDetailsController extends BaseController {
    @Autowired
    private ISdCameraDetailsService sdCameraDetailsService;

    /**
     * 查询摄像机详情列表
     */
    @ApiOperation("查询摄像机详情列表")
    @PreAuthorize("@ss.hasPermi('system:details:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdCameraDetails sdCameraDetails) {
        startPage();
        List<SdCameraDetails> list = sdCameraDetailsService.selectSdCameraDetailsList(sdCameraDetails);
        return getDataTable(list);
    }

    /**
     * 导出摄像机详情列表
     */
    @ApiOperation("导出摄像机详情列表")
    @PreAuthorize("@ss.hasPermi('system:details:export')")
    @Log(title = "摄像机详情", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdCameraDetails sdCameraDetails) {
        List<SdCameraDetails> list = sdCameraDetailsService.selectSdCameraDetailsList(sdCameraDetails);
        ExcelUtil<SdCameraDetails> util = new ExcelUtil<SdCameraDetails>(SdCameraDetails.class);
        return util.exportExcel(list, "摄像机详情");
    }

    /**
     * 获取摄像机详情详细信息
     */
    @ApiOperation("获取摄像机详情详细信息")
    @PreAuthorize("@ss.hasPermi('system:details:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sdCameraDetailsService.selectSdCameraDetailsById(id));
    }

    /**
     * 获取摄像机详情详细信息
     */
    @ApiOperation("获取摄像机详情详细信息")
    @GetMapping(value = "/select/{id}")
    public AjaxResult getInfomation(@PathVariable("id") String camId) {
        return AjaxResult.success(sdCameraDetailsService.selectSdCameraDetailsByCamId(camId));
    }

    /**
     * 新增摄像机详情
     */
    @ApiOperation("新增摄像机详情")
    @PreAuthorize("@ss.hasPermi('system:details:add')")
    @Log(title = "摄像机详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdCameraDetails sdCameraDetails) {
        return toAjax(sdCameraDetailsService.insertSdCameraDetails(sdCameraDetails));
    }

    /**
     * 修改摄像机详情
     */
    @ApiOperation("修改摄像机详情")
    @PreAuthorize("@ss.hasPermi('system:details:edit')")
    @Log(title = "摄像机详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdCameraDetails sdCameraDetails) {
        return toAjax(sdCameraDetailsService.updateSdCameraDetails(sdCameraDetails));
    }

    /**
     * 删除摄像机详情
     */
    @ApiOperation("删除摄像机详情")
    @PreAuthorize("@ss.hasPermi('system:details:remove')")
    @Log(title = "摄像机详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sdCameraDetailsService.deleteSdCameraDetailsByIds(ids));
    }
}
