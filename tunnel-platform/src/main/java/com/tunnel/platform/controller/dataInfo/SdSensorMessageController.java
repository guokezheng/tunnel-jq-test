package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdSensorMessage;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdSensorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 传感器采集数据信息Controller
 *
 * @author yanghousheng
 * @date 2020-12-25
 */
@RestController
@RequestMapping("/message")
public class SdSensorMessageController extends BaseController
{
    @Autowired
    private ISdSensorMessageService sdSensorMessageService;
    @Autowired
    private ISdDevicesService sdDevicesService;

    /**
     * 查询传感器采集数据信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdSensorMessage sdSensorMessage)
    {
        startPage();
        List<SdSensorMessage> list = sdSensorMessageService.selectSdSensorMessageList(sdSensorMessage);
        return getDataTable(list);
    }


    @GetMapping(value = "/eqs")
    public AjaxResult getDeviceInfo(@RequestParam("tunnelId") String tunnelId){
		List<SdDevices> list = sdDevicesService.selectSensorListByTunnelId(tunnelId,null);
    	return AjaxResult.success(list);
    }

    /**
     * 导出传感器采集数据信息列表
     */
    /*@Log(title = "传感器采集数据信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdSensorMessage sdSensorMessage) throws IOException
    {
        List<SdSensorMessage> list = sdSensorMessageService.selectSdSensorMessageList(sdSensorMessage);
        ExcelUtil<SdSensorMessage> util = new ExcelUtil<SdSensorMessage>(SdSensorMessage.class);
        util.exportExcel(response, list, "message");
    }*/

    /**
     * 获取传感器采集数据信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdSensorMessageService.selectSdSensorMessageById(id));
    }

    /**
     * 新增传感器采集数据信息
     */
    @Log(title = "传感器采集数据信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdSensorMessage sdSensorMessage)
    {
        return toAjax(sdSensorMessageService.insertSdSensorMessage(sdSensorMessage));
    }

    /**
     * 修改传感器采集数据信息
     */
    @Log(title = "传感器采集数据信息", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/update")
    public AjaxResult edit(@RequestBody SdSensorMessage sdSensorMessage)
    {
        return toAjax(sdSensorMessageService.updateSdSensorMessage(sdSensorMessage));
    }

    /**
     * 删除传感器采集数据信息
     */
    @Log(title = "传感器采集数据信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/del/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdSensorMessageService.deleteSdSensorMessageByIds(ids));
    }

    /**
     * 查询传感器采集数据信息
     */
	@GetMapping("/select")
    public AjaxResult select(SdSensorMessage sdSensorMessage)
    {
		List<SdSensorMessage> list = sdSensorMessageService.seleteSdSensorMessageByTime(sdSensorMessage);
//		sdSensorMessage.setEqTunnelId("S29-LinYiCompany-BaiYanStation-002");
//		sdSensorMessage.setEqType("120");
//		sdSensorMessage.setEqId("S29-LinYiCompany-BaiYanStation-002-LMZT-001");
//		sdSensorMessage.setSensorValue("100");
//		sdSensorMessage.setGettime(new Date());
//		sdSensorMessage.setCreateTime(new Date());
//        sdSensorMessageService.insertSdSensorMessage(sdSensorMessage);
    	return AjaxResult.success(list);
    }


}
