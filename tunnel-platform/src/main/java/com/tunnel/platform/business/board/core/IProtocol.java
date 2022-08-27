package com.tunnel.platform.business.board.core;

import java.util.Map;

/**
 * 协议解析接口
 * 
 * @author Alvin
 *
 */
public interface IProtocol {

	// 1.协议的校验算法
	public String VALIDATION_MD5 = "md5";
	public String VALIDATION_CRC = "crc";

	/**
	 * 
	 * @Description:获取协议头
	 * @return
	 */
	public String getProtocolHeader();

	/**
	 * 
	 * 获取协议尾
	 * 
	 * @return
	 */
	public String getPortocolFooter();

	/**
	 * 
	 * 获取数据体
	 * 
	 * @return
	 */
	public String getBodyData();

	/**
	 * 
	 * 获取验证码
	 * 
	 * @return
	 */
	public String getValidateCode();

	/**
	 * 
	 * 获取解析数据
	 * 
	 * @return
	 */
	public Map<String, String> getDatas();

	/**
	 * 
	 * 获取验证方式
	 * 
	 * @return
	 */
	public String getValidateType();

	/**
	 * 
	 * 获取协议
	 * 
	 * @return
	 */
	public String getProtocol();

	/**
	 * 
	 * 检查协议是否有效
	 * 
	 * @return
	 */
	public boolean isValid();

}
