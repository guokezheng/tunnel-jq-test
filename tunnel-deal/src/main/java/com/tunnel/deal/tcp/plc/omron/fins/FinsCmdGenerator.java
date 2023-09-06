package com.tunnel.deal.tcp.plc.omron.fins;

/**
 * describe: Fins指令组装工具类
 *
 * @author zs
 * @date 2023/8/22
 */
public class FinsCmdGenerator {

    /**
     * 获取握手发送指令
     * @param clientNodeAddress
     * @return
     */
    public static String getHandshakeCommand(String clientNodeAddress){
        String command = "";
        FinsHandshakeSendCmd send = new FinsHandshakeSendCmd();
        //4字节
        send.setClientNodeAddress(String.format("%08x",getIPLast(clientNodeAddress)));
        command = send.getFullCmd();

        return command;
    }


    /**
     * 读取指令
     readLength
     * @return
     */
    public static String getReadCommand(String sourceAddress,String destinationAddress,String area,String address,String bitAddress,String readLength){
        //46494E53 0000001A 00000002 00000000 80 00 02 00 01 00 00 C8 00 00 01 01 82 01F400 0002
        String command = "";
        FinsReadSendCmd send = new FinsReadSendCmd();
//        send.setLength();
        send.setDa1(String.format("%02x",getIPLast(destinationAddress)));
        send.setSa1(String.format("%02x",getIPLast(sourceAddress)));
        //16进制数，不需要转换
        send.setArea(area);
        //转16进制前补0,2字节4位
        send.setAddress(String.format("%04x",Integer.parseInt(address)));
        if(bitAddress != null && !"".equals(bitAddress)){
            //转16进制前补0,1字节2位
            send.setBitAddress(String.format("%02x",Integer.parseInt(bitAddress)));
        }
        //2字节4位
        send.setReadLength(String.format("%04x",Integer.valueOf(readLength)));

        command = send.getFullCmd();

        return command;
    }

    /**
     * 控制指令
     readLength
     * @return
     */
    public static String getControlCommand(String sourceAddress,String destinationAddress,
                                           String area,String address,String bitAddress,String writeLength,String value){
        //46494E53 0000001E 00000002 00000000 80 00 02 001700 001800 FF 0102 B1 000A00 0002 AABBCCDD
        String command = "";
        FinsWriteSendCmd send = new FinsWriteSendCmd();
//        send.setLength();
        send.setDa1(String.format("%02x",getIPLast(destinationAddress)));
        send.setSa1(String.format("%02x",getIPLast(sourceAddress)));
        //16进制数，不需要转换
        send.setArea(area);
        //转16进制前补0,2字节4位
        send.setAddress(String.format("%04x",Integer.parseInt(address)));
        if(bitAddress != null && !"".equals(bitAddress)){
            //转16进制前补0,1字节2位
            send.setBitAddress(String.format("%02x",Integer.parseInt(bitAddress)));
        }
        //2字节4位
        send.setWriteLength(String.format("%04x",Integer.valueOf(writeLength)));
        //2字节4位
//        send.setValue(String.format("%04x",Integer.valueOf(value)));
        //点表中是十六进制，不需要转换
        send.setValue(value);
        command = send.getFullCmd();

        return command;
    }



    /**
     * 获取ip的最后一位数字
     * @param ip
     * @return
     */
    public static Integer getIPLast(String ip){
        Integer result = null;
        int index = ip.lastIndexOf(".");
        if(ip.length() > index + 1){
            String num = ip.substring(index+1);
            result = Integer.parseInt(num);
        }
        return result;
    }


}
