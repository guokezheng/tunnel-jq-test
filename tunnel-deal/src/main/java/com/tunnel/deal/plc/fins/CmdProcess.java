package com.tunnel.deal.plc.fins;


import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.tunnel.business.datacenter.domain.dataVo.CmdInfo;
import com.tunnel.business.datacenter.domain.dataVo.DataInfo;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.util.ArithmeticUtils;
import com.tunnel.business.domain.dataInfo.*;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdPlcCmdService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.utils.util.CommonUtil;
import com.tunnel.business.utils.util.RadixUtil;
import com.tunnel.business.utils.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.*;
import java.util.*;


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

    public static class CmdUtil {

        private static final Logger logger = LoggerFactory.getLogger(CmdUtil.class);
        public static Map<String, SdHosts> hostList = new HashMap<>();
        public static Map<String, SdDevices> deviceList = new HashMap<>();
        private static Map<String, CmdInfo> cmdMap = new HashMap<>();
        @Resource
        ISdTunnelsService tunnelService;
        @Resource
        ISdPlcCmdService plcCmdService;
        @Resource
        ISdDevicesService devicesService;

        public static Map<String, CmdInfo> getCmdMap() {
            return cmdMap;
        }

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

    /**
     * @author yangqichao
     */
    public static class DataUtil {

        @Autowired
        private static RedisCache redisCache;
        private static Map<String, List<DataInfo>> dataMap = new HashMap<>();
        private static JSONObject object = new JSONObject();
        @Autowired
        ISdTunnelsService tunnelService;
        @Autowired
        ISdDevicesService devicesService;

        public static Map<String, List<DataInfo>> getDataMap() {
            return dataMap;
        }

        //解析DM全部设备的状态数据 id：设备id  content 设备状态反馈报文
        public static void parsingDMContent(String id, String content) {
            if (id.contains("kz")) {
                parsingKZ(id, content);
                return;
            }
            //设定设备状态初始化
            String devState = "";
            //redis初始化
            redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
            //解析内容
            String firstContent = content.replaceAll(" ", "");
            if (firstContent.length() > 60) {
                String endCont = firstContent.substring(60, firstContent.length());
                //获取当前设备
                SdDevices device = CmdUtil.deviceList.get(id);
                if (device.getEqType() == null) {
                    return;
                }
                //获取当前设备分类
                String devType = device.getEqType().toString();
                switch (devType) {
                    case "1"://普通车道指示器
                        if (endCont.equals(device.getEqFeedbackAddress1())) { //正绿反红 endCont = 0009 a1 = 0009
                            devState = "1";
                        } else if (endCont.equals(device.getEqFeedbackAddress2())) {//正红反绿 endCont = 0006 a1 = 0006
                            devState = "2";
                        } else if (endCont.equals(device.getEqFeedbackAddress3())) {//正红反红 endCont = 0005 a1 = 000A
                            devState = "3";
                        } else { //其他全为故障状态
                            devState = "0";
                        }
                        break;
                    case "2"://带左转车道指示器
                        if (endCont.equals(device.getEqFeedbackAddress1())) { //正绿反红 endCont = 0009 a1 = 0009
                            devState = "1";
                        } else if (endCont.equals(device.getEqFeedbackAddress2())) {//正红反绿 endCont = 0006 a1 = 0006
                            devState = "2";
                        } else if (endCont.equals(device.getEqFeedbackAddress3())) {//正红反红 endCont = 0005 a1 = 0005
                            devState = "3";
                        } else if (endCont.equals(device.getEqFeedbackAddress4())) {//正左反红 endCont = 0018 a1 = 0018
                            devState = "5";
                        } else { //其他全为故障状态
                            devState = "4";
                        }
                        break;
                    case "3"://普通交通信号灯
                        if (endCont.equals(device.getEqFeedbackAddress1())) { //绿灯 endCont = 0004 a1 = 0004
                            devState = "1";
                        } else if (endCont.equals(device.getEqFeedbackAddress2())) {//红灯 endCont = 0001 a1 = 0001
                            devState = "2";
                        } else if (endCont.equals(device.getEqFeedbackAddress3())) {//黄灯 endCont = 0002 a1 = 0002
                            devState = "3";
                        } else { //其他全为故障状态
                            devState = "4";
                        }
                        break;
                    case "4"://左转交通信号灯
                        if (endCont.equals(device.getEqFeedbackAddress1())) { //开 endCont = 0001 a1 = 0001
                            devState = "1";
                        } else { //其他全为关闭状态
                            devState = "2";
                        }
                        break;
                    case "5"://洞内亮度 //计算方法除以4000乘以20000，最大20000
                        //devState = endCont;
                        String inSideDev = RadixUtil.hexToDecimal(endCont);
                        BigDecimal inSideMul = ArithmeticUtils.mul(inSideDev, "5");
                        BigDecimal inSideData2 = new BigDecimal("20000");//最大量程20000
                        if (inSideMul.compareTo(inSideData2) > 0) {
                            //	System.out.println("第一位数大！");
                            return;
                        }

                        devState = inSideMul.toString();
                        break;
                    case "6"://洞外亮度 //计算方法除以4000乘以6500，最大6500
                        //devState = RadixUtil.hexToDecimal(endCont);
                        String outSideDev = RadixUtil.hexToDecimal(endCont);
                        BigDecimal outSideMul = ArithmeticUtils.mul(outSideDev, "1.625");
                        BigDecimal outSideData2 = new BigDecimal("6500");//最大量程6500
                        if (outSideMul.compareTo(outSideData2) > 0) {
                            //	System.out.println("第一位数大！");超过最大量程，丢弃
                            return;
                        }
                        devState = outSideMul.toString();
                        //devState = endCont;
                        break;
                    case "7"://加强照明

                        break;
                    case "8"://引道照明

                        break;
                    case "9"://基本照明

                        break;
                    case "10"://主风机
                        if (endCont.equals(device.getEqFeedbackAddress1())) { //远程 endCont = 0001 a1 = 0001
                            devState = "3";
                        } else if (endCont.equals(device.getEqFeedbackAddress2())) {//正传 endCont = 0002 a1 = 0002
                            devState = "1";
                        } else if (endCont.equals(device.getEqFeedbackAddress3())) {//反转 endCont = 0004 a1 = 0004
                            devState = "2";
                        } else { //其他全为故障状态
                            devState = "0";
                        }
                        break;
                    case "11"://备用风机

                        break;
                    case "12"://棚洞照明

                        break;
                    case "13"://风向
                        endCont = turnNeedData(endCont);
                        int index = Integer.parseInt(device.getEqFeedbackAddress1());
                        endCont = endCont.substring(index, index + 1);
                        if ("1".equals(endCont)) { //正 endCont = 0001 a1 = 0001
                            devState = "反向";
                        } else { //其他全为关闭状态
                            devState = "正向";
                        }
                        break;
                    case "14"://CO监测 //计算方法除以4000乘以20000
                        String s1 = RadixUtil.hexToDecimal(endCont);
                        Double d = 0.075;
                        Double coMul = ArithmeticUtils.mul(Double.valueOf(s1), d);

                        BigDecimal coData1 = new BigDecimal(coMul);//接受值
                        BigDecimal coData2 = new BigDecimal("300");//最大量程300
                        if (coData1.compareTo(coData2) > 0) {
                            //	System.out.println("第一位数大！");
                            return;
                        }
                        devState = coMul.toString();
                        break;
                    case "15"://能见度监测 //计算方法除以4000乘以20000---最大值15
                        //devState = RadixUtil.hexToDecimal(endCont);
                        String viOrinal = RadixUtil.hexToDecimal(endCont);
                        Double dou = 0.00375;
                        Double viMul = ArithmeticUtils.mul(Double.valueOf(viOrinal), dou);
                        BigDecimal viData1 = new BigDecimal(viMul);//接受值
                        BigDecimal viData2 = new BigDecimal("15");//最大量程
                        if (viData1.compareTo(viData2) > 0) {
                            //	System.out.println("第一位数大！");
                            return;
                        }
                        devState = viMul.toString();
                        break;
                    case "16"://风速监测，最大40
                        String fsDevState = RadixUtil.hexToDecimal(endCont);
                        Double fsd = 0.01;
                        Double fsMul = ArithmeticUtils.mul(Double.valueOf(fsDevState), fsd);
                        BigDecimal fsData1 = new BigDecimal(fsMul);//接受值
                        BigDecimal fsData2 = new BigDecimal("40");//最大量程40
                        if (fsData1.compareTo(fsData2) > 0) {
                            //	System.out.println("第一位数大！");
                            return;
                        }
                        devState = fsMul.toString();
                        break;
                    case "17"://卷帘门
                        if (endCont.equals(device.getEqFeedbackAddress1())) { //上限位 endCont = 0001 a1 = 0001
                            devState = "1";
                        } else if (endCont.equals(device.getEqFeedbackAddress2())) {//下限位 endCont = 0002 a1 = 0002
                            devState = "2";
                        } else { //其他全为故障状态
                            devState = "0";
                        }
                        break;
                    case "18"://水泵
                        JSONObject object = new JSONObject();
                        if (endCont.length() > 19) {
                            object.put("devState", "1");
                            String beng1 = RadixUtil.hexToDecimal(endCont.substring(0, 4));
                            String beng2 = RadixUtil.hexToDecimal(endCont.substring(4, 4 + 4));
                            String queshui = RadixUtil.hexToDecimal(endCont.substring(8, 8 + 4));
                            String dianyuan = RadixUtil.hexToDecimal(endCont.substring(12, 12 + 4));
                            String liandong = RadixUtil.hexToDecimal(endCont.substring(16, 16 + 4));
                            if ("0".equals(beng1)) {
                                object.put("beng1", "关闭");
                            } else {
                                object.put("beng1", "开启");
                            }
                            if ("0".equals(beng2)) {
                                object.put("beng2", "关闭");
                            } else {
                                object.put("beng2", "开启");
                            }
                            if ("0".equals(queshui)) {
                                object.put("queshui", "无");
                            } else {
                                object.put("queshui", "故障");
                            }
                            if ("0".equals(dianyuan)) {
                                object.put("dianyuan", "无");
                            } else {
                                object.put("dianyuan", "故障");
                            }
                            if ("0".equals(liandong)) {
                                object.put("liandong", "未联动");
                            } else {
                                object.put("liandong", "联动");
                            }
                        }
                        devState = object.toString();
                        break;
                    case "19"://水泵状态：手动/远程

                        break;
                    case "20"://水池液位

                        break;
                    case "108"://微波车检
                        /*青龙崮
                        String weiboCont = firstContent.substring(88, firstContent.length());
                        //存储微波数据
                        System.out.println("微波存放");
                        redisCache.setCacheObject(id,parseWeiBoInfo(id, weiboCont));*/
                        //新台
                        String weiboCont = firstContent.substring(60, firstContent.length());
                        //存储微波数据
                        System.out.println("微波存放");
                        redisCache.setCacheObject(id, parseXTWeiBoInfo(id, weiboCont));
                        return;
                }
                redisCache.setCacheObject(id, devState);

            }
        }

        //解析CIO全部设备的状态数据 id：设备id  content 设备状态反馈报文
        public static void parsingCIOContent(String id, String devType, String content) {
            //redis初始化
            redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);

            switch (devType) {
                case "cz":
                    DataUtil.parsingCZ(id, content);
                    break;
                case "xhd":
                    DataUtil.parsingXHD(id, content);
                    break;
                case "fj":
                    DataUtil.parsingFJ(id, content);
                    break;
                case "dn":
                    DataUtil.parsingDN(id, content);
                    break;
                case "dnw":
                    DataUtil.parsingDN(id, content);
                    DataUtil.parsingDW(id, content);
                    break;
                case "zm":
                    DataUtil.parsingZM(id, content);
                    break;
                case "co":
                    DataUtil.parsingCO(id, content);
                    break;
                case "vi":
                    DataUtil.parsingVI(id, content);
                    break;
                case "fs":
                    DataUtil.parsingFS(id, content);
                    break;
                case "jlm":
                    DataUtil.parsingJLM(id, content);
                    if ("S29-LinYiCompany-BaiYanStation-003-PLC-005".equals(id) || "S29-ZiBoCompany-BoShanStation-001-PLC-003".equals(id)) {
                        DataUtil.parsingFX(id, content);
                    }
                    break;
                case "kz":
                    System.out.println("kz");
                    break;
                case "sb1":
                    DataUtil.parsingSB1(id, content);
                    break;
                case "sb2":
                    DataUtil.parsingSB2(id, content);
                    break;
                case "sbzt":
                    DataUtil.parsingSBZT(id, content);
                    break;
                case "yw":
                    DataUtil.parsingYW(id, content);
                    break;
                default:
                    break;
            }

        }

        public static String transJsgs(String strNums1, String strNums2) {
            // 整数部分
            String zs = strNums1;//1车道平均车速
            // 小数部分
            String xs = strNums2;
            BigDecimal num1 = new BigDecimal(xs);
            BigDecimal num2 = new BigDecimal(256);
            BigDecimal num3 = new BigDecimal(zs);
            BigDecimal result = num1.divide(num2);
            return num3.add(result).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString().toString();
        }

        /**
         * 新台微波解析
         *
         * @param id
         * @param weiboCont
         * @return
         */
        private static Object parseXTWeiBoInfo(String id, String weiboCont) {
            List<SdTrafficStatistics> list = new ArrayList<SdTrafficStatistics>();
            if (weiboCont.contains("FFF9")) {
                String strArr[] = weiboCont.split("FFF9");
                for (int i = 0; i < strArr.length; i++) {
                    String str = strArr[i];
                    String twoLaneCar = "0";//车道车流量
                    String twoZhanyoulv = "0";//车道占有率
                    String twoPingjun = "0";//车道平均车速
                    if (str.length() > 4) {
                        str = "FFF9" + str;
                        String roadId = RadixUtil.hexToDecimal(str.substring(14, 16)); // 车道号
                        twoLaneCar = RadixUtil.hexToDecimal(str.substring(16, 20));// 总车流量
                        twoPingjun = RadixUtil.hexToDecimal(str.substring(40, 42));// 平均车速
                        twoZhanyoulv = RadixUtil.hexToDecimal(str.substring(60, 64)); // 占有率
                        //按位截取=======================================
                        Date nowDate = new Date();
                        SdTrafficStatistics sdTrs1 = new SdTrafficStatistics();//创建车流量对象
                        sdTrs1.setDeviceId(id);//设备id
                        sdTrs1.setByLane(Long.valueOf(roadId) + 1);//车道号
                        sdTrs1.setCreateTime(nowDate);
                        sdTrs1.setByVehicelNum(Long.parseLong(twoLaneCar));//车流量数
                        sdTrs1.setfSpaceOccupyRation(Long.parseLong(twoZhanyoulv));//占有率
                        sdTrs1.setBySpeed(Long.parseLong(twoPingjun));//平均速度
                        list.add(sdTrs1);
                    }
                }
            }
            return list;
        }

        //转换微波数据
        public static List<SdTrafficStatistics> parseWeiBoInfo(String id, String weiboCont) {

            String twoLaneCar = RadixUtil.hexToDecimal(weiboCont.substring(24, 26));//2车道车流量
            String twoZhanyoulv = RadixUtil.hexToDecimal(weiboCont.substring(44, 46));//2车道占有率
            String twoPingjun = RadixUtil.hexToDecimal(weiboCont.substring(46, 48));//2车道平均车速

            String threeLaneCar = RadixUtil.hexToDecimal(weiboCont.substring(48, 50));//2车道车流量
            String threeZhanyoulv = RadixUtil.hexToDecimal(weiboCont.substring(68, 70));//2车道占有率
            String threePingjun = RadixUtil.hexToDecimal(weiboCont.substring(70, weiboCont.length()));//2车道平均车速

            List<SdTrafficStatistics> list = new ArrayList<SdTrafficStatistics>();
            //按位截取=======================================
            Date nowDate = new Date();
            SdTrafficStatistics sdTrs1 = new SdTrafficStatistics();//创建车流量对象
            sdTrs1.setDeviceId(id);//设备id
            sdTrs1.setByLane(1L);//车道号
            sdTrs1.setCreateTime(nowDate);
            sdTrs1.setByVehicelNum(Long.parseLong(twoLaneCar));//车流量数
            sdTrs1.setfSpaceOccupyRation(Long.parseLong(twoZhanyoulv));//占有率
            sdTrs1.setBySpeed(Long.parseLong(twoPingjun));//平均速度
            list.add(sdTrs1);
            SdTrafficStatistics sdTrs2 = new SdTrafficStatistics();//创建车流量对象
            sdTrs2.setDeviceId(id);//设备id
            sdTrs2.setByLane(2L);//车道号
            sdTrs2.setByVehicelNum(Long.parseLong(threeLaneCar));//车流量数
            sdTrs2.setfSpaceOccupyRation(Long.parseLong(threeZhanyoulv));//占有率
            sdTrs2.setBySpeed(Long.parseLong(threePingjun));//平均速度
            sdTrs2.setCreateTime(nowDate);
            list.add(sdTrs2);
            return list;
            //SpringUtils.getBean(ISdTrafficStatisticsService.class).insertSdTrafficStatistics(sdTrs);
        }

        /**
         * 十六进制转二进制后补位反转
         *
         * @param data
         * @return
         */
        public static String turnNeedData(String data) {
            String result = RadixUtil.hex2Binary(data);
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 16 - result.length(); i++) {
                str.append("0");
            }
            result = str.toString() + result;
            //反转
            result = new StringBuilder(result).reverse().toString();
            return result;
        }

        private static void parsingKZ(String id, String content) {
        }

        public static void parsingCZ(SdDevices dev, String content) {
            char[] chars = content.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            if (dev.getEqFeedbackAddress2() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress2())]);
            }
            if (dev.getEqFeedbackAddress3() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress3())]);
            }
            if (dev.getEqFeedbackAddress4() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress4())]);
            }
            //判断
            String devState = "";
            if ("1001".equals(res.toString())) {
                devState = "1";
            } else if ("0110".equals(res.toString())) {
                devState = "2";
            } else if ("0101".equals(res.toString())) {
                devState = "3";
            } else if ("0000".equals(res.toString())) {
                devState = "4";
            } else if ("1000".equals(res.toString())) {
                devState = "5";
            } else if ("0100".equals(res.toString())) {
                devState = "6";
            } else if ("0010".equals(res.toString())) {
                devState = "7";
            } else {
                devState = "8";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        public static void parsingTSCZ(SdDevices dev, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            //转换为二进制，位置反转
            String s1 = RadixUtil.hex2Binary(substring);
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 16 - s1.length(); i++) {
                str.append("0");
            }
            s1 = str.toString() + s1;
            //反转
            String s11 = new StringBuilder(s1).reverse().toString();
            char[] chars1 = s11.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars1[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            if (dev.getEqFeedbackAddress2() != null) {
                res.append(chars1[Integer.parseInt(dev.getEqFeedbackAddress2())]);
            }
            if (dev.getEqFeedbackAddress3() != null) {
                res.append(chars1[Integer.parseInt(dev.getEqFeedbackAddress3())]);
            }
            if (dev.getEqFeedbackAddress4() != null) {
                res.append(chars1[Integer.parseInt(dev.getEqFeedbackAddress4())]);
            }
            if (dev.getEqFeedbackAddress5() != null) {
                res.append(chars1[Integer.parseInt(dev.getEqFeedbackAddress5())]);
            }
            //判断
            String devState = "";
            if ("10010".equals(res.toString())) {
                devState = "1";
            } else if ("01100".equals(res.toString())) {
                devState = "2";
            } else if ("01010".equals(res.toString())) {
                devState = "3";
            } else if ("00000".equals(res.toString())) {
                devState = "4";
            } else if ("00101".equals(res.toString())) {
                devState = "5";
            } else if ("10000".equals(res.toString())) {
                devState = "6";
            } else if ("01000".equals(res.toString())) {
                devState = "7";
            } else if ("00100".equals(res.toString())) {
                devState = "8";
            } else if ("00010".equals(res.toString())) {
                devState = "9";
            } else if ("00001".equals(res.toString())) {
                devState = "10";
            } else if ("00011".equals(res.toString())) {
                devState = "11";
            } else {
                devState = "4";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        public static void parsingXHD(SdDevices dev, String content) {
            char[] chars = content.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            if (dev.getEqFeedbackAddress2() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress2())]);
            }
            if (dev.getEqFeedbackAddress3() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress3())]);
            }
            //判断
            String devState = "";
            if ("100".equals(res.toString())) {
                devState = "2";
            } else if ("010".equals(res.toString())) {
                devState = "3";
            } else if ("001".equals(res.toString())) {
                devState = "1";
            } else {
                devState = "4";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        public static void parsingZZXHD(SdDevices dev, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            //转换为二进制，位置反转
            String s1 = RadixUtil.hex2Binary(substring);
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 16 - s1.length(); i++) {
                str.append("0");
            }
            s1 = str.toString() + s1;
            //反转
            String s11 = new StringBuilder(s1).reverse().toString();
            char[] chars1 = s11.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars1[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            //判断
            String devState = "";
            if ("1".equals(res.toString())) {
                devState = "1";
            } else {
                devState = "2";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        public static void parsingZM(SdDevices dev, String content) {
            char[] chars = content.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            //判断
            String devState = "";
            if ("1".equals(res.toString())) {
                devState = "1";
            } else if ("0".equals(res.toString())) {
                devState = "0";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        public static void parsingFJ(SdDevices dev, String content) {
            char[] chars = content.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            if (dev.getEqFeedbackAddress2() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress2())]);
            }
            if (dev.getEqFeedbackAddress3() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress3())]);
            }
            if (dev.getEqFeedbackAddress4() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress4())]);
            }
            //判断
            String devState = "";
            if ("1000".equals(res.toString())) {
                devState = "3";
            } else if ("0100".equals(res.toString())) {
                devState = "1";
            } else if ("0001".equals(res.toString())) {
                devState = "2";
            } else {
                devState = "0";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        public static void parsingLD(SdDevices dev, String content) {
            String s1 = RadixUtil.hexToDecimal(content);
            BigDecimal mul = ArithmeticUtils.mul(s1, "5");
            System.out.println(dev.getEqId().toString() + mul);
            redisCache.setCacheObject(dev.getEqId().toString(), mul.toString());
        }

        public static void parsingFX(SdDevices dev, String content) {
            char[] chars = content.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            //判断
            String devState = "";
            if ("1".equals(res.toString())) {
                devState = "1";
            } else {
                devState = "2";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        public static void parsingFS(SdDevices dev, String content) {
            String s1 = RadixUtil.hexToDecimal(content);
            Double d = 0.01;
            Double mul = ArithmeticUtils.mul(Double.valueOf(s1), d);
            System.out.println(dev.getEqId().toString() + mul);
            redisCache.setCacheObject(dev.getEqId().toString(), mul.toString());
        }

        public static void parsingCO(SdDevices dev, String content) {
            String s1 = RadixUtil.hexToDecimal(content);
            Double d = 0.075;
            Double mul = ArithmeticUtils.mul(Double.valueOf(s1), d);
            redisCache.setCacheObject(dev.getEqId().toString(), mul.toString());
        }

        public static void parsingVI(SdDevices dev, String content) {
            String s1 = RadixUtil.hexToDecimal(content);
            Double d = 0.00375;
            Double mul = ArithmeticUtils.mul(Double.valueOf(s1), d);
            redisCache.setCacheObject(dev.getEqId().toString(), mul.toString());
        }

        public static void parsingJLM(SdDevices dev, String content) {
            char[] chars = content.toCharArray();
            //点位分析
            StringBuffer res = new StringBuffer();
            if (dev.getEqFeedbackAddress1() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress1())]);
            }
            if (dev.getEqFeedbackAddress2() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress2())]);
            }
            if (dev.getEqFeedbackAddress3() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress3())]);
            }
            if (dev.getEqFeedbackAddress4() != null) {
                res.append(chars[Integer.parseInt(dev.getEqFeedbackAddress4())]);
            }
            //判断
            String devState = "";
            if ("1000".equals(res.toString())) {
                devState = "1";
            } else if ("0100".equals(res.toString())) {
                devState = "2";
            } else if ("0010".equals(res.toString())) {
                devState = "3";
            } else {
                devState = "0";
            }
            redisCache.setCacheObject(dev.getEqId().toString(), devState);
        }

        //解析车指数据
        public static void parsingCZ(String id, String content) {

            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            //
            char data[][] = new char[3][15];
            String substring1 = substring.substring(0, 4);

            String s1 = RadixUtil.hex2Binary(substring1);
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 16 - s1.length(); i++) {
                str.append("0");
            }
            s1 = str.toString() + s1;
            String s11 = new StringBuilder(s1).reverse().toString();
            char[] chars1 = s11.toCharArray();
            data[0] = chars1;

            String substring2 = substring.substring(4, 8);
            String s2 = RadixUtil.hex2Binary(substring2);
            StringBuffer str2 = new StringBuffer();
            for (int i = 0; i < 16 - s2.length(); i++) {
                str2.append("0");
            }
            s2 = str2.toString() + s2;
            String s22 = new StringBuilder(s2).reverse().toString();
            char[] chars2 = s22.toCharArray();
            data[1] = chars2;

            String substring3 = substring.substring(8, 12);
            String s3 = RadixUtil.hex2Binary(substring3);
            StringBuffer str3 = new StringBuffer();
            for (int i = 0; i < 16 - s3.length(); i++) {
                str3.append("0");
            }
            s3 = str3.toString() + s3;
            String s33 = new StringBuilder(s3).reverse().toString();
            char[] chars3 = s33.toCharArray();
            data[2] = chars3;

            List<DataInfo> dataInfos = dataMap.get(id);
            for (DataInfo info : dataInfos) {
                if (info.getDeviceType() == 1) {
                    info.upData(data);
                    String devState = "";
                    devState = getPTCZState(info.getData());//普通车指
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }
                if (info.getDeviceType() == 2) {
                    info.upData(data);
                    String devState = "";
                    devState = getTSCZState(info.getData());//特殊车指
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }

            }


        }

        //普通车指数据转换
        private static String getPTCZState(char[] data) {
            //设定设备状态初始化
            String devState = "";
            if (data[0] == '1' && data[3] == '1') {
                devState = "1";//正绿反红
            } else if (data[1] == '1' && data[2] == '1') {
                devState = "2";//正红反绿
            } else if (data[1] == '1' && data[3] == '1') {
                devState = "3";//正红反红
            } else {
                devState = "0";//其他全为故障状态
            }
            return devState;
        }

        //特殊车指数据转换
        private static String getTSCZState(char[] data) {
            //设定设备状态初始化
            String devState = "";
            if (data[0] == '1' && data[3] == '1') {
                devState = "1";//正绿反红
            } else if (data[1] == '1' && data[2] == '1') {
                devState = "2";//正红反绿
            } else if (data[1] == '1' && data[3] == '1') {
                devState = "3";//正红反红
            } else if (data[4] == '1' && data[3] == '1') {
                devState = "5";//正左反红
            } else {
                devState = "4";//其他全为故障状态
            }
            return devState;
        }

        //解析交通信号灯数据
        public static void parsingXHD(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            char data[][] = new char[1][15];
            String substring1 = substring.substring(0, 4);

            String s1 = RadixUtil.hex2Binary(substring1);
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 16 - s1.length(); i++) {
                str.append("0");
            }
            s1 = str.toString() + s1;

            String s11 = new StringBuilder(s1).reverse().toString();
            char[] chars1 = s11.toCharArray();
            data[0] = chars1;

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 3) {
                    info.upData(data);
                    String devState = "";
                    devState = getXHDState(info.getData());//交通信号灯
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                } else if (info.getDeviceType() == 4) {
                    info.upData(data);
                    String devState = "";
                    devState = getZXHDState(info.getData());//左转交通信号灯
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }
            }
        }

        //交通信号灯数据转换
        private static String getXHDState(char[] data) {
            //设定设备状态初始化
            String devState = "";
            if (data[0] == '1') {
                devState = "2";//红灯
            } else if (data[1] == '1') {
                devState = "3";//黄灯
            } else if (data[2] == '1') {
                devState = "1";//绿灯
            } else {
                devState = "4";//其他全为故障状态
            }
            return devState;
        }

        //左转交通信号灯数据转换
        private static String getZXHDState(char[] data) {
            //设定设备状态初始化
            String devState = "";
            if (data[3] == '1') {
                devState = "1";//左转
            } else if (data[3] == '0') {
                devState = "2";//左转不亮
            } else {
                devState = "4";//其他全为故障状态
            }
            return devState;
        }

        public static void parsingFJ(String id, String content) {

            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            char data[][] = new char[1][15];
            String substring1 = substring.substring(0, 4);

            String s1 = RadixUtil.hex2Binary(substring1);
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 16 - s1.length(); i++) {
                str.append("0");
            }
            s1 = str.toString() + s1;

            String s11 = new StringBuilder(s1).reverse().toString();
            char[] chars1 = s11.toCharArray();
            data[0] = chars1;

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 10) {
                    info.upData(data);
                    String devState = "";
                    devState = getFJ10State(info.getData());//主风机
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }
            }
            if (id.equals("S29-LinYiCompany-BaiYanStation-003-PLC-004") || id.equals("S29-LinYiCompany-BaiYanStation-003-PLC-006") || id.equals("S29-ZiBoCompany-BoShanStation-001-PLC-002") || id.equals("S29-ZiBoCompany-BoShanStation-001-PLC-004")) {
                char data2[][] = new char[1][15];
                String substring2 = substring.substring(4, 8);

                String s2 = RadixUtil.hex2Binary(substring2);
                StringBuffer str2 = new StringBuffer();
                for (int i = 0; i < 16 - s2.length(); i++) {
                    str2.append("0");
                }
                s2 = str2.toString() + s2;

                String s12 = new StringBuilder(s2).reverse().toString();
                char[] chars2 = s12.toCharArray();
                data2[0] = chars2;

                List<DataInfo> dataInfos2 = dataMap.get(id);
                for (DataInfo info : dataInfos2) {
                    if (info.getDeviceType() == 11) {
                        info.upData(data2);
                        String devState = "";
                        devState = getFJ11State(info.getData());//备用风机
                        redisCache.setCacheObject(info.getDeviceId(), devState);
                    }
                }
            }

        }

        //解析风机数据
        private static String getFJ10State(char[] data) {
            //设定设备状态初始化
            String devState = "";
            if (data[0] == '0') {
                devState = "0";//手动状态为故障
            } else if (data[1] == '1') {
                devState = "1";//正转
            } else if (data[2] == '1') {
                devState = "0";//故障
            } else if (data[3] == '1') {
                devState = "2";//反转
            } else {
                devState = "3";//其他为自动状态
            }
            return devState;
        }

        //解析风机数据
        private static String getFJ11State(char[] data) {
            //设定设备状态初始化
            String devState = "";
            if (data[0] == '0') {
                devState = "0";//手动状态为故障
            } else if (data[1] == '1') {
                devState = "1";//正转
            } else if (data[2] == '1') {
                devState = "0";//故障
            } else if (data[3] == '1') {
                devState = "2";//反转
            } else {
                devState = "3";//其他为自动状态
            }
            return devState;
        }

        public static void parsingDN(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            String substring1 = substring.substring(0, 4);
            String s1 = RadixUtil.hexToDecimal(substring1);

            BigDecimal mul = ArithmeticUtils.mul(s1, "5");

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 5) {
                    info.upDistance(mul.toString());
                    redisCache.setCacheObject(info.getDeviceId(), info.getDistance());
                }
            }
        }

        public static void parsingDW(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            String substring1 = substring.substring(4, 8);
            String s1 = RadixUtil.hexToDecimal(substring1);

            BigDecimal mul = ArithmeticUtils.mul(s1, "1.625");
            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 6) {
                    info.upDistance(mul.toString());
                    redisCache.setCacheObject(info.getDeviceId(), info.getDistance());
                    //AutomaticLamp.setLuminance(mul.doubleValue());
                }
            }
        }

        public static void parsingZM(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            char data[][] = new char[2][15];

            String substring1 = substring.substring(0, 4);

            String s1 = RadixUtil.hex2Binary(substring1);
            StringBuffer str = new StringBuffer();
            for (int i = 0; i < 16 - s1.length(); i++) {
                str.append("0");
            }
            s1 = str.toString() + s1;
            String s11 = new StringBuilder(s1).reverse().toString();
            char[] chars1 = s11.toCharArray();
            data[0] = chars1;

            String substring2 = substring.substring(4, 8);
            String s2 = RadixUtil.hex2Binary(substring2);
            StringBuffer str2 = new StringBuffer();
            for (int i = 0; i < 16 - s2.length(); i++) {
                str2.append("0");
            }
            s2 = str2.toString() + s2;
            String s22 = new StringBuilder(s2).reverse().toString();
            char[] chars2 = s22.toCharArray();
            data[1] = chars2;


            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 7 || info.getDeviceType() == 8 || info.getDeviceType() == 9) {
                    info.upData(data);
                    String devState = "";
                    devState = getZMState(info.getData());//照明
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }
            }
        }

        private static String getZMState(char[] data) {
            //设定设备状态初始化
            String devState = "";
            if (data[0] == '1') {
                devState = "1";//开灯
            } else {
                devState = "2";//关灯
            }
            return devState;
        }

        public static void main(String[] args) {
            parsingVI("S29-LinYiCompany-BaiYanStation-002-PLC-002", "46 49 4E 53 00 00 00 18 00 00 00 02 00 00 00 00 C0 00 02 00 02 00 00 2D 00 00 01 01 00 00 00 E4");
        }

        public static void parsingCO(String id, String content) {
            // TODO Auto-generated method stub
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            String substring1 = substring.substring(0, 4);
            String s1 = RadixUtil.hexToDecimal(substring1);

            Double d = 0.075;
            Double mul = ArithmeticUtils.mul(Double.valueOf(s1), d);

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 14) {
                    info.upDistance(mul.toString());
                    redisCache.setCacheObject(info.getDeviceId(), mul.toString());
                }
            }
        }

        public static void parsingVI(String id, String content) {
            // TODO Auto-generated method stub
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            String substring1 = substring.substring(0, 4);
            String s1 = RadixUtil.hexToDecimal(substring1);
            Double d = 0.00375;
            Double mul = ArithmeticUtils.mul(Double.valueOf(s1), d);

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 15) {
                    info.upDistance(mul.toString());
                    redisCache.setCacheObject(info.getDeviceId(), mul.toString());
                }
            }
        }

        public static void parsingFS(String id, String content) {
            // TODO Auto-generated method stub
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(60, firstContent.length());
            String substring1 = substring.substring(0, 4);
            String s1 = RadixUtil.hexToDecimal(substring1);
            Double d = 0.01;
            Double mul = ArithmeticUtils.mul(Double.valueOf(s1), d);

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 16) {
                    info.upDistance(mul.toString());
                    redisCache.setCacheObject(info.getDeviceId(), mul.toString());
                }
            }
        }

        public static void parsingJLM(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(firstContent.length() - 2, firstContent.length() - 1);

            String devState = "";
            if ("1".equals(substring)) {
                devState = "1";//1:上限位
            } else if ("2".equals(substring)) {
                devState = "2";//2:下限位
            } else if ("4".equals(substring)) {
                devState = "0";//0:故障
            } else {
                devState = "0";
            }
            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 17) {
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }
            }
        }

        public static void parsingFX(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(firstContent.length() - 4, firstContent.length() - 3);

            String devState = "";
            if ("8".equals(substring)) {
                devState = "正向";
            } else {
                devState = "反向";
            }
            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 13) {
                    info.setDistance(devState);
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }
            }
        }

        public static void parsingKZ(String id, String content, Map<String, String> cmd) {
            //log.debug("id:" + id + "     cot:" + content);
        }

        public static void parsingSB1(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String devState = firstContent.substring(firstContent.length() - 3, firstContent.length() - 2);

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 18) {
                    if ("0".equals(devState)) {
                        object.put("beng1", "关闭");
                    } else {
                        object.put("beng1", "开启");
                    }
                    redisCache.setCacheObject(info.getDeviceId(), object.toString());
                }
            }
        }

        public static void parsingSB2(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String devState = firstContent.substring(firstContent.length() - 3, firstContent.length() - 2);

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 18) {
                    if ("0".equals(devState)) {
                        object.put("beng2", "关闭");
                    } else {
                        object.put("beng2", "开启");
                    }
                    redisCache.setCacheObject(info.getDeviceId(), object.toString());
                }
            }
        }

        public static void parsingSBZT(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(firstContent.length() - 3, firstContent.length() - 2);

            String devState = "";
            if ("1".equals(substring)) {
                devState = "自动";
            } else if ("0".equals(substring)) {
                devState = "手动";
            } else {
                devState = "未知";
            }
            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 19) {
                    info.setSbState(devState);
                    redisCache.setCacheObject(info.getDeviceId(), devState);
                }
            }
        }

        public static void parsingYW(String id, String content) {
            //去除空格
            String firstContent = content.replaceAll(" ", "");
            //截取返回内容的数据
            String substring = firstContent.substring(firstContent.length() - 4, firstContent.length());
            String s1 = RadixUtil.hexToDecimal(substring);
            Double d = 0.01;
            Double mul = ArithmeticUtils.mul(Double.valueOf(s1), d);

            List<DataInfo> dataInfos1 = dataMap.get(id);
            for (DataInfo info : dataInfos1) {
                if (info.getDeviceType() == 20) {
                    info.setYewei(mul.toString());
                    redisCache.setCacheObject(info.getDeviceId(), mul.toString());
                }
            }
        }

        // 在构造方法执行后执行
        @PostConstruct
        public void init() {
            initMap();
        }

        //初始化内存
        public void initMap() {
            SdTunnels tunnelModel = new SdTunnels();
            tunnelModel.setPoll(1L);
            List<SdTunnels> tunnelList = tunnelService.selectSdTunnelsList(tunnelModel);
            for (SdTunnels tunnel : tunnelList) {
                //查询需要巡检的PLC
                SdDevices sdDevice = new SdDevices();
                sdDevice.setEqTunnelId(tunnel.getTunnelId());//隧道ID
                sdDevice.setEqType(0L);//设备类型
                sdDevice.setIsMonitor(0L);//是否监控
                sdDevice.setProtocol("CIO");//CIO类型
                List<SdDevices> plcLists = devicesService.selectSdDevicesList(sdDevice);
                for (SdDevices plc : plcLists) {
                    SdDevices dev = new SdDevices();
                    dev.setFEqId(plc.getEqId());
                    List<SdDevices> devLists = devicesService.selectSdDevicesList(dev);
                    List<DataInfo> dataInfos = new ArrayList<>();
                    for (SdDevices device : devLists) {
                        DataInfo dataInfo = new DataInfo();
                        dataInfo.setDeviceId(device.getEqId());
                        dataInfo.setDeviceType(device.getEqType());
                        dataInfo.setDeviceName(device.getEqName());
                        dataInfo.setStakeMark(device.getPile());
                        //普通车指
                        if (device.getEqType() == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode()) {
                            String feedbackAddress1 = device.getEqFeedbackAddress1();
                            String feedbackAddress2 = device.getEqFeedbackAddress2();
                            String feedbackAddress3 = device.getEqFeedbackAddress3();
                            String feedbackAddress4 = device.getEqFeedbackAddress4();
                            String[] split0 = feedbackAddress1.split("\\.");
                            dataInfo.setX(0, Integer.parseInt(split0[0]));
                            dataInfo.setY(0, Integer.parseInt(split0[1]));

                            String[] split1 = feedbackAddress2.split("\\.");
                            dataInfo.setX(1, Integer.parseInt(split1[0]));
                            dataInfo.setY(1, Integer.parseInt(split1[1]));

                            String[] split2 = feedbackAddress3.split("\\.");
                            dataInfo.setX(2, Integer.parseInt(split2[0]));
                            dataInfo.setY(2, Integer.parseInt(split2[1]));

                            String[] split3 = feedbackAddress4.split("\\.");
                            dataInfo.setX(3, Integer.parseInt(split3[0]));
                            dataInfo.setY(3, Integer.parseInt(split3[1]));
                            //带左转车指
                        } else if (device.getEqType() == DevicesTypeEnum.ZHUO_ZHUAN_CHE_ZHI.getCode()) {
                            String feedbackAddress1 = device.getEqFeedbackAddress1();
                            String feedbackAddress2 = device.getEqFeedbackAddress2();
                            String feedbackAddress3 = device.getEqFeedbackAddress3();
                            String feedbackAddress4 = device.getEqFeedbackAddress4();
                            String feedbackAddress5 = device.getEqFeedbackAddress5();
                            String[] split0 = feedbackAddress1.split("\\.");
                            dataInfo.setX(0, Integer.parseInt(split0[0]));
                            dataInfo.setY(0, Integer.parseInt(split0[1]));

                            String[] split1 = feedbackAddress2.split("\\.");
                            dataInfo.setX(1, Integer.parseInt(split1[0]));
                            dataInfo.setY(1, Integer.parseInt(split1[1]));

                            String[] split2 = feedbackAddress3.split("\\.");
                            dataInfo.setX(2, Integer.parseInt(split2[0]));
                            dataInfo.setY(2, Integer.parseInt(split2[1]));

                            String[] split3 = feedbackAddress4.split("\\.");
                            dataInfo.setX(3, Integer.parseInt(split3[0]));
                            dataInfo.setY(3, Integer.parseInt(split3[1]));
                            String[] split5 = feedbackAddress5.split("\\.");
                            dataInfo.setX(4, Integer.parseInt(split5[0]));
                            dataInfo.setY(4, Integer.parseInt(split5[1]));
                            //交通信号灯
                        } else if (device.getEqType() == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode()) {
                            String feedbackAddress1 = device.getEqFeedbackAddress1();
                            String feedbackAddress2 = device.getEqFeedbackAddress2();
                            String feedbackAddress3 = device.getEqFeedbackAddress3();
                            String[] split0 = feedbackAddress1.split("\\.");
                            dataInfo.setX(0, 0);
                            dataInfo.setY(0, Integer.parseInt(split0[1]));

                            String[] split1 = feedbackAddress2.split("\\.");
                            dataInfo.setX(1, 0);
                            dataInfo.setY(1, Integer.parseInt(split1[1]));

                            String[] split2 = feedbackAddress3.split("\\.");
                            dataInfo.setX(2, 0);
                            dataInfo.setY(2, Integer.parseInt(split2[1]));

                            //带左转交通信号灯
                        } else if (device.getEqType() == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode()) {
                            String feedbackAddress1 = device.getEqFeedbackAddress1();
                            String feedbackAddress2 = device.getEqFeedbackAddress2();
                            String feedbackAddress3 = device.getEqFeedbackAddress3();
                            String feedbackAddress4 = device.getEqFeedbackAddress4();
                            String[] split0 = feedbackAddress1.split("\\.");
                            dataInfo.setX(0, 0);
                            dataInfo.setY(0, Integer.parseInt(split0[1]));

                            String[] split1 = feedbackAddress2.split("\\.");
                            dataInfo.setX(1, 0);
                            dataInfo.setY(1, Integer.parseInt(split1[1]));

                            String[] split2 = feedbackAddress3.split("\\.");
                            dataInfo.setX(2, 0);
                            dataInfo.setY(2, Integer.parseInt(split2[1]));

                            String[] split3 = feedbackAddress4.split("\\.");
                            dataInfo.setX(3, 0);
                            dataInfo.setY(3, Integer.parseInt(split3[1]));

                            //加强照明 引道照明 基本照明
                        } else if (device.getEqType() == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode() || device.getEqType() == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode() || device.getEqType() == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()) {
                            //如果此处为乐疃的设备,做特殊处理
                            if (plc.getEqId().equals("S29-ZiBoCompany-BoShanStation-001-PLC-006") || plc.getEqId().equals("S29-ZaoZhuangCompany-ShanTingStation-001-PLC-001")) {
                                String feedbackAddress1 = device.getEqFeedbackAddress1();
                                String feedbackAddress2 = device.getEqFeedbackAddress2();
                                String[] split0 = feedbackAddress1.split("\\.");
                                dataInfo.setX(0, Integer.parseInt(split0[0]) - 2);
                                dataInfo.setY(0, Integer.parseInt(split0[1]));
                                String[] split1 = feedbackAddress2.split("\\.");
                                dataInfo.setX(1, Integer.parseInt(split1[0]) - 2);
                                dataInfo.setY(1, Integer.parseInt(split1[1]));
                            } else {
                                String feedbackAddress1 = device.getEqFeedbackAddress1();
                                String feedbackAddress2 = device.getEqFeedbackAddress2();
                                String[] split0 = feedbackAddress1.split("\\.");
                                dataInfo.setX(0, Integer.parseInt(split0[0]));
                                dataInfo.setY(0, Integer.parseInt(split0[1]));
                                String[] split1 = feedbackAddress2.split("\\.");
                                dataInfo.setX(1, Integer.parseInt(split1[0]));
                                dataInfo.setY(1, Integer.parseInt(split1[1]));
                            }
                            //风机
                        } else if (device.getEqType() == DevicesTypeEnum.FENG_JI.getCode()) {
                            String feedbackAddress1 = device.getEqFeedbackAddress1();
                            String feedbackAddress2 = device.getEqFeedbackAddress2();
                            String feedbackAddress3 = device.getEqFeedbackAddress3();
                            String feedbackAddress4 = device.getEqFeedbackAddress4();
                            String[] split0 = feedbackAddress1.split("\\.");
                            dataInfo.setX(0, Integer.parseInt(split0[0]) - 20);
                            dataInfo.setY(0, Integer.parseInt(split0[1]));

                            String[] split1 = feedbackAddress2.split("\\.");
                            dataInfo.setX(1, Integer.parseInt(split1[0]) - 20);
                            dataInfo.setY(1, Integer.parseInt(split1[1]));

                            String[] split2 = feedbackAddress3.split("\\.");
                            dataInfo.setX(2, Integer.parseInt(split2[0]) - 20);
                            dataInfo.setY(2, Integer.parseInt(split2[1]));

                            String[] split3 = feedbackAddress4.split("\\.");
                            dataInfo.setX(3, Integer.parseInt(split3[0]) - 20);
                            dataInfo.setY(3, Integer.parseInt(split3[1]));
                        }
                        //                    else if (device.getEqType() == DevicesTypeEnum.FENG_JI_2.getCode()) {
                        //                        String feedbackAddress1 = device.getEqFeedbackAddress1();
                        //                        String feedbackAddress2 = device.getEqFeedbackAddress2();
                        //                        String feedbackAddress3 = device.getEqFeedbackAddress3();
                        //                        String feedbackAddress4 = device.getEqFeedbackAddress4();
                        //                        String[] split0 = feedbackAddress1.split("\\.");
                        //                        dataInfo.setX(0, Integer.parseInt(split0[0]) - 21);
                        //                        dataInfo.setY(0, Integer.parseInt(split0[1]));
                        //
                        //                        String[] split1 = feedbackAddress2.split("\\.");
                        //                        dataInfo.setX(1, Integer.parseInt(split1[0]) - 21);
                        //                        dataInfo.setY(1, Integer.parseInt(split1[1]));
                        //
                        //                        String[] split2 = feedbackAddress3.split("\\.");
                        //                        dataInfo.setX(2, Integer.parseInt(split2[0]) - 21);
                        //                        dataInfo.setY(2, Integer.parseInt(split2[1]));
                        //
                        //                        String[] split3 = feedbackAddress4.split("\\.");
                        //                        dataInfo.setX(3, Integer.parseInt(split3[0]) - 21);
                        //                        dataInfo.setY(3, Integer.parseInt(split3[1]));
                        //                    }
                        dataInfos.add(dataInfo);
                    }
                    dataMap.put(plc.getEqId(), dataInfos);
                }
            }
        }

    }

    /**
     * @author yangqichao
     * @date 2019/9/17 13:52
     */
    public static class DisPlayUtil {

        private DisPlayUtil() {

        }

        /*
         * 拼装PLC握手报文：
         * 替换掉报文中的地址
         * PLC地址：第一个##
         * 本机地址：第二个@@
         *
         * */
        public static String handshake() {
            StringBuffer content = new StringBuffer();

            String command = "46494E530000000C0000000000000000000000@@";
            //获取PlcIP地址
            String heXipByLocalIp = getHEXipByLocalIp();
            String replace = command.replaceFirst("@@", heXipByLocalIp);
            content.append(replace);
            for (int i = content.length() - 2; i > 0; i -= 2) {
                content.insert(i, " ");
            }
            return content.toString();
        }

        /*
         * 拼装PLC查询报文：
         * 替换掉报文中的地址
         * PLC地址：第一个##
         * 本机地址：第二个@@
         *
         * */
        public static String getComDisPlay(String deviceId, String plcIp, String cmd) {
            StringBuffer content = new StringBuffer();
            if (CommonUtil.isNull(cmd))
                return null;
            String heXipByPlcIp = getHEXipByPlcIp(plcIp);
            String heXipByLocalIp = getHEXipByLocalIp();
            String replaceCmd = cmd.replaceFirst("##", heXipByPlcIp);
            String replace = replaceCmd.replace("@@", heXipByLocalIp);
            content.append(replace);
            for (int i = content.length() - 2; i > 0; i -= 2) {
                content.insert(i, " ");
            }
            return content.toString();
        }

        public static String getComDisPlay(String plcIp, String cmd) {
            StringBuffer content = new StringBuffer();
            if (CommonUtil.isNull(cmd))
                return null;
            String heXipByLocalIp = getHEXipByLocalIp();
            String replaceCmd = cmd.replaceFirst("##", heXipByLocalIp);
            content.append(replaceCmd);
            return content.toString();
        }

        /*
         * 拼装设备控制命令：
         * 替换掉报文中的地址
         * PLC地址：第一个##
         * 本机地址：第二个##
         *
         * */
        //    public static String itemContent(SdDevices sdDevices, SdHosts sdHosts, Integer deviceType, String state) {
        //        String commands = null;
        //        //设备控制状态
        //        int states = Integer.parseInt(state);
        //        //获取上位机的控制地址
        //        String controlPointAddress = sdDevices.getDmcontrolSeat();
        //        if (deviceType == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode() || deviceType == DevicesTypeEnum.YIN_DAO_ZHAO_MING.getCode() ||
        //                deviceType == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode() || deviceType == DevicesTypeEnum.PENG_DONG_ZHAO_MING.getCode()) {
        //            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
        //            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
        //            String[] split = controlPointAddress.split(",");
        //            String controlAddressDevice = split[states];
        //            int x = controlAddressDevice.lastIndexOf(".");
        //            //10.00   前两位位上位机地址码
        //            String controlAddress = controlAddressDevice.substring(0, x);
        //            String command = EnumUtil.getType(deviceType, CommandEnum.class);
        //            //获取PlcIP地址
        //            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
        //            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
        //            //获取本机IP地址
        //            String heXipByLocalIp = getHEXipByLocalIp();
        //            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
        //            //获取上位机地址10 即为0A
        //            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
        //            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
        //            //创建长度为16的字符串
        //            //10.01   后两位位上位机设备码
        //            String controlDevice = controlAddressDevice.substring(x + 1, controlAddressDevice.length());
        //            int i = Integer.parseInt(controlDevice);
        //            String commandStart = "0000000000000000";
        //            char[] chars = commandStart.toCharArray();
        //            chars[i] = '1';
        //            String commandChars = String.valueOf(chars);
        //            String commandReverse = new StringBuilder(commandChars).reverse().toString();
        //            String s = RadixUtil.binary2Hex(commandReverse);
        //            StringBuffer str = new StringBuffer();
        //            for (int q = 0; q < 4 - s.length(); q++) {
        //                str.append("0");
        //            }
        //            s = str.toString() + s;//0001
        //            StringBuffer stringBuffer = new StringBuffer();
        //            StringBuffer cmd = stringBuffer.append(replaceByAddress).append(s);
        //            for (int q = cmd.length() - 2; q > 0; q -= 2) {
        //                cmd.insert(q, " ");
        //            }
        //            commands = cmd.toString();
        //        } else if (deviceType == DevicesTypeEnum.FENG_JI_1.getCode() || deviceType == DevicesTypeEnum.FENG_JI_2.getCode()) {
        //            Map<String, List<DataInfo>> dataMap = DataUtil.getDataMap();
        //            //state = "3,3,3";
        //            String[] stateSplit = state.split(",");
        //            int state1 = Integer.parseInt(stateSplit[0]);
        //            int state2 = Integer.parseInt(stateSplit[1]);
        //            int state3 = Integer.parseInt(stateSplit[2]);
        //            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
        //            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
        //            String[] split = controlPointAddress.split(",");
        //            String controlAddressDevice = split[0];
        //            int x = controlAddressDevice.lastIndexOf(".");
        //            //10.00   前两位位上位机地址码
        //            String controlAddress = controlAddressDevice.substring(0, x);
        //            String command = EnumUtil.getType(deviceType, CommandEnum.class);
        //            //获取PlcIP地址
        //            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
        //            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
        //            //获取本机IP地址
        //            String heXipByLocalIp = getHEXipByLocalIp();
        //            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
        //            //获取上位机地址10 即为0A
        //            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
        //            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
        //            //创建长度为16的字符串
        //            //10.01   后两位位上位机设备码
        //            StringBuffer commandEnd = new StringBuffer("");
        //            for (int i = 0; i < stateSplit.length; i++) {
        //                if ("1".equals(stateSplit[i])) {
        //                    commandEnd.append("1");
        //                } else if ("2".equals(stateSplit[i])) {
        //                    commandEnd.append("0");
        //                } else if ("3".equals(stateSplit[i])) {
        //                    commandEnd.append("0");
        //                }
        //            }
        //            for (int i = 0; i < stateSplit.length; i++) {
        //                if ("1".equals(stateSplit[i])) {
        //                    commandEnd.append("0");
        //                } else if ("2".equals(stateSplit[i])) {
        //                    commandEnd.append("1");
        //                } else if ("3".equals(stateSplit[i])) {
        //                    commandEnd.append("0");
        //                }
        //            }
        //            commandEnd.append("0000000000");
        //            String commandReverse = new StringBuilder(commandEnd).reverse().toString();
        //            String s = RadixUtil.binary2Hex(commandReverse);
        //            StringBuffer commandStart = new StringBuffer();
        //            for (int q = 0; q < 4 - s.length(); q++) {
        //                commandStart.append("0");
        //            }
        //            s = commandStart.toString() + s;//0001
        //            StringBuffer cmd = new StringBuffer().append(replaceByAddress).append(s);
        //            //分割
        //            for (int q = cmd.length() - 2; q > 0; q -= 2) {
        //                cmd.insert(q, " ");
        //            }
        //            commands = cmd.toString();
        //
        //        } else if (deviceType == DevicesTypeEnum.PU_TONG_CHE_ZHI.getCode() || deviceType == DevicesTypeEnum.TU_SHU_CHE_ZHI.getCode() ||
        //                deviceType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode() || deviceType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode()) {
        //            //DM101->101
        //            String controlAddress = null;
        //            if (deviceType == DevicesTypeEnum.JIAO_TONG_XIN_HAO_DENG.getCode() || deviceType == DevicesTypeEnum.ZUO_JIAO_TONG_XIN_HAO_DENG.getCode()) {
        //                if ("5".equals(state) || "6".equals(state)) {
        //                    controlAddress = "7A";
        //                } else {
        //                    controlAddress = RadixUtil.intToHex(Integer.parseInt(controlPointAddress.substring(2, 5)));
        //                }
        //
        //            } else {
        //                controlAddress = RadixUtil.intToHex(Integer.parseInt(controlPointAddress.substring(2, 5)));
        //            }
        //            String command = EnumUtil.getType(deviceType, CommandEnum.class);
        //            //获取PlcIP地址
        //            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
        //            //获取本机IP地址
        //            String heXipByLocalIp = getHEXipByLocalIp();
        //
        //            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
        //            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
        //            String replaceCon = replaceLocal.replace("##", controlAddress);
        //
        //            StringBuffer cmd = new StringBuffer().append(replaceCon).append(state);
        //            for (int i = cmd.length() - 2; i > 0; i -= 2) {
        //                cmd.insert(i, " ");
        //            }
        //            commands = cmd.toString();
        //        } else if (deviceType == DevicesTypeEnum.JUAN_LIAN_MEN.getCode()) {
        //            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
        //            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
        //            String[] split = controlPointAddress.split(",");
        //            String controlAddressDevice = split[states];
        //            int x = controlAddressDevice.lastIndexOf(".");
        //            //10.00   前两位位上位机地址码
        //            String controlAddress = controlAddressDevice.substring(0, x);
        //            String command = EnumUtil.getType(deviceType, CommandEnum.class);
        //            //获取PlcIP地址
        //            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
        //            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
        //            //获取本机IP地址
        //            String heXipByLocalIp = getHEXipByLocalIp();
        //            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
        //            //获取上位机地址10 即为0A
        //            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
        //            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
        //            //创建长度为16的字符串
        //            //10.01   后两位位上位机设备码
        //            String controlDevice = controlAddressDevice.substring(x + 1, controlAddressDevice.length());
        //            int i = Integer.parseInt(controlDevice);
        //            String commandStart = "0000000000000000";
        //            char[] chars = commandStart.toCharArray();
        //            chars[i] = '1';
        //            String commandChars = String.valueOf(chars);
        //            String commandReverse = new StringBuilder(commandChars).reverse().toString();
        //            String s = RadixUtil.binary2Hex(commandReverse);
        //            StringBuffer str = new StringBuffer();
        //            for (int q = 0; q < 4 - s.length(); q++) {
        //                str.append("0");
        //            }
        //            s = str.toString() + s;//0001
        //            StringBuffer cmd = new StringBuffer().append(replaceByAddress).append(s);
        //            for (int q = cmd.length() - 2; q > 0; q -= 2) {
        //                cmd.insert(q, " ");
        //            }
        //            commands = cmd.toString();
        //        } else if (deviceType == DevicesTypeEnum.SHUI_BENG.getCode()) {
        //            String stakeMark = sdDevices.getStakeMark();
        //            Map<String, List<DataInfo>> dataMap = DataUtil.getDataMap();
        //            //例子： "10.00,10.01"   分割后位[0]:10.00   [1]10.01
        //            //states   为设备开关状态  0表示取第0位 , 1表示取第1位
        //            String[] split = controlPointAddress.split(",");
        //            String controlAddressDevice = split[0];
        //            int x = controlAddressDevice.lastIndexOf(".");
        //            //10.00   前两位位上位机地址码
        //            String controlAddress = controlAddressDevice.substring(0, x);
        //            String command = EnumUtil.getType(deviceType, CommandEnum.class);
        //            //获取PlcIP地址
        //            String heXipByPlcIp = getHEXipByPlcIp(sdHosts.getPlcIp());
        //            String replacePLC = command.replaceFirst("##", heXipByPlcIp);
        //            //获取本机IP地址
        //            String heXipByLocalIp = getHEXipByLocalIp();
        //            String replaceLocal = replacePLC.replaceFirst("@@", heXipByLocalIp);
        //            //获取上位机地址10 即为0A
        //            String heXipByAddress = RadixUtil.intToHex(Integer.parseInt(controlAddress));
        //            String replaceByAddress = replaceLocal.replace("##", heXipByAddress);
        //            //创建长度为16的字符串
        //            //10.01   后两位位上位机设备码
        //            StringBuffer commandEnd = new StringBuffer("");
        //            if ("1#水泵".equals(stakeMark)) {
        //                if ("1".equals(state)) {
        //                    commandEnd.append("0001");
        //                } else if ("0".equals(state)) {
        //                    commandEnd.append("0000");
        //                }
        //            } else {
        //                if ("1".equals(state)) {
        //                    commandEnd.append("0002");
        //                } else if ("0".equals(state)) {
        //                    commandEnd.append("0000");
        //                }
        //            }
        //            StringBuffer cmd = new StringBuffer().append(replaceByAddress).append(commandEnd);
        //            //分割
        //            for (int q = cmd.length() - 2; q > 0; q -= 2) {
        //                cmd.insert(q, " ");
        //            }
        //            commands = cmd.toString();
        //        }
        //        return commands;
        //    }


        /*
         * 获取PLCip截取最后两位转换为HEX
         * */
        public static String getHEXipByPlcIp(String PlcIp) {
            int x = PlcIp.lastIndexOf(".");
            String substring = PlcIp.substring(x + 1, PlcIp.length());
            return RadixUtil.intToHex(Integer.parseInt(substring));
        }


        /*
         * 获取本机ip截取最后两位转换为HEX
         * */
        public static String getHEXipByLocalIp() {
            try {
                for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                    NetworkInterface item = e.nextElement();
                    for (InterfaceAddress address : item.getInterfaceAddresses()) {
                        if (item.isLoopback() || !item.isUp()) {
                            continue;
                        }
                        if (address.getAddress() instanceof Inet4Address) {
                            Inet4Address inet4Address = (Inet4Address) address.getAddress();
                            String localIpByNetCard = inet4Address.getHostAddress();
                            int x = localIpByNetCard.lastIndexOf(".");
                            String substring = localIpByNetCard.substring(x + 1, localIpByNetCard.length());
                            return RadixUtil.intToHex(Integer.parseInt(substring));
                        }
                    }
                }
                String localIpByNetCard = InetAddress.getLocalHost().getHostAddress();
                int x = localIpByNetCard.lastIndexOf(".");
                String substring = localIpByNetCard.substring(x + 1, localIpByNetCard.length());
                return RadixUtil.intToHex(Integer.parseInt(substring));
            } catch (SocketException | UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
