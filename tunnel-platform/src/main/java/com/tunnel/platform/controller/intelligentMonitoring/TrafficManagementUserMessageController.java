package com.tunnel.platform.controller.intelligentMonitoring;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.tunnel.platform.domain.intelligentMonitoring.TrafficManagementUserMessage;
import com.tunnel.platform.domain.intelligentMonitoring.TrafficManagementUserMessageDTO;
import com.tunnel.platform.service.intelligentMonitoring.ITrafficManagementUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知管理Controller
 *
 * @author ruoyi
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/business/trafficManagementUserMessage")
public class TrafficManagementUserMessageController extends BaseController
{
    @Autowired
    private ITrafficManagementUserMessageService trafficManagementUserMessageService;

    /**
     * 查询通知管理列表
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagementUserMessage:list')")
    @GetMapping("/list")
    public TableDataInfo list(TrafficManagementUserMessage trafficManagementUserMessage)
    {

        LoginUser loginUser = getLoginUser();

        startPage();
        List<TrafficManagementUserMessageDTO> list = trafficManagementUserMessageService.selectTrafficManagementUserMessageList(trafficManagementUserMessage, loginUser.getUser().getUserId());
        return getDataTable(list);
    }

    /**
     * 导出通知管理列表
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagementUserMessage:export')")
    @Log(title = "通知管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TrafficManagementUserMessage trafficManagementUserMessage)
    {
        LoginUser loginUser = getLoginUser();
        List<TrafficManagementUserMessageDTO> list = trafficManagementUserMessageService.selectTrafficManagementUserMessageList(trafficManagementUserMessage, loginUser.getUser().getUserId());
        ExcelUtil<TrafficManagementUserMessageDTO> util = new ExcelUtil<TrafficManagementUserMessageDTO>(TrafficManagementUserMessageDTO.class);
        return util.exportExcel(list, "通知管理");
    }

    /**
     * 获取通知管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagementUserMessage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return AjaxResult.success(trafficManagementUserMessageService.selectTrafficManagementUserMessageById(id));
    }

    /**
     * 新增通知管理
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagementUserMessage:add')")
    @Log(title = "通知管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TrafficManagementUserMessage trafficManagementUserMessage)
    {
        return toAjax(trafficManagementUserMessageService.insertTrafficManagementUserMessage(trafficManagementUserMessage));
    }

    /**
     * 修改通知管理
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagementUserMessage:edit')")
    @Log(title = "通知管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TrafficManagementUserMessage trafficManagementUserMessage)
    {
        return toAjax(trafficManagementUserMessageService.updateTrafficManagementUserMessage(trafficManagementUserMessage));
    }

    /**
     * 删除通知管理
     */
    @PreAuthorize("@ss.hasPermi('business:trafficManagementUserMessage:remove')")
    @Log(title = "通知管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(trafficManagementUserMessageService.deleteTrafficManagementUserMessageByIds(ids));
    }
}
