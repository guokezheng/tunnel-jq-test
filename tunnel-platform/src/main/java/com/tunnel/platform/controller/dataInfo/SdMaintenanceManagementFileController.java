package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.SdMaintenanceManagementFile;
import com.tunnel.business.service.dataInfo.ISdMaintenanceManagementFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 养护文件Controller
 * 
 * @author ruoyi
 * @date 2022-02-15
 */
@RestController
@RequestMapping("/sdMaintenanceManagementFile")
public class SdMaintenanceManagementFileController extends BaseController
{
    @Autowired
    private ISdMaintenanceManagementFileService sdMaintenanceManagementFileService;

    /**
     * 查询养护文件列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdMaintenanceManagementFile sdMaintenanceManagementFile)
    {
        startPage();
        List<SdMaintenanceManagementFile> list = sdMaintenanceManagementFileService.selectSdMaintenanceManagementFileList(sdMaintenanceManagementFile);
        return getDataTable(list);
    }

    /**
     * 导出养护文件列表
     */
    @Log(title = "养护文件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdMaintenanceManagementFile sdMaintenanceManagementFile)
    {
        List<SdMaintenanceManagementFile> list = sdMaintenanceManagementFileService.selectSdMaintenanceManagementFileList(sdMaintenanceManagementFile);
        ExcelUtil<SdMaintenanceManagementFile> util = new ExcelUtil<SdMaintenanceManagementFile>(SdMaintenanceManagementFile.class);
        return util.exportExcel(list, "养护文件数据");
    }

    /**
     * 获取养护文件详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdMaintenanceManagementFileService.selectSdMaintenanceManagementFileById(id));
    }

    /**
     * 新增养护文件
     */
    @Log(title = "养护文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdMaintenanceManagementFile sdMaintenanceManagementFile)
    {
        return toAjax(sdMaintenanceManagementFileService.insertSdMaintenanceManagementFile(sdMaintenanceManagementFile));
    }

    /**
     * 修改养护文件
     */
    @Log(title = "养护文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdMaintenanceManagementFile sdMaintenanceManagementFile)
    {
        return toAjax(sdMaintenanceManagementFileService.updateSdMaintenanceManagementFile(sdMaintenanceManagementFile));
    }

    /**
     * 删除养护文件
     */
    @Log(title = "养护文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdMaintenanceManagementFileService.deleteSdMaintenanceManagementFileByIds(ids));
    }
}
