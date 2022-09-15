package com.tunnel.platform.business.vms.core;

import com.tunnel.platform.utils.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class ProtocolRules {
	
	private static final Logger log = LoggerFactory.getLogger(ProtocolRules.class);

	/**
	 * 
	 * 根据规则获取协议分割后的协议数据
	 * 
	 * @param protocol
	 *            协议
	 * @param rules
	 *            协议分解规则
	 * @param splitSign
	 *            分隔符号。
	 * @return
	 */
	public static Map<String, String> getProtocolDatas(String protocol,
                                                       String rules, String splitSign) {
		/*
		 * 1.参数校验
		 */
		if (CommonUtil.isNull(protocol, rules)) {
			return new HashMap<String, String>();
		}
		String[] _rules = rules.split(";");
		Map<String, String> result = new HashMap<String, String>();
		result.put("_protocol_" + System.currentTimeMillis(), protocol);
		for (String rule : _rules) {
			String[] _ruleConent = rule.split(":");
			if (_ruleConent == null || _ruleConent.length != 2)
				continue;
			String key = _ruleConent[1];
			String sequent = _ruleConent[0];
			try {
				result.put(key, getIntevalData(protocol, sequent, splitSign));
			} catch (Exception e) {
				log.warn("[ getProtocolDatas() ] 协议[" + protocol + "]无效！\n" + e.getMessage());
				return null;
			}
		}
		return result;
	}

	/**
	 * 
	 * 获取以空格隔开的字符串的值。
	 * 
	 * @param intevalStr
	 *            协议。
	 * @param sequence
	 *            字符串起始数字。中间用短横线隔开。如："3-9".
	 * @param splitSign
	 *            分隔符号
	 * @return
	 */
	public static String getIntevalData(String intevalStr, String sequence,
                                        String splitSign) {
		if (CommonUtil.isNull(intevalStr, sequence)) {
			throw new IllegalArgumentException(
					"传入的截取字符串[ intevalStr ]或上下标 [ sequence ]为空！");
		}
		if (CommonUtil.isNull(splitSign)) {
			splitSign = " ";
		}
		// 分解sequence
		String[] _sequence = sequence.split("-");
		int length = _sequence.length;
		if (_sequence == null || length < 1 || length > 2)
			throw new IllegalArgumentException("上下标 [ sequence ]的长度出现异常！");
		/*
		 * 解析出上下标。
		 */
		int startSequence = 0;
		int endSequence = 0;

		if (length == 1) {
			startSequence = Integer.parseInt(_sequence[0]);
			endSequence = startSequence;
		} else if (length == 2) {
			startSequence = Integer.parseInt(_sequence[0]);
			endSequence = Integer.parseInt(_sequence[1]);
		}

		// 返回截取的数据。
		return getIntevalData(intevalStr, startSequence, endSequence, splitSign);
	}

	/**
	 * 
	 * 获取以空格隔开的字符串的值。
	 * 
	 * @param protocol
	 *            协议。
	 * @param sequence
	 *            字符串起始数字。中间用短横线隔开。如："3-9".
	 * @param splitSign
	 *            分隔符号
	 * @return
	 */
	public static String getIntevalData(String intevalStr, int startSequent, int endSequent, String splitSign) {
		if (CommonUtil.isNull(intevalStr))
			return null;

		// 如果分隔符为null，则默认为空格。
		if (CommonUtil.isNull(splitSign))
			splitSign = " ";
		String[] _p = intevalStr.split(splitSign);

		// 截取字符串上下标的检测。
		if (endSequent >= _p.length)
			throw new IllegalArgumentException("下标[" + endSequent + "],超过字符数组长度[" + intevalStr + "]");
		if (startSequent > endSequent || startSequent < 0 || endSequent < 0)
			throw new IllegalArgumentException("开始下标[" + startSequent + "],结束下标[" + endSequent + "] 有误。");

		StringBuffer sb = new StringBuffer();
		// 截取字符串
		for (int i = startSequent; i <= endSequent; i++) {
			sb.append(_p[i]);
			if (i < endSequent)
				sb.append(splitSign);
		}
		return sb.toString().trim();
	}

}
