package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.tunnel.platform.domain.dataInfo.SdLipowerDevice;
import com.tunnel.platform.service.dataInfo.ISdLipowerDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 照明设备信息Controller
 * 
 * @author wangx
 * @date 2021-03-15
 */
@Component
@RestController
@RequestMapping("/lipowerDevice")
public class SdLipowerDeviceController extends BaseController
{
    @Autowired
    private ISdLipowerDeviceService sdLipowerDeviceService;
    
    // 存起来
    public List<SdLipowerDevice> sdLipowerDeviceList = new ArrayList<SdLipowerDevice>();
    
    public int state = 0;

    /**
     * 查询照明设备信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdLipowerDevice sdLipowerDevice)
    {
        startPage();
        List<SdLipowerDevice> list = sdLipowerDeviceService.selectSdLipowerDeviceList(sdLipowerDevice);
        return getDataTable(list);
    }
    
    @GetMapping("/getLiPowerDevice")
    public AjaxResult sdLipowerDevice() {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("list", sdLipowerDeviceList);
    	map.put("state", state);
        return AjaxResult.success(map);
    }
    
    @GetMapping("/initLipowerDevice")
    public void initLipowerDevice() {
    	/*String[] groupArr = {"6"};
    	SdLipowerDevice sdLipowerDevice = new SdLipowerDevice();
    	List<SdLipowerDevice> lists = new ArrayList<SdLipowerDevice>();
    	for(int i=0;i<groupArr.length;i++){
    		sdLipowerDevice.setTunnelId(groupArr[i]);
    		List<SdLipowerDevice> list 
    			= sdLipowerDeviceService.selectSdLipowerDeviceList(sdLipowerDevice);
    		lists.addAll(list);
    	}
    	sdLipowerDeviceInitList = lists;*/
    }
    
    /**
     * 设备状态查询
     */
//    @Scheduled(fixedRate = 1000)
    public void getLightingSystemInfo()
    {
    	// 隧道ID
    	// 白彦 
    	//String[] groupArr = {"5","6","7"};
    	// 山亭
    	String[] groupArr = {"9","10","11","12"};
    	int stateLighting = 0;
    	List<SdLipowerDevice> lists = new ArrayList<SdLipowerDevice>();
    	for(int i=0;i<groupArr.length;i++){
    		// 查询设备状态接口
        	int result = sdLipowerDeviceService.getLightingSystemInfo(groupArr[i],lists);
        	if(result == -1){
        		continue;
        	}else if(result == -2){
        		stateLighting = -2;
        		break;
        	}
    	}
    	state = stateLighting;
    	sdLipowerDeviceList = lists;
    }

}
