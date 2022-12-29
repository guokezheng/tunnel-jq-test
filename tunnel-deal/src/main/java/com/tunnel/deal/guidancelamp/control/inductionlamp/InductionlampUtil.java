package com.tunnel.deal.guidancelamp.control.inductionlamp;

import com.tunnel.deal.guidancelamp.control.ClientHandler;
import com.tunnel.deal.guidancelamp.control.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InductionlampUtil {

    private static final Logger log = LoggerFactory.getLogger(InductionlampUtil.class);

    private static final int PILOT_LIGHT_MODE_0 = 1;

    private static final int PILOT_LIGHT_MODE_1 = 2;

    private static final int PILOT_LIGHT_MODE_2 = 3;

//    private static final int PILOT_LIGHT_MODE_3 = 3;

//    private static final int PILOT_LIGHT_MODE_4 = 4;

//    private static final int PILOT_LIGHT_MODE_5 = 5;


    public static Map getXianKeDeviceBrightness(String ip, Integer port) {
        //获取当前是否开灯
        String code = "000000000006010300030001";
        NettyClient client = new NettyClient(ip, port, code, 3);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            return null;
        }
        try {
//            client.pushHexCode(code);
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    return null;
                }
                if(clientHandler.DOWNLOADFLAG){
                    //响应指令:
                    String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                    Map<String, Object> map = new HashMap<>();
                    if (codeInfo != "" && codeInfo.startsWith("000") && codeInfo.contains("00000005")) {
                        codeInfo = codeInfo.substring(codeInfo.indexOf("03") + 2);
                        String brightness = codeInfo.substring(0, 2);
                        int brightnessParam = Integer.parseInt(brightness, 16);
                        map.put("brightness", brightnessParam);
                        //发送控制器同步指令
                        client.pushCode("000000000006010600020001");
                        return map;
                    } else {
                        map.put("brightness", "0");
                        return map;
                    }
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            client.stop();
            return null;
        }
        client.stop();
        return null;
    }

    public static Map getXianKeDeviceFrequency(String ip, Integer port) {
        //获取当前是否开灯
        String code = "000000000006010300050001";
        NettyClient client = new NettyClient(ip, port, code, 3);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            return null;
        }
        try {
//            client.pushHexCode(code);
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    return null;
                }
                if(clientHandler.DOWNLOADFLAG){
                    //响应指令:
                    String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                    Map<String, Object> map = new HashMap<>();
                    if (codeInfo != "" && codeInfo.startsWith("000") && codeInfo.contains("00000005")) {
                        codeInfo = codeInfo.substring(codeInfo.indexOf("03") + 2);
                        String frequency = codeInfo.substring(0, 2);
                        int frequencyParam = Integer.parseInt(frequency, 16);
                        map.put("frequency", frequencyParam);
                        return map;
                    } else {
                        map.put("frequency", "0");
                        return map;
                    }
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            client.stop();
            return null;
        }
        client.stop();
        return null;
    }

    public static Map getXianKeDeviceDutyCycle(String ip, Integer port) {
        //获取当前是否开灯
        String code = "000000000006010300070001";
        NettyClient client = new NettyClient(ip, port, code, 3);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            return null;
        }
        try {
//            client.pushHexCode(code);
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    return null;
                }
                if(clientHandler.DOWNLOADFLAG){
                    //响应指令:
                    String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                    Map<String, Object> map = new HashMap<>();
                    if (codeInfo != "" && codeInfo.startsWith("000") && codeInfo.contains("00000005")) {
                        codeInfo = codeInfo.substring(codeInfo.indexOf("03") + 2);
                        String dutyCycle = codeInfo.substring(0, 2);
                        int dutyCycleParam = Integer.parseInt(dutyCycle, 16);
                        map.put("dutyCycle", dutyCycleParam);
                        return map;
                    } else {
                        map.put("dutyCycle", "0");
                        return map;
                    }
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            client.stop();
            return null;
        }
        client.stop();
        return null;
    }

    //当前状态查询
    public static Map getNowOpenState(String ip, Integer port) {
        //获取当前是否开灯
        String code = "1GH+SW?\r\n";
        NettyClient client = new NettyClient(ip, port, code, 1);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            return null;
        }
        try {
            client.pushCode(code);
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    return null;
                }
                if(clientHandler.DOWNLOADFLAG){
                    switch (code) {
                        case "OK":
                            log.info("操作成功。");
                            break;
                        case "ERROR":
                            log.error("操作失败，请检查操作指令并联系管理员。");
                            break;
                        case "INVALID":
                            log.error("无效操作，请检查操作指令并联系管理员。");
                            break;
                        default:
                            //响应指令:
                            String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                            Map<String, Object> map = new HashMap<>();
                            if (codeInfo.equals("1GH+SW=1")) {
                                map.put("openState", "1");
                                return map;
                            } else {
                                map.put("openState", "0");
                                return map;
                            }
                    }
                    client.stop();
                    return null;
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.stop();
        return null;
    }

    public static Map getNowRunMode(String ip, Integer port) {
        //获取当前运行模式
        String code = "1GH+RUNMODE?\r\n";
        NettyClient client = new NettyClient(ip, port, code, 1);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            return null;
        }
        try {
            client.pushCode(code);
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    return null;
                }
                if(clientHandler.DOWNLOADFLAG){
                    switch (code) {
                        case "OK":
                            log.info("操作成功。");
                            break;
                        case "ERROR":
                            log.error("操作失败，请检查操作指令并联系管理员。");
                            break;
                        case "INVALID":
                            log.error("无效操作，请检查操作指令并联系管理员。");
                            break;
                        default:
                            //响应指令:
                            String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                            Map<String, Object> map = new HashMap<>();
                            if (codeInfo.equals("1GH+RUNMODE=B")) {
                                //有线闪烁模式
                                map.put("runMode", "1");
                                return map;
                            } else if (codeInfo.equals("1GH+RUNMODE=W")) {
                                //有线流水模式
                                map.put("runMode", "2");
                                return map;
                            }
                    }
                    client.stop();
                    return null;
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.stop();
        return null;
    }

    public static Map getNowModeAndParameter(String code, String ip, Integer port) {
        NettyClient client = new NettyClient(ip, port, code, 1);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            return null;
        }
        try {
            client.pushCode(code);
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    return null;
                }
                if(clientHandler.DOWNLOADFLAG){
                    switch (code) {
                        case "OK":
                            log.info("操作成功。");
                            break;
                        case "ERROR":
                            log.error("操作失败，请检查操作指令并联系管理员。");
                            break;
                        case "INVALID":
                            log.error("无效操作，请检查操作指令并联系管理员。");
                            break;
                        default:
                            //响应指令: 1GH+BMODE=1,1GH+TPWM= 50, 50,
                            String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                            String[] data = codeInfo.split(",");
                            Map<String, Object> map = new HashMap<>();
                            //拿到当前闪灯模式、亮度、频率，并放到map中
                            String mode = data[0].substring(data[0].indexOf("=") + 1);
                            String brightness = data[1].substring(data[1].indexOf("=") + 1);
                            String frequency = data[2];
                            map.put("mode", mode);
                            map.put("brightness", brightness);
                            map.put("frequency", frequency);
                            return map;
                    }
                    client.stop();
                    return null;
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.stop();
        return null;
    }

    public static Map getNowevacuationSignStatus(String ip, Integer port) {
        //获取当前运行模式
        String code = "1GH+FIRE?\r\n";
        NettyClient client = new NettyClient(ip, port, code, 1);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+port+" 请求链接超时，请联系管理员。");
            client.stop();
            return null;
        }
        try {
            client.pushCode(code);
            //获取返回数据
            ClientHandler clientHandler =  client.getClientHandler();
            //推送数据开始时间
            long st = System.currentTimeMillis();
            //等待返回数据
            while (clientHandler.FLAG){
                long ed = System.currentTimeMillis();
                //判断当前时间是否超时
                if((ed-st)/1000>client.OVERTIME){
                    clientHandler.stop();
                    return null;
                }
                if(clientHandler.DOWNLOADFLAG){
                    switch (code) {
                        case "OK":
                            log.info("操作成功。");
                            break;
                        case "ERROR":
                            log.error("操作失败，请检查操作指令并联系管理员。");
                            break;
                        case "INVALID":
                            log.error("无效操作，请检查操作指令并联系管理员。");
                            break;
                        default:
                            //响应指令:
                            String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                            Map<String, Object> map = new HashMap<>();
                            if (codeInfo.equals("1GH+FIRE=255")) {
                                //直接截取紧急情况对应的疏散标志标号
                                String fireMark = codeInfo.substring(codeInfo.indexOf("=")+1);
                                map.put("fireMark", fireMark);
                                return map;
                            }
                    }
                    client.stop();
                    return null;
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.stop();
        return null;
    }


    /**
     * 根据模式类型以及参数拼接推送数据。
     * @param type      模式类型
     * @param brightnessParam     亮度(需要进行换算处理)
     * @param timeSecond    次数/每秒     最大值159
     * @return
     */
    public static Map getPilotLightMode(Integer type,Integer brightnessParam,Integer timeSecond){
        Map resultMap = new HashMap();
        //查看当前诱导灯： 1有线诱导灯  0无线诱导灯
        //凤凰山隧道为有线诱导灯
        String resultCode = "";
        switch (type){
            case PILOT_LIGHT_MODE_0 :
                resultCode = "1GH+SW=0\r\n";
                resultMap.put("msgInfo","关闭所有灯光。");
                break;
            case PILOT_LIGHT_MODE_1 :
                resultCode = "1GH+SW=1,ABCD=3333,RUNMODE=B,TPWM="+brightnessParam+",TPWMDEF=5,BFREQ="+timeSecond+",TLEDONT=250,BLEDOFFT=50,BMODE=1,\r\n";
                resultMap.put("msgInfo","诱导灯同步单闪，频率"+timeSecond+"，亮度"+brightnessParam+"。");
                break;
            case PILOT_LIGHT_MODE_2 :
                resultCode = "1GH+SW=1,ABCD=3333,RUNMODE=W,TPWM="+brightnessParam+",TPWMDEF=5,WFREQ="+timeSecond+",TLEDONT=250,WLEDDELT=50,WMODE=4,\r\n";
                resultMap.put("msgInfo","流水灯"+timeSecond+"次/min同步闪烁,亮度为"+brightnessParam+"。");
                break;
//            case PILOT_LIGHT_MODE_3 :
//                resultCode = "0GH+SW=1,ABCD=2112,RUNMODE=B,BFREQ="+timeSecond+",BPWM=50,BPWMDEF=5,BMODE=1,\r\n";
//                resultMap.put("msgInfo","黄灯、白灯"+timeSecond+"/min同步闪烁。");
//                break;
//            case PILOT_LIGHT_MODE_4 :
//                resultCode = "0GH+SW=1,ABCD=2112,RUNMODE=B,BFREQ="+timeSecond+",BPWM=50,BPWMDEF=5,BMODE=1,\r\n";
//                resultMap.put("msgInfo","黄灯、白灯"+timeSecond+"次/min同步闪烁。");
//                break;
//            case PILOT_LIGHT_MODE_5 :
//                resultCode = "0GH+SW=1,ABCD=2112,RUNMODE=W,WFREQ=60,WPWM=50,WPWMDEF=5,WMODE=4,\r\n";
//                resultMap.put("msgInfo","逆向流水:与行车方向相反的流水灯(灭).白动模式所有模式灯光亮度3500cd/m.。");
//                break;
            default:
                resultCode = "1GH+SW=0\r\n";
                resultMap.put("msgInfo","关闭所有灯光");
                break;
        }
        resultMap.put("code",resultCode);
        return resultMap;
    }

    public static Map getEvacuationSignLightMode(Integer type,Integer brightnessParam,Integer timeSecond,String fireMark){
        Map resultMap = new HashMap();
        //查看当前疏散标志： 1有线  0无线
        //凤凰山隧道为有线
        String resultCode = "";
        switch (type){
            case PILOT_LIGHT_MODE_0 :
                resultCode = "1GH+FIRE=0\r\n";
                resultMap.put("msgInfo","关闭所有灯光。");
                break;
            case PILOT_LIGHT_MODE_1 :
                resultCode = "1GH+SW=1,FIRE="+fireMark+",RUNMODE=B,TPWM="+brightnessParam+",TPWMDEF=5,BFREQ="+timeSecond+",TLEDONT=250,BLEDOFFT=50,BMODE=1,\r\n";
                resultMap.put("msgInfo","同步单闪，标号地址"+fireMark+"，频率"+timeSecond+"，亮度"+brightnessParam+"。");
                break;
            case PILOT_LIGHT_MODE_2 :
                resultCode = "1GH+SW=1,FIRE="+fireMark+",RUNMODE=W,TPWM="+brightnessParam+",TPWMDEF=5,WFREQ="+timeSecond+",TLEDONT=250,WLEDDELT=50,WMODE=0,\r\n";
                resultMap.put("msgInfo","顺向单闪流水灯，标号地址"+fireMark+"，频率"+timeSecond+"次/min，亮度为"+brightnessParam+"。");
                break;
            default:
                resultCode = "1GH+FIRE=0\r\n";
                resultMap.put("msgInfo","关闭所有灯光");
                break;
        }
        resultMap.put("code",resultCode);
        return resultMap;
    }

    public static byte orVerification(byte[] bytes){
        int nAll = 0;
        for (int i = 0; i < bytes.length; i++) {
            int nTemp = bytes[i];
            nAll = nAll ^ nTemp;
        }
        return (byte) nAll;
    }

    public static Map getDGPilotLightMode(Integer type,String intToHex){
        Map resultMap = new HashMap();
        //查看当前诱导灯： 1有线诱导灯  0无线诱导灯
        String resultCode = "";
        String start = "0a";
        switch (type){
            case PILOT_LIGHT_MODE_0 :
                resultCode = "51" + intToHex + "0700040400000000";
                resultMap.put("msgInfo","关闭所有灯光。");
                break;
            case PILOT_LIGHT_MODE_1 :
                resultCode = "51" + intToHex + "0700010100000000";
                resultMap.put("msgInfo","黄灯白灯常亮。");
                break;
            case PILOT_LIGHT_MODE_2 :
                resultCode = "51" + intToHex + "0700030300000000";
                resultMap.put("msgInfo","灯同步闪烁");
                break;
//            case PILOT_LIGHT_MODE_3 :
//                resultCode = "51" + intToHex + "0700030300000000";
//                resultMap.put("msgInfo","黄灯、白灯同步闪烁。");
//                break;
//            case PILOT_LIGHT_MODE_4 :
//                resultCode = "51" + intToHex + "0700030300000000";
//                resultMap.put("msgInfo","黄灯、白灯同步闪烁。");
//                break;
//            case PILOT_LIGHT_MODE_5 :
//                resultCode = "51" + intToHex + "0700020200000000";
//                resultMap.put("msgInfo","流水");
//                break;
            default:
                resultCode = "51" + intToHex + "0700040400000000";
                resultMap.put("msgInfo","关闭所有灯光");
                break;
        }
        byte b = InductionlampUtil.orVerification(resultCode.getBytes());
        String s = String.valueOf(b);
        String verifyNumber = String.format("%04x",Integer.valueOf(s)).substring(2);
        resultCode = start + resultCode + verifyNumber + "0d";
        resultMap.put("code",resultCode);
        return resultMap;
    }

    //显科设备亮度控制
    public static Map getXianKePilotLightMode(Integer type,Integer brightnessParam){
        Map resultMap = new HashMap();
        String resultCode = "";
        String start = "000000000006";
        switch (type){
            case PILOT_LIGHT_MODE_0 :
                resultCode = "010600040000";
                resultMap.put("msgInfo","关闭所有灯光");
                break;
            case PILOT_LIGHT_MODE_1 :
                resultCode = "01060004000" + brightnessParam;
                resultMap.put("msgInfo","灯亮度为：" + brightnessParam);
                break;
            case PILOT_LIGHT_MODE_2 :
                resultCode = "010600020001";
                resultMap.put("msgInfo","同步所有控制器");
                break;
            default:
                resultCode = "010600040000";
                resultMap.put("msgInfo","关闭所有灯光");
                break;
        }
        resultCode = start + resultCode;
        resultMap.put("code",resultCode);
        return resultMap;
    }

    //显科设备频率控制
    public static Map getXianKeFrequency(Integer type,Integer frequency){
        Map resultMap = new HashMap();
        String resultCode = "";
        String start = "000000000006";
        switch (type){
            case PILOT_LIGHT_MODE_0 :
                resultCode = "010600060000";
                resultMap.put("msgInfo","频率为0");
                break;
            case PILOT_LIGHT_MODE_1 :
                resultCode = "01060006000" + frequency;
                resultMap.put("msgInfo","频率为：" + frequency);
                break;
            default:
                resultCode = "010600060000";
                resultMap.put("msgInfo","频率为0");
                break;
        }
        resultCode = start + resultCode;
        resultMap.put("code",resultCode);
        return resultMap;
    }

    //显科设备占空比控制
    public static Map getXianKeDutyCycle(Integer type,Integer dutyCycle){
        Map resultMap = new HashMap();
        String resultCode = "";
        String start = "000000000006";
        switch (type){
            case PILOT_LIGHT_MODE_0 :
                resultCode = "010600080000";
                resultMap.put("msgInfo","占空比为0");
                break;
            case PILOT_LIGHT_MODE_1 :
                resultCode = "01060008000" + dutyCycle;
                resultMap.put("msgInfo","占空比为：" + dutyCycle);
                break;
            default:
                resultCode = "010600080000";
                resultMap.put("msgInfo","占空比为0");
                break;
        }
        resultCode = start + resultCode;
        resultMap.put("code",resultCode);
        return resultMap;
    }

}
