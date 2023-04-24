package com.tunnel.deal.mca.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * describe: 数据进制转换
 *
 * @author zs
 * @date 2021/2/5
 */
public class NumberSystemConvert {

    private static final Logger log = LoggerFactory.getLogger(NumberSystemConvert.class);

    /**
     * 转换16进制32位浮点数
     * @return
     */
    public static Float convertHexToFloat(String s){
        float value = (float) 0;
        try{
            Long l = Long.valueOf(s.replace(" ","").trim(), 16);
            //l.intValue()不会丢掉精度吗？ todo
            value = Float.intBitsToFloat(l.intValue());
        }catch (Exception e){
            e.printStackTrace();
            log.error("转换出错的数据："+s);
            log.error("convertHexToFloat16进制转换32位浮点数报错："+e.getMessage());
        }

        return value;
    }

    /**
     * 转换16进制用1位表示的整数
     * @param s
     * @return
     */
    public static Integer convertHexToInteger(String s){
        Integer value = Integer.parseInt(s,16);
        return value;
    }

    /**
     * 16进制数据转成二进制字符串
     * 不足8位补0
     * @param s
     * @return
     */
    public static String convertHexToBit(String s){
        //最大值FF[255]超过Byte最大值[127]，不能转换为Byte
//        Byte b = Byte.parseByte(s);
//        String result = Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
//        return result;
        String str = Integer.toBinaryString(Integer.valueOf(s,16));
        int strLen = str.length();
        int strLength = 8;
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                // 左补0
                sb.append("0").append(str);
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }


    /**
     * @param: [content]
     * @return: int
     * @description: 十六进制转十进制
     */
    public static int hex2Decimal(String content){
        content = content.toUpperCase();
        int number=0;
        String [] HighLetter = {"A","B","C","D","E","F"};
        Map<String,Integer> map = new HashMap<>();
        for(int i = 0;i <= 9;i++){
            map.put(i+"",i);
        }
        for(int j= 10;j<HighLetter.length+10;j++){
            map.put(HighLetter[j-10],j);
        }
        String[]str = new String[content.length()];
        for(int i = 0; i < str.length; i++){
            str[i] = content.substring(i,i+1);
        }
        for(int i = 0; i < str.length; i++){
            number += map.get(str[i])*Math.pow(16,str.length-1-i);
        }
        return number;
    }
}
