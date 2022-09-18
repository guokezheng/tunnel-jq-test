package com.tunnel.deal.guidancelamp.protocol;


/**
 *
 * CRC数组处理工具类及数组合并(高位在前低位在后)
 */

public class JavaCrc16CCITT {
    public static String CCITT(String Source) {
        /*
         * CRC16_CCITT：
         * 多项式x^16+x^12+x^5+1（0x1021），初始值0x0000，低位在前，高位在后，结果与0x0000异或；
         * */
        int crc = 0x0000;           //初始值
        int polynomial = 0x1021;    //多项式
        String tmp;
        Source = Source.replace(" ", "");
        int[] ints = new int[Source.length() / 2];
        for (int i = 0; i < Source.length() - 1; i++) {
            if (i % 2 == 0) {
                tmp = Source.substring(i, i + 2);
                ints[i / 2] = Integer.parseInt(tmp, 16);
            }
        }

        //输入反转
        for (int i = 0; i < ints.length; i++) {
            String s1;
            s1 = Integer.toBinaryString(ints[i]);  // 十进制转为二进制字符串
            int stl = s1.length();
            //串前补0
            if (stl < 8) {
                for (int j = 0; j < 8 - stl; j++) {
                    s1 = "0" + s1;
                }
            }
            //逆位
            String s2 = "";
            for (int k = 0; k < 8; k++) {
                s2 += s1.charAt(8 - k - 1);
            }
            int a = Integer.parseInt(s2, 2);
            ints[i] = a;
        }

        //  CRC算法
        for (int b : ints) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xFFFF;

        //输出反转
        String s = Integer.toBinaryString(crc); //十进制转为二进制字符串
        //串前补0
        int l1 = 16 - s.length();
        if (s.length() < 16) {
            for (int i = 0; i < l1; i++) {
                s = "0" + s;
            }
        }
        //逆位
        String s2 = "";
        for (int i = 0; i < s.length(); i++) {
            s2 += s.charAt(s.length() - 1 - i);
        }
        //串后补0
        int l2 = s2.length();
        if (s2.length() < 16) {
            for (int i = 0; i < l2; i++) {
                s2 += "0";
            }
        }
        return s2;
    }


    public static void main(String[] args) {
        String s = JavaCrc16CCITT.CCITT("30 31 30 31 32 31"); //写入十六进制数据
        System.out.println("二进制结果：" + s);
        System.out.println("十六进制结果：" + Integer.toHexString(Integer.parseInt(s, 2)).toUpperCase());

    }
}
