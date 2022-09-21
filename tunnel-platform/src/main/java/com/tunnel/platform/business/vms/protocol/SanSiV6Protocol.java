package com.tunnel.platform.business.vms.protocol;

import com.tunnel.platform.business.vms.core.IProtocol;
import com.tunnel.platform.business.vms.core.ProtocolAnalysis;
import com.tunnel.business.utils.util.RadixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SanSiV6Protocol implements IProtocol {
	private static final Logger logger = LoggerFactory.getLogger(SanSiV6Protocol.class);

	private final String protocol;
	private final String RULES_FAUT = "0:header;1-2:address;3-6:data;1-6:bodyData;7-8:crc;9:footer";
	private final Map<String, String> protocolDatas;

	public SanSiV6Protocol(String protocol) {
		this.protocol = protocol;
		ProtocolAnalysis pa = new ProtocolAnalysis(protocol, RULES_FAUT);
		protocolDatas = pa.getDatas();
	}

	@Override
	public String getProtocolHeader() {
		return protocolDatas.get("header");
	}

	@Override
	public String getPortocolFooter() {
		return protocolDatas.get("footer");
	}

	/**
	 * 
	 * @Description: 获取协议的校验码
	 * @return
	 */
	public String getCRC() {
		return protocolDatas.get("crc");
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public boolean isValid() {
		/*
		 * 1.只有头、尾、校验码的协议是无效的。
		 */
		if (protocol == null) {
			logger.warn("[isValid]  ");
			return false;
		}
		/*
		 * 2.CRC校验
		 */
		// 2.1 协议的CRC值
		String nativeValue = getCRC();
		// 2.2 计算CRC值
		String bodyData = protocolDatas.get("bodyData");
		if (bodyData == null)
			return false;
		byte[] datas = RadixUtil.hex2Byte(bodyData.trim().split(" "));
		String calVavlue = ProtocolCheckAlgorithm.gen_crc(datas);
		// 2.3 比较两个CRC
		if (calVavlue == null || nativeValue == null)
			return false;
		if (calVavlue.toUpperCase().equals(nativeValue.replaceAll(" ", "")))
			return true;
		else
			return false;
	}

	public List<String> getFaultInformation() {
		List<String> result = new ArrayList<String>();
		String dataString = protocolDatas.get("data");
		if (dataString == null)
			throw new IllegalArgumentException("协议[" + protocol + "]获取异常数据为空！");

		String[] datas = dataString.trim().split(" ");
		if (datas.length != 4)
			throw new IllegalArgumentException("协议[" + protocol
					+ "]获取异常数据不足四位为空！");

		/*
		 * 异常信息共四位。 第一位为保留位； 第二位的高位两个字节为保留，低两位分为为温度异常故障和光敏部件异常；
		 * 第三位：防雷器故障、输入220V交流电故障、检测系统故障、单像素管故障； 第四位：显示模组电源故障、显示模组故障、硬件故障和保留位。
		 */
		int second = RadixUtil.hexStringToAscii(datas[1]);
		int three = RadixUtil.hexStringToAscii(datas[2]);
		int four = RadixUtil.hexStringToAscii(datas[3]);

		if ((second & 1) == 1){			
			result.add("光敏部件异常");
		}
		if ((second & 2) == 2){			
			result.add("温度异常故障");
		}
		if ((three & 1) == 1){
			result.add("单像素管故障");
		}
		if ((three & 2) == 2){			
			result.add("检测系统故障");
		}
		if ((three & 4) == 4){			
			result.add("输入220V交流电故障");
		}
		if ((three & 8) == 8){			
			result.add("防雷器故障");
		}
		if ((four & 2) == 2){			
			result.add("硬件故障");
		}
		if ((four & 4) == 4){			
			result.add("显示模组故障");
		}
		if ((four & 8) == 8){			
			result.add("显示模组电源故障");
		}
		/**
		 * 1.转成二进制
		 */
		return result;
	}

	@Override
	public String getBodyData() {
		return protocolDatas.get("bodyData");
	}

	@Override
	public String getValidateCode() {
		return protocolDatas.get("crc");
	}

	@Override
	public String getValidateType() {
		return IProtocol.VALIDATION_CRC;
	}

	@Override
	public Map<String, String> getDatas() {
		List<String> faults = getFaultInformation();
		Map<String, String> result = new HashMap<String, String>();
		if(faults.size()<=0){
			result.put("RESULT", "SUCCESS");
			result.put("MESSAGE", "OK");
			return result;
		}
		
		StringBuffer sb = new StringBuffer();
		for (String falut : faults) {
			sb.append(falut).append(";");
		}
		result.put("RESULT", "Fault");
		result.put("MESSAGE", sb.toString());
		return result;
	}

}
