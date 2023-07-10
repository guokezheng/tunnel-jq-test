package com.tunnel.deal.tcp.modbus.rtu;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.deal.tcp.modbus.ModbusCmdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * describe: Modbus Rtu接收指令解析
 *
 * @author zs
 * @date 2023/7/5
 */
public class ModbusRtuCmdResolver {


    private static final Logger log = LoggerFactory.getLogger(ModbusRtuCmdResolver.class);

    public static JSONObject commandParse(String msg){
        JSONObject jsonObject = new JSONObject();
        String address = msg.substring(0,2);
        String functionCode = msg.substring(2,4);
        if(ModbusRtuFunctionCode.CODE_SIX.equals(functionCode)){
            return jsonObject;
        }
        String dataLength = msg.substring(4,6);
        String readData = msg.substring(6,msg.length() - 4);
        if(ModbusRtuFunctionCode.CODE_THREE.equals(functionCode)){
            if(readData.length() % 4 == 0){
                List<String> list = ModbusCmdResolver.handleTwoBytesData(readData);
                jsonObject.put("data",list);
            }
        }

        return jsonObject;
    }
}
