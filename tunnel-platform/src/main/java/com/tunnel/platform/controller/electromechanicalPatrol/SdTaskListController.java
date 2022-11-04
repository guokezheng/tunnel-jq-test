package com.tunnel.platform.controller.electromechanicalPatrol;

import java.util.List;

import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 巡查任务Controller
 * 
 * @author tjw
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/task/list")
public class SdTaskListController extends BaseController
{
    @Autowired
    private ISdTaskListService sdTaskListService;

    /**
     * 查询巡查任务列表
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(SdTaskList sdTaskList)
    {
        startPage();
        List<SdTaskList> list = sdTaskListService.selectSdTaskListList(sdTaskList);
        return getDataTable(list);
    }

    /**
     * 导出巡查任务列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:export')")*/
    @Log(title = "巡查任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTaskList sdTaskList)
    {
        List<SdTaskList> list = sdTaskListService.selectSdTaskListList(sdTaskList);
        ExcelUtil<SdTaskList> util = new ExcelUtil<SdTaskList>(SdTaskList.class);
        return util.exportExcel(list, "巡查任务数据");
    }

    /**
     * 获取巡查任务详细信息
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sdTaskListService.selectSdTaskListById(id));
    }

    /**
     * 新增巡查任务
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:add')")*/
    @Log(title = "巡查任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTaskList sdTaskList)
    {
        return toAjax(sdTaskListService.insertSdTaskList(sdTaskList));
    }

    /**
     * 修改巡查任务
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:edit')")*/
    @Log(title = "巡查任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTaskList sdTaskList)
    {
        return toAjax(sdTaskListService.updateSdTaskList(sdTaskList));
    }

    /**
     * 删除巡查任务
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:remove')")*/
    @Log(title = "巡查任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sdTaskListService.deleteSdTaskListByIds(ids));
    }
}
