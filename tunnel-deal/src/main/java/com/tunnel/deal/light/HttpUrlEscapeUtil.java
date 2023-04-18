package com.tunnel.deal.light;

import java.util.Arrays;

/**
 * describe: http请求参数中特殊字符转义
 *
 * @author zs
 * @date 2023/4/17
 */
public class HttpUrlEscapeUtil {

    /**
     * 特殊字符
     */
    private static final String[] array = {"+"," ","/","?","%","#","&","="};

    /**
     * 判断字符串中是否包含待转义字符
     * @param param
     * @return
     */
    public static boolean check(String param){
        boolean flag = false;
        for(int i = 0; i < array.length; i++){
            if(param.contains(array[i])){
                flag = true;
                break;
            }
        }
        return flag;
    }


    public static String escape(String param){
        StringBuffer result = new StringBuffer(param);
        String arrayStr = Arrays.toString(array);
        for(int i = 0; i < result.length(); i++){
            String str = result.substring(i,i+1);
            if(arrayStr.contains(str)){
                String header = result.substring(0,i);
                String tail = result.substring(i + 1);
                String escapeStr = Integer.toHexString(str.getBytes()[0]);
                result = new StringBuffer();
                //编码格式：%加字符的ASCII（16进制）码，比如"#"对应"%23"
                result.append(header).append("%").append(escapeStr).append(tail);
                i = i + escapeStr.length();
            }
        }
        return result.toString();
    }

}
