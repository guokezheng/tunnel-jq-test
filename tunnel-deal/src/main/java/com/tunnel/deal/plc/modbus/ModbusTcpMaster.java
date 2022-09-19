package com.tunnel.deal.plc.modbus;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.utils.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModbusTcpMaster {


    public static Map<String, ModbusMaster> masterMap = new HashMap<>();
    /**
     * 懒汉模式实例化
     */
    private static ModbusTcpMaster instance;
    private static ModbusFactory modbusFactory;

    static {
        if (modbusFactory == null) {
            modbusFactory = new ModbusFactory();
        }
    }

    @Autowired
    ISdTunnelsService tunnelService;
    @Autowired
    ISdDevicesService devicesService;

    private ModbusTcpMaster() {
    }

    public static ModbusTcpMaster getInstance() {
        if (instance == null) {
            instance = new ModbusTcpMaster();
        }
        return instance;
    }

    // 在构造方法执行后执行
    public void init() {
        //创建PLC客户端
//        createClient();
    }

    private void createClient() {
        tunnelService = (ISdTunnelsService) SpringContextUtils.getBean(ISdTunnelsService.class);
        devicesService = (ISdDevicesService) SpringContextUtils.getBean(ISdDevicesService.class);
        List<SdTunnels> sdTunnelsList = tunnelService.selectSdTunnelsList(new SdTunnels());
        for (SdTunnels sdTunnels : sdTunnelsList) {
            SdDevices sdDevices = new SdDevices();
            sdDevices.setEqTunnelId(sdTunnels.getTunnelId());
            sdDevices.setEqType(DevicesTypeEnum.PLC.getCode().longValue());
            List<SdDevices> sdDevicesList = devicesService.selectSdDevicesList(sdDevices);
            for (SdDevices sdDevice : sdDevicesList) {
                if (!masterMap.containsKey(sdDevice.getEqId())) {
                    ModbusMaster master = getMaster(sdDevice.getEqId(), sdDevice.getIp(), Integer.parseInt(sdDevice.getPort()));
                    if (master != null) {
                        masterMap.put(sdDevice.getEqId(), master);
                    }
                }
            }
        }
    }


    /**
     * 获取Tcp master
     *
     * @param plcId
     * @param ip
     * @param port
     * @return
     */
    public ModbusMaster getMaster(String plcId, String ip, int port) {
        devicesService = (ISdDevicesService) SpringContextUtils.getBean(ISdDevicesService.class);
        //查询PLC主机，并修改实时状态
        SdDevices plcDev = new SdDevices();
        plcDev.setEqId(plcId);
        plcDev.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
        plcDev.setEqStatusTime(new Date());
        //查询PLC主机关联设备，并根据PLC状态修改其关联设备实时状态
        SdDevices plcDevList = new SdDevices();
        plcDevList.setFEqId(plcId);
        plcDevList.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
        IpParameters params = new IpParameters();
        params.setHost(ip);
        params.setPort(port);
        //这个属性确定了协议帧是否是通过tcp封装的RTU结构，采用modbus tcp/ip时，要设为false, 采用modbus rtu over tcp/ip时，要设为true
        params.setEncapsulated(false);
        // 参数1：IP和端口信息 参数2：保持连接激活
        ModbusMaster master = null;
        master = modbusFactory.createTcpMaster(params, true);
        try {
            //设置超时时间
            master.setTimeout(500);
            //设置重连次数
            master.setRetries(0);
            //初始化
            master.init();
        } catch (ModbusInitException e) {
            plcDev.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            devicesService.updateSdDevices(plcDev);
            plcDevList.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            devicesService.updateSdDevicesByFEqId(plcDevList);
            return null;
        }
        devicesService.updateSdDevices(plcDev);
        devicesService.updateSdDevicesByFEqId(plcDevList);
        return master;
    }


}
