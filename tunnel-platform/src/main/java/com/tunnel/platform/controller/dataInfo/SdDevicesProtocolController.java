package com.tunnel.platform.controller.dataInfo;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdDevicesProtocol;
import com.tunnel.business.service.dataInfo.ISdDevicesProtocolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备协议Controller
 *
 * @author ruoyi
 * @date 2022-12-07
 */
@RestController
@RequestMapping("/device/protocol")
@Api(tags = "设备协议")
@ApiSupport(order = 16)
public class SdDevicesProtocolController extends BaseController {
    @Autowired
    private ISdDevicesProtocolService sdDevicesProtocolService;

    /**
     * 查询设备协议列表
     */
    // @PreAuthorize("@ss.hasPermi('device:protocol:list')")
    @ApiOperation("查询设备协议列表")
    @GetMapping("/list")
    public TableDataInfo list(SdDevicesProtocol sdDevicesProtocol) {
        startPage();
        List<SdDevicesProtocol> list = sdDevicesProtocolService.selectSdDevicesProtocolList(sdDevicesProtocol);
        return getDataTable(list);
    }

    /**
     * 导出设备协议列表
     */
    @ApiOperation("导出设备协议列表")
    @PreAuthorize("@ss.hasPermi('device:protocol:export')")
    @Log(title = "设备协议", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdDevicesProtocol sdDevicesProtocol) {
        List<SdDevicesProtocol> list = sdDevicesProtocolService.selectSdDevicesProtocolList(sdDevicesProtocol);
        ExcelUtil<SdDevicesProtocol> util = new ExcelUtil<SdDevicesProtocol>(SdDevicesProtocol.class);
        return util.exportExcel(list, "设备协议数据");
    }

    /**
     * 获取设备协议详细信息
     */
    @ApiOperation("获取设备协议详细信息")
    @PreAuthorize("@ss.hasPermi('device:protocol:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sdDevicesProtocolService.selectSdDevicesProtocolById(id));
    }

    /**
     * 新增设备协议
     */
    @ApiOperation("新增设备协议")
    @PreAuthorize("@ss.hasPermi('device:protocol:add')")
    @Log(title = "设备协议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDevicesProtocol sdDevicesProtocol) {
        return toAjax(sdDevicesProtocolService.insertSdDevicesProtocol(sdDevicesProtocol));
    }

    /**
     * 修改设备协议
     */
    @ApiOperation("修改设备协议")
    @PreAuthorize("@ss.hasPermi('device:protocol:edit')")
    @Log(title = "设备协议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDevicesProtocol sdDevicesProtocol) {
        return toAjax(sdDevicesProtocolService.updateSdDevicesProtocol(sdDevicesProtocol));
    }

    /**
     * 删除设备协议
     */
    @ApiOperation("删除设备协议")
    @PreAuthorize("@ss.hasPermi('device:protocol:remove')")
    @Log(title = "设备协议", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sdDevicesProtocolService.deleteSdDevicesProtocolByIds(ids));
    }
}
