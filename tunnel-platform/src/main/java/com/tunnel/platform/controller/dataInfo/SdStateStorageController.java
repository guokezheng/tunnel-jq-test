package com.tunnel.platform.controller.dataInfo;

import com.alibaba.fastjson.JSON;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdStateStorage;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdStateStorageService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 隧道数据存储表Controller
 *
 * @author 刘方堃
 * @date 2022-01-07
 */
@RestController
@RequestMapping("/system/storage")
@Api(tags = "隧道数据存储表")
@ApiSupport(order = 16)
public class SdStateStorageController extends BaseController {
    @Autowired
    private ISdStateStorageService sdStateStorageService;
    @Autowired
    private ISdTunnelsService sdTunnelsService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    /**
     * 模拟获取隧道设备状态
     */
    @ApiOperation("模拟获取隧道设备状态")
    @GetMapping("/getDevState")
    public String getDevState(SdStateStorage storage) {
        String tunnelId = storage.getTunnelId();
        List<Map<String, Object>> devList = new ArrayList<>();
        if (tunnelId == null || tunnelId.equals("")) {
            return JSON.toJSONString(devList);
        }
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqTunnelId(tunnelId);
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        for (SdDevices sdDevice : list) {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("devId", sdDevice.getEqId().toString());
            if (sdDevice.getEqType() != null) {
                json.put("devType", sdDevice.getEqType().toString());
            }
            //方向
            if (sdDevice.getEqDirection() != null) {
                json.put("direction", sdDevice.getEqDirection());
            }
            //设备状态
            if (sdDevice.getEqType() != null && "109".equals(sdDevice.getEqType().toString())) {
                continue;
            }
            SdStateStorage sdStateStorage = sdStateStorageService.selectSdStateStorage(String.valueOf(sdDevice.getEqId()) + "");
            if (sdStateStorage == null || sdStateStorage.getState() == null) {
                json.put("state", "");
            } else {
                json.put("state", sdStateStorage.getState());
            }
            devList.add(json);
        }
        String res = JSON.toJSONString(devList);
        return res;
    }


    /**
     * 查询隧道数据存储表列表
     */
    @ApiOperation("查询隧道数据存储表列表")
    @PreAuthorize("@ss.hasPermi('system:storage:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdStateStorage sdStateStorage) {
        startPage();
        List<SdStateStorage> list = sdStateStorageService.selectSdStateStorageList(sdStateStorage);
        return getDataTable(list);
    }

    /**
     * 导出隧道数据存储表列表
     */
    @ApiOperation("导出隧道数据存储表列表")
    @PreAuthorize("@ss.hasPermi('system:storage:export')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdStateStorage sdStateStorage) {
        List<SdStateStorage> list = sdStateStorageService.selectSdStateStorageList(sdStateStorage);
        ExcelUtil<SdStateStorage> util = new ExcelUtil<SdStateStorage>(SdStateStorage.class);
        return util.exportExcel(list, "隧道数据存储表数据");
    }

    /**
     * 获取隧道数据存储表详细信息
     */
    @ApiOperation("获取隧道数据存储表详细信息")
    @PreAuthorize("@ss.hasPermi('system:storage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sdStateStorageService.selectSdStateStorageById(id));
    }

    /**
     * 新增隧道数据存储表
     */
    @ApiOperation("新增隧道数据存储表")
    @PreAuthorize("@ss.hasPermi('system:storage:add')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdStateStorage sdStateStorage) {
        return toAjax(sdStateStorageService.insertSdStateStorage(sdStateStorage));
    }

    /**
     * 修改隧道数据存储表
     */
    @ApiOperation("修改隧道数据存储表")
    @PreAuthorize("@ss.hasPermi('system:storage:edit')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdStateStorage sdStateStorage) {
        return toAjax(sdStateStorageService.updateSdStateStorage(sdStateStorage));
    }

    /**
     * 删除隧道数据存储表
     */
    @ApiOperation("删除隧道数据存储表")
    @PreAuthorize("@ss.hasPermi('system:storage:remove')")
    @Log(title = "隧道数据存储表", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sdStateStorageService.deleteSdStateStorageByIds(ids));
    }
}
