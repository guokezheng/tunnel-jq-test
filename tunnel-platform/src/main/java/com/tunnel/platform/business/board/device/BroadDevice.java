package com.tunnel.platform.business.board.device;

import com.github.pagehelper.util.StringUtil;
import com.tunnel.platform.business.board.DriverBean;
import com.tunnel.platform.business.board.core.DeviceDisplayFactory;
import com.tunnel.platform.business.board.core.DevicesManager;
import com.tunnel.platform.business.board.core.IDeviceProtocol;
import com.tunnel.platform.utils.exception.BusinessException;
import com.tunnel.platform.utils.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BroadDevice implements IDeviceMonitor {

    /*
     * 目前支持的类型。
     */
    final static int DEVICE_TYPE_BOARD = 0;
    final static int DEVICE_TYPE_CAMERA = 1;
    final static int DEVICE_TYPE_TOLL = 2;
    final static int DEVICE_TYPE_NONE = -1;

    final static String DEVICE_PREFIX_CAMERA = "36";
    final static String DEVICE_PREFIX_BOARD = "1";
    final static String DEVICE_PREFIX_TOLL = "38";
    final static String DEVICE_PREFIX_NONE = "0";


    /**
     * 获取情报板当前显示信息
     */
    @Override
    public Map<String, String> getDeviceDisplayByDeviceId(String deviceId) {
        Map<String, String> values = new HashMap<String, String>();
        // 1.存放返回信息
        String command;
        try {
            DriverBean driverBean = DevicesManager.getInstance().getDeviceDriverById(deviceId);
            if (driverBean == null)
                throw new IllegalArgumentException("找不到该设备的驱动！");
            command = driverBean.getCommands().get("DISPLAY");
            String content = DevicesManager.getInstance().executeCommand(deviceId, command ,"100");
            values.put("RESULT", DeviceDisplayFactory.getCurrentDisplay(deviceId, content));
        } catch (Exception e) {
            values.put("ERROR", "获取协议命令有误！" + e.getMessage());
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 获取情报板所有显示信息
     */
    @Override
    public Map<String, String> getDeviceDisplayListByDeviceId(String deviceId) {

        String content = null; //定义设备返回信息

        DriverBean driverBean = DevicesManager.getInstance().getDeviceDriverById(deviceId);
        if (driverBean == null)
            throw new BusinessException("暂无该设备驱动信息");
        //获取该设备协议名称
        String vender = driverBean.getVender_version();

        //获取协议参数信息创建链接获取情报板内容
        if (vender.startsWith(IDeviceProtocol.TONGZHOU) || vender.startsWith(IDeviceProtocol.DIANMING)) {
            //此处改动  先获取当前播放列表编号  （下发情报板内容编号默认为00。）根据列表编号下发信息
            content = DisPlayUtil.getListDisPlayForTongZhouAndDianMing(deviceId, driverBean.getCommands().get("DISPLAYLISTNUM"));
        } else if (vender.startsWith(IDeviceProtocol.XIANKE)) {
            content = DevicesManager.getInstance().executeCommand(deviceId, driverBean.getCommands().get("DISPLAYLIST"),"100");
        } else if (vender.startsWith(IDeviceProtocol.SANSI) || vender.startsWith(IDeviceProtocol.GUANGDIAN)) {
            content = DisPlayUtil.getlistDisPlayForSanSiAndGuangDian(driverBean.getCommands().get("DISPLAYLIST"), deviceId);
        } else if (vender.startsWith(IDeviceProtocol.DINGEN)) {
            content = DevicesManager.getInstance().executeCommand(deviceId, driverBean.getCommands().get("DISPLAY"),"100");
        } else {
            content = DevicesManager.getInstance().executeCommand(deviceId, driverBean.getCommands().get("DISPLAYLIST"),"100");
        }

        Map<String, String> disPlayList = new HashMap<String, String>();
        //获取数据进行解析（鼎恩无次协议，获取当前显示内容进行解析）
        if (vender.startsWith(IDeviceProtocol.DINGEN)) {
            content = DeviceDisplayFactory.getCurrentDisplay(deviceId, content);
            disPlayList.put("result", content);
            disPlayList.put("vender", vender);
        } else {
            content = DeviceDisplayFactory.getPlayListDisplay(deviceId, content);
            disPlayList.put("vender", vender);
            disPlayList.put("result", content);

        }
        return disPlayList;
    }

    @Override
    public Boolean controlDeviceByDeviceId(String deviceId, String protocolType, String content) {

        Boolean result = false;
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("ID=" + deviceId);
        System.out.println("content1=" + content);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
        String protocol = DevicesManager.getInstance().executeCommand(deviceId, content,"100");
        if (protocolType.startsWith(IDeviceProtocol.TONGZHOU) || protocolType.startsWith(IDeviceProtocol.DIANMING)) {
            if ("31".equals(protocol.substring(21, 23))) {
                result = true;
            }
        } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
            if ("01".equals(protocol.substring(15, 17))) {
                result = true;
            }
        } else if (protocolType.startsWith(IDeviceProtocol.SANSI) || protocolType.startsWith(IDeviceProtocol.GUANGDIAN)) {
            if ("30".equals(protocol.substring(9, 11))) {
                result = true;
            }
        } else if (protocolType.startsWith(IDeviceProtocol.DINGEN)) {
            if ("30".equals(CommonUtil.find(protocol, 2, " "))) {
                result = true;
            }
        } else {
            if ("30".equals(CommonUtil.find(protocol, 2, " "))) {
                result = true;
            }
        }
        if (result) {
            System.out.println("内容[" + content + "]已上传情报板！");
        } else {
            System.out.println("内容[" + content + "]上传情报板失败！");
        }
        return result;
    }


    @Override
    public List<String> getDeviceFaultByDeviceId(String deviceId) {
        List<String> faultInformation = new ArrayList<String>();
        // 1.存放返回信息
        String command;
        String content;
        DriverBean driverBean = DevicesManager.getInstance().getDeviceDriverById(deviceId);
        if (driverBean == null)
            throw new BusinessException("找不到该设备的驱动！");
        String vender = driverBean.getVender_version();
        command = driverBean.getCommands().get("FAULT");
        if (vender.startsWith(IDeviceProtocol.TONGZHOU) || vender.startsWith(IDeviceProtocol.DIANMING)) {
            content = DevicesManager.getInstance().executeCommand(deviceId, command,"100");
        } else if (vender.startsWith(IDeviceProtocol.XIANKE)) {
            content = DevicesManager.getInstance().executeCommand(deviceId, command,"100");
        } else if (vender.startsWith(IDeviceProtocol.SANSI) || vender.startsWith(IDeviceProtocol.GUANGDIAN)) {
            content = DevicesManager.getInstance().executeCommand(deviceId, command,"100");
        } else {
            content = DevicesManager.getInstance().executeCommand(deviceId, command,"100");
        }

        faultInformation = DeviceDisplayFactory.getDeviceFault(deviceId, content);
        return faultInformation;
    }

    @Override
    public Map<String, String> getDeviceBadPointsByDeviceId(String deviceId) {
        Map<String, String> values = new HashMap<String, String>();
        // 1.存放返回信息
        String command;
        try {
            DriverBean driverBean = DevicesManager.getInstance().getDeviceDriverById(deviceId);
            if (driverBean == null)
                throw new IllegalArgumentException("找不到该设备的驱动！");
            command = driverBean.getCommands().get("BRIGHTNESS");
            String content = DevicesManager.getInstance().executeCommand(deviceId, command,"100");
            String currentBrightness = DeviceDisplayFactory.getCurrentBrightness(deviceId, content);
            if (!StringUtil.isEmpty(currentBrightness)) {
                String TJFS = currentBrightness.substring(0, currentBrightness.length() - 2);
                String BRIGHTNESS = currentBrightness.substring(currentBrightness.length() - 2, currentBrightness.length());

                values.put("state", "0");
                values.put("brightness", BRIGHTNESS);
                values.put("mode", TJFS);
            } else {
                values.put("state", "1");
                values.put("result", "无法获取到此设备的亮度信息");

            }
        } catch (Exception e) {
            values.put("state", "1");
            values.put("result", "获取协议命令有误！" + e.getMessage());
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public Map<String, String> getDeviceBrightnessByDeviceId(String deviceId) {
        Map<String, String> values = new HashMap<String, String>();
        // 1.存放返回信息
        String command;
        try {
            DriverBean driverBean = DevicesManager.getInstance().getDeviceDriverById(deviceId);
            if (driverBean == null)
                throw new IllegalArgumentException("找不到该设备的驱动！");
            command = driverBean.getCommands().get("BRIGHTNESS");
            String content = DevicesManager.getInstance().executeCommand(deviceId, command,"100");
            String currentBrightness = DeviceDisplayFactory.getCurrentBrightness(deviceId, content);
            if (!StringUtil.isEmpty(currentBrightness)) {
                String TJFS = currentBrightness.substring(0, currentBrightness.length() - 2);
                String BRIGHTNESS = currentBrightness.substring(currentBrightness.length() - 2, currentBrightness.length());

                values.put("state", "0");
                values.put("brightness", BRIGHTNESS);
                values.put("mode", TJFS);
            } else {
                values.put("state", "1");
                values.put("result", "无法获取到此设备的亮度信息");

            }
        } catch (Exception e) {
            values.put("state", "1");
            values.put("result", "获取协议命令有误！" + e.getMessage());
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public Map<String, String> updateBrightnessByDeviceId(String id, String mode, String brightness) {
        Map<String, String> values = new HashMap<String, String>();
        // 1.存放返回信息
        String command;
        String command1;
        try {
            DriverBean driverBean = DevicesManager.getInstance().getDeviceDriverById(id);
            if (driverBean == null)
                throw new IllegalArgumentException("找不到该设备的驱动！");

            String vender = driverBean.getVender_version();

            if (vender.startsWith(IDeviceProtocol.TONGZHOU) || vender.startsWith(IDeviceProtocol.DIANMING)) {
                command = BrightnessUtil.getDianMingBrightnessCommond(mode, brightness);
                String protocol = DevicesManager.getInstance().executeCommand(id, command,"100");
                protocol = protocol.replaceAll("1B E7", "02");
                protocol = protocol.replaceAll("1B E8", "03");
                protocol = protocol.replaceAll("1B 00", "1B");
                String substring = protocol.substring(protocol.length() - 11, protocol.length() - 9);
                if ("31".equals(substring)) {
                    values.put("state", "0");
                } else {
                    values.put("state", "1");
                }

            } else if (vender.startsWith(IDeviceProtocol.XIANKE)) {
                command = BrightnessUtil.getXiankeBrightnessCommond(mode, brightness);
                String protocol = DevicesManager.getInstance().executeCommand(id, command,"100");
                protocol = protocol.replaceAll("1B E7", "02");
                protocol = protocol.replaceAll("1B E8", "03");
                protocol = protocol.replaceAll("1B 00", "1B");
                String substring = protocol.substring(protocol.length() - 14, protocol.length() - 12);
                if ("01".equals(substring)) {
                    values.put("state", "0");
                } else {
                    values.put("state", "1");
                }
            } else if (vender.startsWith(IDeviceProtocol.SANSI)) {
                command = BrightnessUtil.getSanSiBrightnessCommond1(mode);
                String protocol = DevicesManager.getInstance().executeCommand(id, command,"100");
                protocol = protocol.replaceAll("1B E7", "02");
                protocol = protocol.replaceAll("1B E8", "03");
                protocol = protocol.replaceAll("1B 00", "1B");
                String substring = protocol.substring(protocol.length() - 11, protocol.length() - 9);
                if ("30".equals(substring)) {
                    if ("1".equals(mode)) {
                        command1 = BrightnessUtil.getSanSiBrightnessCommond2(brightness);
                        String protocol1 = DevicesManager.getInstance().executeCommand(id, command1,"100");
                        protocol1 = protocol1.replaceAll("1B E7", "02");
                        protocol1 = protocol1.replaceAll("1B E8", "03");
                        protocol1 = protocol1.replaceAll("1B 00", "1B");
                        String substring1 = protocol.substring(protocol1.length() - 11, protocol1.length() - 9);
                        if ("30".equals(substring1)) {
                            values.put("state", "0");
                        } else {
                            values.put("state", "1");
                        }
                    } else {
                        values.put("state", "0");
                    }
                } else {
                    values.put("state", "1");
                }


            } else if (vender.startsWith(IDeviceProtocol.GUANGDIAN)) {
                command = BrightnessUtil.getGuangDianBrightnessCommond(mode, brightness);
                String protocol = DevicesManager.getInstance().executeCommand(id, command,"100");
                protocol = protocol.replaceAll("1B E7", "02");
                protocol = protocol.replaceAll("1B E8", "03");
                protocol = protocol.replaceAll("1B 00", "1B");
                String substring = protocol.substring(protocol.length() - 11, protocol.length() - 9);
                if ("30".equals(substring)) {
                    values.put("state", "0");
                } else {
                    values.put("state", "1");
                }
            } else {
                values.put("state", "1");
            }
        } catch (Exception e) {
            values.put("state", "1");
            values.put("result", "获取协议命令有误！" + e.getMessage());
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public Map<String, String> getDevicePixelByDeviceId(String deviceId) {
        Map<String, String> values = new HashMap<String, String>();
        // 1.存放返回信息
        String command;
        try {
            DriverBean driverBean = DevicesManager.getInstance().getDeviceDriverById(deviceId);
            if (driverBean == null)
                throw new IllegalArgumentException("找不到该设备的驱动！");
            command = driverBean.getCommands().get("DISPLAY");
            String content = DevicesManager.getInstance().executeCommand(deviceId, command,"100");
            values.put("RESULT", DeviceDisplayFactory.getCurrentDisplay(deviceId, content));
        } catch (Exception e) {
            values.put("ERROR", "获取协议命令有误！" + e.getMessage());
            e.printStackTrace();
        }
        return values;
    }

}
