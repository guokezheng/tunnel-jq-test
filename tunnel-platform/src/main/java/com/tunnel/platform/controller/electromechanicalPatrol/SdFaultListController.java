package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.utils.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 故障清单Controller
 * 
 * @author tjw
 * @date 2022-11-02
 */
@RestController
@RequestMapping("/system/list")
public class SdFaultListController extends BaseController
{
    @Autowired
    private ISdFaultListService sdFaultListService;

    /**
     * 查询故障清单列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(SdFaultList sdFaultList)
    {
        startPage();
        List<SdFaultList> list = sdFaultListService.selectSdFaultListList(sdFaultList);
        return getDataTable(list);
    }

    /**
     * 导出故障清单列表
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:export')")*/
    @Log(title = "故障清单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdFaultList sdFaultList)
    {
        List<SdFaultList> list = sdFaultListService.selectSdFaultListList(sdFaultList);
        ExcelUtil<SdFaultList> util = new ExcelUtil<SdFaultList>(SdFaultList.class);
        return util.exportExcel(list, "故障清单数据");
    }

    /**
     * 获取故障清单详细信息
     */
 /*   @PreAuthorize("@ss.hasPermi('system:list:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sdFaultListService.selectSdFaultListById(id));
    }

    /**
     * 新增故障清单
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:add')")*/
    @Log(title = "故障清单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdFaultList sdFaultList)
    {
        sdFaultList.setId(UUIDUtil.getRandom32BeginTimePK());
        SysUser user = SecurityUtils.getLoginUser().getUser();
        return toAjax(sdFaultListService.insertSdFaultList(sdFaultList));
    }

    /**
     * 修改故障清单
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:edit')")*/
    @Log(title = "故障清单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdFaultList sdFaultList)
    {
        return toAjax(sdFaultListService.updateSdFaultList(sdFaultList));
    }

    /**
     * 删除故障清单
     */
  /*  @PreAuthorize("@ss.hasPermi('system:list:remove')")*/
    @Log(title = "故障清单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sdFaultListService.deleteSdFaultListByIds(ids));
    }
}
