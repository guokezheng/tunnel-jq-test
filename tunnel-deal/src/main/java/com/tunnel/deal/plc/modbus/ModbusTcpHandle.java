package com.tunnel.deal.plc.modbus;

import com.serotonin.modbus4j.ModbusMaster;
import com.tunnel.deal.plc.modbus.util.Modbus4jWriteUtils;

/*
 *
 * 设备控制处理类
 * */
public class ModbusTcpHandle {

    /**
     * 懒汉模式实例化
     */
    private static ModbusTcpHandle instance;

    public static ModbusTcpHandle getInstance() {
        if (instance == null) {
            instance = new ModbusTcpHandle();
        }
        return instance;
    }

    private ModbusTcpHandle() {
    }

    /*
     * 车道指示器控制（根据现场实际点位情况做控制）-网联测试基地凤凰山隧道
     * 针对每一隧道点位需重写此方法
     * 对照表 现场状态->管控平台     {需在此处对状态做出转换}
     *     1:正绿反红->正绿反红1  初始信号{无点位}
     *     2:正反全关->正反全关4  {true, false, false}
     *     3:正红反红->正红反红3  {false, true, false}
     *     4:正红反绿->正红反绿2  {false, false, true}
     * */
    public void toControlCZ(ModbusMaster master, int slaveId, int startOffset, Integer ctrState) {
        try {
            boolean[] data = new boolean[]{false, false, false};
            if (ctrState == 1) {
                //初始信号为正绿反红，不做任何处理
            } else if (ctrState == 2) {
                //参考点位表做出变更
                data[3] = true;
            } else if (ctrState == 3) {
                //参考点位表做出变更对应 2
                data[2] = true;
            } else {
                data[1] = true;
            }
            Modbus4jWriteUtils.writeCoils(master, slaveId, startOffset, data);
        } catch (Exception e) {
            System.out.println("编号为" + slaveId + "的设备：控制失败");
        }
    }

    //复位-正向通行-1
    boolean[] one = {false, false, false};
    //复位-关闭-2
    boolean[] four = {true, false, false};
    //复位-禁行-3
    boolean[] three = {false, true, false};
    //复位-逆向通行-4
    boolean[] two = {false, false, true};

}
