package com.tunnel.platform.business.vms.core;

import com.ruoyi.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class DeviceStatusHelper {

	/**
	 * 返回状态结果
	 * 
	 * @param isSuccess
	 * @param message
	 * @return
	 */
	public static Map<String, String> getStatusResult(boolean isSuccess, String message) {
		Map<String, String> result = new HashMap<String, String>();
		if (isSuccess) {
			result.put("RESULT", "SUCCESS");
		} else {
			result.put("RESULT", "FALSE");
		}
		if (!StringUtils.isEmpty(message)) {
			result.put("MESSAGE", message);
		}
		return result;
	}

}
