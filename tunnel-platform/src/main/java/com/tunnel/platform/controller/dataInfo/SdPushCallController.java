package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.platform.domain.dataInfo.SdPushCall;
import com.tunnel.platform.service.dataInfo.ISdPushCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 紧急电话推送记录Controller
 * 
 * @author wangx
 * @date 2021-03-15
 */
@RestController
@RequestMapping("/pushCall")
public class SdPushCallController extends BaseController
{
    @Autowired
    private ISdPushCallService sdPushCallService;

    /**
     * 查询紧急电话推送记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdPushCall sdPushCall)
    {
        startPage();
        List<SdPushCall> list = sdPushCallService.selectSdPushCallList(sdPushCall);
        return getDataTable(list);
    }

    /**
     * 导出紧急电话推送记录列表
     */
   /* @PreAuthorize(hasPermi = "system:pushCall:export")
    @Log(title = "紧急电话推送记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdPushCall sdPushCall) throws IOException
    {
        List<SdPushCall> list = sdPushCallService.selectSdPushCallList(sdPushCall);
        ExcelUtil<SdPushCall> util = new ExcelUtil<SdPushCall>(SdPushCall.class);
        util.exportExcel(response, list, "pushCall");
    }*/

    /**
     * 获取紧急电话推送记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdPushCallService.selectSdPushCallById(id));
    }

    /**
     * 新增紧急电话推送记录
     */
    @Log(title = "紧急电话推送记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdPushCall sdPushCall)
    {
        return toAjax(sdPushCallService.insertSdPushCall(sdPushCall));
    }

    /**
     * 修改紧急电话推送记录
     */
    @Log(title = "紧急电话推送记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdPushCall sdPushCall)
    {
        return toAjax(sdPushCallService.updateSdPushCall(sdPushCall));
    }

    /**
     * 删除紧急电话推送记录
     */
    @Log(title = "紧急电话推送记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdPushCallService.deleteSdPushCallByIds(ids));
    }
}
