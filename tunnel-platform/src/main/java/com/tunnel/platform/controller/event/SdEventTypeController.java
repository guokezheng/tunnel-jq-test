package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdEventType;
import com.tunnel.business.service.event.ISdEventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 事件类型Controller
 * 
 * @author gongfanfei
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/eventType")
public class SdEventTypeController extends BaseController
{
    @Autowired
    private ISdEventTypeService sdEventTypeService;

    /**
     * 查询事件类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdEventType sdEventType)
    {
        startPage();
        List<SdEventType> list = sdEventTypeService.selectSdEventTypeList(sdEventType);
        return getDataTable(list);
    }

    /**
     * 导出事件类型列表
     */
    /*@PreAuthorize(hasPermi = "system:eventType:export")
    @Log(title = "事件类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdEventType sdEventType) throws IOException
    {
        List<SdEventType> list = sdEventTypeService.selectSdEventTypeList(sdEventType);
        ExcelUtil<SdEventType> util = new ExcelUtil<SdEventType>(SdEventType.class);
        util.exportExcel(response, list, "eventType");
    }*/

    /**
     * 获取事件类型详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdEventTypeService.selectSdEventTypeById(id));
    }

    /**
     * 新增事件类型
     */
    @Log(title = "事件类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam("file") MultipartFile[] file,SdEventType sdEventType)
    {
        return toAjax(sdEventTypeService.insertSdEventType(file,sdEventType));
    }

    /**
     * 修改事件类型
     */
    @Log(title = "事件类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(MultipartFile[] file,SdEventType sdEventType)
    {
        return toAjax(sdEventTypeService.updateSdEventType(file,sdEventType));
    }

    /**
     * 删除事件类型
     */
    @Log(title = "事件类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdEventTypeService.deleteSdEventTypeByIds(ids));
    }
}