package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.InductionlampControlStatusDetails;
import com.tunnel.business.service.dataInfo.IInductionlampControlStatusDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备控制状态详情关联Controller
 *
 * @author ruoyi
 * @date 2022-08-30
 */
@RestController
@RequestMapping("/inductionlamp/statusdetails")
public class InductionlampControlStatusDetailsController extends BaseController
{
    @Autowired
    private IInductionlampControlStatusDetailsService inductionlampControlStatusDetailsService;

    /**
     * 查询设备控制状态详情关联列表
     */
//    @PreAuthorize("@ss.hasPermi('inductionlamp:statusdetails:list')")
    @GetMapping("/list")
    public TableDataInfo list(InductionlampControlStatusDetails inductionlampControlStatusDetails)
    {
        startPage();
        List<InductionlampControlStatusDetails> list = inductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
        return getDataTable(list);
    }

    /**
     * 导出设备控制状态详情关联列表
     */
//    @PreAuthorize("@ss.hasPermi('inductionlamp:statusdetails:export')")
    @Log(title = "设备控制状态详情关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InductionlampControlStatusDetails inductionlampControlStatusDetails)
    {
        List<InductionlampControlStatusDetails> list = inductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
        ExcelUtil<InductionlampControlStatusDetails> util = new ExcelUtil<InductionlampControlStatusDetails>(InductionlampControlStatusDetails.class);
        return util.exportExcel(list, "设备控制状态详情关联数据");
    }

    /**
     * 获取设备控制状态详情关联详细信息
     */
//    @PreAuthorize("@ss.hasPermi('inductionlamp:statusdetails:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(inductionlampControlStatusDetailsService.selectInductionlampControlStatusDetailsById(id));
    }

    /**
     * 新增设备控制状态详情关联
     */
//    @PreAuthorize("@ss.hasPermi('inductionlamp:statusdetails:add')")
    @Log(title = "设备控制状态详情关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InductionlampControlStatusDetails inductionlampControlStatusDetails)
    {
        return toAjax(inductionlampControlStatusDetailsService.insertInductionlampControlStatusDetails(inductionlampControlStatusDetails));
    }

    /**
     * 修改设备控制状态详情关联
     */
//    @PreAuthorize("@ss.hasPermi('inductionlamp:statusdetails:edit')")
    @Log(title = "设备控制状态详情关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InductionlampControlStatusDetails inductionlampControlStatusDetails)
    {
        return toAjax(inductionlampControlStatusDetailsService.updateInductionlampControlStatusDetails(inductionlampControlStatusDetails));
    }

    /**
     * 删除设备控制状态详情关联
     */
//    @PreAuthorize("@ss.hasPermi('inductionlamp:statusdetails:remove')")
    @Log(title = "设备控制状态详情关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inductionlampControlStatusDetailsService.deleteInductionlampControlStatusDetailsByIds(ids));
    }
}
