package com.tunnel.platform.controller.informationBoard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.informationBoard.SdIotDevice;
import com.tunnel.business.service.informationBoard.ISdIotDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备列Controller
 *
 * @author yanghousheng
 * @date 2020-09-11
 */
@RestController
@RequestMapping("/information")
public class SdIotDeviceController extends BaseController
{
    @Autowired
    private ISdIotDeviceService sdIotDeviceService;

    /**
     * 查询设备列列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdIotDevice sdIotDevice)
    {
        startPage();
        List<SdIotDevice> list = sdIotDeviceService.selectIotDeviceArrayList(sdIotDevice);
        return getDataTable(list);
    }

    @GetMapping("/getIotBoardList")
    public AjaxResult getIotBoardList(SdIotDevice sdIotDevice)
    {
        return AjaxResult.success(sdIotDeviceService.getIotBoardList(sdIotDevice));
    }

    @GetMapping(value = "/getdevicessize")
    public AjaxResult getDevicesSize(SdIotDevice sdIotDevice)
    {
        return AjaxResult.success(sdIotDeviceService.getDevicesSize(sdIotDevice));
    }

    /**
     * 导出设备列列表
     */
   /* @Log(title = "设备列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdIotDevice sdIotDevice) throws IOException
    {
        List<SdIotDevice> list = sdIotDeviceService.selectIotDeviceList(sdIotDevice);
        ExcelUtil<SdIotDevice> util = new ExcelUtil<SdIotDevice>(SdIotDevice.class);
        util.exportExcel(response, list, "device");
    }*/

    /**
     * 大屏查询事件报警列表
     */
    @GetMapping("/bigscreenBoardList")
    public String[][] bigscreenBoardList()
    {
    	/*List<SdEvent> list = sdEventService.selectSdEventList(sdEvent);
    	return list;*/
    	String[][] boardList={{"2020-12-23 19:25:00", "隧道限速80m/s"},{"2020-12-23 19:26:00","前方隧道，请注意保持车距"},{"2020-12-23 19:27:00", "道路千万条，安全第一条"},{"2020-12-23 19:28:00", "前方隧道正在施工，请注意车速"}};
    	for(int i=0;i<boardList.length;i++){
    		for(int j=0;j<boardList[i].length;j++){
    			/*System.out.println(a[i][j]);*/
    		}
    	}
    	return boardList;
    }

    /**
     * 获取设备列详细信息
     */
    @GetMapping(value = "/{deviceId}")
    public AjaxResultb getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return AjaxResultb.success(sdIotDeviceService.selectIotDeviceById(deviceId));
    }

    /**
     * 新增设备列
     */
    @Log(title = "设备列", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdIotDevice sdIotDevice)
    {
        return toAjax(sdIotDeviceService.insertIotDevice(sdIotDevice));
    }

    /**
     * 修改设备列
     */
    @Log(title = "设备列", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdIotDevice sdIotDevice)
    {
        return toAjax(sdIotDeviceService.updateIotDevice(sdIotDevice));
    }

/*    *//**
     * 删除设备列
     *//*
    @PreAuthorize("@ss.hasPermi('system:device:remove')")
    @Log(title = "设备列", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(sdIotDeviceService.deleteIotDeviceByIds(deviceIds));
    }*/
}
