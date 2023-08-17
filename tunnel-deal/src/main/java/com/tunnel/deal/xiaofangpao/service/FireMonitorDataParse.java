package com.tunnel.deal.xiaofangpao.service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.event.SdEvent;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.electromechanicalPatrol.SdFaultListMapper;
import com.tunnel.business.mapper.event.SdEventMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.event.ISdEventService;
import com.tunnel.business.service.event.impl.SdEventServiceImpl;
import com.tunnel.business.utils.util.UUIDUtil;
import com.tunnel.deal.tcp.util.ByteBufUtil;
import com.tunnel.deal.xiaofangpao.msgEnum.DataTypeCodeEnum;
import com.tunnel.deal.xiaofangpao.msgEnum.DevicesDirectionCodeEnum;
import com.tunnel.deal.xiaofangpao.msgEnum.SystemStatusResponseCodeEnum;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * describe: 测控执行器数据解析类
 *
 * @author zs
 * @date 2023/4/13
 */
@Component
public class FireMonitorDataParse {

    private static final Logger log = LoggerFactory.getLogger(FireMonitorDataParse.class);

    @Autowired
    private FireMonitorService fireMonitorService;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private SdFaultListMapper sdFaultListMapper;

    @Autowired
    private SdEventMapper sdEventMapper;

    @Autowired
    private ISdFaultListService sdFaultListService;

    @Autowired
    private SdEventServiceImpl sdEventServiceImpl;


    /**
     * 数据解析
     * @param ctx
     * @param msg 数据
     */
    public int dataParse(ChannelHandlerContext ctx, String msg){
        //接收到的16进制数
        if(msg == null||msg.equals("")){
            return 400;
        }
        //截取类型标志（01系统状态 02设备状态 3c权限校验）55-57位
        String str = msg.substring(54, 56);
        if(str.toUpperCase().equals(DataTypeCodeEnum.QUANXIANJIAOYAN.getCode())){//权限校验
            if(msg.substring(52, 54).equals("06")){//权限校验失败
                log.error("权限校验失败");
                return 401;
            }
            return 201;
        }else if(str.toUpperCase().equals(DataTypeCodeEnum.SHEBEIZHUANGTAI_UP.getCode())||str.toUpperCase().equals(DataTypeCodeEnum.SHEBEIZHUANGTAI_DOWN.getCode())){//设备状态
//            40 40 启动字符
//            01 00 数据流水号
//            01 06 协议版本号
//            28 3a 0f 0f 06 17 时间标签
//            00 00 00 00 00 00 源地址
//            00 00 00 00 00 00 目的地址
//            ce 01 应用数据单元长度
//            02 命令字节
//            02 0a应用数据单元（类型标志+信息对象数目）
//            0102026401a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130300000 0e300b0f0617（以下此条数据解析为例）
//            0102026501a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130310000 0e300b0f0617
//            0102026601a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130320000 0e300b0f0617
//            0102026701a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130330000 0e300b0f0617
//            0102026801a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130340000 0e300b0f0617
//            0102026901a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130350000 0e300b0f0617
//            0102026a01a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130360000 0e300b0f0617
//            0102026b01a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130370000 0e300b0f0617
//            0102026c01a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130380000 0e300b0f0617
//            0102026d01a8c00200c9e8b1b8d7b4ccacd0c5cfa2b2e2cad43139322e3136382e312e3130390000 0e300b0f0617
//            4e 校验和
//            2323   结束符

            //数据解析例
            //01 系统类型标志
            //02 系统地址
            //02 部件类型
            //64 01 a8 c0 部件地址
            //02 00 部件状态
            //c9 e8 b1 b8 d7 b4 cc ac d0 c5 cf a2 b2 e2 ca d4 31 39 32 2e 31 36 38 2e 31 2e 31 30 30 00 00 部件说明
            //0e 30 0b 0f 06 17 时间戳
            log.info("发送的设备状态");
            int s = Integer.parseInt(msg.substring(56, 58),16);//数目即包含多少设备
            for(int i=0;i<s;i++){
                //截取设备状态数据
                int start = 58+92*i;
                int end = 58+92*(i+1);
                String deviceStatusMsg = msg.substring(start, end);
                String deviceAddress = hex6To10(deviceStatusMsg.substring(6,14));//ip地址
                String deviceState = deviceStatusMsg.substring(14,18);//部件状态
                String deviceStateLow = hex6To2(deviceState.substring(0,2));//低字节
                String deviceStateHigh = hex6To2(deviceState.substring(2));//高字节
                //根据ip查询消防炮设备信息
                SdDevices sdDevices = sdDevicesMapper.getXfpDevicesInfo(deviceAddress);
                String direction="";
                if(sdDevices.getEqDirection().equals(DevicesDirectionCodeEnum.JINANFANGXIANG.getCode())){
                    direction = DevicesDirectionCodeEnum.JINANFANGXIANG.getName()+sdDevices.getPile();
                }else{
                    direction = DevicesDirectionCodeEnum.WEIFANGFANGXIANG.getName()+sdDevices.getPile();
                }
                //部件状态 ----todo 待完善
                String eqStatus = null,dataStatus = null;
                //低字节状态解析
                if(deviceStateLow.substring(7).equals("1")){//正常
                    eqStatus = "1";
                    log.info("设备状态为正常");
                }
                if(deviceStateLow.substring(5,6).equals("1")){//故障
                    eqStatus = "3";
                    log.info("设备状态为故障");
                    //故障位置处理
                    //存 故障清单表sd_fault_list
                    SdFaultList sdFaultList = new SdFaultList();
                    sdFaultList.setId(UUIDUtil.getRandom32BeginTimePK());
                    sdFaultList.setFaultTbtime(DateUtils.getNowDate());//故障填报时间
                    sdFaultList.setCreateTime(DateUtils.getNowDate());// 创建时间
                    sdFaultList.setTunnelId(sdDevices.getEqTunnelId());//隧道
                    sdFaultList.setFaultLocation(direction);//设备位置   方向+桩号拼接
                    sdFaultList.setFaultType("6");//故障类型  其他
                    sdFaultList.setFaultEscalationType("1");//故障来源  系统上报
                    sdFaultList.setFaultFxtime(DateUtils.getNowDate());//故障发现时间
                    sdFaultList.setEqId(sdDevices.getEqId());//设备id
                    sdFaultList.setEqStatus("3");//设备状态  故障
                    sdFaultList.setFaultLevel("0");//故障等级  一般
                    sdFaultList.setFalltRemoveStatue("1");//故障消除状态  未消除
                    sdFaultList.setFaultStatus("0");//故障状态  已发布
                    sdFaultListMapper.insertSdFaultList(sdFaultList);
                    sdFaultListService.faultSendWeb(sdFaultList);//故障推送
                }
                if(deviceStateLow.substring(6,7).equals("1")){//火警
                    eqStatus = "4";
                    log.info("设备状态为火警");
                    //存 故障清单表sd_event
                    SdEvent sdEvent = new SdEvent();
                    sdEvent.setTunnelId(sdDevices.getEqTunnelId());//隧道
                    sdEvent.setEventSource("5");//事件来源  消防炮
                    sdEvent.setEventTypeId((long) 20);//事件类型
                    sdEvent.setEventTitle(sdDevices.getTunnel()+direction+"发生火警");//事件标题  隧道+方向+桩号+发生火警
                    sdEvent.setEventTime(DateUtils.getNowDate());//时间
                    sdEvent.setEventState("3");//状态  待确认
                    sdEvent.setEventGrade("1");//事件等级 一般
                    sdEvent.setStakeNum(sdDevices.getPile());//事件桩号
                    sdEvent.setCreateTime(DateUtils.getNowDate());//创建时间
                    sdEvent.setDirection(sdDevices.getDirection());//方向
                    sdEventMapper.insertSdEvent(sdEvent);
                    sdEventServiceImpl.eventSendWeb(sdEvent);//事件推送
                }
                //高字节状态解析
                if(deviceStateHigh.substring(4,5).equals("1")){//阀开
                    dataStatus = "1";
                    log.info("设备状态为阀开");
                }
                if(deviceStateHigh.substring(5,6).equals("1")){//阀关
                    dataStatus = "2";
                    log.info("设备状态为阀关");
                }
                //更新/新增消防炮设备状态
                fireMonitorService.updateFireMonitorStatus(deviceAddress,eqStatus,dataStatus);
            }

            //应答(读取+上传)根据命令字节判断是上传（02）还是下行（04）
            String commandByte = msg.substring(52, 54);
            //上传时（发送）
            if(commandByte.equals(SystemStatusResponseCodeEnum.XITONGZHUANGTAI_SHANGCHUAN.getCode())){
                String controlUnit = msg.substring(0, 24)+msg.substring(36, 48)+msg.substring(24, 36)+"0000"+SystemStatusResponseCodeEnum.XITONGZHUANGTAI_SHANGCHUANYINGDA.getCode();
                String inStr = controlUnit.substring(4);
                String jyh = makeChecksum(inStr);
                String responseMsg = "4040"+inStr+jyh+"2323";//应答的内容
                System.out.println("msg======="+msg);
                System.out.println("responseMsg======="+responseMsg);
                try {
                    ctx.writeAndFlush(ByteBufUtil.convertStringToByteBuf(responseMsg.replace(" ","")));
                } catch (DecoderException e) {
                    e.printStackTrace();
                }
            }

        }

        return 200;

    }


    /**
     * 应答机制- 应用数据单元计算
     * @param infObjNum
     * @return
     */
    public static String countDataUnit(String infObjNum) {
        String sjdycd = Integer.toHexString(2+10*Integer.parseInt(infObjNum,16));//应用数据单元
        if(sjdycd.length()==1){
            sjdycd = "000"+sjdycd;
        }else if(sjdycd.length()==2){
            sjdycd = "00"+sjdycd;
        }else if(sjdycd.length()==3){
            sjdycd = "0"+sjdycd;
        }else{
            sjdycd = sjdycd;
        }
        return sjdycd;
    }
    /**
     * 应答机制- 校验和计算
     * @param data
     * @return
     */
    public static String makeChecksum(String data) {
        if (data == null || data.equals("")) {
            return "";
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            System.out.println(s);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 16进制转10进制
     * 返回ip地址
     */
    public static String hex6To10(String data) {
        int numbOne = Integer.parseInt(data.substring(0,2),16);
        int numbTwo = Integer.parseInt(data.substring(2,4),16);
        int numbThree = Integer.parseInt(data.substring(4,6),16);
        int numbFour = Integer.parseInt(data.substring(6),16);
        return numbFour+"."+numbThree+"."+numbTwo+"."+numbOne;
    }

    public static String hex6To2(String hexString) {
        int num = Integer.parseInt(hexString,16);
        BigInteger big =  new BigInteger(num+"");
        String binStr = big.toString(2);
        while(binStr.length()<8){
            binStr = "0"+binStr;
        }
        return binStr;
    }



}
