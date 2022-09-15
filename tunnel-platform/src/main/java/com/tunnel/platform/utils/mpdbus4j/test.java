package com.tunnel.platform.utils.mpdbus4j;

import com.tunnel.platform.utils.util.StringUtil;

import java.io.IOException;
import java.math.BigInteger;

public class test {
    public static void main(String[] args) throws Exception{
        //ModbusMaster master = ModbusTcpMaster.getMaster("10.3.19.150",502);
        int slaveId = 1;
        //寄存器读取开始地址
        int offset = 0;
        //读取寄存器数量
        int quantity = 100;

        //读取模拟量
       /* short[] revShorts = Modbus4jReadUtils.readHoldingRegister( master,1, 0, 10);
        getCo_Vi(offset,revShorts);*/

        //读取车指数据
      /*  boolean[] registers = Modbus4jReadUtils.readInputStatus(master,slaveId, offset, quantity);
        for (boolean e:registers){
            System.out.println("读取开关数据:"+(offset++)+"-"+e);
        }
        boolean[] bdata={false,false,false};
        //改变车指
            boolean b = Modbus4jWriteUtils.writeCoils(master, slaveId, 23,bdata );
            System.out.println("写寄存器数据:"+">>"+b);*/

    }
    public static void getCo_Vi_WS_DN_DW(int offset,short[] revShorts) throws IOException {
        String hexString = "";
        for (int i = 0; i < revShorts.length; i++) {
            System.out.println("读取寄存器数据:" +(offset++)+">>"+ revShorts[i]);
            hexString = StringUtil.hexStringByShort(revShorts[i],2);
            String hex1 = StringUtil.hexStringByShort(revShorts[i+1],2);
            if (hex1.contains("-")){
                BigInteger bi = new BigInteger(hex1, 16);
                String string = Long.toHexString(0x100000000L + bi.longValue());
                hexString+=string.substring(string.length()-5,string.length());
            }else {
                hexString+=StringUtil.hexStringByShort(revShorts[i+1],2);
            }
            Float aFloat = StringUtil.HexConvertToString(hexString);
            String format = String.format("%.3f", aFloat);
            System.err.println(format);
            i++;
        }
    }
}
