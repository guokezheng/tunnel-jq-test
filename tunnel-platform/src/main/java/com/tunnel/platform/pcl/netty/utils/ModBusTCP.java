package com.tunnel.platform.pcl.netty.utils;

import com.tunnel.platform.pcl.netty.client.ClientHandler;
import com.tunnel.platform.pcl.netty.client.NettyClient;
import com.tunnel.platform.utils.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModBusTCP implements Runnable{

    private String ip;

    private int port;

    private int plcIndex;

    private List<Map> hostsInfo;

    /**
     * 根据rtu柜机数据结构拼接数据   并解析数据。
     */
    public String getPushCode(int initial){
        //字节1(4字节)	字节5(2字节)	    字节7(1字节)	    字节8(1字节)	    字节9(2字节)	    字节11(2字节)
        //事务 /  协议	长度	        单元标识	        功能码	        起始值	        数量(字符)
        //00 00  00 00	00 06 	        01	            03	            00 00	        00 0A
        //读取柜机数据
        String resultCode = "0000000000060103";
        //1获取 rtu 柜机 起始位置
        String initialCode= StringUtil.seamProtectionHexString(Integer.toHexString(initial),2);
        //2   读取柜机寄存器个数  默认柜机寄存器个数为 13
        String endCode= StringUtil.seamProtectionHexString(Integer.toHexString(13),2);
        //3   拼接读取数据
        resultCode+=initialCode+endCode;
        return resultCode;
    }


    @Override
    public void run() {
        pushTCP();
    }

    public ModBusTCP(String ip, int port, int plcIndex) {
        this.ip = ip;
        this.port = port;
        this.plcIndex = plcIndex;
    }

    public void pushTCP(){
        NettyClient client = new NettyClient(ip, port);
        try {
            client.start();
        } catch (Exception e) {
            System.out.println("链接失败。");
            return;
        }
        client.pushHexCode(getPushCode(plcIndex));
        ClientHandler clientHandler =  client.getClientHandler();
        while(true){
            if(clientHandler.DOWNLOADFLAG){
                String dataCode = clientHandler.getCode().toString();
                System.out.println("返回详情信息"+dataCode);
                //获取数据解析
                //字节1(4字节)	字节5(2字节)	字节7(1字节)	字节8(1字节)	字节9(1字节)	字节10
                //事务/协议	    长度	单元标识	    功能码	    字节数	数据
                //0000  0000	0017	01	        03	        14
                String length = dataCode.substring(16,18);

                String data = dataCode.substring(18);

                System.out.println("length:"+length);

                System.out.println("data:"+data);
                //data 存入 redis
                String binCode = data;
                System.out.println(binCode);
                //获取当前柜机对应数据详情信息。
                List<Map> list = new ArrayList<>();
                //解析设备状态详情信息。
                //每字节代表 一个 寄存器存储点。 将每个节点对应解析。
                for (int i = 0; i < list.size(); i++) {
                    list.get(i);
                    String code = binCode.substring(0,2);

                    binCode = binCode.substring(2);
                }
                //hostsInfo
                client.stop();
                return;
            }
        }
    }

}
