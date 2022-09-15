package com.tunnel.platform.business.vms;


import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.platform.domain.informationBoard.SdIotDevice;
import com.tunnel.platform.service.informationBoard.ISdIotDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 设备配置类
 *
 * @author Alvin
 * @date 2018年7月13日
 */
@Component
public class DevicesConfig {


    @Autowired
    private ISdIotDeviceService sdIotDeviceService = SpringUtils.getBean(ISdIotDeviceService.class);

    private static DevicesConfig iIot;

    @PostConstruct
    public void init() {
        iIot = this;
        iIot.sdIotDeviceService = this.sdIotDeviceService;
    }

    /**
     * 根据设备的id获取设备的配置信息
     *
     * @param id
     * @return
     */
    public DevicesBean getDevicesById(String id) {
        SdIotDevice iotDevice = iIot.sdIotDeviceService.selectDeviceAccessById(Long.parseLong(id));
        DevicesBean devicesBean = new DevicesBean();
        devicesBean.setId(id);
        devicesBean.setIp(iotDevice.getDeviceIp());
        devicesBean.setPort(Integer.valueOf(iotDevice.getDevicePort()));
        devicesBean.setVender_version(iotDevice.getProtocolName());
        devicesBean.setType(iotDevice.getDeviceTypeNumber().toString());
        return devicesBean;
    }

    /**
     * 根据设备的id获取设备的配置信息
     *
     * @param id
     * @return
     */
/*	public DevicesBean getDevicesInfo(String id) {
		// 设备配置未更改先写死

		SdIotDevice iotDevice = iIot.sdIotDeviceService.selectDeviceAccessById(Long.parseLong(id));
		DevicesBean devicesBean = new DevicesBean();
		devicesBean.setId(id);
		devicesBean.setIp("10.7.98.44");
		devicesBean.setPort(Integer.valueOf("4001"));
		return devicesBean;
	}*/

    /**
     * 获取隧道诱导信息
     *
     * @param id
     * @return
     */
    public DevicesBean getDevicesFogInduction(String id) {
        DevicesBean devicesBean = new DevicesBean();
        devicesBean.setId(id);
        devicesBean.setIp("192.168.1.107");
        devicesBean.setPort(Integer.valueOf("1030"));
        return devicesBean;
    }


}