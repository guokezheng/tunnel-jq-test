package com.tunnel.platform.business.board.core;

import java.util.Map;

public class ProtocolAnalysis {

	private final String protocol;

	private final String rules;

	/**
	 * 
	 * @param protocol
	 * @param rules
	 *            。格式如下：
	 *            "0:header;1-2:address;3-6:data;1-6:bodyData;7-8:crc;9:footer";
	 */
	public ProtocolAnalysis(String protocol, String rules) {
		this.protocol = protocol;
		this.rules = rules;
	}

	/**
	 * 
	 * 获取协议解析数据。
	 * 
	 * @return
	 */
	public Map<String, String> getDatas(String splitSign) {
		return ProtocolRules.getProtocolDatas(protocol, rules, splitSign);
	}

	/**
	 * 
	 * 获取协议解析数据。
	 * 
	 * @return
	 */
	public Map<String, String> getDatas() {
		return ProtocolRules.getProtocolDatas(protocol, rules, " ");
	}

}
