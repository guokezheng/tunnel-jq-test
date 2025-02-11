package com.tunnel.deal.plc.fins.mina;


import com.tunnel.business.datacenter.config.MapCache;
import com.tunnel.business.datacenter.domain.dataVo.CmdInfo;
import com.tunnel.deal.plc.fins.CmdProcess;
import com.tunnel.deal.plc.fins.PlcProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 设备管理类
 *
 * @author yangqichao
 */
public class DevicesManager {

    private static final Logger logger = LoggerFactory.getLogger(DevicesManager.class);

    /**
     * 懒汉模式实例化
     */
    private static DevicesManager instance;

    private DevicesManager() {

    }

    public static DevicesManager getInstance() {
        if (instance == null) {
            instance = new DevicesManager();
        }
        return instance;
    }

    /**
     * 执行Iot设备的查询指令
     *
     * @param plcId
     * @param
     * @return
     */
    public void executeCommand(String plcId) {
        /*
         * 1.查询设备的配置信息。
         */
        Map<String, CmdInfo> cmdMap = CmdProcess.CmdUtil.getCmdMap();
        CmdInfo cmdInfo = cmdMap.get(plcId);
        /*
         * 2.mina连接，并获取指令执行返回的结果
         */
        MinaClient client = new MinaClient(plcId, cmdInfo.getPlcIp(), 9600);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*
         * 3.先执行握手协议。在发送命令
         */
        client.closeNow();

        MapCache.sessionCache.remove(String.valueOf(plcId));

        PlcProcess.shutdown();

    }
}
