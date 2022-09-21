package com.tunnel.platform.business.vms.display;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.utils.exception.BusinessException;
import com.tunnel.business.utils.util.RadixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GuangDianAndSanSiDeviceDisplay extends CommonDeviceDisplay {

    private static final Logger logger = LoggerFactory.getLogger(GuangDianAndSanSiDeviceDisplay.class);


    @Override
    public String getCurrentBrightness(String responseContent) {
        /*
         * 1.参数校验
         */
        if (StringUtils.isEmpty(responseContent)) {
            throw new BusinessException("ASI转换：内容参数为空");
        }
        String result = DisplayFormat.preSanSiBrightnessHandle(responseContent);
        result = result.replaceAll(" ", "");
        try {
            result = RadixUtil.gb2312ToWord(result);
        } catch (Exception e) {

        }
        return result;

    }

    @Override
    public List<String> getDeviceFault(String responseContent) {
        List<String> faultInformation = new ArrayList<String>();
        /*
         * 1.参数校验
         */
        if (StringUtils.isEmpty(responseContent)) {
            throw new BusinessException("ASI转换：内容参数为空");
        }
        String result = DisplayFormat.preSanSiFaultHandle(responseContent);
        result = result.replaceAll(" ", "");
        try {
            result = RadixUtil.gb2312ToWord(result);
            faultInformation = getFaultInformation(result);
        } catch (Exception e) {

        }
        return faultInformation;

    }

    public static List<String> getFaultInformation(String deviceFault) {
        List<String> result = new ArrayList<String>();
        String fault = RadixUtil.hex2Binary(deviceFault);
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 16 - fault.length(); i++) {
            str.append("0");
        }
        fault = str.toString() + fault;
        String s11 = new StringBuilder(fault).reverse().toString();
        char[] chars1 = s11.toCharArray();

        String[] faultName = {"", "充放电控器故障", "显示模组故障", "显示模组电源故障", "单LED故障", "检测系统故障", "输入220V交流电故障", "防雷器故障", "光敏固件故障", "温度异常故障", "机箱门开关", "", "", "", "", ""};
        for (int i = 0; i < chars1.length; i++) {
            if (chars1[i] == '1') {
                if (faultName[i] != "") {
                    result.add(faultName[i]);
                }
            }
        }
        return result;
    }

    @Override
    public String getCurrentDisplay(String responseContent) {
        /*
         * 1.参数校验
         */
        if (StringUtils.isEmpty(responseContent)) {
            throw new BusinessException("ASI转换：内容参数为空");
        }
        try {
            String result = DisplayFormat.preSanSiCurrentHandle(responseContent);
            result = RadixUtil.gb2312ToWord(result);
            result = result.substring(result.indexOf("\\"));
            return DisplayFormat.sanSiGuangDianFilterContent(result);
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
            String result = DisplayFormat.preSanSiAllListHandle(responseContent);
            result = RadixUtil.gb2312ToWord(result);
            StringBuffer itemSb = new StringBuffer("");
            //加乱码判断
            Boolean isMessy = isMessyCode(result);
            if (isMessy) {
                throw new BusinessException("播放列表存在乱码，无法解析");
            }else {
                logger.debug("[getPlayListDisplay()] 协议解析为：\n");
                logger.debug("\n******** start ********\n");
                logger.debug(result);
                logger.debug("\n******** end ********\n");

                result = result.replaceAll("(\\r\\n)(?i)ITEM", ";-ITEM");
                result = result.replaceAll("(\\n)(?i)ITEM", ";-ITEM");
                result = result.replaceAll("\\r\\n", "<br>");
                result = result.replaceAll("\\n", "<br>");
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
