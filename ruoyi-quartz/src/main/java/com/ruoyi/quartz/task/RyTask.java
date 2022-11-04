package com.ruoyi.quartz.task;

import com.ruoyi.framework.web.domain.server.Sys;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * 定时任务调度测试
 *
 */
@Component("ryTask")
public class RyTask {

    @Value("${authorize.name}")
    private String deploymentType;

    @Value("${spring.kafka.wulian.bootstrap-servers}")
    private String servers;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    private static final Logger log = LoggerFactory.getLogger(RyTask.class);

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params) {
        if (params.equals("devices-status_check") && deploymentType.equals("GSY")) {
            String[] serverGroup = servers.split(",");
            boolean flag = false;
            for (int i = 0;i < serverGroup.length;i++) {
                String server = serverGroup[i];
                String ip = server.substring(0, server.indexOf(":"));
                String port = server.substring(server.indexOf(":") + 1);
                Boolean detection = ipDetection(ip, Integer.parseInt(port), 3000);
                if (detection) {
                    flag = true;
                }
            }
            if (!flag) {
                List<SdDevices> sdDevices = sdDevicesMapper.selectSdDevicesList(new SdDevices());
                for (int i = 0;i < sdDevices.size();i++) {
                    SdDevices devices = sdDevices.get(i);
                    devices.setEqStatus("2");
                    devices.setEqStatusTime(new Date());
                    sdDevicesMapper.updateSdDevices(devices);
                }
            }
        }
    }

    public Boolean ipDetection(String ipAddress, Integer port, Integer timeout) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(InetAddress.getByName(ipAddress), port), timeout);
        } catch (IOException e) {
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




    public void ryNoParams() {
        System.out.println("执行无参方法");
    }



}
