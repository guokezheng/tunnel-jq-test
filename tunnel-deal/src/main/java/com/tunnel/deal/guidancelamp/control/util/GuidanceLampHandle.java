package com.tunnel.deal.guidancelamp.control.util;

import com.ruoyi.framework.web.domain.server.Sys;
import com.serotonin.modbus4j.ModbusMaster;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.deal.guidancelamp.control.NettyClient;
import com.tunnel.deal.guidancelamp.control.inductionlamp.InductionlampUtil;
import com.tunnel.deal.plc.modbus.ModbusTcpMaster;
import com.tunnel.deal.plc.modbus.util.Modbus4jWriteUtils;

import java.util.Map;

/*
 *
 * 设备控制处理类
 * */
public class GuidanceLampHandle {

    /**
     * 懒汉模式实例化
     */
    private static GuidanceLampHandle instance;

    private GuidanceLampHandle() {
    }

    public static GuidanceLampHandle getInstance() {
        if (instance == null) {
            instance = new GuidanceLampHandle();
        }
        return instance;
    }

    public int toControlDev(String deviceId,Integer ctrState,SdDevices sdDevices,String brightness, String frequency, String fireMark) {
        String ip = sdDevices.getIp();
        Integer port = Integer.parseInt(sdDevices.getPort());
        if (sdDevices.getEqType().longValue() == DevicesTypeEnum.YOU_DAO_DENG.getCode().longValue()) {
            //发送诱导灯控制指令
            try {
                String code = "1GH+STATUS?\r\n";
                NettyClient client = new NettyClient(ip, port,code,1);
                client.start(null);
                Map codeMap = InductionlampUtil.getPilotLightMode(ctrState,Integer.parseInt(brightness),Integer.parseInt(frequency));
                client.pushCode(codeMap.get("code").toString());
                client.stop();
            } catch (Exception e) {
                System.err.println("设备编号为" + deviceId + "的设备变更状态失败");
                return 0;
            }
        } else if (sdDevices.getEqType().longValue() == DevicesTypeEnum.SHU_SAN_BIAO_ZHI.getCode().longValue() && !fireMark.equals("")) {
            //发送疏散标志控制指令
            try {
//                String code = "1GH+FIRE?\r\n";
//                NettyClient client = new NettyClient(ip, port,code,1);
//                client.start(null);
//                Map codeMap = InductionlampUtil.getEvacuationSignLightMode(ctrState,Integer.parseInt(brightness),Integer.parseInt(frequency),fireMark);
//                client.pushCode(codeMap.get("code").toString());
//                client.stop();
            } catch (Exception e) {
                System.err.println("设备编号为" + deviceId + "的设备变更状态失败");
                return 0;
            }
        }
        return 1;
    }

}
