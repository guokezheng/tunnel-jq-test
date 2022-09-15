package com.tunnel.platform.business.vms.display;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.utils.exception.BusinessException;
import com.tunnel.platform.utils.util.RadixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DingEnDeviceDisplay extends CommonDeviceDisplay {

	private static final Logger logger = LoggerFactory.getLogger(DingEnDeviceDisplay.class);

	@Override
	public String getCurrentDisplay(String responseContent) {
		/*
		 * 1.参数校验
		 */
		if (StringUtils.isEmpty(responseContent)) {
			throw new BusinessException("ASI转换：内容参数为空");
		}
		try {
			String result = DisplayFormat.preDingEnHandle(responseContent);
			result = RadixUtil.gb2312ToWord(result);
			result = result.substring(result.indexOf("\\"));
			return DisplayFormat.dingEnFilterContent(result);
		} catch (Exception e) {
		}

		return null;
	}
	@Override
	public String getPlayListDisplay(String responseContent) {
		/*
		 * 1.参数校验
		 */
		if (StringUtils.isEmpty(responseContent)) {
			throw new BusinessException("ASI转换：内容参数为空");
		}
		try {
			String result = DisplayFormat.preXianKeAllListHandle(responseContent);
			result = RadixUtil.gb2312ToWord(result);
			StringBuffer itemSb = new StringBuffer("");
			//加乱码判断
			Boolean isMessy = isMessyCode(result);
			if (isMessy) {
				throw new BusinessException("播放列表存在乱码，无法解析");
			} else {
				logger.debug("[getPlayListDisplay()] 协议解析为：\n");
				logger.debug("\n******** start ********\n");
				logger.debug(result);
				logger.debug("\n******** end ********\n");

				result = result.replaceAll("(\\r\\n)(?i)ITEM", ";-Item");
				result = result.replaceAll("(\\r)(?i)ITEM", ";-Item");
				result = result.replaceAll("(\\n)(?i)ITEM", ";-Item");
				result = result.replaceAll("\\r\\n", "");
				result = result.replaceAll("\\\\n", "<br>");
				result = result.replaceAll("\n", "<br>");
				String strReplace = "<br>";
				for (int i = 1; i < 10; i++) {
					strReplace += strReplace;
					result = result.replaceAll(strReplace, "");
				}
				String[] temp = result.split(";-");


				for (String _str : temp) {
					if (_str == null)
						continue;
					if (_str.toUpperCase().startsWith("ITEM")) {
						if (_str.endsWith("<br>")) {
							_str = _str.substring(0, _str.length() - 4);
						}
						itemSb.append(_str);
					}
				}

			}
			return itemSb.toString();
		} catch (Exception e) {
		}
		return null;
	}
}
