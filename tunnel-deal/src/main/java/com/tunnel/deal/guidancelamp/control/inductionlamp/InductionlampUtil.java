package com.tunnel.deal.guidancelamp.control.inductionlamp;

import com.tunnel.business.domain.dataInfo.InductionlampControlStatusDetails;
import com.tunnel.business.domain.dataInfo.InductionlampControlStatusParam;
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

    /**
     * 传感器查询
     * 查询指令:
     * 0GH+STATUS?\r\n
     *
     * 响应指令:
     * 0GH+STATUS=GPS1,LUX10000,HUM850,TEMP+280
     */
    public static Map  sensorQuery(List<InductionlampControlStatusParam> paramList, InductionlampControlStatusDetails statusDetails, String ip, Integer prot){
        String code = "0GH+STATUS?\r\n";
        NettyClient client = new NettyClient(ip, prot,code,1);
        try {
            client.start(null);
        } catch (Exception e) {
            log.error(ip+":"+prot+" 请求链接超时，请联系管理员。");
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
                            //0GH+STATUS=GPS1,LUX10000,HUM850,TEMP+280
                            //GPS2表示  GPS后面的数字 0:表示正在搜星 1:表示授时成功 2:表示授时失败
                            String codeInfo = clientHandler.getCode().toString().replace("\r\n","").replace(" ","");
                            String[] strCode = codeInfo.split(",");
                            log.info("codeInfo:"+codeInfo);
                            // LUX后面的数字表示光照传感器数值
                            BigDecimal lux  = new BigDecimal(strCode[1].substring(3));
                            log.info(strCode[1]+":光照："+lux);
                            // HUM413表示湿度41.3%
                            BigDecimal humBig  = new BigDecimal(strCode[2].substring(3)).divide(new BigDecimal(10),1,BigDecimal.ROUND_HALF_UP);
                            log.info(strCode[2]+":湿度："+humBig);
                            // TEMP+261表示温度26.1℃
                            BigDecimal tempB  = new BigDecimal(strCode[3].substring(5)).divide(new BigDecimal(10),1,BigDecimal.ROUND_HALF_UP);
                            log.info(strCode[3]+":湿度："+tempB);
                            //模式类型
                            InductionlampControlStatusParam statusParam = getPilotLightMode(lux,humBig,tempB,paramList);
                            Integer modeType;
                            if(statusDetails!=null){
                                //获取当前设备模式类型
                                modeType = statusDetails.getEquipmentModeType();
                                //查看当前模式是否更改。如果更改，则重新推送  模式类型
                                if(statusParam.getModeCode() != modeType){
                                    //获取模式参数   亮度、次/min
                                    Integer brightnessParam  = statusParam.getBrightnessParam();
                                    Integer timeSecond = statusParam.getTimeSecond();
                                    Map codeMap = getPilotLightMode(statusParam.getModeCode(),brightnessParam,timeSecond);
                                    String pushCode = codeMap.get("code").toString();
                                    //推送模式指令
                                    client.pushCode(pushCode);
                                    //关闭状态链接
                                    client.stop();
                                    log.info("推送模式为："+statusParam.getModeCode());
                                    log.info("推送指令为："+pushCode);
                                    codeMap.put("modeCode",statusParam.getModeCode());
                                    return codeMap;
                                }
                            }else{
                                //默认类型为 0
                                modeType = 0;
                                //获取模式参数   亮度、次/min
                                Integer brightnessParam  = statusParam.getBrightnessParam();
                                Integer timeSecond = statusParam.getTimeSecond();
                                Map codeMap = InductionlampUtil.getPilotLightMode(modeType,brightnessParam,timeSecond);
                                String pushCode = codeMap.get("code").toString();
                                //推送模式指令
                                client.pushCode(pushCode);
                                //关闭状态链接
                                client.stop();
                                log.info("推送模式为："+modeType);
                                log.info("推送指令为："+pushCode);
                                codeMap.put("modeCode",modeType);
                                return codeMap;
                            }
                            break;
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

    //诱导灯当前状态查询
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



    /**
     * 根据参数 获取模式信息
     * @param illumination  光照
     * @param humidity      湿度
     * @param temperature   温度
     */
    public static InductionlampControlStatusParam getPilotLightMode(BigDecimal illumination, BigDecimal humidity, BigDecimal temperature, List<InductionlampControlStatusParam> paramList){
        for (InductionlampControlStatusParam statusParam:paramList) {
            //光照
            if(statusParam.getIlluminationStart()==null){
                if(statusParam.getIlluminationEnd()!=null){
                    int result = illumination.compareTo(new BigDecimal(statusParam.getIlluminationEnd()));
                    if(result == 0||result == -1){
                        return statusParam;
                    }
                }
            }else{
                if(statusParam.getIlluminationEnd()!=null){
                    int resultS = illumination.compareTo(new BigDecimal(statusParam.getIlluminationStart()));
                    int result = illumination.compareTo(new BigDecimal(statusParam.getIlluminationEnd()));
                    if((result == 0||result == -1)&&resultS == 1){
                        return statusParam;
                    }
                }else{
                    int resultS = illumination.compareTo(new BigDecimal(statusParam.getIlluminationStart()));
                    if(resultS == 1){
                        return statusParam;
                    }
                }
            }
            //湿度
            if(statusParam.getHumidityStart()==null){
                if(statusParam.getHumidityEnd()!=null){
                    int result = humidity.compareTo(new BigDecimal(statusParam.getHumidityEnd()));
                    if(result == 0||result == -1){
                        return statusParam;
                    }
                }
            }else{
                if(statusParam.getHumidityEnd()!=null){
                    int resultS = humidity.compareTo(new BigDecimal(statusParam.getHumidityStart()));
                    int result = humidity.compareTo(new BigDecimal(statusParam.getHumidityEnd()));
                    if((result == 0||result == -1)&&resultS == 1){
                        return statusParam;
                    }
                }else{
                    int resultS = humidity.compareTo(new BigDecimal(statusParam.getHumidityStart()));
                    if(resultS == 1){
                        return statusParam;
                    }
                }
            }
            //温度
            if(statusParam.getTemperatureStart()==null){
                if(statusParam.getTemperatureEnd()!=null){
                    int result = temperature.compareTo(new BigDecimal(statusParam.getTemperatureEnd()));
                    if(result == 0||result == -1){
                        return statusParam;
                    }
                }
            }else{
                if(statusParam.getTemperatureEnd()!=null){
                    int resultS = temperature.compareTo(new BigDecimal(statusParam.getTemperatureStart()));
                    int result = temperature.compareTo(new BigDecimal(statusParam.getTemperatureEnd()));
                    if((result == 0||result == -1)&&resultS == 1){
                        return statusParam;
                    }
                }else{
                    int resultS = temperature.compareTo(new BigDecimal(statusParam.getTemperatureStart()));
                    if(resultS == 1){
                        return statusParam;
                    }
                }
            }
        }
        if(paramList.size()>0) {
            //默认推送模式0
            return paramList.get(0);
        }
        return null;
    }


    public static void main(String[] args) {
//        String code = "0GH+STATUS=GPS1,LUX10000,HUM854,TEMP+282";
//        String[] strCode = code.split(",");
//        // LUX后面的数字表示光照传感器数值
//        String lux = strCode[1].substring(3);
//        // HUM413表示湿度41.3%
//        // HUM413表示湿度41.3%
//        BigDecimal humBig  = new BigDecimal(strCode[2].substring(3)).divide(new BigDecimal(10),1,BigDecimal.ROUND_HALF_UP);
//        // TEMP+261表示温度26.1℃
//        BigDecimal tempB  = new BigDecimal(strCode[3].substring(5)).divide(new BigDecimal(10),1,BigDecimal.ROUND_HALF_UP);
//        System.out.println("光照:"+lux);
//        System.out.println("湿度:"+humBig+"%");
//        System.out.println("温度:"+tempB+"℃");
//        //测试模拟数据
        List<InductionlampControlStatusParam> paramList = new ArrayList<>();
        InductionlampControlStatusParam statusParam1 = new InductionlampControlStatusParam();
        statusParam1.setModeCode(1);
        statusParam1.setModeName("模式1");
        //关系运算符  0 =  1  >  2 <  3 >=   4 <=
        statusParam1.setIlluminationStart(500);//光照开始值
        statusParam1.setIlluminationStartSymbol(1);  //光照开始值关系运算符
        statusParam1.setIlluminationEnd(1000);//光照结束值
        statusParam1.setIlluminationEndSymbol(4);   //光照结束值关系运算符
        statusParam1.setHumidityStart(90);      //湿度开始值
        statusParam1.setHumidityStartSymbol(1); //湿度开始值运算符
        statusParam1.setHumidityEnd(100);       //湿度结束值
        statusParam1.setHumidityEndSymbol(4);   //湿度结束值运算符
        statusParam1.setTemperatureStart(30);      //温度开始值
        statusParam1.setTemperatureStartSymbol(1); //温度开始值运算符
        statusParam1.setTemperatureEnd(40);       //温度结束值
        statusParam1.setTemperatureEndSymbol(4);   //温度结束值运算符

        InductionlampControlStatusParam statusParam2 = new InductionlampControlStatusParam();
        statusParam2.setModeCode(2);
        statusParam2.setModeName("模式2");
        //关系运算符  0 =  1  >  2 <  3 >=   4 <=
        statusParam2.setIlluminationStart(300);//光照开始值
        statusParam2.setIlluminationStartSymbol(1);  //光照开始值关系运算符
        statusParam2.setIlluminationEnd(500);//光照结束值
        statusParam2.setIlluminationEndSymbol(4);   //光照结束值关系运算符
        statusParam2.setHumidityStart(90);      //湿度开始值
        statusParam2.setHumidityStartSymbol(1); //湿度开始值运算符
        statusParam2.setHumidityEnd(100);       //湿度结束值
        statusParam2.setHumidityEndSymbol(4);   //湿度结束值运算符
        statusParam2.setTemperatureStart(20);      //温度开始值
        statusParam2.setTemperatureStartSymbol(1); //温度开始值运算符
        statusParam2.setTemperatureEnd(30);       //温度结束值
        statusParam2.setTemperatureEndSymbol(4);   //温度结束值运算符
        //根据 mode_code 排序
        paramList.add(statusParam2);
        paramList.add(statusParam1);
        System.out.println( getPilotLightMode(new BigDecimal(800),new BigDecimal(90.1),new BigDecimal(31.1),paramList));
    }
}
