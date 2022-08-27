package com.tunnel.platform.controller.informationBoard;

import com.tunnel.platform.domain.informationBoard.SdAuditVocabulary;
import com.tunnel.platform.service.informationBoard.ISdAuditVocabularyService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 情报板敏感字管理Controller
 * 
 * @author 刘方堃
 * @date 2021-11-29
 */
@RestController
@RequestMapping("/system/vocabulary")
public class SdAuditVocabularyController extends BaseController
{
    @Autowired
    private ISdAuditVocabularyService sdAuditVocabularyService;

    /**
     * 查询情报板敏感字管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:list')")
    @GetMapping("/list")
    public TableDataInfo list(SdAuditVocabulary sdAuditVocabulary)
    {
        startPage();
        List<SdAuditVocabulary> list = sdAuditVocabularyService.selectSdAuditVocabularyList(sdAuditVocabulary);
        return getDataTable(list);
    }

    /**
     * 导出情报板敏感字管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:export')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdAuditVocabulary sdAuditVocabulary)
    {
        List<SdAuditVocabulary> list = sdAuditVocabularyService.selectSdAuditVocabularyList(sdAuditVocabulary);
        ExcelUtil<SdAuditVocabulary> util = new ExcelUtil<SdAuditVocabulary>(SdAuditVocabulary.class);
        return util.exportExcel(list, "情报板敏感字列表");
    }

    /**
     * 获取情报板敏感字管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdAuditVocabularyService.selectSdAuditVocabularyById(id));
    }

    /**
     * 新增情报板敏感字管理
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:add')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdAuditVocabulary sdAuditVocabulary)
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdAuditVocabulary.setCreatTime(formatter.format(currentTime));
        return toAjax(sdAuditVocabularyService.insertSdAuditVocabulary(sdAuditVocabulary));
    }

    /**
     * 修改情报板敏感字管理
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:edit')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdAuditVocabulary sdAuditVocabulary)
    {
        return toAjax(sdAuditVocabularyService.updateSdAuditVocabulary(sdAuditVocabulary));
    }

    /**
     * 删除情报板敏感字管理
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:remove')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdAuditVocabularyService.deleteSdAuditVocabularyByIds(ids));
    }
}
