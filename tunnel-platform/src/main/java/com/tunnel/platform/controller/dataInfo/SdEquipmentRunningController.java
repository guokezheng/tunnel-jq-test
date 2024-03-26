package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * 设备Controller
 * 
 * @author yanghousheng
 * @date 2020-12-15
 */
@RestController
@RequestMapping("/equipmentRunning")
public class SdEquipmentRunningController extends BaseController {
	
	 @Autowired
	    private ISdDevicesService sdDevicesService;

	    /**
	     * 查询设备列表
	     */
		@ApiOperation("查询设备列表")
	    @GetMapping("/list")
	    public TableDataInfo list(SdDevices sdDevices)
	    {
	        startPage();
	        List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
	        return getDataTable(list);
	    }

}
