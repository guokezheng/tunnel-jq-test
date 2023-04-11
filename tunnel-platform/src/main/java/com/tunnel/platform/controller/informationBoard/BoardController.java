package com.tunnel.platform.controller.informationBoard;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysDeptService;
import com.tunnel.business.datacenter.domain.enumeration.DevicesStatusEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.informationBoard.*;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.informationBoard.*;
import com.tunnel.business.utils.exception.BusinessException;
import com.tunnel.platform.business.vms.core.IDeviceProtocol;
import com.tunnel.platform.business.vms.device.DataUtils;
import com.tunnel.platform.business.vms.device.DeviceManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

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

    public static Map<String, List<String>> resultMap = new HashMap<String, List<String>>();//返回内容存储
    public static Map<String, String> releaseContentMap = new HashMap<String, String>();//发布内容存储
    public static Map<String, List<String>> modifyResultMap = new HashMap<String, List<String>>();//返回修改内容存储
    private static int fontSizeList[] = {64, 48, 40, 32, 24, 16};//字体大小集合
    public static Map<String, Object> nowContentMap = new HashMap<String, Object>();
    @Autowired
    private ISdIotDeviceService sdIotDeviceService;
    @Autowired
    private IIotBoardReleaseLogService iIotBoardReleaseLogService;
    @Autowired
    private IIotBoradFontService iIotBoradFontService;
    private ExecutorService executorService;
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

    /**
     *
     * 1分钟检测一次情报板的状态
     */
    @Scheduled(fixedRate = 60000 * 5)
    public void getIcyData() {

    	List<SdIotDevice> list = sdIotDeviceService.selectIotDeviceList(new SdIotDevice());
    	for(int i=0;i<list.size();i++){
    		SdIotDevice iotDevice = list.get(i);
            Long deviceId = iotDevice.getDeviceId();
            SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(deviceId);
            try {
                DeviceManagerFactory.getInstance().getDeviceDisplayListByDeviceId(String.valueOf(iotDevice.getDeviceId()));
                iotDevice.setDeviceStatus("0");
                sdIotDeviceService.updateIotDevice(iotDevice);
                device.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
                device.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(device);
            } catch (Exception e) {
            	iotDevice.setDeviceStatus("1");
    			sdIotDeviceService.updateIotDevice(iotDevice);
                device.setEqStatus(DevicesStatusEnum.DEVICE_OFF_LINE.getCode());
                device.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(device);
            }
    	}

    }


    /**
     * 获取单条情报板实时信息
     *
     * @return
     */
    @GetMapping("/getBoardContent")
    @ResponseBody
    public AjaxResult loadRealtimeInf(Long deviceId) {
        SdDevices device = sdDevicesService.getDeviceByAssociationDeviceId(deviceId);
        if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
            return null;
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
        if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
            return null;
        }
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
            return null;
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
                if (device.getEqStatus() != null && device.getEqStatus().equals(DevicesStatusEnum.DEVICE_OFF_LINE.getCode())) {
                    flag = true;
                    newContent = newContent.replaceAll("\n", "<n>");
                    newContent = newContent.replaceAll("\r", "<r>");
                    iotBoardReleaseLog.setReleaseNewContent(newContent);
                    iotBoardReleaseLog.setReleaseStatus("1");
                    iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                    continue;
                }
                for (int g = 0;g < iotBoardVocabularies.size();g++) {
                    String word = iotBoardVocabularies.get(g).getWord();
                    if (parameters.contains(word)) {
                        flag = true;
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
                    ajaxResult = new AjaxResult(HttpStatus.ERROR, "系统异常");
                }
                iotBoardReleaseLog.setReleaseStatus("1");
                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
            }
        }
        if (flag) {
            ajaxResult = new AjaxResult(HttpStatus.ERROR, "部分设备发布失败，请检查后重试！");
        }
        return ajaxResult;
    }


    /*
     * 创建线程池获取情报板内容信息并存储在缓存中
     * */
    @GetMapping("/addBoardCacheByDeviceid")
    @ResponseBody
    public void loadBoardContentAdd(Long deviceId) {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(20);//线程池
        }

        executorService.execute(new Task(deviceId));

    }

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
     * 线程任务
     */
    public class Task implements Runnable {
        Long deviceId;

        public Task(Long deviceId) {
            this.deviceId = deviceId;
        }

        @Override
        public void run() {
            try {
                List<String> list = new ArrayList<String>();
                Map<String, String> displayList = DeviceManagerFactory.getInstance().getDeviceDisplayListByDeviceId(String.valueOf(deviceId));
                String result = displayList.get("result");
                String protocolType = displayList.get("vender");
                String jsonContent = DataUtils.itemContentToJson(result, protocolType);
                list.add(protocolType);
                list.add(jsonContent);
                resultMap.put(String.valueOf(deviceId), list);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    /*
     * 根据设备编号删除BoardCache中的数据
     * */
    @GetMapping("/delBoardCacheByDeviceid")
    @ResponseBody
    public void loadBoardContentdelete(Long deviceId) {
        resultMap.remove(String.valueOf(deviceId));
    }

    /*
     * 清空BoardCache中的数据
     * */
    @GetMapping("/removeBoardCache")
    @ResponseBody
    public void removeResultMap() {
        resultMap.clear();
    }

    /**
     * 批量添加情报板     常用菜单
     *
     * @return
     */
    @GetMapping(value = "/releaseIntelligenceModule")
    @ResponseBody
    public AjaxResult releaseIntelligenceModule(String fFamily, String fColor, String fContent, String fTime) {
        fContent = fContent.replaceAll(" ", "");
        AjaxResult ajaxResult = new AjaxResult();
        List sblist = new ArrayList<>();
        List sblistlog = new ArrayList<>();//设备编号日志

        if (!resultMap.isEmpty()) {
            for (String key : resultMap.keySet()) {
                String releaseNewContent = "";//发布后的内容
                sblistlog.add(key);
                // 获取设备编号
                String fPixel = "000*000";
                SdIotDevice iotDevice = sdIotDeviceService.selectIotDeviceById(Long.valueOf(key));
                fPixel = iotDevice.getDevicePixel();
                List list = resultMap.get(key);
                String protocolType = (String) list.get(0);
                JSONArray contentObject = JSONArray.parseArray(list.get(1).toString());
                int contentSize = Integer.parseInt(String.valueOf(contentObject.size())) + 1;
                Map map = reorganizationMessage(protocolType, contentObject);
                StringBuffer Buffer = new StringBuffer(map.get("content").toString());
                String releaseOldContent = map.get("releaseOldContent").toString();
                String playList = map.get("playList").toString();
                String Item_Start = map.get("Item_Start").toString();
                String Item_Content = map.get("Item_Content").toString();
                String coordinate = centeredCoordinate(fContent, fPixel);
                List fontSizes = getFontSizes(fPixel, fContent);
                releaseNewContent = releaseOldContent + fContent;
                if (protocolType.startsWith(IDeviceProtocol.SANSI) || protocolType.startsWith(IDeviceProtocol.GUANGDIAN)) {
                    Buffer.append(Item_Content + String.valueOf(contentSize - 1) + "=" + fTime + ",1,1,\\C" + coordinate + "\\f" + DataUtils.fontTranslate(fFamily) + fontSizes.get(0).toString() + fontSizes.get(0).toString() + "\\c" + DataUtils.colorTranslate(fColor) + fontSizes.get(1));
                } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                    Buffer.append(Item_Content + String.valueOf(contentSize - 1) + "=" + String.valueOf(Integer.valueOf(fTime) / 100) + ",0,1,1,1,\\C" + coordinate + "\\F" + DataUtils.fontTranslate(fFamily) + fontSizes.get(0).toString() + "\\T" + DataUtils.colorTranslate(fColor) + "\\U" + fontSizes.get(1));
                } else if (protocolType.startsWith(IDeviceProtocol.TONGZHOU) || protocolType.startsWith(IDeviceProtocol.DIANMING)) {
                    Buffer.append(Item_Content + String.valueOf(contentSize - 1) + "=" + String.valueOf(Integer.valueOf(fTime) / 10) + ",0,0,0,0,\\C" + coordinate + "\\F" + DataUtils.fontTranslate(fFamily) + fontSizes.get(0).toString() + fontSizes.get(0).toString() + "\\T" + DataUtils.colorTranslate(fColor) + "\\W" + fontSizes.get(1));
                }

                String parameters = Buffer.toString();
                parameters = playList + Item_Start + String.valueOf(contentSize) + "\r\n" + parameters;
                parameters = parameters.replaceAll("—", "-");
                parameters = parameters.replaceAll("<br>", "\\\\n");
                parameters = parameters.replaceAll("<br>", "\n");
                if (protocolType.startsWith(IDeviceProtocol.DIANMING)) {
                    parameters = parameters.replaceAll("\\\\n", "\\\\A");
                } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                    parameters = parameters.replaceAll("\\\\n", "\\\\N");
                }
                String commands = DataUtils.contentToGb2312_CG(key, parameters, protocolType);
                Boolean values = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(key, protocolType, commands);
                if (!values) {
                    sblist.add(iotDevice.getPileNumber());
                }
//                String userName = (String) PermissionUtils.getPrincipalProperty("userName");
//                IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
//                iotBoardReleaseLog.setDeviceId(key);
//                iotBoardReleaseLog.setReleaseBy(userName);
//                iotBoardReleaseLog.setReleaseOldContent(releaseOldContent);
//                iotBoardReleaseLog.setReleaseNewContent(releaseNewContent);
//                iotBoardReleaseLog.setReleaseTime(new Date());
//                iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
            }
            resultMap.clear();
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "发布成功", sblist);
        } else {
            ajaxResult = new AjaxResult(HttpStatus.ERROR, "发布失败");
        }
        return ajaxResult;
    }

    /**
     * 修改并返回选中情报板实时信息
     *
     * @return
     */
    @RequestMapping(value = "/loadBoardContentUp")
    @ResponseBody
    public AjaxResult modifyloadRealtimeInfo() {
        Map<String, String> map = new HashMap<String, String>();
        AjaxResult ajaxResult = new AjaxResult();
        try {
            for (String key : resultMap.keySet()) {
                JSONObject _items = new JSONObject();
                List list = resultMap.get(key);
                String protocolType = list.get(0).toString();
                String val = list.get(1).toString();
                _items.put("support", DataUtils.getSupport(key, protocolType));
                _items.put("content", val);
                SdIotDevice iotDevice = sdIotDeviceService.selectIotDeviceById(Long.valueOf(key));
                String pileNumber = iotDevice.getPileNumber();
                _items.put("pileNumber", pileNumber);
                String devicePixel = iotDevice.getDevicePixel();
                if (devicePixel != null && devicePixel != " ") {
                    _items.put("devicePixel", devicePixel);
                } else {
                    _items.put("devicePixel", " ");
                }
                String result = _items.toString();
                map.put(key, result);
            }
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "查询成功", map);
        } catch (Exception e) {
            ajaxResult = new AjaxResult(HttpStatus.ERROR, "没有选择设备");
        }
        return ajaxResult;
    }

    /**
     * 修改并返回选中情报板实时信息(多选)
     *
     * @param
     * @return
     */
    @GetMapping(value = "/deletemodifyloadRealtimeInfos")
    @ResponseBody
    public AjaxResult deletemodifyloadRealtimeInfo(String content) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            for (String sb : resultMap.keySet()) {
                List<String> list = new ArrayList<String>();
                List deleteList = new ArrayList<>();
                List<String> res = resultMap.get(sb);
                String venderVersion = res.get(0);
                JSONArray contentObject = JSONArray.parseArray(res.get(1).toString());
                for (int i = 0; i < contentObject.size(); i++) {
                    Map contents = (Map) contentObject.get(i);
                    JSONArray item = (JSONArray) contents.get("ITEM00" + String.valueOf(i));
                    String ite = (String) ((JSONObject) item.get(0)).get("CONTENT");//文本
                    boolean contains = ite.contains(content);
                    if (contains) {
//                        contentObject.remove(i);
                        deleteList.add(i);
                    }
                }
                for (int i = 0; i < deleteList.size(); i++) {
                    contentObject.remove(deleteList.get(i));
                }
                for (int i = 0; i < contentObject.size(); i++) {
                    Map contents = (Map) contentObject.get(i);
                    for (Object key : contents.keySet()) {
                        contents.put("ITEM00" + String.valueOf(i), contents.get(key));
                        if (contents.size() > 1) {
                            contents.remove(key);
                        }
                    }
                }
                list.add(venderVersion);
                list.add(contentObject.toString());
                resultMap.put(sb, list);
            }
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "删除成功");
        } catch (Exception e) {
            ajaxResult = new AjaxResult(HttpStatus.ERROR, "删除失败");
        }

        return ajaxResult;
    }

    /**
     * 修改并返回选中情报板实时信息(单条)
     *
     * @param
     * @return
     */
    @GetMapping(value = "/deletemodifyloadRealtimeInfo")
    @ResponseBody
    public AjaxResult deletemodifyloadRealtimeInfo(String deviceid, String content) {
        AjaxResult ajaxResult = new AjaxResult();

        deviceid = deviceid.substring(7, 16);
        try {
            List<String> list = new ArrayList<String>();
            int deleteNum = 0;
            List<String> res = resultMap.get(deviceid);
            String venderVersion = res.get(0);
            JSONArray contentObject = JSONArray.parseArray(res.get(1).toString());
            for (int i = 0; i < contentObject.size(); i++) {
                Map contents = (Map) contentObject.get(i);
                JSONArray item = (JSONArray) contents.get("ITEM00" + String.valueOf(i));
                String ite = (String) ((JSONObject) item.get(0)).get("CONTENT");//文本
                boolean contains = ite.contains(content);
                if (contains) {
                    deleteNum = i;
                }
            }
            contentObject.remove(deleteNum);
            for (int i = 0; i < contentObject.size(); i++) {
                Map contents = (Map) contentObject.get(i);
                for (Object key : contents.keySet()) {
                    contents.put("ITEM00" + String.valueOf(i), contents.get(key));
                    if (contents.size() > 1) {
                        contents.remove(key);
                    }
                }
            }
            list.add(venderVersion);
            list.add(contentObject.toString());
            resultMap.put(deviceid, list);
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "删除成功");
        } catch (Exception e) {
            ajaxResult = new AjaxResult(HttpStatus.ERROR, "删除失败");
        }

        return ajaxResult;
    }

    /**
     * 修改并返回选中情报板实时信息
     *
     * @return
     */
    @GetMapping(value = "/keepModifyloadRealtimeInfo")
    @ResponseBody
    public AjaxResult keepModifyloadRealtimeInfo(String deviceId, String content, String protocolType) {
        Map<String, String> map = new HashMap<String, String>();
        deviceId = deviceId.substring(6, 15);
        List list = new ArrayList();
        list.add(protocolType);//存储设备得协议
        list.add(content);//存储发送的内容
        modifyResultMap.put(deviceId, list);
        AjaxResult ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "修改成功");
        return ajaxResult;
    }

    /**
     * 发布修改情报板实时信息
     *
     * @return
     */
    @RequestMapping(value = "/releaseModifyloadRealtimeInfo")
    @ResponseBody
    public AjaxResult releaseModifyloadRealtimeInfo() {
        AjaxResult ajaxResult = new AjaxResult();
        Map<String, String> map = new HashMap<String, String>();
        List<String> oldList = new ArrayList();//原设备编号
        List<String> newList = new ArrayList();//新设备编号
        List sblist = new ArrayList<>();

        for (String key : modifyResultMap.keySet()) {
            newList.add(key);
        }
        for (String key : resultMap.keySet()) {
            oldList.add(key);
        }
        oldList.removeAll(newList);//找出没有修改的设备编号
        //发送修改过的设备
        try {
            for (String key : newList) {
                List modifyList = modifyResultMap.get(key);
                List<String> strings = resultMap.get(key);
                String content = strings.get(1);
                String protocolType = (String) modifyList.get(0);
                String parameters = (String) modifyList.get(1);
                parameters = parameters.replaceAll("—", "-");
                if (protocolType.startsWith(IDeviceProtocol.DIANMING)) {
                    parameters = parameters.replaceAll("\\\\n", "\\\\A");
                } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                    parameters = parameters.replaceAll("\\\\n", "\\\\N");
                }
                String commands = DataUtils.contentToGb2312_CG(key, parameters, protocolType);
                Boolean values = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(key, protocolType, commands);
                if (values) {
                    sblist.add(key);
                } else {
//                    String userName = (String) PermissionUtils.getPrincipalProperty("userName");
//                    IotBoardReleaseLog iotBoardReleaseLog = new IotBoardReleaseLog();
//                    iotBoardReleaseLog.setDeviceId(key);
//                    iotBoardReleaseLog.setReleaseBy(userName);
//                    iotBoardReleaseLog.setReleaseOldContent(content);
//                    iotBoardReleaseLog.setReleaseNewContent(parameters);
//                    iotBoardReleaseLog.setReleaseTime(new Date());
//                    iIotBoardReleaseLogService.insertIotBoardReleaseLog(iotBoardReleaseLog);
                }
            }
            //发送没有修改的设备
            for (String key : oldList) {
                // 获取设备编号
                List list = resultMap.get(key);
                String protocolType = (String) list.get(0);
                JSONArray contentObject = JSONArray.parseArray(list.get(1).toString());
                Map mapData = reorganizationMessage(protocolType, contentObject);
                String playList = mapData.get("playList").toString();
                String Item_Start = mapData.get("Item_Start").toString();
                StringBuffer Buffer = new StringBuffer(mapData.get("content").toString());
                String parameters = Buffer.toString();
                parameters = playList + Item_Start + String.valueOf(contentObject.size()) + "\r\n" + parameters;
                parameters = parameters.replaceAll("—", "-");
                parameters = parameters.replaceAll("<br>", "\\\\n");
                parameters = parameters.replaceAll("<br>", "\n");
                if (protocolType.startsWith(IDeviceProtocol.DIANMING)) {
                    parameters = parameters.replaceAll("\\\\n", "\\\\A");
                } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                    parameters = parameters.replaceAll("\\\\n", "\\\\N");
                }
                String commands = DataUtils.contentToGb2312_CG(key, parameters, protocolType);
                Boolean values = DeviceManagerFactory.getInstance().controlDeviceByDeviceId(key, protocolType, commands);
                if (values) {
                    sblist.add(key);
                }
            }
            resultMap.clear();
            modifyResultMap.clear();
            ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "发布成功", sblist);


        } catch (Exception e) {
            ajaxResult = new AjaxResult(HttpStatus.ERROR, "发布失败", sblist);
        }
        return ajaxResult;
    }

    /**
     * 获取居中情况情报板的坐标值
     *
     * @param content   情报板内容
     * @param boardSize 情报板长度宽度
     * @return 情报板的坐标
     */
    private String centeredCoordinate(String content, String boardSize) {
        if (boardSize == null || "".equals(boardSize)) {
            return "000000";
        }
        // 根据情报板长宽来设定初始字体大小
//        int fontSize = getFontSize(boardSize);
        // 情报板坐标
        String coordinate = "000000";

        // 获取文字长度
        int length = content.length();

        List fontSizes = getFontSizes(boardSize, content);
        int fontSize = Integer.valueOf(fontSizes.get(0).toString());
        String contents = fontSizes.get(1).toString();
        int rowLength = 1;
        if (contents != null && contents.length() > 0) {
            rowLength = contents.split("\\\\n").length;
        }

        // 如果存在换行符，则根据换行符的位置读取
        if (rowLength >= 1) {
            // 根据最长的一行来决定居中的位置
            int maxI = 0;
            for (int i = 0; i < rowLength; i++) {
                int nowLength = contents.split("\\\\n")[i].length();
                if (nowLength > maxI) {
                    maxI = nowLength;
                }
            }
            length = maxI;
        }
        int divWidth = length * Integer.valueOf(fontSize);
        // 情报板的长度
        int boardWidth = 0;
        int boardHeight = 0;
        if (boardSize != null && boardSize.length() > 0) {
            boardWidth = Integer.parseInt(boardSize.split("\\*")[0]);
            boardHeight = Integer.parseInt(boardSize.split("\\*")[1]);
        }
        // 左右居中
        int coorX = (boardWidth - divWidth) / 2;
        // 上下居中
        int divHeight = Integer.valueOf(fontSize) * rowLength;
        int coorY = (boardHeight - divHeight) / 2;
        coordinate = formatNum(coorX, 3) + formatNum(coorY, 3);
        return coordinate;
    }


    private static List getFontSizes(String boardSize, String content) {

        int fontSize;//字体大小
        int contentLength = content.length();//文本长度
        int lineNum;//行数
        String contents;//换行后的文本

        List boardContent = new ArrayList<>();
        // 情报板的长度
        int boardWidth = 0;//长
        int boardHeight = 0;//宽
        if (boardSize != null && boardSize.length() > 0) {
            boardWidth = Integer.parseInt(boardSize.split("\\*")[0]);
            boardHeight = Integer.parseInt(boardSize.split("\\*")[1]);
        }
        for (int i = 0; 1 < fontSizeList.length; i++) {
            if (boardHeight >= fontSizeList[i]) { //判断高和字体大小
                if (boardWidth >= contentLength * fontSizeList[i]) {//判断长度和字体大小
                    fontSize = fontSizeList[i];
                    contents = content;
                    boardContent.add(fontSize);
                    boardContent.add(content);
                    break;
                } else if (boardHeight >= fontSizeList[i] * 2) {//判断高和两行字符
                    int contentsLineNum = 0;//每行的字数
                    for (int k = 1; k <= contentLength; k++) {
                        if (boardWidth >= fontSizeList[i] * (contentLength - k)) {
                            contentsLineNum = contentLength - k;
                            break;
                        }
                    }
                    if (boardWidth >= contentsLineNum * fontSizeList[i] && contentsLineNum * 2 >= contentLength) {//判断长度和换行字体
                        fontSize = fontSizeList[i];
                        StringBuilder stringBuilder = new StringBuilder(content);
                        if (contentLength % 2 <= contentsLineNum) {//如果第二行字数小与平均字数，那么就提前换行

                            contents = stringBuilder.insert(contentsLineNum, "\\n").toString();
                        } else {
                            contents = stringBuilder.insert((contentsLineNum / 2) + 1, "\\n").toString();
                        }
                        boardContent.add(fontSize);
                        boardContent.add(contents);
                        break;
                    } else if (boardHeight >= fontSizeList[i] * 3) {//判断高和三行字符
                        if (boardWidth >= contentsLineNum * fontSizeList[i] && contentsLineNum * 3 >= contentLength) {//判断长度和换行字体
                            fontSize = fontSizeList[i];
                            StringBuilder stringBuilder = new StringBuilder(content);
                            if (contentLength % 3 <= contentsLineNum / 2) {//如果第二行字数小与平均字数，那么就提前换行
                                contents = stringBuilder.insert(contentsLineNum, "\\n").toString();
                                contents = stringBuilder.insert((contentsLineNum * 2) + 2, "\\n").toString();
                            } else {
                                contents = stringBuilder.insert((contentsLineNum / 2) + 1, "\\n").toString();
                                contents = stringBuilder.insert(contentsLineNum + 2, "\\n").toString();
                            }
                            boardContent.add(fontSize);
                            boardContent.add(contents);
                            break;
                        } else if (boardHeight >= fontSizeList[i] * 4) {//判断高和三行字符
                            if (boardWidth >= contentsLineNum * fontSizeList[i] && contentsLineNum * 4 >= contentLength) {//判断长度和换行字体
                                fontSize = fontSizeList[i];
                                StringBuilder stringBuilder = new StringBuilder(content);
                                if (contentLength % 4 <= contentsLineNum / 2) {//如果第二行字数小与平均字数，那么就提前换行
                                    contents = stringBuilder.insert(contentsLineNum, "\\n").toString();
                                    contents = stringBuilder.insert((contentsLineNum * 2) + 2, "\\n").toString();
                                    contents = stringBuilder.insert((contentsLineNum * 3) + 4, "\\n").toString();
                                } else {

                                    contents = stringBuilder.insert((contentsLineNum / 2) + 1, "\\n").toString();
                                    contents = stringBuilder.insert(contentsLineNum + 2, "\\n").toString();
                                    contents = stringBuilder.insert((contentsLineNum * 2) + 4, "\\n").toString();
                                }
                                boardContent.add(fontSize);
                                boardContent.add(contents);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return boardContent;
    }


    /**
     * 不足某长度前面补0
     *
     * @param num    坐标
     * @param length 长度
     * @return
     */
    private static String formatNum(int num, int length) {
        if (num < 0) {
            num = 0;
        }
        return String.format("%0" + length + "d", num);
    }

    /**
     * 获取敏感词
     *
     * @return
     */
    @GetMapping("/getVerifyWord")
    @ResponseBody
    public AjaxResult getVerifyWord() {
        List<Map<String, String>> verifyWord = sdIotDeviceService.getVerifyWord();
        AjaxResult ajaxResult = new AjaxResult(HttpStatus.SUCCESS, "返回成功", verifyWord);
        return ajaxResult;
    }


    /**
     * 判断是否连接超时
     *
     * @param host
     * @param timeOut
     * @return
     */
    protected Boolean isHostReachable(String host, int timeOut) {
        // TODO Auto-generated method stub
        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是否ip及端口能否连接
     *
     * @param host
     * @param port
     * @return
     */
    protected Boolean isHostConnectable(String host, int port) {
        // TODO Auto-generated method stub
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 5000);
        } catch (IOException e) {
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

    /*
     * 重组发送情报板内容
     * */
    private Map reorganizationMessage(String protocolType, JSONArray contentObject) {
        String playList = "";//报文头
        String Item_Start = "";//起始数量
        String Item_Content = "";//内容
        String releaseOldContent = "";//发布前的内容
        StringBuffer Buffer = new StringBuffer("");
        Map<Object, Object> map = new HashMap<>();
        if (protocolType.startsWith(IDeviceProtocol.SANSI) || protocolType.startsWith(IDeviceProtocol.GUANGDIAN)) {
            playList = "[Playlist]\r\n";
            Item_Start = "ITEM_NO=";
            Item_Content = "ITEM";
        } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
            playList = "[LIST]\r\n";
            Item_Start = "ItemCount=00";
            Item_Content = "Item0";
        } else if (protocolType.startsWith(IDeviceProtocol.TONGZHOU) || protocolType.startsWith(IDeviceProtocol.DIANMING)) {
            playList = "[PLAYLIST]\r\n";
            Item_Start = "ITEM_NO=00";
            Item_Content = "ITEM00";
        }
        for (int k = 0; k < contentObject.size(); k++) {
            Map contents = (Map) contentObject.get(k);
            JSONArray item = (JSONArray) contents.get("ITEM00" + String.valueOf(k));
            String speed = (String) ((JSONObject) item.get(0)).get("SPEED");//速度
            String fontSize = (String) ((JSONObject) item.get(0)).get("FONT_SIZE");//字体大小
            String action = (String) ((JSONObject) item.get(0)).get("ACTION");//入屏方式
            String color = (String) ((JSONObject) item.get(0)).get("COLOR");//颜色
            String content = (String) ((JSONObject) item.get(0)).get("CONTENT");//文本
            String coordinate = (String) ((JSONObject) item.get(0)).get("COORDINATE");//起始坐标
            String font = (String) ((JSONObject) item.get(0)).get("FONT");//字体
            String stay = (String) ((JSONObject) item.get(0)).get("STAY");//停留时间
            releaseOldContent = releaseOldContent + content + ";";
            if (protocolType.startsWith(IDeviceProtocol.SANSI) || protocolType.startsWith(IDeviceProtocol.GUANGDIAN)) {
                Buffer.append(Item_Content + String.valueOf(k) + "=" + stay + "," + action + "," + speed + ",\\C" + coordinate + "\\f" + DataUtils.fontTranslate(font) + fontSize + "\\c" + DataUtils.colorTranslate(color) + content + "\r\n");
            } else if (protocolType.startsWith(IDeviceProtocol.XIANKE)) {
                Buffer.append(Item_Content + String.valueOf(k) + "=" + stay + "," + action + "," + "0,1," + speed + ",\\C" + coordinate + "\\F" + DataUtils.fontTranslate(font) + fontSize + "\\T" + DataUtils.colorTranslate(color) + "\\U" + content + "\r\n");

            } else if (protocolType.startsWith(IDeviceProtocol.TONGZHOU) || protocolType.startsWith(IDeviceProtocol.DIANMING)) {
                Buffer.append(Item_Content + String.valueOf(k) + "=" + stay + "," + action + ",0,0," + speed + ",\\C" + coordinate + "\\F" + DataUtils.fontTranslate(font) + fontSize + "\\T" + DataUtils.colorTranslate(color) + "\\W" + content + "\r\n");

            }
        }
        map.put("content", Buffer);
        map.put("releaseOldContent", releaseOldContent);
        map.put("playList", playList);
        map.put("Item_Start", Item_Start);
        map.put("Item_Content", Item_Content);
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

    private String getBoardFontSize(String boardPixel, String content) {
        String fontSize = "32";
        try {
            IotBoradFont iotBoradFont = new IotBoradFont();
            iotBoradFont.setDevicePixel(boardPixel);
            Long contentLength = Long.valueOf(content.length());
            List<IotBoradFont> iotBoradFonts = iIotBoradFontService.selectIotBoradFontList(iotBoradFont);
            for (int i = 0; i < iotBoradFonts.size(); i++) {
                IotBoradFont iotBoradFont1 = iotBoradFonts.get(i);
                Long maxLength = iotBoradFont1.getMaxLength();
                if (contentLength <= maxLength) {
                    fontSize = String.valueOf(iotBoradFont1.getFontSize());
                    break;
                }
            }
        } catch (Exception e) {
            fontSize = "32";
        }
        return fontSize;
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
}
