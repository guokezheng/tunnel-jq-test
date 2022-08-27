package com.tunnel.platform.controller.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdControlCode;
import com.tunnel.platform.domain.dataInfo.SdDeviceCmd;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.service.dataInfo.ISdControlCodeService;
import com.tunnel.platform.service.dataInfo.ISdDeviceCmdService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.utils.util.RadixUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备指令Controller
 *
 * @author zhangweitian
 * @date 2020-09-04
 */
@RestController
@RequestMapping("/devCmd")
public class SdDeviceCmdController extends BaseController
{
    @Autowired
    private ISdDeviceCmdService sdDeviceCmdService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdControlCodeService sdControlCodeService;

    /**
     * 查询设备指令列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdDeviceCmd sdDeviceCmd)
    {
        startPage();
        List<SdDeviceCmd> list = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
        return getDataTable(list);
    }

    /**
     * 获取设备指令详细信息
     */
    @GetMapping(value = "/{codeId}")
    public AjaxResult getInfo(@PathVariable("codeId") Long codeId)
    {
        return AjaxResult.success(sdDeviceCmdService.selectSdDeviceCmdById(codeId));
    }

    /**
     * 新增设备指令
     */
    @Log(title = "设备指令", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdDeviceCmd sdDeviceCmd)
    {
        //这部分解析代码不再适用，实际项目中可能直接录入完整的指令，不需要拼接生成。
//    	SdControlCode constant =new SdControlCode();
//    	constant.setControlKey("数据报头部");
//    	List<SdControlCode> list1 = sdControlCodeService.selectSdControlCodeByKey(constant);
//    	String valueHeader =list1.get(0).getControlValue();
//        String cmdPlcId =sdDeviceCmd.getCodePlcId();
//        SdDevices host = sdDevicesService.selectSdDevicesById(cmdPlcId);
//    	String serverIP =host.getIp();
//    	String[] ipServer = serverIP.split("\\.");
//    	int serverIPLast = Integer.parseInt(ipServer[3]);
//    	String serverIPLastHex = RadixUtil.intToHex(serverIPLast);
//    	String serverIPLastHexReplace = valueHeader.replace("##",serverIPLastHex);
//    	constant.setControlKey("本机IP地址");
//    	List<SdControlCode> list2 = sdControlCodeService.selectSdControlCodeByKey(constant);
//    	String localAddress =list2.get(0).getControlValue();
//    	String[] ipLocal =localAddress.split("\\.");
//    	int localAddressLast = Integer.parseInt(ipLocal[3]);
//    	String localAddressLastHex = RadixUtil.intToHex(localAddressLast);
//    	String localAddressLastHexReplace = serverIPLastHexReplace.replace("@@",localAddressLastHex);
//    	String command1 =sdDeviceCmd.getCommand();
//    	String regex = "(.{2})";
//    	command1 = command1.replaceAll(regex, "$1 ");
//    	StringBuffer s = new StringBuffer(command1);
//    	s.insert(0, " ");
//    	String t = new String(s);
//    	String command2 = localAddressLastHexReplace.concat(t);
//    	sdDeviceCmd.setCommand(command2);

        return toAjax(sdDeviceCmdService.insertSdDeviceCmd(sdDeviceCmd));
    }

    /**
     * 修改设备指令
     */
    @Log(title = "设备指令", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdDeviceCmd sdDeviceCmd)
    {
    	/*SdControlCode constant =new SdControlCode();
    	constant.setControlKey("数据报头部");
    	List<SdControlCode> list1 = sdControlCodeService.selectSdControlCodeByKey(constant);
    	String valueHeader =list1.get(0).getControlValue();
    	Long cmdPlcId =sdDeviceCmd.getCodePlcId();
    	SdHosts host = sdHostsService.selectSdHostsById(cmdPlcId);
    	String serverIP =host.getPlcIp();
    	String[] ipServer = serverIP.split("\\.");
    	int serverIPLast = Integer.parseInt(ipServer[3]);
    	RadixUtil radixUtil =new RadixUtil();
    	String serverIPLastHex =radixUtil.intToHex(serverIPLast);
    	String serverIPLastHexReplace = valueHeader.replace("##",serverIPLastHex);
    	constant.setControlKey("本机IP地址");
    	List<SdControlCode> list2 = sdControlCodeService.selectSdControlCodeByKey(constant);
    	String localAddress =list2.get(0).getControlValue();
    	String[] ipLocal =localAddress.split("\\.");
    	int localAddressLast = Integer.parseInt(ipLocal[3]);
    	String localAddressLastHex =radixUtil.intToHex(localAddressLast);
    	String localAddressLastHexReplace = serverIPLastHexReplace.replace("@@",localAddressLastHex);
    	Long codeId =sdDeviceCmd.getCodeId();
    	String command3 =sdDeviceCmdService.selectSdDeviceCmdById(codeId).getCommand();
    	String command1 =sdDeviceCmd.getCommand();
	    out:if(command1.equals(command3) ){
	    		command1 ="";
			}else{
				break out;
			}
    	String regex = "(.{2})";
    	command1 = command1.replaceAll(regex, "$1 ");
    	StringBuffer s = new StringBuffer(command1);
    	out1:if(command1.length()==0){
    		break out1;
		}else{
			s.insert(0, " ");
		}
    	String t = new String(s);
    	String command2 = localAddressLastHexReplace.concat(t);
    	int changdu1 = localAddressLastHexReplace.length();
    	int changdu2 = command3.length();
    	String command4 = command3.substring(changdu1,changdu2);
		if (command1.isEmpty()) {
			String command5 = command2.concat(command4);
			sdDeviceCmd.setCommand(command5);
		} else {
	        sdDeviceCmd.setCommand(command2);
		}*/
		List<SdDeviceCmd> list = sdDeviceCmdService.selectSdDeviceCmdList(sdDeviceCmd);
		if(list.size()>0){
			sdDeviceCmd.setCodeId(list.get(0).getCodeId());
			return toAjax(sdDeviceCmdService.updateSdDeviceCmd(sdDeviceCmd));
		}else{
			return toAjax(sdDeviceCmdService.insertSdDeviceCmd(sdDeviceCmd));
		}
    }

    /**
     * 删除设备指令
     */
    @Log(title = "设备指令", businessType = BusinessType.DELETE)
	@DeleteMapping("/{codeIds}")
    public AjaxResult remove(@PathVariable Long[] codeIds)
    {
        return toAjax(sdDeviceCmdService.deleteSdDeviceCmdByIds(codeIds));
    }
}
