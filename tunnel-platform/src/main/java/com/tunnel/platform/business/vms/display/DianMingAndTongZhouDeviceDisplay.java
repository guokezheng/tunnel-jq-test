package com.tunnel.platform.business.vms.display;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.utils.exception.BusinessException;
import com.tunnel.business.utils.util.RadixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DianMingAndTongZhouDeviceDisplay extends CommonDeviceDisplay {

    private static final Logger logger = LoggerFactory.getLogger(DianMingAndTongZhouDeviceDisplay.class);


    @Override
    public String getCurrentBrightness(String responseContent) {
        /*
         * 1.参数校验
		 */
        if (StringUtils.isEmpty(responseContent)) {
            throw new BusinessException("ASI转换：内容参数为空");
        }
        String result = DisplayFormat.preTongZhouBrightnessHandle(responseContent);
        result = result.replaceAll(" ", "");
        try {
            StringBuffer stringBuffer = new StringBuffer();
            result = RadixUtil.gb2312ToWord(result);
            String substring = result.substring(0, 6);
            if ("FFFFFF".equals(substring)) {
                result = stringBuffer.append("0").append(result.substring(result.length() - 2, result.length())).toString();
            }else {
                result = stringBuffer.append("1").append(result.substring(result.length() - 2, result.length())).toString();
            }
        } catch (Exception e) {

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
            String result = DisplayFormat.preTongZhouCurrentHandle(responseContent);
            result = RadixUtil.gb2312ToWord(result);
            result = result.substring(result.indexOf("\\"));
            return DisplayFormat.touZhouDianMingFilterContent(result);
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
            String result = DisplayFormat.preTongZhouAllListHandle(responseContent);
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

                result = result.replaceAll("(\\r\\n)(?i)ITEM", ";-ITEM");
                result = result.replaceAll("(\\n)(?i)ITEM", ";-ITEM");
                result = result.replaceAll("\\r", "");
                result = result.replaceAll("\\\\A", "<br>");
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

    @Override
    public List<String> getDeviceFault(String responseContent) {
        List<String> faultInformation = new ArrayList<String>();
        /*
         * 1.参数校验
         */
        if (StringUtils.isEmpty(responseContent)) {
            throw new BusinessException("ASI转换：内容参数为空");
        }
        String result = DisplayFormat.preDianMingFaultHandle(responseContent);
        result = result.replaceAll(" ", "");
        try {
            result =getFaultStatus(result);
            faultInformation = getFaultInformation(result);
        } catch (Exception e) {

        }
        return faultInformation;

    }

    public static List<String> getFaultInformation(String deviceFault) {
        List<String> result = new ArrayList<String>();
        char[] chars1 = deviceFault.toCharArray();

        String[] faultName = {"温度异常", "风扇异常", "箱体电压异常", "门开关异常", "亮度异常", "防雷器异常"};
        for (int i = 0; i < chars1.length; i++) {
            if (chars1[i] == '1') {
                if (faultName[i] != "") {
                    result.add(faultName[i]);
                }
            }
        }
        return result;
    }
    public static  String  getFaultStatus(String str){
        String res="";
        for (int i=0,k=0;i<12;i++,k++){
            if ("31".equals(str.substring(k,k+2))){
                res+="0";
            }else {
                res+="1";
            }
            k++;
        }
        return res;
    }
}
