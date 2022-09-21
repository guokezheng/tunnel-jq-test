package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.event.SdSafetyIndex;
import com.tunnel.business.service.event.ISdSafetyIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安全指数Controller
 *
 * @author ruoyi
 * @date 2021-12-27
 */
@RestController
@RequestMapping("/system/index")
public class SdSafetyIndexController extends BaseController
{
    @Autowired
    private ISdSafetyIndexService sdSafetyIndexService;

    /**
     * 查询安全指数列表
     */
//    @PreAuthorize("@ss.hasPermi('system:index:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdSafetyIndex sdSafetyIndex)
    {
        startPage();
        List<SdSafetyIndex> list = sdSafetyIndexService.selectSdSafetyIndexList(sdSafetyIndex);
        return getDataTable(list);
    }

    /**
     * 导出安全指数列表
     */
//    @PreAuthorize("@ss.hasPermi('system:index:export')")
    @Log(title = "安全指数", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdSafetyIndex sdSafetyIndex)
    {
        List<SdSafetyIndex> list = sdSafetyIndexService.selectSdSafetyIndexList(sdSafetyIndex);
        ExcelUtil<SdSafetyIndex> util = new ExcelUtil<SdSafetyIndex>(SdSafetyIndex.class);
        return util.exportExcel(list, "安全指数数据");
    }

    /**
     * 获取安全指数详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:index:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdSafetyIndexService.selectSdSafetyIndexById(id));
    }

    /**
     * 新增安全指数
     */
//    @PreAuthorize("@ss.hasPermi('system:index:add')")
    @Log(title = "安全指数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdSafetyIndex sdSafetyIndex)
    {
        return toAjax(sdSafetyIndexService.insertSdSafetyIndex(sdSafetyIndex));
    }

    /**
     * 修改安全指数
     */
//    @PreAuthorize("@ss.hasPermi('system:index:edit')")
    @Log(title = "安全指数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdSafetyIndex sdSafetyIndex)
    {
        return toAjax(sdSafetyIndexService.updateSdSafetyIndex(sdSafetyIndex));
    }

    /**
     * 删除安全指数
     */
//    @PreAuthorize("@ss.hasPermi('system:index:remove')")
    @Log(title = "安全指数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdSafetyIndexService.deleteSdSafetyIndexByIds(ids));
    }
}
