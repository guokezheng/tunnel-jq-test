package com.tunnel.deal.modbusFourJ.util;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.*;
import com.tunnel.deal.tcp.modbus.ModbusFunctionCode;

/**
 * describe: modbus4j 写入数据工具类
 *
 * @author zs
 * @date 2023/10/26
 */
public class Modbus4jWriteUtil {

    public static boolean writeData(ModbusMaster master,int writeOffset, String writeValue,String functionCode){
        int slaveId = 1;
        return writeData(master,slaveId,writeOffset,writeValue,functionCode);
    }


    public static boolean writeData(ModbusMaster master, int slaveId, int writeOffset, String writeValue,String functionCode){
        boolean result = false;
        switch (functionCode) {
            case ModbusFunctionCode.CODE_FIVE:
                //todo 点位如何配置待测试
                // 0,1
                boolean writeB = str2boolean(writeValue);
                result = writeCoil(master,slaveId,writeOffset,writeB);
                break;
            case ModbusFunctionCode.CODE_SIX:
                short writeS = Short.valueOf(writeValue);
                result = writeRegister(master,slaveId,writeOffset,writeS);
                break;
            case ModbusFunctionCode.CODE_FIFTEEN:
                //0100
//                Boolean[] array = writeValue.chars().mapToObj((c) -> (char)c == '1').toArray(Boolean[]::new);

                boolean[] array = str2booleanArray(writeValue);
                result = writeCoils(master,slaveId,writeOffset,array);
                break;
            case ModbusFunctionCode.CODE_SIXTEEN:
                //todo 点位如何配置待测试
//                result = writeRegisters(master,slaveId,writeOffset,writeValue);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 将字符串"0"、"1"转换成boolean型false、true
     * @param str
     * @return
     */
    public static boolean str2boolean(String str){
        boolean result = false;
        if("1".equals(str)){
            result = true;
        }
        return result;
    }

    /**
     * 将由0、1组成的字符串转换成布尔类型的数组（0是false,1是true）
     * @param str
     * @return
     */
    public static boolean[] str2booleanArray(String str){

        char[] array = str.toCharArray();
        boolean[] result = new boolean[array.length];
        for(int i = 0; i < array.length; i++){
            char c = array[i];
            if(c == 0){
                result[i] = false;
            }else{
                result[i] = true;
            }
        }
        return result;
    }



    /**
     * 写单个（线圈）开关量数据
     * 功能码为：05，开关量输出点Q置位或复位，写入数据到真机的DO类型的寄存器上面，可以读写的布尔类型(0x)
     * @param slaveId     slave的ID
     * @param writeOffset 位置-预访问的地址-地址范围：0-255
     * @param writeValue  值-置位则为1，复位则为0
     * @return 是否写入成功
     */
    public static boolean writeCoil(ModbusMaster master, int slaveId, int writeOffset, boolean writeValue){
        boolean flag = false;
        try {
            // 创建请求
            WriteCoilRequest request = new WriteCoilRequest(slaveId, writeOffset, writeValue);
            // 发送请求并获取响应对象
            WriteCoilResponse response = (WriteCoilResponse) master.send(request);
            flag =  !response.isException();
        }catch (ModbusTransportException e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 写多个开关量数据（线圈）
     * 功能码为：0F，写多个开关量数据（线圈）
     * @param slaveId     slaveId
     * @param startOffset 开始位置
     * @param bdata       写入的数据
     * @return 是否写入成功
     */
    public static boolean writeCoils(ModbusMaster master,int slaveId, int startOffset, boolean[] bdata) {
        boolean flag = false;
        try {
            // 创建请求
            WriteCoilsRequest request = new WriteCoilsRequest(slaveId, startOffset, bdata);
            // 发送请求并获取响应对象
            WriteCoilsResponse response = (WriteCoilsResponse) master.send(request);
            flag = !response.isException();
        }catch (ModbusTransportException e){
            e.printStackTrace();
        }
        return flag;
    }

    /***
     *  保持寄存器写单个
     *  功能码为：06，将数据写入至V存储器， 数据到真机，数据类型是Int,可以读写的数字类型(4x)
     * @param slaveId slaveId
     * @param writeOffset 开始位置
     * @param writeValue 写入的数据
     */
    public static boolean writeRegister(ModbusMaster master,int slaveId, int writeOffset, short writeValue){
        boolean flag = false;
        try {
            // 创建请求对象
            WriteRegisterRequest request = new WriteRegisterRequest(slaveId, writeOffset, writeValue);
            // 发送请求并获取响应对象
            WriteRegisterResponse response = (WriteRegisterResponse) master.send(request);
            flag = !response.isException();
        }catch (ModbusTransportException e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 保持寄存器写入多个模拟量数据
     * 功能码为：16,将数据写入至多个V存储器，写入数据到真机，数据类型是short[],可以读写的数字类型(4x)
     * @param slaveId     modbus的slaveID
     * @param startOffset 起始位置偏移量值
     * @param sdata       写入的数据
     * @return 返回是否写入成功
     */
    public static boolean writeRegisters(ModbusMaster master,int slaveId, int startOffset, short[] sdata) {
        boolean flag = false;
        try {
            // 创建请求对象
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, startOffset, sdata);
            // 发送请求并获取响应对象
            WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
            flag = !response.isException();
        }catch (ModbusTransportException e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据类型写数据（如:写入Float类型的模拟量、Double类型模拟量、整数类型Short、Integer、Long）
     *
     * @param value    写入值
     * @param dataType com.serotonin.modbus4j.code.DataType
     */
    public static void writeHoldingRegister(ModbusMaster master,int slaveId, int offset, Number value, int dataType) {
        try {
            // 类型
            BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, dataType);
            master.setValue(locator, value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
