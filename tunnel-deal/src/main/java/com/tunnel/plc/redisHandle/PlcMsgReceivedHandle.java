package com.tunnel.plc.redisHandle;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.tunnel.platform.datacenter.domain.dataVo.CmdInfo;
import com.tunnel.plc.fins.CmdProcess;
import com.zc.common.core.redis.RedisMessageDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Integer a = 0;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        a++;
        String body = new String(message.getBody());
        logger.info("收到的mq消息" + body);
        addControl(body);
    }

    @Override
    public boolean isChannelExist(String channel) {
        if ("PLC:CONTROL".equals(channel)) {
            return true;
        }
        return false;
    }

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
