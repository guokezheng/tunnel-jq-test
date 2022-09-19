package com.tunnel.deal.plc.redisHandle;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.serotonin.modbus4j.ModbusMaster;
import com.tunnel.business.datacenter.domain.dataVo.CmdInfo;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.utils.util.SpringContextUtils;
import com.tunnel.deal.plc.fins.CmdProcess;
import com.tunnel.deal.plc.modbus.ModbusTcpHandle;
import com.tunnel.deal.plc.modbus.ModbusTcpMaster;
import com.zc.common.core.redis.RedisMessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理接收 Redis PLC控制缓存订阅消息
 *
 * @author yangqichao
 */
@AutoService(RedisMessageDispatcher.class)
public class PlcMsgReceivedHandle implements RedisMessageDispatcher {

    private static Logger logger = LoggerFactory.getLogger(PlcMsgReceivedHandle.class);

    @Autowired
    ISdDevicesService devicesService;

    private Integer a = 0;

    @Override
    public boolean isChannelExist(String channel) {
        if ("PLC:CONTROL".equals(channel)) {
            return true;
        }
        return false;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        a++;
        String ctrBody = new String(message.getBody());
        logger.info("收到的mq消息" + ctrBody);
        toControlDev(ctrBody);
    }

    private void toControlDev(String ctrBody) {
        Map<String, Object> ctrResult = (Map<String, Object>) JSON.parse(ctrBody);
        String deviceId = ctrResult.get("deviceId").toString();
        devicesService = (ISdDevicesService) SpringContextUtils.getBean(ISdDevicesService.class);
        SdDevices sdDevices = devicesService.selectSdDevicesById(deviceId);
        String[] point = sdDevices.getEqControlPointAddress().split(",");
        Long deviceType = sdDevices.getEqType();
        String plcId = sdDevices.getFEqId();
        Map<String, ModbusMaster> masterMap = ModbusTcpMaster.masterMap;
        ModbusMaster master = masterMap.get(plcId);
        if (deviceType == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode()) {
            Integer ctrState = Integer.parseInt(ctrResult.get("ctrState").toString());
            ModbusTcpHandle.getInstance().toControlCZ(master, 1, Integer.parseInt(point[0]) - 1, ctrState);
        }
    }


    public void getCheZhiState(boolean fHong, boolean fLv, boolean zHong, boolean zLv) {


        Integer state = null;

        boolean[] data = new boolean[4];
        for (int i = 0; i < data.length; i++) {
            data[i] = false;
        }

        data[state] = false;
        data[state] = true;
        data[state] = true;
        data[state] = true;


        //复位-正向通行-1
        boolean[] one = {true, false, false, false};
        //复位-逆向通行-4
        boolean[] two = {false, false, false, true};
        //复位-禁行-3
        boolean[] three = {false, false, true, false};
        //复位-关闭-2
        boolean[] four = {false, true, false, false};


    }


    /*
     * 此方法需重新对接
     * */
    private void addControl(String json) {
        Map<String, Object> result = (Map<String, Object>) JSON.parse(json);
        Long plcId = Long.valueOf(result.get("plcId").toString());
        Integer deviceType = Integer.valueOf(result.get("deviceType").toString());
        String command = result.get("command").toString();
        HashMap<String, String> commandMap = new HashMap<>();
        CmdInfo cmdInfo = CmdProcess.CmdUtil.getCmdMap().get(plcId);
        if (cmdInfo != null) {
            String cmd = CmdProcess.DisPlayUtil.getComDisPlay(cmdInfo.getPlcIp(), command);
            if (deviceType == 7 || deviceType == 8 || deviceType == 9 || deviceType == 12) {
                resetZM(cmd, plcId);
            }
            commandMap.put("kz", cmd);
            List<Map<String, String>> cmdList = cmdInfo.getCmdList();
            List<Map<String, String>> cmdList2 = new ArrayList<>();
            cmdList2.add(commandMap);
            for (int i = 0; i < cmdList.size(); i++) {
                cmdList2.add(cmdList.get(i));
            }
            cmdInfo.setCmdList(cmdList2);
        }
    }

    //照明设备需要二次复位
    public void resetZM(String cmd, Long plcId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String command = cmd.substring(0, cmd.length() - 5) + "00 00";
                HashMap<String, String> commandMap = new HashMap<>();
                commandMap.put("kz", command);
                CmdInfo cmdInfo = CmdProcess.CmdUtil.getCmdMap().get(plcId);
                List<Map<String, String>> cmdList2 = new ArrayList<>();
                cmdList2.add(commandMap);
                for (int i = 0; i < cmdInfo.getCmdList().size(); i++) {
                    cmdList2.add(cmdInfo.getCmdList().get(i));
                }
                cmdInfo.setCmdList(cmdList2);
            }
        });
        thread.start();
    }


}
