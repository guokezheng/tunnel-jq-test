package com.tunnel.platform.datacenter.util;

import com.tunnel.platform.utils.util.CommonUtil;
import com.tunnel.platform.utils.util.RadixUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

public abstract class ProtocolCheckAlgorithm {

	/**
	 * 
	 * CRC16的校验码生成。
	 * 
	 * @param str
	 * @return
	 */
	public static String gen_crc(String str) {
		if (CommonUtil.isNull(str))
			return null;
		byte[] datas = RadixUtil.hex2Byte(str.trim().split(" "));
		return gen_crc(datas);
	}

	/**
	 * 
	 * CRC16的校验码生成。
	 * 
	 * @param buffer
	 * @return
	 */
	public static String gen_crc(byte buffer[]) {

		char c, treat, bcrc;
		int wcrc = 0;
		int i, j;
		int buffer_length = buffer.length;
		for (i = 0; i < buffer_length; i++) {
			c = (char) (buffer[i] & 0xFF);
			for (j = 0; j < 8; j++) {
				treat = (char) (c & 0x80);
				c <<= 1;
				bcrc = (char) ((wcrc >> 8) & 0x80);
				wcrc <<= 1;
				if (treat != bcrc)
					wcrc ^= 0x1021;
				wcrc &= 0xFFFF;
			}
		}
		return String.format("%04x", wcrc & 0xFFFF);
	}

	/**
	 * 
	 * 产生MD5码。
	 * 
	 * @param bodyData
	 * @return
	 */
	public static String gen_md5(String plaintText) {
		String newstr = null;
		if (plaintText == null || plaintText.trim().length() <= 0){
			throw new IllegalArgumentException("input argument plaintText is null!");			
		}

		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			Encoder encoder = Base64.getEncoder();
			// 加密后的字符串
			return encoder.encodeToString(md5.digest(plaintText.getBytes("utf-8")));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newstr;
	}

	/**
	 * 
	 * 计算crc校验。
	 * 
	 * @param hexs
	 * @return
	 */
	public static long calHexToLong(String... hexs) {
		Long result = 0l;
		for (String str : hexs) {
			if (!CommonUtil.isNull(str))
				result += Long.parseLong(str, 16);
		}
		return result;
	}

	/**
	 * 
	 * 计算crc校验。
	 * 
	 * @param hexs
	 * @return
	 */
	public static String calHex(String... hexs) {
		Long result = calHexToLong(hexs);
		return Long.toHexString(result);
	}

}
