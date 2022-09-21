package com.tunnel.platform.business.vms.core;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.business.vms.DevicesConfig;
import com.tunnel.platform.business.vms.DriverBean;
import com.tunnel.platform.business.vms.DriverConfig;
import com.tunnel.platform.business.vms.DevicesBean;
import com.tunnel.business.utils.exception.BusinessException;
import com.tunnel.business.utils.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 设备管理类
 *
 * @author 杨旗超
 * @date 2018年7月13日
 */
public class DevicesManager {

    private static final Logger logger = LoggerFactory.getLogger(DevicesManager.class);

    /**
     * 懒汉模式实例化
     */
    private static DevicesManager instance;

    public static DevicesManager getInstance() {
        if (instance == null)
            instance = new DevicesManager();
        return instance;
    }

    private DevicesManager() {

    }

    /**
     * 获取设备的驱动配置信息
     *
     * @param deviceId
     * @return
     */
    public DriverBean getDeviceDriverById(String deviceId) {
        if (StringUtils.isEmpty(deviceId))
            throw new BusinessException("设备编号为空");
        DevicesBean bean = new DevicesConfig().getDevicesById(deviceId);
        if (bean == null || bean.getVender_version() == null)
            throw new BusinessException("未找到当前设备驱动信息");
        DriverBean driverBean = new DriverConfig().getDriverById(bean
                .getVender_version());
        return driverBean;
    }

    /**
     * 执行Iot设备的指令
     *
     * @param deviceId
     * @param command
     * @return
     */
    public String executeCommand(String deviceId, String command,String type) {
        if (CommonUtil.isNull(deviceId, command)) {
            throw new BusinessException("传入的设备Id或指令为null！");
        }
        /*
         * 2.查询设备的配置信息。
         */
        DevicesBean dcBean = null;
        // 情报板
        if("100".equals(type)){
            dcBean = new DevicesConfig().getDevicesById(deviceId);
        }
        // 道路结冰
     /*else if("110".equals(type)){
        	dcBean = new DevicesConfig().getDevicesInfo(deviceId);
        }*/else if("112".equals(type)){
            dcBean = new DevicesConfig().getDevicesFogInduction(deviceId);
        }


        if (dcBean == null) {
            throw new BusinessException("设备配置文件中没有配置该设备！设备Id:[" + deviceId + "]");
        }
        /*
         * 3.mina连接，并获取指令执行返回的结果
         */
        MinaClient client = new MinaClient(dcBean.getIp(), dcBean.getPort());
        logger.info("Mina连接：设备编号：[" + deviceId + "]");
        String content = client.sendCommand(command, 16);
        if (content == null) {
            throw new BusinessException("Iot设备执行指令异常！");
        }
        logger.info("[handler(standardProtocol)] -OK- 成功完成命令执行！返回结果：["
                + content + "],命令：[" + command + "]");
        return content;
    }

    public String executeCommand1(String deviceId, String command,String type) {
        if (CommonUtil.isNull(deviceId, command)) {
            throw new BusinessException("传入的设备Id或指令为null！");
        }
        /*
         * 2.查询设备的配置信息。
         */
        DevicesBean dcBean = null;
       if("112".equals(type)){
            dcBean = new DevicesConfig().getDevicesFogInduction(deviceId);
        }
        if (dcBean == null) {
            throw new BusinessException("设备配置文件中没有配置该设备！设备Id:[" + deviceId + "]");
        }
        /*
         * 3.mina连接，并获取指令执行返回的结果
         */
        MinaClient client = new MinaClient(dcBean.getIp(), dcBean.getPort());
        logger.info("Mina连接：设备编号：[" + deviceId + "]");
        String content = client.sendCommand1(command, 16);
        if (content == null) {
            throw new BusinessException("Iot设备执行指令异常！");
        }
        logger.info("[handler(standardProtocol)] -OK- 成功完成命令执行！返回结果：["
                + content + "],命令：[" + command + "]");
        return content;
    }
    /**
     * 查询主机配置信息
     * @param ip
     * @param command
     * @return
     */
 public String hostConfigurationExecuteCommand(String ip, String command, String type) {
        if (CommonUtil.isNull(ip, command)) {
            throw new BusinessException("传入的主机配置IP或主机配置指令为null！");
        }
        DevicesBean dcBean = null;
        /*if("112".equals(type)){
        	dcBean = new DevicesConfig().getDevicesFogInduction(hostId);
        }
        if (dcBean == null) {
            throw new BusinessException("设备配置文件中没有配置该设备！设备Id:[" + hostId + "]");
        }*/
         // 3.mina连接，并获取指令执行返回的结果
        MinaClient client = new MinaClient(ip, Integer.parseInt("1030"));
      //  logger.info("Mina连接：设备编号：[" + ip + "]");
        String content = client.sendCommand(command, 16);
        if (content == null) {
            throw new BusinessException("查询设备控制指令异常！");
        }
        System.out.println("返回指令为"+content+"");
        logger.info("[handler(standardProtocol)] -OK- 成功完成命令执行！返回结果：["
                + content + "],命令：[" + command + "]");
        return content;
    }



}