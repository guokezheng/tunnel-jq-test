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

    public int toControlDev(String deviceId,Integer ctrState,SdDevices sdDevices,String brightness, String frequency) {
        String ip = sdDevices.getIp();
        Integer port = Integer.parseInt(sdDevices.getPort());
        //发送诱导灯控制指令
        try {
            String code = "1GH+STATUS?\r\n";
            NettyClient client = new NettyClient(ip, port,code,1);
            client.start(null);
            Map codeMap = InductionlampUtil.getPilotLightMode(ctrState,Integer.parseInt(brightness),Integer.parseInt(frequency));
            client.pushCode(codeMap.get("code").toString());
            client.stop();
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println("设备编号为" + deviceId + "的设备变更状态失败");
            return 0;
        }
        return 1;
    }

    /*
     * 车道指示器控制（根据现场实际点位情况做控制）-网联测试基地凤凰山隧道
     * 针对每一隧道点位需重写此方法
     * 对照表 现场状态->管控平台     {需在此处对状态做出转换}
     * */
    public void toControlCZ(ModbusMaster master, int slaveId, int startOffset, Integer ctrState) {
        try {
            boolean[] data = new boolean[]{false, false, false};
            if (ctrState == 1) {
                //初始信号为正绿反红，不做任何处理
            } else if (ctrState == 2) {
                //参考点位表做出变更
                data[2] = true;
            } else if (ctrState == 3) {
                //参考点位表做出变更对应 2
                data[1] = true;
            } else {
                data[0] = true;
            }
            Modbus4jWriteUtils.writeCoils(master, slaveId, startOffset, data);
        } catch (Exception e) {
            System.out.println("编号为" + slaveId + "的设备：控制失败");
        }
    }

}
