package com.tunnel.deal.tcp.plc.omron.fins;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.deal.tcp.plc.omron.task.OmronFinsTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * describe: Fins指令解析类
 *
 * @author zs
 * @date 2023/8/24
 */
public class FinsCmdResolver {

    private static final Logger log = LoggerFactory.getLogger(FinsCmdResolver.class);

    public static JSONObject commandParse(String deviceId,String msg){
        //信息接收成功的标志，成功之后可以关闭通道
        boolean flag = false;

        JSONObject jsonObject = new JSONObject();
        //46494E53 00000010 00000001 00000000 000000C8 00000001
        int cmdLength = msg.length();
        if(cmdLength == FinsCmdValues.ERROR_REPLY_LENGTH){
            //46494e53000000080000000300000003
            String header = msg.substring(0,8);
            String length = msg.substring(8,16);
            String command = msg.substring(16,24);
            String errorCode = msg.substring(24,32);
            if(FinsErrorCodeEnum.UNSUPPORTED_COMMAND.getCode().equals(errorCode)){
                log.error(FinsErrorCodeEnum.UNSUPPORTED_COMMAND.getName()+"，指令="+msg);
            }
        }
        if(cmdLength == FinsCmdValues.HANDSHAKE_CMD_LENGTH){
            //握手命令
            String header = msg.substring(0,8);
            String length = msg.substring(8,16);
            String command = msg.substring(16,24);
            String errorCode = msg.substring(24,32);
            String clientNodeAddress = msg.substring(32,40);
            String serverNodeAddress = msg.substring(40);
            if(FinsErrorCodeEnum.NORMAL.getCode().equals(errorCode) || FinsErrorCodeEnum.CONNECT_DONE.getCode().equals(errorCode)){
                //正常，连接建立
                //如果发生错误，服务器回应的命令会包含错误码，连接断开，端口立刻关闭。
                // 当连接建立之后，不要再次发送这个命令，否则服务器会返回03错误码，即不支持的命令。
                log.info("握手命令成功，连接建立：msg="+msg);
                //握手成功
                OmronFinsTask.deviceMap.get(deviceId).put("handshake",true);
            }else{
                OmronFinsTask.deviceMap.get(deviceId).put("handshake",false);
            }
        }
        if(cmdLength > FinsCmdValues.COMMAND_MIN_LENGTH){
            //46 49 4E 53 00 00 00 1A 00 00 00 02 00 00 00 00 C0 0002 00 C8 00 00 01 00 00 01 01 00 00 00 00 00 01
            String header = msg.substring(0,8);
            String length = msg.substring(8,16);
            String command = msg.substring(16,24);
            String fixedErrorCode = msg.substring(24,32);
            String fixedValue = msg.substring(32,40);
            String da1 = msg.substring(40,42);
            String fixedValueTwo = msg.substring(42,46);
            String sa1 = msg.substring(46,48);
            String fixedValueThree = msg.substring(48,52);
            String queryCode = msg.substring(52,56);

            if(FinsQueryCodeEnum.READ_CODE.getCode().equals(queryCode)){
                //读操作
                String errorCode = msg.substring(56,60);
                if(FinsCmdValues.NORMAL_REPLY_CODE.equals(errorCode)){
                    //读取成功
                    String value = msg.substring(60);
                    jsonObject.put("value",value);
                    flag = true;

                    //读取数据缓存
                    OmronFinsTask.pointCmdCache.put(deviceId+"query",value);
                }

            }
            if(FinsQueryCodeEnum.WRITE_CODE.getCode().equals(queryCode)){
                //写操作
                String errorCode = msg.substring(56,60);
                if(FinsCmdValues.NORMAL_REPLY_CODE.equals(errorCode)){
                    //写入成功
                    flag = true;
                }
            }

        }
        jsonObject.put("flag",flag);
        return jsonObject;
    }

    /**
     * 解析读取的数据,2个字节作为一个数据
     * @param data
     * @return
     */
    public static List<String> handleTwoBytesData(String data){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < data.length(); ){
            int len = 4;
            String item = data.substring(i,i+len);
            list.add(item);
            i += len;
        }
        return list;
    }

}
