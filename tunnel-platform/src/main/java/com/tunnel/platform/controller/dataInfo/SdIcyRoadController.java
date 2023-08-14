package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdIcyRoad;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdIcyRoadService;
import com.tunnel.platform.business.vms.core.DevicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 道路结冰记录Controller
 *
 * @author liubaokui
 * @date 2021-03-26
 */
@RestController
@RequestMapping("/icyRoad")
public class SdIcyRoadController extends BaseController
{
    @Autowired
    private ISdIcyRoadService sdIcyRoadService;
    @Autowired
    private ISdDevicesService sdDevicesService;

    public Map<String, SdIcyRoad> sdIcyRoadMap = new HashMap<String,SdIcyRoad>();

    /**
     * 查询道路结冰记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdIcyRoad sdIcyRoad)
    {
        startPage();
        List<SdIcyRoad> list = sdIcyRoadService.selectSdIcyRoadList(sdIcyRoad);
        return getDataTable(list);
    }

    /**
     * 导出道路结冰记录列表
     */
    /*@Log(title = "道路结冰记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdIcyRoad sdIcyRoad) throws IOException
    {
        List<SdIcyRoad> list = sdIcyRoadService.selectSdIcyRoadList(sdIcyRoad);
        ExcelUtil<SdIcyRoad> util = new ExcelUtil<SdIcyRoad>(SdIcyRoad.class);
        util.exportExcel(response, list, "icyRoad");
    }*/

    /**
     * 获取道路结冰记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdIcyRoadService.selectSdIcyRoadById(id));
    }

    /**
     * 新增道路结冰记录
     */
    @Log(title = "道路结冰记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdIcyRoad sdIcyRoad)
    {
        return toAjax(sdIcyRoadService.insertSdIcyRoad(sdIcyRoad));
    }

    /**
     * 修改道路结冰记录
     */
    @Log(title = "道路结冰记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdIcyRoad sdIcyRoad)
    {
        return toAjax(sdIcyRoadService.updateSdIcyRoad(sdIcyRoad));
    }

    /**
     * 删除道路结冰记录
     */
    @Log(title = "道路结冰记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdIcyRoadService.deleteSdIcyRoadByIds(ids));
    }

    /**
     * 查询实时道路结冰记录
     */
    @GetMapping("/getIcy")
    public AjaxResult getIcy(SdIcyRoad sdIcyRoad)
    {
    	String deviceId = sdIcyRoad.getDeviceId();
    	if(sdIcyRoadMap.containsKey(deviceId)){
    		sdIcyRoad = sdIcyRoadMap.get(sdIcyRoad.getDeviceId());
    	}

        return AjaxResult.success("", sdIcyRoad);
    }


    /**
     * 获取道路结冰信息
     * 3秒调用一次
     */
  //  @Scheduled(fixedRate = 3000)
    public void getIcyData() {
        // 命令
        String command = "01 10 01 50 16 f0 0f 02 2f 10 06 64 00 6e 00 58 02 20 03 2a 03 84 03 03 1e 92 04";
        // 设备ID
        SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(110l);
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        for(int i=0;i<list.size();i++){
        	String deviceId = list.get(i).getEqId();
        	try {
        		String ip = list.get(i).getQueryPointAddress();
                // 返回内容
                String content = DevicesManager.getInstance().executeCommand(deviceId + "," + ip, command , "110");
                // 协议解析
                sdIcyRoadService.ParsingProtocol(content,deviceId);
            } catch (Exception e) {
              //  System.out.print("ERROR");
                // 存在替换，不存在添加
                SdIcyRoad sdIcyRoad = new SdIcyRoad();
                sdIcyRoadService.ErrorParsingProtocol(sdIcyRoad,deviceId);

            }
        	list.get(i).getEqId();
        }
    }

    /**
     * 获取道路结冰存储
     * 3分钟秒调用一次
     */
//    @Scheduled(fixedRate = 180000)
    public void inserttoIcyRecord() {
    	SdDevices sdDevices = new SdDevices();
        sdDevices.setEqType(110l);
        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
        for(int i=0;i<list.size();i++){
        	SdIcyRoad sdIcyRoad = new SdIcyRoad();
        	sdIcyRoad.setDeviceId(list.get(i).getEqId());
        //	sdIcyRoadService.insertSdIcyRoad(sdIcyRoad);
        }
    }

    /**
     * 获取雾区诱导信息
     */
   /* @Scheduled(fixedDelay = 1800000)
    public void getFogInductionData() {
        Map<String, String> values = new HashMap<String, String>();
        // 命令
        String command = "67 68 00 02 00 01 07 0D 0A";//心跳
        // 设备ID
        String deviceId = "14";
        try {
            String content = DevicesManager.getInstance().executeCommand(deviceId, command , "112");
            System.out.println("心跳："+content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Scheduled(fixedDelay = 60000)
    public void getFogInductionData1() {
        Map<String, String> values = new HashMap<String, String>();
        // 命令
        String command = "67 68 00 05 00 05 01 FF FF 01 0D 0A 00";//诱导灯闪烁
        // 设备ID
        String deviceId = "14";
        try {

            String content = DevicesManager.getInstance().executeCommand1(deviceId, command , "112");
            System.out.println("诱导灯："+ MinaClient.dataCache);
           // generateCmd(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public void  selectHostConfiguration(){
	   //查询主机配置指令
	   String command = "67 68 00 02 00 03 09 0D 0A 00";
	   //主机id
	   String hostId = "14";
	   try {
           // 返回内容
           String content = DevicesManager.getInstance().hostConfigurationExecuteCommand(hostId, command , "112");
           System.out.println(content);
       } catch (Exception e) {
           e.printStackTrace();
       }
   } */



}
