package com.tunnel.deal.modbusFourJ.util;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.*;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;


/**
 * describe: modbus4j 批量读取数据工具类
 * 功能码01/02/03/04，读取多个连续地址
 *
 * @author zs
 * @date 2023/10/26
 */
public class Modbus4jReadMultipleUtil {

    public static short[] readRegister(ModbusMaster master,int offset, int numberOfBits,String functionCode,int dataLength){
        int slaveId = 1;
        return readRegister(master,slaveId,offset,numberOfBits,functionCode,dataLength);
    }


    public static short[] readRegister(ModbusMaster master, int slaveId, int offset, int numberOfBits,String functionCode,int dataLength){
        short[] result = null;
        try {
            switch (functionCode) {
                case ModbusFunctionCode.CODE_THREE:
                    result = readHoldingRegister(master, slaveId, offset, numberOfBits);
                    break;
                case ModbusFunctionCode.CODE_FOUR:
                    result = readInputRegisters(master, slaveId, offset, numberOfBits);
                    break;
                default:
                    break;
            }
        } catch (ModbusInitException | ModbusTransportException | ErrorResponseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 读（线圈）开关量数据
     * 功能码为：01； 读取开关量输出点的ON/OFF状态,可以读写的布尔类型(0x)---00001 至 0xxxx – 开关量输出
     *
     * @param slaveId slaveId-从站编号-自行约定
     * @param offset  位置
     * @return 读取值-读取多少个
     */
    public static boolean[] readCoilStatus(ModbusMaster master, int slaveId, int offset, int numberOfBits)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {

        ReadCoilsRequest request = new ReadCoilsRequest(slaveId, offset, numberOfBits);
        ReadCoilsResponse response = (ReadCoilsResponse) master.send(request);
        boolean[] booleans = response.getBooleanData();
        return valueRegroup(numberOfBits, booleans);
    }



    /**
     * 开关数据 读取外围设备输入的开关量
     * 功能码为：02；读取开关量输入点的ON/OFF状态,只能读的布尔类型(1x)---10001 至 1xxxx – 开关量输入
     *
     * @param slaveId-从站编号-自行约定
     * @param offset-预访问的地址-地址范围：0-255
     * @param numberOfBits-读取多少个
     * @return
     * @throws ModbusTransportException
     * @throws ErrorResponseException
     * @throws ModbusInitException
     */
    public static boolean[] readInputStatus(ModbusMaster master, int slaveId, int offset, int numberOfBits)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, offset, numberOfBits);
        ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
        boolean[] booleans = response.getBooleanData();
//        System.out.println(StringUtil.bytesString16(response.getData()) );
        return valueRegroup(numberOfBits, booleans);
    }

//    public static byte[] readHoldingRegisterFloat(ModbusMaster master, int slaveId, int offset, int numberOfBits)
//            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
//        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, offset, numberOfBits);
//        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
//        return response.getData();
//    }


    /**
     * 读取保持寄存器数据
     * 功能码为：03 读取保持寄存器的数据,可以读写的数字类型(4x)---40001 至 4xxxx – 保持寄存器
     * <p>
     * *举例子说明：S7-200
     * Smart PLC中，设置  [HoldStr~]=&VB1000;则对应的保持寄存器地址为VW1000\VW1002\VW10004
     * *在java中对应的address为：0、1、2
     *
     * @param slaveId      slave Id-从站编号-自行约定
     * @param offset       位置
     * @param numberOfBits numberOfRegisters 寄存器个数  每个寄存器表示一个16位无符号整数 相当于一个short
     */
    public static short[] readHoldingRegister(ModbusMaster master, int slaveId, int offset, int numberOfBits)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, offset, numberOfBits);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
        return response.getShortData();
    }

    /**
     * 读取保持寄存器数据
     * 功能码为：03 读取保持寄存器的数据,可以读写的数字类型(4x)---40001 至 4xxxx – 保持寄存器
     * <p>
     * *举例子说明：S7-200
     * Smart PLC中，设置  [HoldStr~]=&VB1000;则对应的保持寄存器地址为VW1000\VW1002\VW10004
     * *在java中对应的address为：0、1、2
     *
     * @param slaveId      slave Id-从站编号-自行约定
     * @param offset       位置
     * @param numberOfBits numberOfRegisters 寄存器个数  每个寄存器表示一个16位无符号整数 相当于一个short
     */
    public static byte[] readHoldingRegisterByte(ModbusMaster master, int slaveId, int offset, int numberOfBits)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, offset, numberOfBits);
        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
        byte[] result =  response.getData();
        return result;
    }


    /**
     * 读取外围设备输入的数据
     * 功能码为：04 读取模拟量输入值，只能读的数字类型(3x)---30001 至 3xxxx – 模拟量输入
     * <p>
     * 举例子说明：S7-200 Smart PLC中，模拟量输入寄存器AIW16\AIW18,则对应
     * java中对应的address为：8\9
     *
     * @param slaveId slaveId-从站编号-自行约定
     * @param offset  位置-预访问的地址-地址范围：0-55
     */
    public static short[] readInputRegisters(ModbusMaster master, int slaveId, int offset, int numberOfBits)
            throws ModbusTransportException, ErrorResponseException, ModbusInitException {
        ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, offset, numberOfBits);
        ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);
        return response.getShortData();
    }

    /**
     * 数据重组
     *
     * @param numberOfBits
     * @param values
     * @return
     */
    private static boolean[] valueRegroup(int numberOfBits, boolean[] values) {
        boolean[] bs = new boolean[numberOfBits];
        int temp = 1;
        for (boolean b : values) {
            bs[temp - 1] = b;
            temp++;
            if (temp > numberOfBits) {
                break;
            }
        }
        return bs;
    }


    /**
     *  将byte[]数组转换成16进制字符。一个byte生成两个字符，长度对应1:2
     * @param bytes，输入byte[]数组
     * @return 16进制字符
     */
    public static String byte2Hex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        // 遍历byte[]数组，将每个byte数字转换成16进制字符，再拼接起来成字符串
        for (int i = 0; i < bytes.length; i++) {
            // 每个byte转换成16进制字符时，bytes[i] & 0xff如果高位是0，输出将会去掉，所以+0x100(在更高位加1)，再截取后两位字符
            builder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return builder.toString();
    }

}
