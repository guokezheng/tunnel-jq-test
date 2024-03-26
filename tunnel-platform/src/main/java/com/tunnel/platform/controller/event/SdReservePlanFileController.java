package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdReservePlanFile;
import com.tunnel.business.service.event.ISdReservePlanFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预案文件Controller
 * 
 * @author gongfanfei
 * @date 2020-09-10
 */
@RestController
@RequestMapping("/reservePlanFile")
public class SdReservePlanFileController extends BaseController
{
    @Autowired
    private ISdReservePlanFileService sdReservePlanFileService;

    /**
     * 查询预案文件列表
     */
    @ApiOperation("查询预案文件列表")
    @GetMapping("/list")
    public TableDataInfo list(SdReservePlanFile sdReservePlanFile)
    {
        startPage();
        List<SdReservePlanFile> list = sdReservePlanFileService.selectSdReservePlanFileList(sdReservePlanFile);
        return getDataTable(list);
    }

    /**
     * 导出预案文件列表
     */
   /* @PreAuthorize(hasPermi = "business:reservePlanFile:export")
    @Log(title = "预案文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdReservePlanFile sdReservePlanFile) throws IOException
    {
        List<SdReservePlanFile> list = sdReservePlanFileService.selectSdReservePlanFileList(sdReservePlanFile);
        ExcelUtil<SdReservePlanFile> util = new ExcelUtil<SdReservePlanFile>(SdReservePlanFile.class);
        util.exportExcel(response, list, "reservePlanFile");
    }*/

    /**
     * 获取预案文件详细信息
     */
    @ApiOperation("获取预案文件详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdReservePlanFileService.selectSdReservePlanFileById(id));
    }

    /**
     * 新增预案文件
     */
    @ApiOperation("新增预案文件")
    @Log(title = "预案文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdReservePlanFile sdReservePlanFile)
    {
        return toAjax(sdReservePlanFileService.insertSdReservePlanFile(sdReservePlanFile));
    }

    /**
     * 修改预案文件
     */
    @ApiOperation("修改预案文件")
    @Log(title = "预案文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdReservePlanFile sdReservePlanFile)
    {
        return toAjax(sdReservePlanFileService.updateSdReservePlanFile(sdReservePlanFile));
    }

    /**
     * 删除预案文件
     */
    @ApiOperation("删除预案文件")
    @Log(title = "预案文件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdReservePlanFileService.deleteSdReservePlanFileByIds(ids));
    }
}