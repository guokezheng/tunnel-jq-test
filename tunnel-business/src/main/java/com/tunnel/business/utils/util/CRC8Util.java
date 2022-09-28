package com.tunnel.business.utils.util;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 * @author zs
 */
public class CRC8Util {
    static byte[] crc8_tab = {(byte) 0, (byte) 94, (byte) 188, (byte) 226, (byte) 97, (byte) 63, (byte) 221, (byte) 131, (byte) 194, (byte) 156, (byte) 126, (byte) 32, (byte) 163, (byte) 253, (byte) 31, (byte) 65, (byte) 157, (byte) 195, (byte) 33, (byte) 127, (byte) 252, (byte) 162, (byte) 64, (byte) 30, (byte) 95, (byte) 1, (byte) 227, (byte) 189, (byte) 62, (byte) 96, (byte) 130, (byte) 220, (byte) 35, (byte) 125, (byte) 159, (byte) 193, (byte) 66, (byte) 28, (byte) 254, (byte) 160, (byte) 225, (byte) 191, (byte) 93, (byte) 3, (byte) 128, (byte) 222, (byte) 60, (byte) 98, (byte) 190, (byte) 224, (byte) 2, (byte) 92, (byte) 223, (byte) 129, (byte) 99, (byte) 61, (byte) 124, (byte) 34, (byte) 192, (byte) 158, (byte) 29, (byte) 67, (byte) 161, (byte) 255, (byte) 70, (byte) 24,
            (byte) 250, (byte) 164, (byte) 39, (byte) 121, (byte) 155, (byte) 197, (byte) 132, (byte) 218, (byte) 56, (byte) 102, (byte) 229, (byte) 187, (byte) 89, (byte) 7, (byte) 219, (byte) 133, (byte) 103, (byte) 57, (byte) 186, (byte) 228, (byte) 6, (byte) 88, (byte) 25, (byte) 71, (byte) 165, (byte) 251, (byte) 120, (byte) 38, (byte) 196, (byte) 154, (byte) 101, (byte) 59, (byte) 217, (byte) 135, (byte) 4, (byte) 90, (byte) 184, (byte) 230, (byte) 167, (byte) 249, (byte) 27, (byte) 69, (byte) 198, (byte) 152, (byte) 122, (byte) 36, (byte) 248, (byte) 166, (byte) 68, (byte) 26, (byte) 153, (byte) 199, (byte) 37, (byte) 123, (byte) 58, (byte) 100, (byte) 134, (byte) 216, (byte) 91, (byte) 5, (byte) 231, (byte) 185, (byte) 140, (byte) 210, (byte) 48, (byte) 110, (byte) 237,
            (byte) 179, (byte) 81, (byte) 15, (byte) 78, (byte) 16, (byte) 242, (byte) 172, (byte) 47, (byte) 113, (byte) 147, (byte) 205, (byte) 17, (byte) 79, (byte) 173, (byte) 243, (byte) 112, (byte) 46, (byte) 204, (byte) 146, (byte) 211, (byte) 141, (byte) 111, (byte) 49, (byte) 178, (byte) 236, (byte) 14, (byte) 80, (byte) 175, (byte) 241, (byte) 19, (byte) 77, (byte) 206, (byte) 144, (byte) 114, (byte) 44, (byte) 109, (byte) 51, (byte) 209, (byte) 143, (byte) 12, (byte) 82, (byte) 176, (byte) 238, (byte) 50, (byte) 108, (byte) 142, (byte) 208, (byte) 83, (byte) 13, (byte) 239, (byte) 177, (byte) 240, (byte) 174, (byte) 76, (byte) 18, (byte) 145, (byte) 207, (byte) 45, (byte) 115, (byte) 202, (byte) 148, (byte) 118, (byte) 40, (byte) 171, (byte) 245, (byte) 23, (byte) 73, (byte) 8,
            (byte) 86, (byte) 180, (byte) 234, (byte) 105, (byte) 55, (byte) 213, (byte) 139, (byte) 87, (byte) 9, (byte) 235, (byte) 181, (byte) 54, (byte) 104, (byte) 138, (byte) 212, (byte) 149, (byte) 203, (byte) 41, (byte) 119, (byte) 244, (byte) 170, (byte) 72, (byte) 22, (byte) 233, (byte) 183, (byte) 85, (byte) 11, (byte) 136, (byte) 214, (byte) 52, (byte) 106, (byte) 43, (byte) 117, (byte) 151, (byte) 201, (byte) 74, (byte) 20, (byte) 246, (byte) 168, (byte) 116, (byte) 42, (byte) 200, (byte) 150, (byte) 21, (byte) 75, (byte) 169, (byte) 247, (byte) 182, (byte) 232, (byte) 10, (byte) 84, (byte) 215, (byte) 137, (byte) 107, 53};

    public static void main(String[] args) {
       // String hex="00 06 01 FF FE FF 02 CC CC 00 00 00 00 00";
        String hex="00 05 01 00 50";
        byte[] bytes = stringToByteArr(hex);
        //System.out.println(hex6To10("FF"));
        System.out.println(CRC8(bytes));
    }

    /**
     *
     */
    public static String createCheck(String hex){
        byte[] bytes = stringToByteArr(hex);
        return CRC8(bytes);
    }
    /**
     * 校验位转byte
     * @param srtHex 命令串 //00 06 01 FF F5 FF 00 4C 66 01 14 1E 00 00
     * @return
     */
    public static byte[] stringToByteArr(String srtHex){
        String[] srtHexArr = srtHex.split("\\s+");
        List<Byte> hexByte = new ArrayList();
        for (String hex:srtHexArr){
            hexByte.add((byte) hex6To10(hex));
        }
        return listTobyte(hexByte);
    }

    /**
     * List转 byte[]
     * @param list
     * @return
     */
    private static byte[] listTobyte(List<Byte> list) {
        if (list == null || list.size() < 0)
            return null;
        byte[] bytes = new byte[list.size()];
        int i = 0;
        Iterator<Byte> iterator = list.iterator();
        while (iterator.hasNext()) {
            bytes[i] = iterator.next();
            i++;
        }
        return bytes;
    }

    /**
     * 16进制转10进制
     * @param srtHex
     * @return
     */
    public static int hex6To10(String srtHex){
        BigInteger ingNum=new BigInteger(srtHex,16);
        return ingNum.intValue();
    }
    /*************************************************************
     *	函数名称：	gh_crc8
     *
     *	函数功能：	生成crc8 密匙
     *
     *	入口参数：	p_buffer 需要校验的数据首地址 buf_size:需要校验的数据的长度
     *
     *	返回参数：	生成的crc8 秘钥
     *
     *	说明：p_buffer的空间必须要要>=buf_size 此函数不负责检测数据溢出
     *************************************************************/
    public static String CRC8(byte[] buffer)
    {
        byte crc = 0;

        for (int j = 0; j < buffer.length; j++)
        {
            for (int i = 0x80; i != 0; i /= 2)
            {
                if ((crc & 0x80) != 0)
                {
                    crc *= 2;
                    crc ^= 0x07; // 多项式：X8 + X2 + X + 1
                }
                else
                {
                    crc *= 2;
                }

                if ((buffer[j] & i) != 0)
                {
                    crc ^= 0x07;
                }
            }
        }

        String hex = Integer.toHexString(0x00ff & crc);
        return hex.toUpperCase();
    }

}