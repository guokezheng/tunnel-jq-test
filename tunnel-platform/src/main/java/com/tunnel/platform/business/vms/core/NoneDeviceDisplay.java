package com.tunnel.platform.business.vms.core;

import java.util.List;

/**
 * 空值
 * 
 * @author Alvin
 * @date 2018年7月13日
 */
public class NoneDeviceDisplay implements IDeviceDisplay {

	@Override
	public String getCurrentBrightness(String responseContent) {
		return null;
	}

	@Override
	public List<String> getDeviceFault(String responseContent) {
		return null;
	}

	@Override
	public String getCurrentDisplay(String responseContent) {
		return null;
	}

	@Override
	public String getPlayListDisplay(String responseContent) {
		return null;
	}

}
