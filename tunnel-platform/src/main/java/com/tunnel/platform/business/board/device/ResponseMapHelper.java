package com.tunnel.platform.business.board.device;


import com.tunnel.platform.utils.util.CommonUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @Description 返回信息辅助类。
 * 
 * @author 
 * @since 1.4
 * @version 0.1
 */

public class ResponseMapHelper {

	/**
	 * 懒汉模式实例化。
	 */
	private static ResponseMapHelper instance;

	public static ResponseMapHelper getInstance() {
		if (instance == null)
			instance = new ResponseMapHelper();
		return instance;
	}

	private ResponseMapHelper() {
	}

	/**
	 * 
	 * @Description: 返回消息。
	 * @param message
	 * @return
	 */
	public Map<String, String> getMessageResult(String message) {
		Map<String, String> result = new HashMap<String, String>();
		if (!CommonUtil.isNull(message)) {
			result.put("MESSAGE", message);
		}
		return result;
	}

	/**
	 * 
	 * @Description: 返回状态结果。
	 * @param isSuccess
	 *            设备状态。如果设备正常，则为true；否则为false；
	 * @param message
	 *            设备状态信息。显示设备状态的描述信息。
	 * @return
	 */
	public Map<String, String> getStatusResult(boolean isSuccess, String message) {
		Map<String, String> result = new HashMap<String, String>();
		if (isSuccess) {
			result.put("RESULT", "SUCCESS");
		} else {
			result.put("RESULT", "FALSE");
		}
		if (!CommonUtil.isNull(message)) {
			result.put("MESSAGE", message);
		}
		return result;
	}

}
