package com.tunnel.deal.tcp.modbus;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.deal.tcp.client.config.MessageType;
import com.tunnel.deal.tcp.util.NumberSystemConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * describe: modbus协议--接收指令解析类
 *
 * @author zs
 * @date 2021/2/5
 */
public class ModbusCmdResolver {

    private static final Logger log = LoggerFactory.getLogger(ModbusCmdResolver.class);

    // todo 考虑错误码的情况  00 00 00 00 00 03 01 84 03

    public static JSONObject commandParse(String ip, String deviceId, String msg){
        JSONObject jsonObject = new JSONObject();
        if(msg.length() < ModbusCmdValues.RECV_CMD_MIN_LENGTH){
            jsonObject.put("msg", MessageType.ERROR_MESSAGE);
            log.error("查询指令返回错误结果："+msg+",指令可能粘包，请排查问题;ip="+ip+",deviceId="+deviceId);
            System.out.println("查询指令返回错误结果："+msg+",指令可能粘包，请排查问题;ip="+ip+",deviceId="+deviceId);
            return jsonObject;
        }
        String serial = msg.substring(0,4);
//        Integer serialInt = NumberSystemConvert.convertHexToInteger(serial);
        //返回指令中的起始地址
        jsonObject.put("address",serial);
        String remainLength = msg.substring(8,12);
        String deviceAddress = msg.substring(12,14);
        String functionCode = msg.substring(14,16);
//        jsonObject.put("serial",serial);
        jsonObject.put("functionCode",functionCode);

        if(msg.length() <= ModbusCmdValues.RECV_CMD_NORMAL_MIN_LENGTH){
            //分析错误码
            log.error("查询指令返回错误结果："+msg+",功能码为："+functionCode+"ip="+ip+",deviceId="+deviceId);
        }else{
            String dataLength = msg.substring(16,18);
            String readData = msg.substring(18);
            jsonObject.put("readData",readData);
        }
        return jsonObject;
    }

    /**
     * 解析读取的数据,2个字节作为一个数据
     * (不做进制转换，输出十六进制数据)
     * @param data
     * @return
     */
    public static List<String> handleTwoBytesDataHex(String data){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < data.length(); ){
            int len = 4;
            String item = data.substring(i,i+len);
            list.add(item);
            i += len;
        }
        return list;
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

            try{
                Integer num = NumberSystemConvert.convertHexToInteger(item);
                list.add(String.valueOf(num));
            }catch (Exception e){
                e.printStackTrace();
                log.error("转换报错的数据是："+item);
                log.error("handleTwoBytesData 数据转换报错："+e.getMessage());
            }

            i += len;
        }

        return list;
    }


    /**
     * 解析读取的数据,4个字节作为一个数据，比如32位浮点数
     * @return
     */
    public static List<String> handleFourBytesData(String data){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < data.length(); ){
            int len = 8;
            String item = data.substring(i,i+len);

            try{
                Float num = NumberSystemConvert.convertHexToFloat(item);
                list.add(String.valueOf(num));
            }catch (Exception e){
                e.printStackTrace();
                log.error("转换报错的数据是："+item);
                log.error("handleFourBytesData浮点数转换报错："+e.getMessage());
            }

            i += len;
        }

        return list;
    }

    /**
     * 解析数据：1位表示一个数据，通常是状态量
     * @return
     */
    public static List<String> handleOneBitData(String data){
        List<String> list = new ArrayList<>();
       //00 01 00 00 00 07 01 03 04 00 00 00 00
        for(int i = 0; i < data.length(); ){
            int len = 2;
            String item = data.substring(i,i+len);

            String bit = NumberSystemConvert.convertHexToBit(item);
            char[] array = bit.toCharArray();
            for(char c : array){
                list.add(String.valueOf(c));
            }

            i += len;
        }

        return list;
    }


    /**
     * 解析数据：1位表示一个数据，通常是状态量
     * 将每个数字转换为整型，再转换为二进制
     * @return
     */
    public static List<String> handleOneBitDataNew(String data){
        List<String> list = new ArrayList<>();
        //00 01 00 00 00 07 01 03 04 00 00 00 00
        for(int i = 0; i < data.length(); ){
            int len = 2;
            String item = data.substring(i,i+len);
            Integer itemNum = Integer.parseInt(item,16);
            String bit = Integer.toBinaryString(itemNum);
            Integer bitNum = Integer.parseInt(bit);
            String result = String.format("%08d",bitNum);
            //00001010
            char[] array = result.toCharArray();
            //逆序
            for(int j = array.length - 1; j >= 0; j--){
                char c = array[j];
                list.add(String.valueOf(c));
            }
            i += len;
        }

        return list;
    }
}
