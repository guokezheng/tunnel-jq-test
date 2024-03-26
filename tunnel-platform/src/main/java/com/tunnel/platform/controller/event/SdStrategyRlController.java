package com.tunnel.platform.controller.event;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.service.event.ISdStrategyRlService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 策略关联设备信息Controller
 * 
 * @author gongfanfei
 * @date 2020-08-31
 */
@RestController
@RequestMapping("/strategyRl")
public class SdStrategyRlController extends BaseController
{
    @Autowired
    private ISdStrategyRlService sdStrategyRlService;

    /**
     * 查询策略关联设备信息列表
     */
    @ApiOperation("查询策略关联设备信息列表")
    @GetMapping("/list")
    public TableDataInfo list(SdStrategyRl sdStrategyRl)
    {
        startPage();
        List<SdStrategyRl> list = sdStrategyRlService.selectSdStrategyRlList(sdStrategyRl);
        return getDataTable(list);
    }

    /**
     * 根据策略id查询策略设备信息
     * @param strategyId
     * @return
     */
    @ApiOperation("根据策略id查询策略设备信息")
    @GetMapping("/getStrategyRlByStrategyId/{strategyId}")
    public TableDataInfo getStrategyRlByStrategyId(@PathVariable("strategyId") Long strategyId) {
        startPage();
        List<SdStrategyRl> rlList = sdStrategyRlService.selectSdStrategyRlListByStrategyId(strategyId);
        return getDataTable(rlList);
    }

    /**
     * 导出策略关联设备信息列表
     */
   /* @PreAuthorize(hasPermi = "system:strategyRl:export")
    @Log(title = "策略关联设备信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdStrategyRl sdStrategyRl) throws IOException
    {
        List<SdStrategyRl> list = sdStrategyRlService.selectSdStrategyRlList(sdStrategyRl);
        ExcelUtil<SdStrategyRl> util = new ExcelUtil<SdStrategyRl>(SdStrategyRl.class);
        util.exportExcel(response, list, "rl");
    }
*/
    /**
     * 获取策略关联设备信息详细信息
     */
    @ApiOperation("获取策略关联设备信息详细信息")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdStrategyRlService.selectSdStrategyRlById(id));
    }

    /**
     * 新增策略关联设备信息
     */
    @ApiOperation("新增策略关联设备信息")
    @Log(title = "策略关联设备信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdStrategyRl sdStrategyRl)
    {
        return toAjax(sdStrategyRlService.insertSdStrategyRl(sdStrategyRl));
    }

    /**
     * 修改策略关联设备信息
     */
    @ApiOperation("修改策略关联设备信息")
    @Log(title = "策略关联设备信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdStrategyRl sdStrategyRl)
    {
        return toAjax(sdStrategyRlService.updateSdStrategyRl(sdStrategyRl));
    }

    /**
     * 删除策略关联设备信息
     */
    @ApiOperation("删除策略关联设备信息")
    @Log(title = "策略关联设备信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdStrategyRlService.deleteSdStrategyRlByIds(ids));
    }

    /**
     * 查询全部策略关联设备信息列表
     */
    @ApiOperation("查询全部策略关联设备信息列表")
    @GetMapping("/listAll")
    public TableDataInfo listAll(SdStrategyRl sdStrategyRl)
    {
        startPage();
        List<SdStrategyRl> list = sdStrategyRlService.selectSdStrategyRlList(sdStrategyRl);
        return getDataTable(list);
    }
}