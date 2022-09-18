package com.tunnel.deal.guidancelamp.protocol;

import com.tunnel.platform.utils.util.XorCheckUtils;

import java.io.UnsupportedEncodingException;

public class RulesUtil {

    /**
     * 头部校验
     * @return
     */
    public static boolean getHeadRules(String code,String rulesHead){
        if(code.length()>12){
            return rulesHead.equals(code.substring(0,2));
        }
        return false;
    }

    /**
     * 尾部校验
     * @return
     */
    public static boolean getFootRules(String code,String rulesFoot){
        if(code.length()>12){
            return rulesFoot.equals(code.substring(code.length()-2));
        }
        return false;
    }

    /**
     * CRC校验
     * @return
     */
    public static boolean getCRCRules(String code){
        if(code.length()>12){
            String crc = code.substring(code.length()-6,code.length()-2);
            byte[] b = new byte[0];
            try {
                b = StringUtil.hexStr2StrType(code.substring(2,code.length()-6)).getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int a = JavaCrc16XMODEM.getCRC1021(b, b.length);
            String crcRules = Integer.toHexString(a).toUpperCase();
            return crcRules.equalsIgnoreCase(crc);
        }
        return false;
    }


    /**
     * 生成校验码
     * @param content   内容
     * @param type      校验码类型
     * @param charsetName   编码类型名称
     * @return
     */
    public static String getCrcCheckCodeByTypeAndContent(String content , int type,String charsetName){
        String result = "";
        switch (type){
            case 1: //crc16XMODEM
                byte[] b = new byte[0];
                try {
                    b = StringUtil.hexStr2StrType(content.replace(" ","")).getBytes(charsetName);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                int a = JavaCrc16XMODEM.getCRC1021(b, b.length);
                result = Integer.toHexString(a).toUpperCase();
                break;
            case 2: //crc16MODBUS
                result = JavaCrc16.getCRC(content.replaceAll(" ",""));
                break;
            case 3: //crc16CCITT
                String s = JavaCrc16CCITT.CCITT(content); //写入十六进制数据
                result = Integer.toHexString(Integer.parseInt(s, 2)).toUpperCase();
                break;
            case 4: //bcc(异域校验)
                result =  XorCheckUtils.yihuo(content.replace(" ",""));
                break;
            default:
        }
        return result;
    }



    /**
     * byte数组转int数组
     * int 占4个byte字节
     *
     * @param btarr
     * @return
     */
    public static int[] byteToInt(byte[] btarr) {
        if (btarr.length % 4 != 0) {
            return null;
        }
        int[] intarr = new int[btarr.length / 4];

        int i1, i2, i3, i4;
        // j循环int	k循环byte数组
        for (int j = 0, k = 0; j < intarr.length; j++, k += 4) {
            i1 = btarr[k];
            i2 = btarr[k + 1];
            i3 = btarr[k + 2];
            i4 = btarr[k + 3];

            if (i1 < 0) {
                i1 += 256;
            }
            if (i2 < 0) {
                i2 += 256;
            }
            if (i3 < 0) {
                i3 += 256;
            }
            if (i4 < 0) {
                i4 += 256;
            }
            // 保存Int数据类型转换
            intarr[j] = (i1 << 24) + (i2 << 16) + (i3 << 8) + (i4 << 0);

        }
        return intarr;
    }

}
