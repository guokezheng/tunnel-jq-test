package com.tunnel.platform.business.vms.device;

import java.util.HashMap;
import java.util.Map;


/**
 * 情报板信息处理类
 * @date 2018年11月21日
 *
 */
public class BoardUtil {
	
	/**
	 * 处理类实例化。
	 */
	private static BoardUtil instance;

	public static BoardUtil getInstance() {
		if (instance == null)
			instance = new BoardUtil();
		return instance;
	}

	private BoardUtil() {
	}
	/**
	 * 
	 * @Description: 将指定格式的字符串，分解成数组。格式如:FAULT:02;SUCCESS:Y;
	 * @param str
	 *            格式为 key:value;key:value;key:value
	 * @return
	 */
	public static Map<String, String> splitStringToMap(String str) {
		Map<String, String> result = new HashMap<String, String>();
		if (isNull(str))
			return result;
		String[] strs = str.split(";");
		for (String _str : strs) {
			if (isNull(strs))
				continue;
			String[] keyValue = _str.split(":");
			if (keyValue == null || keyValue.length != 2)
				continue;
			result.put(keyValue[0], keyValue[1]);
		}
		return result;
	}
	
	/**
	 * 
	 * @Description: 判断字符串是否为空。
	 * @param strs
	 * @return
	 */
	public static boolean isNull(String... strs) {
		for (String str : strs) {
			if (str == null || str.trim().length() <= 0)
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Description: 字符串按照分隔符切分，返回指定索引的段。查不到则返回null
	 * @param str
	 *            待搜索的字符串
	 * @param index
	 *            索引
	 * @param split
	 *            分隔符。
	 * @return
	 */
	public static String find(String str, int index, String split) {
		if (isNull(str, split))
			return null;
		if (index < 0)
			return null;
		String[] strs = str.split(split);
		if (strs.length > index)
			return strs[index];
		return null;
	}
	
	/**
	 * 
	 * @Description: 返回消息。
	 * @param message
	 * @return
	 */
	public Map<String, String> getMessageResult(String message) {
		Map<String, String> result = new HashMap<String, String>();
		if (!isNull(message)) {
			result.put("MESSAGE", message);
		}
		return result;
	}
}
