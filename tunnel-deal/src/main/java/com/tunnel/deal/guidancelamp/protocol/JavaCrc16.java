package com.tunnel.deal.guidancelamp.protocol;

import java.util.BitSet;

/**
 * @author lwt
 * @date 2018-06-26
 *
 * CRC16校验码计算
 * <p>
 * (1)．预置1个16位的寄存器为十六进制FFFF（即全为1），称此寄存器为CRC寄存器；
 * (2)．把第一个8位二进制数据（既通讯信息帧的第一个字节）与16位的CRC寄存器的低
 * 8位相异或，把结果放于CRC寄存器；
 * (3)．把CRC寄存器的内容右移一位（朝低位）用0填补最高位，并检查右移后的移出位；
 * (4)．如果移出位为0：重复第3步（再次右移一位）；如果移出位为1：CRC寄存器与多项式A001（1010 0000 0000 0001）进行异或；
 * (5)．重复步骤3和4，直到右移8次，这样整个8位数据全部进行了处理；
 * (6)．重复步骤2到步骤5，进行通讯信息帧下一个字节的处理；
 * (7)．将该通讯信息帧所有字节按上述步骤计算完成后，得到的16位CRC寄存器的高、低
 * 字节进行交换；
 * (8)．最后得到的CRC寄存器内容即为CRC16码。(注意得到的CRC码即为低前高后顺序)
 */
public class JavaCrc16 {
    /**
     * 计算CRC16校验码
     *
     * @param data 需要校验的字符串
     * @return 校验码
     */
    public static String getCRC(String data) {
        data = data.replace(" ", "");
        int len = data.length();
        if (!(len % 2 == 0)) {
            return "0000";
        }
        int num = len / 2;
        byte[] para = new byte[num];
        for (int i = 0; i < num; i++) {
            int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
            para[i] = (byte) value;
        }
        return getCRC(para);
    }


    /**
     * 计算CRC16校验码
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getCRC(byte[] bytes) {
        //CRC寄存器全为1
        int CRC = 0x0000ffff;
        //多项式校验值
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        //结果转换为16进制
        String result = Integer.toHexString(CRC).toUpperCase();
        if (result.length() != 4) {
            StringBuffer sb = new StringBuffer("0000");
            result = sb.replace(4 - result.length(), 4, result).toString();
        }
        //交换高低位
        return result.substring(2, 4) + result.substring(0, 2);
        //return result;
    }


    //十六进制基本字符
    protected static final char[] hexadecimal =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'A', 'B', 'C', 'D', 'E', 'F'};

    //Array containing the safe characters set.
    //BitSet 实现了一个按需增长的位向量
    protected BitSet safeCharacters = new BitSet(256);

    public JavaCrc16() {
        for (char i = 'a'; i <= 'z'; i++) {
            addSafeCharacter(i);
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            addSafeCharacter(i);
        }
        for (char i = '0'; i <= '9'; i++) {
            addSafeCharacter(i);
        }
    }

    public void addSafeCharacter( char c ) {
        safeCharacters.set( c );
    }


    public static void main(String[] args) {
        //00	01	0002	0B	0000	02	0026		0224010100010101242400000A010100010001B2E2CAD4
        //String crc = "000100020B00000200260224010100010101242400000A010100010001B2E2CAD4242400000A234F00010001B2E2CAD4";
        String crc = "00 05 27 2B 0B 07 02 01 00 01 02 ".replace(" ","");

        System.out.println(crc);
        String fcrc = getCRC(crc.replaceAll(" ",""));
        System.out.println(fcrc);
    }
}
