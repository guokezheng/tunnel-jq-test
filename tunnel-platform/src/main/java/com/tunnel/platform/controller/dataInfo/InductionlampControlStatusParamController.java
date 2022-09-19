package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.dataInfo.InductionlampControlStatusParam;
import com.tunnel.business.service.dataInfo.IInductionlampControlStatusParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备控制状态参数Controller
 *
 * @author ruoyi
 * @date 2022-08-30
 */
@RestController
@RequestMapping("/inductionlamp/statusParam")
public class InductionlampControlStatusParamController extends BaseController
{
    @Autowired
    private IInductionlampControlStatusParamService inductionlampControlStatusParamService;

    /**
     * 查询设备控制状态参数列表
     */
    @PreAuthorize("@ss.hasPermi('inductionlamp:statusParam:list')")
    @GetMapping("/list")
    public TableDataInfo list(InductionlampControlStatusParam inductionlampControlStatusParam)
    {
        startPage();
        List<InductionlampControlStatusParam> list = inductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
        return getDataTable(list);
    }

    /**
     * 导出设备控制状态参数列表
     */
    @PreAuthorize("@ss.hasPermi('inductionlamp:statusParam:export')")
    @Log(title = "设备控制状态参数", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(InductionlampControlStatusParam inductionlampControlStatusParam)
    {
        List<InductionlampControlStatusParam> list = inductionlampControlStatusParamService.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
        ExcelUtil<InductionlampControlStatusParam> util = new ExcelUtil<InductionlampControlStatusParam>(InductionlampControlStatusParam.class);
        return util.exportExcel(list, "设备控制状态参数数据");
    }

    /**
     * 获取设备控制状态参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('inductionlamp:statusParam:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(inductionlampControlStatusParamService.selectInductionlampControlStatusParamById(id));
    }

    /**
     * 新增设备控制状态参数
     */
    @PreAuthorize("@ss.hasPermi('inductionlamp:statusParam:add')")
    @Log(title = "设备控制状态参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody InductionlampControlStatusParam inductionlampControlStatusParam)
    {
        return toAjax(inductionlampControlStatusParamService.insertInductionlampControlStatusParam(inductionlampControlStatusParam));
    }

    /**
     * 修改设备控制状态参数
     */
    @PreAuthorize("@ss.hasPermi('inductionlamp:statusParam:edit')")
    @Log(title = "设备控制状态参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody InductionlampControlStatusParam inductionlampControlStatusParam)
    {
        return toAjax(inductionlampControlStatusParamService.updateInductionlampControlStatusParam(inductionlampControlStatusParam));
    }

    /**
     * 删除设备控制状态参数
     */
    @PreAuthorize("@ss.hasPermi('inductionlamp:statusParam:remove')")
    @Log(title = "设备控制状态参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(inductionlampControlStatusParamService.deleteInductionlampControlStatusParamByIds(ids));
    }
}
