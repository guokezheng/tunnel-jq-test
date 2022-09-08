package com.tunnel.platform.utils.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class StringUtil {


    /**
     * 字符串转化成为16进制字符串
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }


    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }


    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    /**
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     * @param src  16进制字符串
     * @return 字节数组
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer
                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * 10进制转16进制 补位
     */
    public static String hexStringByInteger(Integer num,int byteNum){
        String str = "";
        str = Integer.toHexString(num);
        if(byteNum == 1){
            if(str.length() == 1){
                str = "0"+str;
            }
        }else if(byteNum == 2){
            if(str.length() == 1){
                str = "000"+str;
            }else if(str.length() == 2){
                str = "00"+str;
            }else if(str.length() == 3){
                str = "0"+str;
            }
        }else{
            int len = str.length();
            for (int i = 0; i < byteNum*2-len; i++) {
                str = "0"+str;
            }
        }
        return str.toUpperCase();
    }
    public static String hexStringByShort(short num,int byteNum){
        String str = "";
        str = Integer.toHexString(num);
        if(byteNum == 1){
            if(str.length() == 1){
                str = "0"+str;
            }
        }else if(byteNum == 2){
            if(str.length() == 1){
                str = "000"+str;
            }else if(str.length() == 2){
                str = "00"+str;
            }else if(str.length() == 3){
                str = "0"+str;
            }
        }else{
            int len = str.length();
            for (int i = 0; i < byteNum*2-len; i++) {
                str = "0"+str;
            }
        }
        return str.toUpperCase();
    }
    public static Float HexConvertToString(String hex) throws IOException {
        byte[] bytes = HexString2Bytes(hex);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        float value = dataInputStream.readFloat();
        return value;
    }
    public static byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] {src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }
    /**
     * 16进制 补位
     */
    public static String seamProtectionHexString(String str,int byteNum){
        int len = str.length();
        for (int i = 0; i < byteNum*2-len; i++) {
            str = "0"+str;
        }
        return str.toUpperCase();
    }


    /**
     * 16进制转2进制
     * @param hex
     * @return
     */
    public static String hexStringToByte(String hex) {
        int num = Integer.parseInt(hex,16);
        BigInteger big =  new BigInteger(num+"");
        String binStr = big.toString(2);
        while(binStr.length()<16){
            binStr = "0"+binStr;
        }
        return binStr;

    }

    /**
     * UTF-8 16进制转换成为string类型字符串
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * GBK 解析 16进制 转字符串
     * @param hex
     * @return
     */
    public static String hexStr2StrType(String hex) {
        String hexStr = "";
        String str = "0123456789ABCDEF"; //16进制能用到的所有字符 0-15
        for(int i=0;i<hex.length();i++){
            String s = hex.substring(i, i+1);
            if(s.equals("a")||s.equals("b")||s.equals("c")||s.equals("d")||s.equals("e")||s.equals("f")){
                s=s.toUpperCase().substring(0, 1);
            }
            hexStr+=s;
        }
        char[] hexs = hexStr.toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        String name = "";
        try {
            name = new String(bytes,"GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return name;
    }


    /**
     * 解析 16进制 转字符串
     * @param hex
     * @param charsetName
     * @return
     */
    public static String hexStr2StrType(String hex,String charsetName) {
        String hexStr = "";
        String str = "0123456789ABCDEF"; //16进制能用到的所有字符 0-15
        for(int i=0;i<hex.length();i++){
            String s = hex.substring(i, i+1);
            if(s.equals("a")||s.equals("b")||s.equals("c")||s.equals("d")||s.equals("e")||s.equals("f")){
                s=s.toUpperCase().substring(0, 1);
            }
            hexStr+=s;
        }
        char[] hexs = hexStr.toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        String name = "";
        try {
            name = new String(bytes,charsetName);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 高低字节转换
     * @param code
     * @return
     */
    public static String getHighToLow(String code){
        String high = code.substring(0,2);
        String low = code.substring(2,4);
        return low+high;
    }

    /**
     * byte[] 转16进制字符串
     * @param b
     * @return
     */
    public static String bytesString16(byte[] b) {
        char[] _16 = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i<b.length;i++) {
            sb.append(_16[b[i]>>4&0xf])
                    .append(_16[b[i]&0xf]);
        }
        return sb.toString();
    }


    /**
     * 16进制 数字 字符串转化
     * @return
     */
    public static  String getStringNumByString(String code){
        //查看code 是否等于#
        if("23".equals(code)){
            return "#";
        }
        Integer num = Integer.parseInt(code,16);
        if(num<10){
            return "0" + num;
        }
        return ""+num;
    }



    /**
     *  10进制补位
     */
    public static String getStringByLong(Long code,int num){
        String codeStr = Long.toString(code);
        int length = codeStr.length();
        for (int i = 0; i < num - length; i++) {
            codeStr="0"+codeStr;
        }
        return codeStr;
    }


    /**
     *  10进制补位
     */
    public static String getStringByInteger(Integer code,int num){
        String codeStr = Integer.toString(code);
        int length = codeStr.length();
        for (int i = 0; i < num - length; i++) {
            codeStr="0"+codeStr;
        }
        return codeStr;
    }

    public static String hexToDecimal(String hex) {
        if (hex.startsWith("0x")) {
            return String.valueOf(Integer.parseInt(hex.substring(2), 16));
        } else {
            return String.valueOf(Integer.parseInt(hex, 16));
        }
    }


    /**
     * GBK ：  先将字符串转化为GBK ，再转化为 BYTE[] 。 bytesString16将byte[]进制转化为16进制
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str =  "02 00 01 00 01 37 31 2B 30 30 30 30 30 30 30 30 70 6C 61 79 30 2E 6C 73 74 5B 50 4C 41 59 4C 49 53 54 5D 0D 0A 49 54 45 4D 5F 4E 4F 3D 30 30 33 0D 0A 49 54 45 4D 30 30 30 3D 32 2C 30 2C 30 2C 33 30 2C 33 30 2C 5C 43 30 31 36 30 30 30 5C 54 32 35 35 32 35 35 30 30 30 30 30 30 5C 4B 30 30 30 30 30 30 30 30 30 30 30 30 5C 4D 30 30 5C 46 73 33 32 33 32 5C 57 B2 E2 CA D4 CA FD BE DD 30 0D 0A 49 54 45 4D 30 30 31 3D 32 2C 30 2C 30 2C 33 30 2C 33 30 2C 5C 43 30 31 36 30 30 30 5C 54 32 35 35 32 35 35 30 30 30 30 30 30 5C 4B 30 30 30 30 30 30 30 30 30 30 30 30 5C 4D 30 30 5C 46 73 33 32 33 32 5C 57 B2 E2 CA D4 CA FD BE DD 31 0D 0A 49 54 45 4D 30 30 32 3D 32 2C 30 2C 30 2C 33 30 2C 33 30 2C 5C 43 30 31 36 30 30 30 5C 54 32 35 35 32 35 35 30 30 30 30 30 30 5C 4B 30 30 30 30 30 30 30 30 30 30 30 30 5C 4D 30 30 5C 46 73 33 32 33 32 5C 57 B2 E2 CA D4 CA FD BE DD 32 0D 0A 71 CA 03".replace(" ","");
        String str1 = "02 30 30 30 31 37 31 2B 30 30 30 30 30 30 30 30 70 6C 61 79 30 30 2E 6C 73 74 5B 50 4C 41 59 4C 49 53 54 5D 0D 0A 49 54 45 4D 5F 4E 4F 3D 30 30 32 0D 0A 49 54 45 4D 30 30 30 3D 33 30 2C 30 2C 30 2C 30 2C 30 2C 5C 43 31 31 30 30 30 33 5C 46 73 33 32 33 32 5C 54 32 35 35 30 30 30 30 30 30 30 30 30 5C 4B 30 30 30 30 30 30 30 30 30 30 30 30 5C 57 B5 C0 C2 B7 0D 0A 49 54 45 4D 30 30 31 3D 33 30 2C 30 2C 30 2C 30 2C 30 2C 5C 43 31 30 36 30 30 30 5C 46 73 33 32 33 32 5C 54 32 35 35 30 30 30 30 30 30 30 30 30 5C 4B 30 30 30 30 30 30 30 30 30 30 30 30 5C 57 B2 E2 CA D4 C4 DA C8 DD 91 05 03".replace(" ","");

        //String str2 = "30303130706C61792E6C73742B000000005B6C6973745D0D0A6974656D5F6E6F3D310D0A6974656D303D 3530302C312C30 2C5C6673333233325C433031363030305C633235353235353030303030302CBDF7C9F7BCDDCABB2020B1A3B3D6B3B5BEE00D0A".replace(" ","");

        String str2 = "02 30 30 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 5B 6C 69 73 74 5D 0D 0A 69 74 65 6D 5F 6E 6F 3D 31 0D 0A 69 74 65 6D 30 3D 35 30 30 2C 20 31 2C 20 30 2C 5C 66 73 33 32 33 32 5C 43 30 31 36 30 30 30 5C 63 32 35 35 32 35 35 30 30 30 30 30 30 BD F7 C9 F7 BC DD CA BB 20 20 B1 A3 B3 D6 B3 B5 BE E0 0D 0A 54 5B 03".replace(" ","");

        //String str1 = "02 30 30 31 30 70 6C 61 79 2E 6C 73 74 2B 00 00 00 00 5B 6C 69 73 74 5D 0D 0A 69 74 65 6D 5F 6E 6F 3D 31 0D 0A 69 74 65 6D 30 3D 35 30 30 2C 20 31 2C 20 30 2C 5C 66 73 33 32 33 32 5C 43 30 31 36 30 30 30 5C 63 32 35 35 32 35 35 30 30 30 30 30 30 BD F7 C9 F7 BC DD CA BB 20 20 B1 A3 B3 D6 B3 B5 BE E0 0D 0A 54 5B 03".replace(" ","");

        String str3 = "BD F7 C9 F7 BC DD CA BB 20 20 B1 A3 B3 D6 B3 B5 BE E0".replace(" ","");

        System.out.println("=======================================");
        System.out.println(hexStr2StrType(str));
        System.out.println("=======================================");

        System.out.println(hexStr2StrType(str1));
        String rstr = hexStr2StrType(str2);
        System.out.println(rstr);
        System.out.println(str2.equals(str1));
        System.out.println("========================");
        //字符串转换为GBK
        String strs = "你";
        System.out.println("========================");
        String str161  = bytesString16(strs.getBytes("GBK"));
        System.out.println(str161);
        System.out.println("========================");
        System.out.println("========================");
        String str1611  = bytesString16(strs.getBytes("utf-8"));
        System.out.println(str1611);

        String strss = "0230303130706C61792E6C73742B000000005B6C6973745D0D0A6974656D5F6E6F3D310D0A6974656D303D3530302C20312C20302C5C6673333233325C433031363030305C63323535323535303030303030BDF7C9F7BCDDCABB2020B1A3B3D6B3B5BEE00D0A3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333CEF103".replace(" ","");
        System.out.println(strss.length());
        System.out.println();
        String length = StringUtil.seamProtectionHexString(Integer.toHexString(12),2);
        System.out.println(length);
        System.out.println( Integer.valueOf(length, 16));
        String binaryString = Integer.toBinaryString(Integer.valueOf(length, 16));
        System.out.println(binaryString);

        System.out.println(hexStringToString("C4E3"));
        System.out.println(hexStr2StrType("31","GBK"));

    }
}
