package com.tunnel.platform.controller.informationBoard;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.domain.dataInfo.EventTypeEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.digitalmodel.SdRadarDevice;
import com.tunnel.business.domain.informationBoard.*;
import com.tunnel.business.mapper.dataInfo.SdDeviceDataMapper;
import com.tunnel.business.mapper.dataInfo.SdDevicesMapper;
import com.tunnel.business.mapper.informationBoard.IotBoardReleaseLogMapper;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.digitalmodel.impl.RadarEventServiceImpl;
import com.tunnel.business.service.informationBoard.*;
import com.tunnel.business.utils.exception.BusinessException;
import com.tunnel.platform.business.vms.core.IDeviceProtocol;
import com.tunnel.platform.business.vms.device.DataUtils;
import com.tunnel.platform.business.vms.device.DeviceManagerFactory;
import com.tunnel.platform.service.deviceControl.PhoneSpkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 情报板操作Controller
 *
 * @author yangqichao
 * @date 2020-03-25
 */
@RestController
@RequestMapping("/parser/board")
public class BoardController extends BaseController {

    private String prefix = "parser/board";

    public static ConcurrentHashMap<String, List<String>> resultMap = new ConcurrentHashMap<String, List<String>>();//返回内容存储
    public static ConcurrentHashMap<String, String> releaseContentMap = new ConcurrentHashMap<String, String>();//发布内容存储
    public static ConcurrentHashMap<String, List<String>> modifyResultMap = new ConcurrentHashMap<String, List<String>>();//返回修改内容存储
    private static int fontSizeList[] = {64, 48, 40, 32, 24, 16};//字体大小集合
    public static ConcurrentHashMap<String, Object> nowContentMap = new ConcurrentHashMap<String, Object>();
    @Autowired
    private ISdIotDeviceService sdIotDeviceService;
    @Autowired
    private IIotBoardReleaseLogService iIotBoardReleaseLogService;
    @Autowired
    private IIotBoradFontService iIotBoradFontService;
//    private ExecutorService executorService;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdReleaseRecordService sdReleaseRecordService;
    @Autowired
    private IIotDeviceAccessService iotDeviceAccessService;
    @Autowired
    private IIotBoardVocabularyService iotBoardVocabularyService;
    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private SdDeviceDataMapper deviceDataMapper;

    @Autowired
    private SdDevicesMapper sdDevicesMapper;

    @Autowired
    private RadarEventServiceImpl radarEventServiceImpl;

    @Autowired
    @Qualifier("kafkaOneTemplate")
    private KafkaTemplate<String, String> kafkaOneTemplate;

    private static Map<String, Object> boardContentMap = new HashMap<>();

    //默认情报板不准控制
    private static int wjModelNum = 0;

    //默认广播不准控制
    private static int wjGbModelNum = 0;

    @Autowired
    private IotBoardReleaseLogMapper iotBoardReleaseLogMapper;

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

//    /**
//     *
//     * 1分钟检测一次情报板的状态
//     */
//    @Scheduled(fixedRate = 60000 * 5)
//    public void getIcyData() {
//
//    	List<SdIotDevice> list = sdIotDeviceService.selectIotDeviceList(new SdIotDevice());
//    	for(int i=0;i<list.size();i++){
//    		SdIotDevice iotDevice = list.get(i);
//            Long deviceId = iotDevice.getDeviceId();
//            SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(deviceId);
//            try {
//                Map<String, String> boardData = DeviceManagerFactory.getInstance().getDeviceDisplayListByDeviceId(String.valueOf(iotDevice.getDeviceId()));
//                iotDevice.setDeviceStatus("0");
//                sdIotDeviceService.updateIotDevice(iotDevice);
//                device.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
//                device.setEqStatusTime(new Date());
//                sdDevicesService.updateSdDevices(device);
//                String result = boardData.get("result");
//                String protocolType = boardData.get("vender");
//                String jsonResult = DataUtils.itemContentToJson(result, protocolType);
//                //隧道内
//                Long sdnCode = Long.valueOf(DevicesTypeItemEnum.SUI_DAO_NEI_CONTENT.getCode());
//                //门架式
//                Long mjsCode = Long.valueOf(DevicesTypeItemEnum.MEN_JIA_CONTENT.getCode());
//                //查询情报板实时内容
//                SdDeviceData sdDeviceData = new SdDeviceData();
//                sdDeviceData.setDeviceId(device.getEqId());
//                if("16".equals(device.getEqType().toString())){
//                    sdDeviceData.setItemId(sdnCode);
//                }else {
//                    sdDeviceData.setItemId(mjsCode);
//                }
//                List<SdDeviceData> sdDeviceDataList = deviceDataMapper.selectSdDeviceDataList(sdDeviceData);
//                //存入实时数据
//                if("16".equals(device.getEqType().toString())){
//                    setDeviceData(device.getEqId(),sdnCode,jsonResult,sdDeviceDataList);
//                }else {
//                    setDeviceData(device.getEqId(),mjsCode,jsonResult,sdDeviceDataList);
//                }
//            } catch (Exception e) {
//            	iotDevice.setDeviceStatus("1");
//    			sdIotDeviceService.updateIotDevice(iotDevice);
//                device.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
//                device.setEqStatusTime(new Date());
//                sdDevicesService.updateSdDevices(device);
//            }
//    	}
//
//    }


    /**
     * 获取单条情报板实时信息
     *
     * @return
     */
    @GetMapping("/getBoardContent")
    @ResponseBody
    public AjaxResult loadRealtimeInf(Long deviceId) {
        SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(deviceId);
        if(device == null){
            return AjaxResult.error("设备不存在");
        }
        List<String> paramsList = new ArrayList<String>();
        AjaxResult ajaxResult;
        try {
            Map<String, String> displayList = DeviceManagerFactory.getInstance().getDeviceDisplayListByDeviceId(String.valueOf(deviceId));
            String result = displayList.get("result");
            String protocolType = displayList.get("vender");
            String jsonResult = DataUtils.itemContentToJson(result, protocolType);

            JSONObject items = new JSONObject();

            JSONArray resultObj = JSONArray.parseArray(jsonResult);
            for (int i = 0;i < resultObj.size();i++) {
                JSONObject jsonObject = resultObj.getJSONObject(i);
                int length = String.valueOf(i).length();
                String num = "";
                if (length < 3) {
                    int size = 3-length;
                    for (int j = 0;j < size;j++) {
                        num = num + "0";
                    }
                    num = num + String.valueOf(i);
                } else if (length == 3) {
                    num = String.valueOf(i);
                }
                JSONObject object = jsonObject.getJSONArray("ITEM" + num).getJSONObject(0);
                String content = object.getString("CONTENT");
                if (content.startsWith("S0")) {
                    object.put("CONTENT", content.substring(3));
                }
            }
            items.put("content", resultObj);
            IotDeviceAccess deviceAccess = iotDeviceAccessService.selectIotDeviceAccessById(deviceId);
            items.put("devicePixel", deviceAccess.getDevicePixel());

            paramsList.add(items.toString());
            nowContentMap.put(deviceId.toString(), items.toString());
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "返回成功", paramsList);
            device.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
        } catch (Exception e) {
//        	return AjaxResult.error(-1, e.getMessage());
            device.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            return null;
        }
        return ajaxResult;
    }
	/**
	 * 获取情报板信息
	 * @param deviceId
	 * @return
	 */
    @GetMapping("/getBoardInfo")
    @ResponseBody
    public AjaxResult getBoardInfo(Long deviceId){
        SdIotDevice iotDevice = sdIotDeviceService.selectIotDeviceById(deviceId);
        Map map = sdIotDeviceService.selectIotDeviceAccessById(deviceId);
        Map<String,Object> returnMap = new HashMap<String,Object>();
        returnMap.put("deviceId", iotDevice.getDeviceId());
        returnMap.put("deviceName", iotDevice.getDeviceName());
        returnMap.put("manageAgencyId", iotDevice.getManageAgencyId());
        returnMap.put("pileNumber", iotDevice.getPileNumber());
        returnMap.put("route", iotDevice.getRouteId());
        returnMap.put("firmId", iotDevice.getFirmId());
        returnMap.put("ip", map.get("device_ip"));
        returnMap.put("port", map.get("device_port"));
        returnMap.put("pixel", map.get("device_pixel"));
        return AjaxResult.success(returnMap);
    }


    /**
     * 获取单条情报板编辑信息
     *
     * @return
     */
    @GetMapping("/getBoardEditInfo")
    @ResponseBody
    public AjaxResult getBoardEditInfo(Long deviceId) {
        AjaxResult ajaxResult = new AjaxResult();
        SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(deviceId);
        /*if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
            Object object = nowContentMap.get(deviceId.toString());
            List<String> paramsList = new ArrayList<String>();
            if(object != null && !"".equals(object)){
                SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(deviceId);
                JSONObject jsonObject = JSONObject.parseObject(object.toString());
                if (jsonObject != null && !jsonObject.equals("{}") && jsonObject.get("devicePixel") != null) {
                    JSONObject items = new JSONObject();
                    items.put("content",jsonObject.get("content"));
                    items.put("support", DataUtils.getSupport(String.valueOf(deviceId), sdIotDevice.getProtocolName()));
                    paramsList.add(items.toString());
                    return new AjaxResult(HttpStatus.SUCCESS, "返回成功", paramsList);
                }
            }
            return null;
        }*/
        List<String> paramsList = new ArrayList<String>();
        try {
            // 1.获取设备状态
            Map<String, String> displayList = DeviceManagerFactory.getInstance().getDeviceDisplayListByDeviceId(String.valueOf(deviceId));
            String result = displayList.get("result");
            String protocolType = displayList.get("vender");
            String jsonContent = DataUtils.itemContentToJson(result, protocolType);
            releaseContentMap.put(deviceId.toString(), result);
            JSONArray contentObject = JSONArray.parseArray(jsonContent);
            JSONObject items = new JSONObject();
            for (int i = 0;i < contentObject.size();i++) {
                JSONObject jsonObject = contentObject.getJSONObject(i);
                int length = String.valueOf(i).length();
                String num = "";
                if (length < 3) {
                    int size = 3-length;
                    for (int j = 0;j < size;j++) {
                        num = num + "0";
                    }
                    num = num + String.valueOf(i);
                } else if (length == 3) {
                    num = String.valueOf(i);
                }
                JSONObject object = jsonObject.getJSONArray("ITEM" + num).getJSONObject(0);
                String content = object.getString("CONTENT");
                if (content.startsWith("S0")) {
                    object.put("CONTENT", content.substring(3));
                }
            }
            items.put("content", contentObject);
            items.put("support", DataUtils.getSupport(String.valueOf(deviceId), protocolType));
            paramsList.add(items.toString());
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "返回成功", paramsList);
//        } catch (BusinessException e) {
//            return AjaxResult.error(-1, e.getMessage());
        } catch (Exception e) {
//        	return AjaxResult.error(-1, e.getMessage());
            device.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            JSONObject items = new JSONObject();
            if(paramsList.size() == 0){
                Object object = nowContentMap.get(deviceId.toString());
                if(object != null && !"".equals(object)){
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(deviceId);
                    if (jsonObject != null && !jsonObject.equals("{}") && jsonObject.get("devicePixel") != null) {
                        items.put("content",jsonObject.get("content"));
                        items.put("support", DataUtils.getSupport(String.valueOf(deviceId), sdIotDevice.getProtocolName()));
                        paramsList.add(items.toString());
                        return new AjaxResult(HttpStatus.SUCCESS, "返回成功", paramsList);
                    }
                }
                return null;

            }

        }

        return ajaxResult;
    }



    /**
     * 发布单条情报板编辑信息
     *
     * @return
     */
    @GetMapping("/uploadBoardEditInfo")
    @ResponseBody
    public AjaxResult uploadBoardEditInfo(String deviceIds, String protocolType,String parameters) {
        AjaxResult ajaxResult = new AjaxResult();
        String[] devices = deviceIds.split(",");
        Boolean flag = false;
        List<IotBoardVocabulary> iotBoardVocabularies = iotBoardVocabularyService.selectIotBoardVocabularyList(null);
        String deptId = SecurityUtils.getDeptId();
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        String userId = SecurityUtils.getUserId().toString();
        String username = SecurityUtils.getUsername();
        //储存发布失败的设备
        List<String> failDevList = new ArrayList<>();
        for (int i = 0;i < devices.length;i++) {
            String deviceId = devices[i];
            SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(Long.parseLong(deviceId));
            IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
            iotBoardReleaseLog.setDeviceId(deviceId);
            iotBoardReleaseLog.setReleaseTime(new Date());
            iotBoardReleaseLog.setReleaseDeptId(deptId);
            iotBoardReleaseLog.setReleaseDeptName(sysDept.getDeptName());
            iotBoardReleaseLog.setReleaseUserId(userId);
            iotBoardReleaseLog.setReleaseUserName(username);
            String releaseOldContent = releaseContentMap.get(deviceId);
            iotBoardReleaseLog.setReleaseOldContent(releaseOldContent);
//            if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
//                flag = true;
//                iotBoardReleaseLog.setReleaseStatus("1");
//                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
//                continue;
//            }
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(Long.parseLong(deviceId));
            if(sdIotDevice == null){
                flag = true;
                failDevList.add(device.getEqName());
                continue;
            }
            protocolType = sdIotDevice.getProtocolName();
            List<String> paramsList = new ArrayList<String>();
            try {
                parameters = URLDecoder.decode(parameters, "UTF-8");
                //parameters = "[PLAYLIST]<r><n>ITEM_NO=001<r><n>ITEM000=30,0,0,0,0,\\C000000\\Fh2424\\T255255000000\\W注意安全\\A谨慎驾驶";
                if (protocolType.startsWith(IDeviceProtocol.DIANMING) || protocolType.startsWith(IDeviceProtocol.TONGZHOU)) {
                    parameters = parameters.replaceAll("—", "-");
                    parameters = parameters.replaceAll("\\\\n", "\\\\A");
                } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                    parameters = parameters.replaceAll("—", "-");
                    parameters = parameters.replaceAll("\\\\n", "\\\\N");
                } else {
                    parameters = parameters.replaceAll("—", "-");
                }
                parameters = parameters.replaceAll("<r><n>","\r\n");
                parameters = parameters.replaceAll("<br>","\\\\n");
                String newContent = parameters;
                /*if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
                    flag = true;
                    newContent = newContent.replaceAll("\n", "<n>");
                    newContent = newContent.replaceAll("\r", "<r>");
                    iotBoardReleaseLog.setReleaseNewContent(newContent);
                    iotBoardReleaseLog.setReleaseStatus("1");
                    iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                    //储存失败设备名称
                    failDevList.add(device.getEqName());
                    continue;
                }*/
                for (int g = 0;g < iotBoardVocabularies.size();g++) {
                    String word = iotBoardVocabularies.get(g).getWord();
                    if (parameters.contains(word)) {
                        flag = true;
                        //储存失败设备名称
                        failDevList.add(device.getEqName());
                        break;
                    }
                }
                if (flag) {
                    newContent = newContent.replaceAll("\n", "<n>");
                    newContent = newContent.replaceAll("\r", "<r>");
                    iotBoardReleaseLog.setReleaseNewContent(newContent);
                    iotBoardReleaseLog.setReleaseStatus("1");
                    iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                    throw new RuntimeException("发送的内容包含不恰当的关键字，请修改后重试！");
                }
                String commands = DataUtils.contentToGb2312_CG(deviceId, parameters, protocolType);
                Boolean result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, commands);
                //Boolean result = false;
                if (result) {
                    if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                        String XKcommands = "02 32 32 30 30 30 30 30 2E 78 6B 6C 7A 93 03";
                        result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, XKcommands);
                        if (result) {
                            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                        } else {
                            ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                        }
                    } else {
                        ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                    }
                    iotBoardReleaseLog.setReleaseStatus("0");
                } else {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                    iotBoardReleaseLog.setReleaseStatus("1");
                }
                parameters = parameters.replaceAll("\n", "<n>");
                parameters = parameters.replaceAll("\r", "<r>");
                iotBoardReleaseLog.setReleaseNewContent(parameters);
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                releaseContentMap.clear();
            } catch (Exception e) {
                if (flag) {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "发送的内容包含不恰当的关键字，请修改后重试！");
                } else {
                    parameters = parameters.replaceAll("\n", "<n>");
                    parameters = parameters.replaceAll("\r", "<r>");
                    iotBoardReleaseLog.setReleaseNewContent(parameters);
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "网络异常,发送失败");
                }
                iotBoardReleaseLog.setReleaseStatus("1");
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
            }
        }

        if(flag){
            ajaxResult = new AjaxResult(900, StringUtils.join(failDevList,","));
        }

        return ajaxResult;
    }


//    /*
//     * 创建线程池获取情报板内容信息并存储在缓存中
//     * */
//    @GetMapping("/addBoardCacheByDeviceid")
//    @ResponseBody
//    public void loadBoardContentAdd(Long deviceId) {
//        if (executorService == null) {
//            executorService = Executors.newFixedThreadPool(20);//线程池
//        }
//
//        executorService.execute(new Task(deviceId));
//
//    }

    @GetMapping("/getWorkBenchBoardContent/{tunnelId}")
    public Map<String, Object> getWorkBenchBoardContent(@PathVariable String tunnelId) {
        Map<String, Object> map = new HashMap<>();
        List<Long> deviceIds = sdIotDeviceService.selectIotDevicesByTunnelId(tunnelId);
        for (int i = 0;i < deviceIds.size();i++) {
            Long deviceId = deviceIds.get(i);
            String devId = deviceId.toString();
            IotDeviceAccess iotDeviceAccess = iotDeviceAccessService.selectIotDeviceAccessById(deviceId);
            if (iotDeviceAccess == null || iotDeviceAccess.getDevicePixel() == null || iotDeviceAccess.getDevicePixel().equals("")) {
                continue;
            }
            String deviceInformation = "{\"devicePixel\":\"" + iotDeviceAccess.getDevicePixel() + "\"}";
            map.put(devId, deviceInformation);
            if (nowContentMap != null && !nowContentMap.isEmpty() && nowContentMap.get(devId) != null) {
                Object result = nowContentMap.get(devId);
                JSONObject jsonObject = JSONObject.parseObject(result.toString());
                if (jsonObject != null && !jsonObject.equals("{}") && jsonObject.get("devicePixel") != null) {
                    map.put(devId, result);
                }
            }
        }
        return map;
    }

    /**
     * 获取情报板亮度信息
     *
     * @return
     */
    @GetMapping(value = "/readBoardLightInfo")
    @ResponseBody
    public AjaxResult readBoardLightInfo(String deviceId) {
        // 1.获取设备状态
        Map<String, String> values = DeviceManagerFactory.getInstance().getDeviceBrightnessByDeviceId(deviceId);
        AjaxResult ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "返回成功", values);
        return ajaxResult;
    }

    /**
     * 获取情报板信息
     * @param boardReleaseLog
     * @return
     */
    @GetMapping("/getBoardContentData")
    public AjaxResult getBoardContentData(IotBoardReleaseLog boardReleaseLog){
        return AjaxResult.success(getBoard(boardReleaseLog.getDeviceId()));
    }

    /**
     * 定时查询情报板
     * @param tunnelId
     * @return
     */
    @GetMapping("/getTimedReading/{tunnelId}")
    public AjaxResult getTimedReading(@PathVariable String tunnelId){
        List<Long> deviceIds = sdIotDeviceService.selectIotDevicesByTunnelId(tunnelId);
        Map<Long, Object> map = new HashMap<>();
        for(Long id : deviceIds){
            IotDeviceAccess iotDeviceAccess = iotDeviceAccessService.selectIotDeviceAccessById(id);
            JSONObject board = getBoard(id.toString());
            if(board != null){
                board.put("devicePixel",iotDeviceAccess.getDevicePixel());
                map.put(id,board);
            }else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("devicePixel",iotDeviceAccess.getDevicePixel());
                map.put(id,jsonObject);
            }
        }
        return AjaxResult.success(map);
    }

    //情报板回读
    public JSONObject getBoard(String deviceId){
        SdDevices sdDevices = sdDevicesService.selectBoardSdDevicesById(deviceId);
        if(sdDevices == null){
            return null;
        }
        /*Object boardContent = boardContentMap.get(sdDevices.getAssociatedDeviceId().toString());
        if(boardContent != null){
            JSONObject jsonObject = JSONObject.parseObject(boardContent.toString());
            return jsonObject;
        }else {
            Map<String, Object> boardContentData = iotBoardReleaseLogMapper.getBoardContentData(deviceId);
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(sdDevices.getAssociatedDeviceId());
            String protocolType = sdIotDevice.getProtocolName();
            if(boardContentData != null){
                Object content2 = boardContentData.get("content");
                if(content2 == null || "".equals(content2)){
                    return null;
                }
                String content = boardContentData.get("content").toString();
                String substring = "";
                substring = content.substring(31,content.length());
                *//*if(!protocolType.startsWith(IDeviceProtocol.SANSI)){
                    substring = content.substring(31,content.length());
                }else {
                    substring = content.substring(27,content.length());
                }*//*
                String boardCon = DataUtils.itemContentToJson(substring, protocolType);
                JSONArray objects = new JSONArray();
                if(boardCon != null && !"".equals(boardCon) && boardCon.contains("[")){
                    objects = JSONObject.parseArray(boardCon);
                }
                String o = "";
                List<JSONObject> list = new ArrayList<>();
                for(int i = 0; i < objects.size(); i++){
                    JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
                    if(protocolType.startsWith(IDeviceProtocol.GUANGDIAN) || protocolType.startsWith(IDeviceProtocol.DINGEN) || protocolType.startsWith(IDeviceProtocol.TONGZHOU) || protocolType.startsWith(IDeviceProtocol.DIANMING) || protocolType.startsWith(IDeviceProtocol.SANSI)){
                        JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get("ITEM" + String.format("%03d", i)).toString());
                        for(int a = 0; a < jsonArray.size(); a++){
                            JSONObject jsonObject2 = JSONObject.parseObject(jsonArray.get(a).toString());
                            *//*o = jsonObject2.get("ITEM" + String.format("%03d", i)).toString();
                            String s = o.replaceAll("\\[", "").replaceAll("]", "");
                            JSONObject jsonObject = JSONObject.parseObject(s);*//*
                            if(protocolType.startsWith(IDeviceProtocol.DINGEN)){
                                String content1 = jsonObject2.getString("CONTENT");
                                if(content1.contains("W")){
                                    content1 = content1.substring(1, jsonObject2.getString("CONTENT").length());
                                }
                                jsonObject2.put("CONTENT",content1);
                            }
                            list.add(jsonObject2);
                        }

                    }
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deviceIds",sdDevices.getAssociatedDeviceId());
                jsonObject.put("parameters",JSON.toJSON(list));
                return jsonObject;
            }else {
                return null;
            }
        }*/
        Map<String, Object> boardContentData = iotBoardReleaseLogMapper.getBoardContentData(sdDevices.getAssociatedDeviceId().toString());
        SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(sdDevices.getAssociatedDeviceId());
        String protocolType = sdIotDevice.getProtocolName();
        if(boardContentData != null){
            Object content2 = boardContentData.get("content");
            if(content2 == null || "".equals(content2)){
                return null;
            }
            String content = boardContentData.get("content").toString();
            String substring = "";
            substring = content.substring(content.indexOf("ITEM0"), content.length());
            //substring = content.substring(31,content.length());
                /*if(!protocolType.startsWith(IDeviceProtocol.SANSI)){
                    substring = content.substring(31,content.length());
                }else {
                    substring = content.substring(27,content.length());
                }*/
            String boardCon = DataUtils.itemContentToJson(substring, protocolType);
            JSONArray objects = new JSONArray();
            if(boardCon != null && !"".equals(boardCon) && boardCon.contains("[")){
                objects = JSONObject.parseArray(boardCon);
            }
            String o = "";
            List<JSONObject> list = new ArrayList<>();
            for(int i = 0; i < objects.size(); i++){
                JSONObject jsonObject1 = JSONObject.parseObject(objects.get(i).toString());
                if(protocolType.startsWith(IDeviceProtocol.GUANGDIAN) || protocolType.startsWith(IDeviceProtocol.DINGEN) || protocolType.startsWith(IDeviceProtocol.TONGZHOU) || protocolType.startsWith(IDeviceProtocol.DIANMING) || protocolType.startsWith(IDeviceProtocol.SANSI)){
                    JSONArray jsonArray = JSONObject.parseArray(jsonObject1.get("ITEM" + String.format("%03d", i)).toString());
                    for(int a = 0; a < jsonArray.size(); a++){
                        JSONObject jsonObject2 = JSONObject.parseObject(jsonArray.get(a).toString());
                            /*o = jsonObject2.get("ITEM" + String.format("%03d", i)).toString();
                            String s = o.replaceAll("\\[", "").replaceAll("]", "");
                            JSONObject jsonObject = JSONObject.parseObject(s);*/
                        if(protocolType.startsWith(IDeviceProtocol.DINGEN)){
                            String content1 = jsonObject2.getString("CONTENT");
                            if(content1.contains("W")){
                                content1 = content1.substring(1, jsonObject2.getString("CONTENT").length());
                            }
                            jsonObject2.put("CONTENT",content1);
                        }
                        list.add(jsonObject2);
                    }

                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceIds",sdDevices.getAssociatedDeviceId());
            jsonObject.put("parameters",JSON.toJSON(list));
            return jsonObject;
        }else {
            return null;
        }
    }

    /**
     * 根据设备ID获取设备详情
     * @param deviceId
     * @return
     */
    public SdDevices getDevicesInfo(String deviceId){
    	SdDevices sd = sdDevicesService.selectSdDevicesById(deviceId);
    	return sd;
    }

    /**
     * 获取单条情报板基础信息
     *
     * @return
     */
    @GetMapping("/getDeviceBase")
    @ResponseBody
    public AjaxResult getDeviceBase(String deviceId) {
    	SdIotDevice iotDevice = sdIotDeviceService.selectDeviceAccessById(Long.parseLong(deviceId));
        AjaxResult ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "返回成功", iotDevice);
        return ajaxResult;
    }

    /**
     * 获取单条情报板编辑信息
     *
     * @return
     */
    @GetMapping("/getNewBoardEditInfo")
    @ResponseBody
    public AjaxResult getNewBoardEditInfo(String deviceId) {
        AjaxResult ajaxResult = new AjaxResult();
        List<String> paramsList = new ArrayList<String>();
        SdDevices sd = getDevicesInfo(deviceId);
        try {
            // 1.获取设备状态
            Map<String, String> displayList = DeviceManagerFactory.getInstance().getDeviceDisplayListByDeviceId(deviceId);
            String result = displayList.get("result");
            String protocolType = displayList.get("vender");
            String jsonContent = DataUtils.itemContentToJson(result, protocolType);
            releaseContentMap.put(deviceId.toString(), result);
            JSONArray contentObject = JSONArray.parseArray(jsonContent);
            JSONObject items = new JSONObject();
            items.put("content", contentObject);
            items.put("support", DataUtils.getSupport(String.valueOf(deviceId), protocolType));
            paramsList.add(items.toString());
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "返回成功", paramsList);
        } catch (BusinessException e) {
            return AjaxResult.error(-1, e.getMessage());
        } catch (Exception e) {
        	return AjaxResult.error(-1, e.getMessage());
        }

        return ajaxResult;
    }

    @GetMapping("/getFontSizeByDevicePixel/{devicePixel}")
    public AjaxResult getFontSizeByDevicePixel(@PathVariable("devicePixel") String devicePixel) {
        return AjaxResult.success(iIotBoradFontService.getFontSizeByDevicePixel(devicePixel));
    }

    /**
     * 为第三方提供控制接口
     * @param objectData
     * @return
     */
    @GetMapping("/commonControlBoard")
    public AjaxResult commonControlBoard(@RequestBody String objectData){
        if(wjModelNum == 0){
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(objectData);
        try {
            JSONArray parameters = jsonObject.getJSONArray("parameters");
            if(parameters.size() == 0){
                return AjaxResult.error("内容不能为空");
            }
            if(parameters.size() == 0){
                return null;
            }
            String[] deviceIds = jsonObject.getString("deviceIds").split(",");
            SdDevices sdDevices = sdDevicesService.selectBoardSdDevicesById(deviceIds[0]);
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(sdDevices.getAssociatedDeviceId());
            String protocolType = sdIotDevice.getProtocolName();
            List<String> deviceIdList = new ArrayList<>();
            List<String> eqIdList = new ArrayList<>();
            for(int i = 0; i < deviceIds.length; i++){
                SdDevices sdDevices1 = sdDevicesService.selectBoardSdDevicesById(deviceIds[i]);
                deviceIdList.add(sdDevices1.getAssociatedDeviceId().toString());
                eqIdList.add(sdDevices1.getEqId());
            }
            //拼接内容
            StringBuffer content = new StringBuffer();
            if(protocolType.startsWith(IDeviceProtocol.IB_DINGEN_V10)){
                content.append("[PLAYLIST]<r><n>ITEM_NO=").append(String.format("%03d",parameters.size()));
                for(int i = 0; i < parameters.size(); i++){
                    Map<String, Object> map = (Map<String, Object>) parameters.get(i);
                    content.append("<r><n>ITEM").append(String.format("%03d",i)).append("=");
                    content.append(map.get("STAY")).append(",").append("0,0,0,0,");
                    content.append("\\").append("C").append(map.get("COORDINATE"));
                    content.append("\\").append("F").append(map.get("FONT")).append(map.get("FONT_SIZE")).append(map.get("FONT_SIZE"));
                    content.append("\\").append("T").append(map.get("COLOR"));
                    String boardContent = map.get("CONTENT").toString();
                    content.append("\\").append("W").append(boardContent);
                }
                /*AjaxResult ajaxResult = dsfDingEnBoard(deviceIdList.stream().collect(Collectors.joining(",")), "", parameters,content);
                if(ajaxResult.get("code").toString().equals("200")){
                    JSONObject boardData = new JSONObject();
                    boardData.put("deviceIds",sdDevices.getAssociatedDeviceId());
                    boardData.put("parameters",parameters);
                    boardContentMap.put(sdDevices.getAssociatedDeviceId()+"",boardData);
                    eqIdList.stream().forEach(item ->{
                        SdDevices sdDevices1 = new SdDevices();
                        sdDevices1.setEqId(item);
                        sdDevices1.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                        sdDevices1.setEqStatusTime(new Date());
                        sdDevicesService.updateSdDevices(sdDevices1);
                    });
                }
                return ajaxResult;*/
                IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
                iotBoardReleaseLog.setDeviceId(sdDevices.getAssociatedDeviceId().toString());
                iotBoardReleaseLog.setReleaseTime(new Date());
                iotBoardReleaseLog.setReleaseStatus("0");
                iotBoardReleaseLog.setReleaseNewContent("万集："+content.toString());
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                return AjaxResult.success();
            }else if(protocolType.startsWith(IDeviceProtocol.DIANMING) || protocolType.startsWith(IDeviceProtocol.TONGZHOU)){
                content.append("[PLAYLIST]<r><n>ITEM_NO=").append(String.format("%03d",parameters.size()));
                for(int i = 0; i < parameters.size(); i++){
                    Map<String, Object> map = (Map<String, Object>) parameters.get(i);
                    content.append("<r><n>ITEM").append(String.format("%03d",i)).append("=");
                    content.append(map.get("STAY")).append(",").append("0,0,0,0,");
                    content.append("\\").append("C").append(map.get("COORDINATE"));
                    content.append("\\").append("F").append(map.get("FONT")).append(map.get("FONT_SIZE")).append(map.get("FONT_SIZE"));
                    content.append("\\").append("T").append(map.get("COLOR"));
                    String boardContent = map.get("CONTENT").toString();
                    content.append("\\").append("W").append(boardContent.replaceAll("<r><n>","\\\\A"));
                }
            }else if(protocolType.startsWith(IDeviceProtocol.GUANGDIAN) || protocolType.startsWith(IDeviceProtocol.SANSI)){
                content.append("[Playlist]<r><n>ITEM_NO=").append(parameters.size());
                for(int i = 0; i < parameters.size(); i++){
                    Map<String, Object> map = (Map<String, Object>) parameters.get(i);
                    content.append("<r><n>ITEM").append(String.format("%03d",i)).append("=");
                    content.append(map.get("STAY")).append(",").append(map.get("ACTION")).append(",");
                    content.append(map.get("SPEED")).append(",").append("\\").append("C");
                    content.append(map.get("COORDINATE")).append("\\").append("S00\\").append("c");
                    content.append(map.get("COLOR")).append("\\").append("f").append(map.get("FONT"));
                    content.append(map.get("FONT_SIZE")).append(map.get("FONT_SIZE")).append(map.get("CONTENT"));
                }
            }
            /*String encode = URLEncoder.encode(String.valueOf(content), "UTF-8");
            AjaxResult ajaxResult = releaseBoardEditInfo(deviceIdList.stream().collect(Collectors.joining(",")), "", encode);
            if(ajaxResult.get("code").toString().equals("200")){
                JSONObject boardData = new JSONObject();
                boardData.put("deviceIds",sdDevices.getAssociatedDeviceId());
                boardData.put("parameters",parameters);
                boardContentMap.put(sdDevices.getAssociatedDeviceId()+"",boardData);
                eqIdList.stream().forEach(item ->{
                    SdDevices sdDevices1 = new SdDevices();
                    sdDevices1.setEqId(item);
                    sdDevices1.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                    sdDevices1.setEqStatusTime(new Date());
                    sdDevicesService.updateSdDevices(sdDevices1);
                });
            }
            return ajaxResult;*/
            IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
            iotBoardReleaseLog.setDeviceId(sdDevices.getAssociatedDeviceId().toString());
            iotBoardReleaseLog.setReleaseTime(new Date());
            iotBoardReleaseLog.setReleaseStatus("0");
            iotBoardReleaseLog.setReleaseNewContent("万集："+content.toString());
            iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
            return AjaxResult.success();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 拼接报文
     * @param mapData
     * @return
     */
    @GetMapping("/splicingBoard")
    public AjaxResult splicingBoard(@RequestParam Map<String, Object> mapData){
        Object objectData = mapData.get("objectData");
        JSONObject jsonObject = JSONObject.parseObject(objectData.toString());
        try {
            JSONArray parameters = jsonObject.getJSONArray("parameters");
            if(parameters.size() == 0){
                return null;
            }
            String[] deviceIds = jsonObject.getString("deviceIds").split(",");
            SdDevices sdDevices = sdDevicesService.selectBoardSdDevicesById(deviceIds[0]);
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(sdDevices.getAssociatedDeviceId());
            String protocolType = sdIotDevice.getProtocolName();
            List<String> deviceIdList = new ArrayList<>();
            List<String> eqIdList = new ArrayList<>();
            for(int i = 0; i < deviceIds.length; i++){
                SdDevices sdDevices1 = sdDevicesService.selectBoardSdDevicesById(deviceIds[i]);
                deviceIdList.add(sdDevices1.getAssociatedDeviceId().toString());
                eqIdList.add(sdDevices1.getEqId());
            }
            //拼接内容
            StringBuffer content = new StringBuffer();
            if(protocolType.startsWith(IDeviceProtocol.IB_DINGEN_V10)){
                content.append("[PLAYLIST]<r><n>ITEM_NO=").append(String.format("%03d",parameters.size()));
                for(int i = 0; i < parameters.size(); i++){
                    Map<String, Object> map = (Map<String, Object>) parameters.get(i);
                    content.append("<r><n>ITEM").append(String.format("%03d",i)).append("=");
                    content.append(map.get("STAY")).append(",").append("0,0,0,0,");
                    content.append("\\").append("C").append(map.get("COORDINATE"));
                    content.append("\\").append("F").append(map.get("FONT")).append(map.get("FONT_SIZE")).append(map.get("FONT_SIZE"));
                    content.append("\\").append("T").append(map.get("COLOR"));
                    String boardContent = map.get("CONTENT").toString();
                    content.append("\\").append("W").append(boardContent);
                }
                AjaxResult ajaxResult = dingEnBoard(deviceIdList.stream().collect(Collectors.joining(",")), "", parameters,content);
                if(ajaxResult.get("code").toString().equals("200")){
                    JSONObject boardData = new JSONObject();
                    boardData.put("deviceIds",sdDevices.getAssociatedDeviceId());
                    boardData.put("parameters",parameters);
                    boardContentMap.put(sdDevices.getAssociatedDeviceId()+"",boardData);
                    eqIdList.stream().forEach(item ->{
                        SdDevices sdDevices1 = new SdDevices();
                        sdDevices1.setEqId(item);
                        sdDevices1.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                        sdDevices1.setEqStatusTime(new Date());
                        sdDevicesService.updateSdDevices(sdDevices1);
                    });
                }
                return ajaxResult;
            }else if(protocolType.startsWith(IDeviceProtocol.DIANMING) || protocolType.startsWith(IDeviceProtocol.TONGZHOU)){
                content.append("[PLAYLIST]<r><n>ITEM_NO=").append(String.format("%03d",parameters.size()));
                for(int i = 0; i < parameters.size(); i++){
                    Map<String, Object> map = (Map<String, Object>) parameters.get(i);
                    content.append("<r><n>ITEM").append(String.format("%03d",i)).append("=");
                    content.append(map.get("STAY")).append(",").append("0,0,0,0,");
                    content.append("\\").append("C").append(map.get("COORDINATE"));
                    content.append("\\").append("F").append(map.get("FONT")).append(map.get("FONT_SIZE")).append(map.get("FONT_SIZE"));
                    content.append("\\").append("T").append(map.get("COLOR"));
                    String boardContent = map.get("CONTENT").toString();
                    content.append("\\").append("W").append(boardContent.replaceAll("<r><n>","\\\\A"));
                }
            }else if(protocolType.startsWith(IDeviceProtocol.GUANGDIAN) || protocolType.startsWith(IDeviceProtocol.SANSI)){
                content.append("[Playlist]<r><n>ITEM_NO=").append(parameters.size());
                for(int i = 0; i < parameters.size(); i++){
                    Map<String, Object> map = (Map<String, Object>) parameters.get(i);
                    content.append("<r><n>ITEM").append(String.format("%03d",i)).append("=");
                    content.append(map.get("STAY")).append(",").append(map.get("ACTION")).append(",");
                    content.append(map.get("SPEED")).append(",").append("\\").append("C");
                    content.append(map.get("COORDINATE")).append("\\").append("S00\\").append("c");
                    content.append(map.get("COLOR")).append("\\").append("f").append(map.get("FONT"));
                    content.append(map.get("FONT_SIZE")).append(map.get("FONT_SIZE")).append(map.get("CONTENT"));
                }
            }
            String encode = URLEncoder.encode(String.valueOf(content), "UTF-8");
            AjaxResult ajaxResult = uploadBoardEditInfo(deviceIdList.stream().collect(Collectors.joining(",")), "", encode);
            if(ajaxResult.get("code").toString().equals("200")){
                JSONObject boardData = new JSONObject();
                boardData.put("deviceIds",sdDevices.getAssociatedDeviceId());
                boardData.put("parameters",parameters);
                boardContentMap.put(sdDevices.getAssociatedDeviceId()+"",boardData);
                eqIdList.stream().forEach(item ->{
                    SdDevices sdDevices1 = new SdDevices();
                    sdDevices1.setEqId(item);
                    sdDevices1.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                    sdDevices1.setEqStatusTime(new Date());
                    sdDevicesService.updateSdDevices(sdDevices1);
                });
                //隧道内情报板
                List<SdRadarDevice> list = new ArrayList<>();
                SdDevices sdDevicesBoard = sdDevicesMapper.selectSdDevicesById(sdDevices.getEqId());
                JSONObject devMap = new JSONObject();
                //情报板编号
                devMap.put("boardNo",sdDevicesBoard.getAssociatedDeviceId());
                //隧道id
                devMap.put("tunnelId",sdDevicesBoard.getEqTunnelId());
                //设备id
                devMap.put("deviceCode",sdDevicesBoard.getEqId());
                //情报板数据
                String substring = content.substring(31,content.length());
                String boardCon = DataUtils.itemContentToJson(substring, protocolType);
                JSONArray objects = new JSONArray();
                if(boardCon != null && !"".equals(boardCon) && boardCon.contains("[")){
                    objects = JSONObject.parseArray(boardCon);
                }
                Object o = new Object();
                for(int i = 0; i < objects.size(); i++){
                    JSONObject jsonObject1 = JSONObject.parseObject(objects.get(0).toString());
                    if(protocolType.startsWith(IDeviceProtocol.GUANGDIAN)){
                        o = jsonObject1.get("ITEM" + String.format("%03d",i));
                    }
                }
                devMap.put("list",o);
                SdRadarDevice sdRadarDevice = radarEventServiceImpl.setRadarDevice(sdDevicesBoard);
                sdRadarDevice.setDeviceData(devMap);
                list.add(sdRadarDevice);
                threadPoolTaskExecutor.execute(()->{
                    kafkaOneTemplate.send("baseDeviceStatus", JSON.toJSONString(list));
                });
            }
            return ajaxResult;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public AjaxResult dingEnBoard(String deviceIds, String protocolType,JSONArray parameters,StringBuffer content) {
        String boardContent = null;
        String color = null;
        for(int i = 0; i < parameters.size(); i++){
            Map<String, Object> map = (Map<String, Object>) parameters.get(i);
            boardContent = map.get("CONTENT").toString();
            color = map.get("COLOR").toString();
        }
        AjaxResult ajaxResult = new AjaxResult();
        String[] devices = deviceIds.split(",");
        Boolean flag = false;
        List<IotBoardVocabulary> iotBoardVocabularies = iotBoardVocabularyService.selectIotBoardVocabularyList(null);
        String deptId = SecurityUtils.getDeptId();
        SysDept sysDept = sysDeptService.selectDeptById(deptId);
        String userId = SecurityUtils.getUserId().toString();
        String username = SecurityUtils.getUsername();
        //储存发布失败的设备
        List<String> failDevList = new ArrayList<>();
        for (int i = 0;i < devices.length;i++) {
            String deviceId = devices[i];
            SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(Long.parseLong(deviceId));
            IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
            iotBoardReleaseLog.setDeviceId(deviceId);
            iotBoardReleaseLog.setReleaseTime(new Date());
            iotBoardReleaseLog.setReleaseDeptId(deptId);
            iotBoardReleaseLog.setReleaseDeptName(sysDept.getDeptName());
            iotBoardReleaseLog.setReleaseUserId(userId);
            iotBoardReleaseLog.setReleaseUserName(username);
            String releaseOldContent = releaseContentMap.get(deviceId);
            iotBoardReleaseLog.setReleaseOldContent(releaseOldContent);
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(Long.parseLong(deviceId));
            if(sdIotDevice == null){
                flag = true;
                failDevList.add(device.getEqName());
                continue;
            }
            protocolType = sdIotDevice.getProtocolName();
            List<String> paramsList = new ArrayList<String>();
            try {
                String commands = DataUtils.dengEnContentToGb2312_CG(boardContent, color);
                Boolean result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, commands);
//                Boolean result = false;
                if (result) {
                    if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                        String XKcommands = "02 32 32 30 30 30 30 30 2E 78 6B 6C 7A 93 03";
                        result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, XKcommands);
                        if (result) {
                            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                        } else {
                            ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                        }
                    } else {
                        ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                    }
                    iotBoardReleaseLog.setReleaseStatus("0");
                } else {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                    iotBoardReleaseLog.setReleaseStatus("1");
                }
                iotBoardReleaseLog.setReleaseNewContent(content.toString());
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                releaseContentMap.clear();
            } catch (Exception e) {
                if (flag) {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "发送的内容包含不恰当的关键字，请修改后重试！");
                } else {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "网络异常,发送失败");
                }
                iotBoardReleaseLog.setReleaseStatus("1");
                iotBoardReleaseLog.setReleaseNewContent(content.toString());
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
            }
        }

        if(flag){
            ajaxResult = new AjaxResult(900, StringUtils.join(failDevList,","));
        }

        return ajaxResult;
    }

    public AjaxResult dsfDingEnBoard(String deviceIds, String protocolType,JSONArray parameters,StringBuffer content) {
        String boardContent = null;
        String color = null;
        for(int i = 0; i < parameters.size(); i++){
            Map<String, Object> map = (Map<String, Object>) parameters.get(i);
            boardContent = map.get("CONTENT").toString();
            color = map.get("COLOR").toString();
        }
        AjaxResult ajaxResult = new AjaxResult();
        String[] devices = deviceIds.split(",");
        Boolean flag = false;
        List<IotBoardVocabulary> iotBoardVocabularies = iotBoardVocabularyService.selectIotBoardVocabularyList(null);
        //储存发布失败的设备
        List<String> failDevList = new ArrayList<>();
        for (int i = 0;i < devices.length;i++) {
            String deviceId = devices[i];
            SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(Long.parseLong(deviceId));
            IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
            iotBoardReleaseLog.setDeviceId(deviceId);
            iotBoardReleaseLog.setReleaseTime(new Date());
            String releaseOldContent = releaseContentMap.get(deviceId);
            iotBoardReleaseLog.setReleaseOldContent(releaseOldContent);
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(Long.parseLong(deviceId));
            if(sdIotDevice == null){
                flag = true;
                failDevList.add(device.getEqName());
                continue;
            }
            protocolType = sdIotDevice.getProtocolName();
            List<String> paramsList = new ArrayList<String>();
            try {
                String commands = DataUtils.dengEnContentToGb2312_CG(boardContent, color);
                Boolean result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, commands);
//                Boolean result = false;
                if (result) {
                    if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                        String XKcommands = "02 32 32 30 30 30 30 30 2E 78 6B 6C 7A 93 03";
                        result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, XKcommands);
                        if (result) {
                            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                        } else {
                            ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                        }
                    } else {
                        ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                    }
                    iotBoardReleaseLog.setReleaseStatus("0");
                } else {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                    iotBoardReleaseLog.setReleaseStatus("1");
                }
                iotBoardReleaseLog.setReleaseNewContent(content.toString());
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                releaseContentMap.clear();
            } catch (Exception e) {
                if (flag) {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "发送的内容包含不恰当的关键字，请修改后重试！");
                } else {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "网络异常,发送失败");
                }
                iotBoardReleaseLog.setReleaseStatus("1");
                iotBoardReleaseLog.setReleaseNewContent(content.toString());
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
            }
        }

        if(flag){
            ajaxResult = new AjaxResult(900, StringUtils.join(failDevList,","));
        }

        return ajaxResult;
    }

    /*public AjaxResult dengEnBoard(String eqId){
        ProgramCreator programCreator = new ProgramCreator(144,48, ProtocolConstant.COLOR_TYPE_FULL_COLOR);
        int winNo = programCreator.addWindow(0,0,144,48);
        programCreator.addTextItem(winNo,"山东高速欢迎你",0x57899C,ProtocolConstant.FONTSIZE_16,100,5, ShowEffect.Instant.getEffect(),1,1,1);
        programCreator.createLpbFile("D:\\qingbaoban\\" + eqId+"\\111","111");
        FileUploadProtocol uploadProtocol = new FileUploadProtocol();
        List<String> files = new ArrayList<>();
        files.add("D:\\qingbaoban\\" + eqId + "\\111\\" + "111.lpb");
        uploadProtocol.setListener(new FileUploadProtocol.OnUploadListener(){

            @Override
            public void onStatus(int i, int i1) {

            }

            @Override
            public void onProcess(int i, int i1, int i2) {

            }
        });
        uploadProtocol.uploadFile("D:\\qingbaoban\\JQ-ZiBo-TaiHe-QFL-TCMS-001\\111",
                files,144,48, ProtocolConstant.COLOR_TYPE_FULL_COLOR,
                "10.7.183.165",5200,1);

        return AjaxResult.success();
    }*/

    /*public void setListener(){
        ControlUtil controlUtil = new ControlUtil();
        //设置监听
        controlUtil.setOnControlListener(new ControlUtil.OnControlListener(){

            @Override
            public void onBackInfos(String s, int i) {
                
            }

            @Override
            public void onNetStatus(int i, int i1) {

            }

            @Override
            public void breakSocket(int i) {

            }

            @Override
            public void onProcess(int i, int i1, int i2) {

            }

            @Override
            public void onBackData(int[] ints, int i) {

            }

            @Override
            public void onResultCode(int i, int i1) {

            }
        });
        //回读内容
        controlUtil.getCurrentPlayProgramContent("10.7.183,165",5200,1,5);
    }*/

//    public void setDeviceData(String deviceId, Long typeId, String content, List<SdDeviceData> sdDeviceDataList){
//        if(sdDeviceDataList.size() > 0){
//            SdDeviceData sdDeviceData1 = sdDeviceDataList.get(0);
//            sdDeviceData1.setData(content);
//            sdDeviceData1.setUpdateTime(DateUtils.getNowDate());
//            deviceDataMapper.updateKafkaDeviceData(sdDeviceData1);
//        }else {
//            SdDeviceData sdDeviceData1 = new SdDeviceData();
//            sdDeviceData1.setData(content);
//            sdDeviceData1.setItemId(typeId);
//            sdDeviceData1.setDeviceId(deviceId);
//            sdDeviceData1.setCreateTime(DateUtils.getNowDate());
//            deviceDataMapper.insertSdDeviceData(sdDeviceData1);
//        }
//    }

    /**
     * 提供情报板实时信息
     * @param deviceId
     * @return
     */
    @GetMapping("/getRealTimeBoard")
    public AjaxResult getRealTimeBoard(String deviceId){
        /*if(wjModelNum == 0){
            return null;
        }*/
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);
        if(sdDevices == null || sdDevices.getAssociatedDeviceId() == null || "".equals(sdDevices.getAssociatedDeviceId())){
            return AjaxResult.error("设备不存在");
        }else {
            JSONObject board = getBoard(deviceId);
            /*AjaxResult boardEditInfo = getBoardEditInfo(sdDevices.getAssociatedDeviceId());
            if(boardEditInfo == null){
                return AjaxResult.error("设备离线");
            }
            int code = Integer.valueOf(boardEditInfo.get("code").toString());
            if(code == 200 && boardEditInfo != null){
                List<String> data = (List<String>)boardEditInfo.get("data");
                IotDeviceAccess deviceAccess = iotDeviceAccessService.selectIotDeviceAccessById(sdDevices.getAssociatedDeviceId());
                JSONObject jsObject = new JSONObject();
                JSONObject jsonObject = JSONObject.parseObject(data.get(0));
                JSONArray jsonObject1 = JSONObject.parseArray(jsonObject.get("content").toString());
                List<Object> contentList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();
                for(int i = 0; i < jsonObject1.size(); i++){
                    JSONObject jsonObject2 = jsonObject1.getJSONObject(i);
                    String key = "ITEM";
                    String format = String.format("%03d", i);
                    JSONArray objects = JSONObject.parseArray(jsonObject2.get(key + format).toString());
                    JSONObject jsonObject3 = objects.getJSONObject(0);
                    jsonArray.add(jsonObject3);
                }
                jsObject.put("content", jsonArray);
                jsObject.put("devicePixel",deviceAccess.getDevicePixel());
                return AjaxResult.success("返回成功",jsObject);
            }*/
            IotDeviceAccess deviceAccess = iotDeviceAccessService.selectIotDeviceAccessById(sdDevices.getAssociatedDeviceId());
            JSONObject boardJsonObject = new JSONObject();
            JSONArray jsonArray = JSONObject.parseArray(board.get("parameters").toString());
            JSONArray array = new JSONArray();
            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
                String content = jsonObject.getString("CONTENT");
                content = content.replaceAll("<n>", "<br>");
                content = content.replaceAll("<r>", "\r");
                jsonObject.put("CONTENT",content);
                array.add(jsonObject);
            }
            boardJsonObject.put("content",array);
            boardJsonObject.put("devicePixel",deviceAccess.getDevicePixel());
            return AjaxResult.success("返回成功",boardJsonObject);
        }
    }

    public AjaxResult releaseBoardEditInfo(String deviceIds, String protocolType,String parameters) {
        AjaxResult ajaxResult = new AjaxResult();
        String[] devices = deviceIds.split(",");
        Boolean flag = false;
        List<IotBoardVocabulary> iotBoardVocabularies = iotBoardVocabularyService.selectIotBoardVocabularyList(null);
        //储存发布失败的设备
        List<String> failDevList = new ArrayList<>();
        for (int i = 0;i < devices.length;i++) {
            SdDevices sdDevices = sdDevicesService.selectBoardSdDevicesById(devices[i]);
            String deviceId = sdDevices.getAssociatedDeviceId().toString();
            SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(Long.parseLong(deviceId));
            IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
            iotBoardReleaseLog.setDeviceId(deviceId);
            iotBoardReleaseLog.setReleaseTime(new Date());
            String releaseOldContent = releaseContentMap.get(deviceId);
            iotBoardReleaseLog.setReleaseOldContent("万集："+releaseOldContent);
//            if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
//                flag = true;
//                iotBoardReleaseLog.setReleaseStatus("1");
//                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
//                continue;
//            }
            SdIotDevice sdIotDevice = sdIotDeviceService.selectIotDeviceById(Long.parseLong(deviceId));
            if(sdIotDevice == null){
                flag = true;
                failDevList.add(device.getEqName());
                continue;
            }
            protocolType = sdIotDevice.getProtocolName();
            List<String> paramsList = new ArrayList<String>();
            try {
                parameters = URLDecoder.decode(parameters, "UTF-8");
                if (protocolType.startsWith(IDeviceProtocol.DIANMING) || protocolType.startsWith(IDeviceProtocol.TONGZHOU)) {
                    parameters = parameters.replaceAll("—", "-");
                    parameters = parameters.replaceAll("\\\\n", "\\\\A");
                } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                    parameters = parameters.replaceAll("—", "-");
                    parameters = parameters.replaceAll("\\\\n", "\\\\N");
                } else {
                    parameters = parameters.replaceAll("—", "-");
                }
                parameters = parameters.replaceAll("<r><n>","\r\n");
                parameters = parameters.replaceAll("<br>","\\\\n");
                String newContent = parameters;
                /*if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
                    flag = true;
                    newContent = newContent.replaceAll("\n", "<n>");
                    newContent = newContent.replaceAll("\r", "<r>");
                    iotBoardReleaseLog.setReleaseNewContent(newContent);
                    iotBoardReleaseLog.setReleaseStatus("1");
                    iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                    //储存失败设备名称
                    failDevList.add(device.getEqName());
                    continue;
                }*/
                for (int g = 0;g < iotBoardVocabularies.size();g++) {
                    String word = iotBoardVocabularies.get(g).getWord();
                    if (parameters.contains(word)) {
                        flag = true;
                        //储存失败设备名称
                        failDevList.add(device.getEqName());
                        break;
                    }
                }
                if (flag) {
                    newContent = newContent.replaceAll("\n", "<n>");
                    newContent = newContent.replaceAll("\r", "<r>");
                    iotBoardReleaseLog.setReleaseNewContent("万集："+newContent);
                    iotBoardReleaseLog.setReleaseStatus("1");
                    iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                    return AjaxResult.error("发送的内容包含不恰当的关键字，请修改后重试！");
                }
                String commands = DataUtils.contentToGb2312_CG(deviceId, parameters, protocolType);
                Boolean result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, commands);
//                Boolean result = false;
                if (result) {
                    if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                        String XKcommands = "02 32 32 30 30 30 30 30 2E 78 6B 6C 7A 93 03";
                        result = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(deviceId, protocolType, XKcommands);
                        if (result) {
                            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                        } else {
                            ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                        }
                    } else {
                        ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
                    }
                    iotBoardReleaseLog.setReleaseStatus("0");
                } else {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "修改失败");
                    iotBoardReleaseLog.setReleaseStatus("1");
                }
                parameters = parameters.replaceAll("\n", "<n>");
                parameters = parameters.replaceAll("\r", "<r>");
                iotBoardReleaseLog.setReleaseNewContent("万集："+parameters);
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                releaseContentMap.clear();
            } catch (Exception e) {
                if (flag) {
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "发送的内容包含不恰当的关键字，请修改后重试！");
                } else {
                    parameters = parameters.replaceAll("\n", "<n>");
                    parameters = parameters.replaceAll("\r", "<r>");
                    iotBoardReleaseLog.setReleaseNewContent("万集："+parameters);
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "网络异常,发送失败");
                }
                iotBoardReleaseLog.setReleaseStatus("1");
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
            }
        }

        if(flag){
            ajaxResult = new AjaxResult(500, "设备离线");
        }

        return ajaxResult;
    }

    /**
     * 第三方控制广播
     * @param objectData
     * @return
     */
    @GetMapping("/commonControlPhone")
    public AjaxResult commonControlPhone(@RequestBody String objectData){
        if(wjGbModelNum == 0){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(objectData);
        PhoneSpkService phoneSpkService = SpringUtils.getBean(PhoneSpkService.class);
        //获取文件列表
        Map<String, Object> audioMap = new HashMap<>();
        audioMap.put("deviceId",jsonObject.getString("spkDeviceIds"));
        AjaxResult audioFileList = phoneSpkService.getAudioFileList(audioMap);
        List<Map> list = new ArrayList<>();
        if("200".equals(audioFileList.get("code").toString())){
            //list = (ArrayList)audioFileList.get("data");
            list = JSONObject.parseArray(audioFileList.get("data").toString(), Map.class);
        }
        List<String> fileName = new ArrayList<>();
        List<String> devId = new ArrayList<>();
        String fileNameData = "";
        if(jsonObject.getLongValue("eventTypeId") == 1){
            fileNameData = "正常情况";
        }
        for(Map<String, Object> item : list){
            if(fileNameData.equals(item.get("name").toString())){
                fileName.add(item.get("fileUrl").toString());
            }
        }
        devId.add(jsonObject.getString("spkDeviceIds"));
        map.put("fileNames",fileName);
        map.put("spkDeviceIds",devId);
        map.put("controlType","0");
        map.put("operIp",jsonObject.getString("operIp"));
        map.put("tunnelId",jsonObject.getString("tunnelId"));
        //phoneSpkService.playVoice(map);
        return AjaxResult.success(map);
    }

    /**
     * 对外情报板广播开启关闭
     * @param wjModel
     * @return
     */
    @GetMapping("/setWjModel/{wjModel}")
    public AjaxResult setWjModel(@PathVariable("wjModel") int wjModel){
        wjModelNum = wjModel;
        //wjGbModelNum = wjModel;
        return AjaxResult.success();
    }
}
