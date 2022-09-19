package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdMaintenanceManagement;
import com.tunnel.business.service.dataInfo.ISdMaintenanceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 养护管理Controller
 *
 * @author ruoyi
 * @date 2022-02-14
 */
@RestController
@RequestMapping("/system/management")
public class SdMaintenanceManagementController extends BaseController
{
    @Autowired
    private ISdMaintenanceManagementService sdMaintenanceManagementService;

    /**
     * 查询养护管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:management:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdMaintenanceManagement sdMaintenanceManagement) {
        startPage();
        List<SdMaintenanceManagement> list = sdMaintenanceManagementService.selectSdMaintenanceManagementList(sdMaintenanceManagement);
        return getDataTable(list);
    }

    /**
     * 导出养护管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:management:export')")
    @Log(title = "养护管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdMaintenanceManagement sdMaintenanceManagement) {
        List<SdMaintenanceManagement> list = sdMaintenanceManagementService.selectSdMaintenanceManagementList(sdMaintenanceManagement);
        ExcelUtil<SdMaintenanceManagement> util = new ExcelUtil<SdMaintenanceManagement>(SdMaintenanceManagement.class);
        return util.exportExcel(list, "养护管理数据");
    }

    /**
     * 获取养护管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:management:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) throws IOException {
        return AjaxResult.success(sdMaintenanceManagementService.selectSdMaintenanceManagementById(id));
    }

    /**
     * 新增养护管理
     */
    @Log(title = "养护管理", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody SdMaintenanceManagement sdMaintenanceManagement)
    {
        return toAjax(sdMaintenanceManagementService.add(sdMaintenanceManagement));
    }

    @PostMapping(value = "/addMaintenanceManagement")
    public AjaxResult addMaintenanceManagement(@RequestParam("file") MultipartFile[] file, SdMaintenanceManagement sdMaintenanceManagement)
    {
        return toAjax(sdMaintenanceManagementService.insertSdMaintenanceManagement(file,sdMaintenanceManagement));
    }

    @PostMapping(value = "/uploadPicture")
    public AjaxResult uploadPicture(@RequestParam("file") MultipartFile[] file) throws IOException {
        return AjaxResult.success(sdMaintenanceManagementService.uploadPicture(file));
    }

    /**
     * 修改养护管理
     */
    @PreAuthorize("@ss.hasPermi('system:management:edit')")
    @Log(title = "养护管理", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/update")
    public AjaxResult edit(@RequestBody SdMaintenanceManagement sdMaintenanceManagement)
    {
        return toAjax(sdMaintenanceManagementService.edit(sdMaintenanceManagement));
    }

    @PostMapping(value = "/updateMaintenanceManagement")
    public AjaxResult updateMaintenanceManagement(MultipartFile[] file,
                                                  @RequestParam("id") Long id,
                                                  @RequestParam("maintenancePerson") String maintenancePerson,
                                                  @RequestParam("tunnelId") String tunnelId,
                                                  @RequestParam("maintenanceLocation") String maintenanceLocation,
                                                  @RequestParam("phone") String phone,
                                                  @RequestParam("maintenanceInformation") String maintenanceInformation,
                                                  @RequestParam("curingProgress") String curingProgress,
                                                  Long[] removeIds)
    {
        SdMaintenanceManagement sdMaintenanceManagement = new SdMaintenanceManagement();
        sdMaintenanceManagement.setId(id);
        sdMaintenanceManagement.setMaintenancePerson(maintenancePerson);
        sdMaintenanceManagement.setTunnelId(tunnelId);
        sdMaintenanceManagement.setMaintenanceLocation(maintenanceLocation);
        sdMaintenanceManagement.setPhone(phone);
        sdMaintenanceManagement.setMaintenanceInformation(maintenanceInformation);
        sdMaintenanceManagement.setCuringProgress(curingProgress);
        return toAjax(sdMaintenanceManagementService.updateSdMaintenanceManagement(file,sdMaintenanceManagement,removeIds));
    }

    /**
     * 删除养护管理
     */
    @PreAuthorize("@ss.hasPermi('system:management:remove')")
    @Log(title = "养护管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdMaintenanceManagementService.deleteSdMaintenanceManagementByIds(ids));
    }
}
