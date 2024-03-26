package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdEquipmentFile;
import com.tunnel.business.service.dataInfo.ISdEquipmentFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备档案文件Controller
 * 
 * @author yanghousheng
 * @date 2020-11-20
 */
@RestController
@RequestMapping("/file")
public class SdEquipmentFileController extends BaseController
{
    @Autowired
    private ISdEquipmentFileService sdEquipmentFileService;

    /**
     * 查询设备档案文件列表
     */
    @ApiOperation("查询设备档案文件列表")
    @GetMapping("/list")
    public TableDataInfo list(SdEquipmentFile sdEquipmentFile)
    {
        startPage();
        List<SdEquipmentFile> list = sdEquipmentFileService.selectSdEquipmentFileList(sdEquipmentFile);
        return getDataTable(list);
    }

    /**
     * 导出设备档案文件列表
     */
    /*@PreAuthorize(hasPermi = "system:file:export")
    @Log(title = "设备档案文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdEquipmentFile sdEquipmentFile) throws IOException
    {
        List<SdEquipmentFile> list = sdEquipmentFileService.selectSdEquipmentFileList(sdEquipmentFile);
        ExcelUtil<SdEquipmentFile> util = new ExcelUtil<SdEquipmentFile>(SdEquipmentFile.class);
        util.exportExcel(response, list, "file");
    }*/

    /**
     * 获取设备档案文件详细信息
     */
    @ApiOperation("获取设备档案文件详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEquipmentFileService.selectSdEquipmentFileById(id));
    }

    /**
     * 新增设备档案文件
     */
    @ApiOperation("新增设备档案文件")
    @Log(title = "设备档案文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdEquipmentFile sdEquipmentFile)
    {
        return toAjax(sdEquipmentFileService.insertSdEquipmentFile(sdEquipmentFile));
    }

    /**
     * 修改设备档案文件
     */
    @ApiOperation("修改设备档案文件")
    @Log(title = "设备档案文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdEquipmentFile sdEquipmentFile)
    {
        return toAjax(sdEquipmentFileService.updateSdEquipmentFile(sdEquipmentFile));
    }

    /**
     * 删除设备档案文件
     */
    @ApiOperation("删除设备档案文件")
    @Log(title = "设备档案文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEquipmentFileService.deleteSdEquipmentFileByIds(ids));
    }
}
