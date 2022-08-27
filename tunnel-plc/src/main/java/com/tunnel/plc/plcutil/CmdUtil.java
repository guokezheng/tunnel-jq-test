package com.tunnel.plc.plcutil;


import com.tunnel.platform.datacenter.domain.dataVo.CmdInfo;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdHosts;
import com.tunnel.platform.domain.dataInfo.SdPlcCmd;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.tunnel.platform.service.dataInfo.ISdPlcCmdService;
import com.tunnel.platform.service.dataInfo.ISdTunnelsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdUtil {

    private static final Logger logger = LoggerFactory.getLogger(CmdUtil.class);

    private static Map<String, CmdInfo> cmdMap = new HashMap<>();

    public static Map<String, SdHosts> hostList = new HashMap<>();

    public static Map<String, SdDevices> deviceList = new HashMap<>();

    public static Map<String, CmdInfo> getCmdMap() {
        return cmdMap;
    }

    @Resource
    ISdTunnelsService tunnelService;
    @Resource
    ISdPlcCmdService plcCmdService;
    @Resource
    ISdDevicesService devicesService;


    // 在构造方法执行后执行
    @PostConstruct
    public void init() throws UnknownHostException {
        //此处表结构改变，逻辑需要重新修改 todo
//        initMap();
    }

    //初始化内存
    @SuppressWarnings("static-access")
    public void initMap() throws UnknownHostException {
        //查询需要巡检的隧道
        SdTunnels tunnelModel = new SdTunnels();
        tunnelModel.setPoll(0L);
        List<SdTunnels> tunnelList = tunnelService.selectSdTunnelsList(tunnelModel);
        for (SdTunnels tunnel : tunnelList) {
            //查询需要巡检的PLC
            SdDevices sdDevice = new SdDevices();
            sdDevice.setEqTunnelId(tunnel.getTunnelId());//隧道ID
            sdDevice.setEqType(0L);//设备类型
            sdDevice.setIsMonitor(0L);//是否监控
            List<SdDevices> plcLists = devicesService.selectSdDevicesList(sdDevice);

            for (SdDevices plcDevice : plcLists) {
                SdHosts plc = new SdHosts();
                plc.setPlcTunnelId(tunnel.getTunnelId());
                //PLC类型的设备ip不能为空？ todo
                plc.setPlcIp(plcDevice.getIp());
                plc.setPlcPort(plcDevice.getPort());
                plc.setPlcId(plcDevice.getEqId());
                hostList.put(plc.getPlcId(), plc);
                //查询需要巡检的设备并放入缓存中
                SdDevices sdDevices = new SdDevices();
                sdDevices.setFEqId(plcDevice.getEqId());
                List<SdDevices> devLists = devicesService.selectSdDevicesList(sdDevices);
                for (SdDevices dev : devLists) {
                    deviceList.put(dev.getEqId(), dev);
                }
                /*
                 *  目前只支持DM与CIO协议
                 *  DM：支持单个设备的数据获取
                 *  CIO：PLC下关联的某种类型的设备数据获取
                 *
                 *  获取所有的指令并放入缓存中
                 * */
                String plcId = plcDevice.getEqId();
                String plcIp = plcDevice.getIp();
                CmdInfo cmdInfo = new CmdInfo();
                List<Map<String, String>> commands = new ArrayList<>();
                switch (plcDevice.getProtocol()) {
                    case "DM":
                        SdDevices dev = new SdDevices();
                        dev.setFEqId(plcId);
                        List<SdDevices> plcDeviceLists = devicesService.selectSdDevicesList(dev);
                        for (SdDevices device : plcDeviceLists) {
                            HashMap<String, String> commandMap = new HashMap<>();
                            if (device.getEqControlPointAddress() == null || "".equals(device.getEqControlPointAddress())) {//设备没有查询指令直接跳过
                                continue;
                            }
                            String mandInfo = device.getEqControlPointAddress();
                            if (mandInfo != null) {
                                InetAddress ia = null;
                                ia = ia.getLocalHost();
                                String localIp = ia.getHostAddress();
                                int index = localIp.lastIndexOf(".");
                                int ipEnd = Integer.parseInt(localIp.substring(index + 1));
                                String hex = Integer.toHexString(ipEnd);
                                if (hex.length() < 2) {
                                    hex = "0" + hex.toUpperCase();
                                }
                                mandInfo = mandInfo.replace("##", hex);
                            }
                            commandMap.put(device.getEqId(), mandInfo);//设备ID+查询指令替换之前的设备类型+查询指令
                            commands.add(commandMap);
                        }
                        //添加plcId
                        cmdInfo.setHostId(plcId);
                        //添加plcIp
                        cmdInfo.setPlcIp(plcIp);
                        //添加plc控制类型
                        cmdInfo.setHostControlType("DM");
                        //添加指令集合
                        cmdInfo.setCmdList(commands);
                        cmdMap.put(plcId, cmdInfo);
                        break;
                    case "CIO":
                        SdPlcCmd sdPlcCmd = new SdPlcCmd();
                        sdPlcCmd.setCmdPlcId(plcId);
                        List<SdPlcCmd> sdPlcCmdList = plcCmdService.selectSdPlcCmdList(sdPlcCmd);
                        for (SdPlcCmd plcCmd : sdPlcCmdList) {
                            HashMap<String, String> commandMap = new HashMap<>();
                            commandMap.put(plcCmd.getCmdDevicesType(), DisPlayUtil.getComDisPlay(plcId, plcIp, plcCmd.getCommand()));
                            commands.add(commandMap);
                        }
                        cmdInfo.setHostId(plcId);
                        cmdInfo.setPlcIp(plcIp);
                        //添加plc控制类型
                        cmdInfo.setHostControlType("CIO");
                        cmdInfo.setCmdList(commands);
                        cmdMap.put(plcId, cmdInfo);
                        break;
                    default:
                        break;
                }
            }
        }

        for (Map.Entry<String, CmdInfo> entry : cmdMap.entrySet()) {
            logger.info("Key =" + entry.getKey() + ",Value =" + entry.getValue().getCmdList());
        }

    }
}

