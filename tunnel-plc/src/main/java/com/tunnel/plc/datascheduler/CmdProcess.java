package com.tunnel.plc.datascheduler;


import com.tunnel.platform.datacenter.domain.dataVo.CmdInfo;
import com.tunnel.plc.plcutil.CmdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author yangqichao
 * @date 2019/9/17 13:52
 */
public class CmdProcess {

    private static final Logger logger = LoggerFactory.getLogger(CmdProcess.class);

    public static List<String> PlcList = new ArrayList<>();

    public void updateCmdCache() {
        PlcList.clear();
        Map<String, CmdInfo> cmdMap = CmdUtil.getCmdMap();
        for (Map.Entry<String, CmdInfo> entry : cmdMap.entrySet()) {
            String plcIp = entry.getValue().getPlcIp();
            Boolean hostReachable = isHostReachable(plcIp, 5000);
            Boolean hostConnectable = isHostConnectable(plcIp, 9600);
            if (hostReachable && hostConnectable) {
                PlcList.add(entry.getKey());
            }
        }
    }


    protected Boolean isHostReachable(String host, int timeout) {
        try {
            return InetAddress.getByName(host).isReachable(timeout);
        } catch (UnknownHostException e) {
            logger.debug("host=" + host);
            e.printStackTrace();
        } catch (IOException e) {
            logger.debug("host+" + host);
            e.printStackTrace();
        }
        return false;
    }


    protected Boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 5000);
        } catch (IOException e) {
            logger.error("Host+Port=" + host + ":" + port);
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
