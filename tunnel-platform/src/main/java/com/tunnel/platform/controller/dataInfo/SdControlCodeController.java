package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdControlCode;
import com.tunnel.business.service.dataInfo.ISdControlCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制码Controller
 * 
 * @author zhangweitian
 * @date 2020-09-08
 */
@RestController
@RequestMapping("/code")
public class SdControlCodeController extends BaseController
{
    @Autowired
    private ISdControlCodeService sdControlCodeService;

    /**
     * 查询控制码列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdControlCode sdControlCode)
    {
        startPage();
        List<SdControlCode> list = sdControlCodeService.selectSdControlCodeList(sdControlCode);
        return getDataTable(list);
    }

    /**
     * 导出控制码列表
     */
  /*  @PreAuthorize(hasPermi = "system:code:export")
    @Log(title = "控制码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdControlCode sdControlCode) throws IOException
    {
        List<SdControlCode> list = sdControlCodeService.selectSdControlCodeList(sdControlCode);
        ExcelUtil<SdControlCode> util = new ExcelUtil<SdControlCode>(SdControlCode.class);
        util.exportExcel(response, list, "code");
    }*/

    /**
     * 获取控制码详细信息
     */
    @GetMapping(value = "/{controlId}")
    public AjaxResult getInfo(@PathVariable("controlId") Long controlId)
    {
        return AjaxResult.success(sdControlCodeService.selectSdControlCodeById(controlId));
    }

    /**
     * 新增控制码
     */
    @Log(title = "控制码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdControlCode sdControlCode)
    {
        return toAjax(sdControlCodeService.insertSdControlCode(sdControlCode));
    }

    /**
     * 修改控制码
     */
    @Log(title = "控制码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdControlCode sdControlCode)
    {
        return toAjax(sdControlCodeService.updateSdControlCode(sdControlCode));
    }

    /**
     * 删除控制码
     */
    @Log(title = "控制码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{controlIds}")
    public AjaxResult remove(@PathVariable Long[] controlIds)
    {
        return toAjax(sdControlCodeService.deleteSdControlCodeByIds(controlIds));
    }
}