package com.tunnel.platform.controller.dataInfo;

import java.util.List;
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
import com.tunnel.business.domain.dataInfo.TunnelAssociation;
import com.tunnel.business.service.dataInfo.ITunnelAssociationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 隧道关联关系Controller
 */
@RestController
@RequestMapping("/system/association")
public class TunnelAssociationController extends BaseController
{
    @Autowired
    private ITunnelAssociationService tunnelAssociationService;

    /**
     * 查询隧道关联关系列表
     */
    @PreAuthorize("@ss.hasPermi('system:association:list')")
    @GetMapping("/list")
    public TableDataInfo list(TunnelAssociation tunnelAssociation)
    {
        startPage();
        List<TunnelAssociation> list = tunnelAssociationService.selectTunnelAssociationList(tunnelAssociation);
        return getDataTable(list);
    }

    /**
     * 导出隧道关联关系列表
     */
    @PreAuthorize("@ss.hasPermi('system:association:export')")
    @Log(title = "隧道关联关系", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TunnelAssociation tunnelAssociation)
    {
        List<TunnelAssociation> list = tunnelAssociationService.selectTunnelAssociationList(tunnelAssociation);
        ExcelUtil<TunnelAssociation> util = new ExcelUtil<TunnelAssociation>(TunnelAssociation.class);
        return util.exportExcel(list, "隧道关联关系数据");
    }

    /**
     * 获取隧道关联关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:association:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(tunnelAssociationService.selectTunnelAssociationById(id));
    }

    /**
     * 新增隧道关联关系
     */
    @PreAuthorize("@ss.hasPermi('system:association:add')")
    @Log(title = "隧道关联关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TunnelAssociation tunnelAssociation)
    {
        return toAjax(tunnelAssociationService.insertTunnelAssociation(tunnelAssociation));
    }

    /**
     * 修改隧道关联关系
     */
    @PreAuthorize("@ss.hasPermi('system:association:edit')")
    @Log(title = "隧道关联关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TunnelAssociation tunnelAssociation)
    {
        return toAjax(tunnelAssociationService.updateTunnelAssociation(tunnelAssociation));
    }

    /**
     * 删除隧道关联关系
     */
    @PreAuthorize("@ss.hasPermi('system:association:remove')")
    @Log(title = "隧道关联关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tunnelAssociationService.deleteTunnelAssociationByIds(ids));
    }
}
