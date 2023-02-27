package com.tunnel.platform.controller.informationBoard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.business.domain.informationBoard.IotBoardVocabulary;
import com.tunnel.business.service.informationBoard.IIotBoardVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 情报板敏感字管理Controller
 *
 * @author 刘方堃
 * @date 2021-11-29
 */
@RestController
@RequestMapping("/system/vocabulary")
public class IotBoardVocabularyController extends BaseController
{
    @Autowired
    private IIotBoardVocabularyService iotBoardVocabularyService;

    /**
     * 查询情报板敏感字管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:list')")
    @GetMapping("/list")
    public TableDataInfo list(IotBoardVocabulary sdAuditVocabulary)
    {
        startPage();
        List<IotBoardVocabulary> list = iotBoardVocabularyService.selectIotBoardVocabularyList(sdAuditVocabulary);
        return getDataTable(list);
    }

    /**
     * 导出情报板敏感字管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:export')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(IotBoardVocabulary sdAuditVocabulary)
    {
        List<IotBoardVocabulary> list = iotBoardVocabularyService.selectIotBoardVocabularyList(sdAuditVocabulary);
        ExcelUtil<IotBoardVocabulary> util = new ExcelUtil<IotBoardVocabulary>(IotBoardVocabulary.class);
        return util.exportExcel(list, "情报板敏感字列表");
    }

    /**
     * 获取情报板敏感字管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(iotBoardVocabularyService.selectIotBoardVocabularyById(id));
    }

    /**
     * 新增情报板敏感字管理
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:add')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody IotBoardVocabulary sdAuditVocabulary)
    {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdAuditVocabulary.setCreatTime(formatter.format(currentTime));
        return toAjax(iotBoardVocabularyService.insertIotBoardVocabulary(sdAuditVocabulary));
    }

    /**
     * 修改情报板敏感字管理
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:edit')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody IotBoardVocabulary sdAuditVocabulary)
    {
        return toAjax(iotBoardVocabularyService.updateIotBoardVocabulary(sdAuditVocabulary));
    }

    /**
     * 删除情报板敏感字管理
     */
    @PreAuthorize("@ss.hasPermi('system:vocabulary:remove')")
    @Log(title = "情报板敏感字管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(iotBoardVocabularyService.deleteIotBoardVocabularyByIds(ids));
    }

    @PostMapping("/checkIotBoardContent")
    public AjaxResult checkIotBoardContent(@RequestBody Map<String, Object> map) {
        return AjaxResult.success(iotBoardVocabularyService.checkIotBoardContent(map));
    }
}
