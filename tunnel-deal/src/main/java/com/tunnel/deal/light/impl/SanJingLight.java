package com.tunnel.deal.light.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDeviceData;
import com.tunnel.business.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.*;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.light.HttpUrlEscapeUtil;
import com.tunnel.deal.light.Light;
import com.tunnel.deal.light.enums.SanjingLightStateEnum;
import com.zc.common.core.ThreadPool.ThreadPool;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class SanJingLight implements Light, GeneralControlBean {

    private static final Logger logger = LoggerFactory.getLogger(SanJingLight.class);

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ITunnelAssociationService tunnelAssociationService;

    @Autowired
    private IExternalSystemService externalSystemService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private ISdDeviceDataService sdDeviceDataService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

    @Autowired
    private RedisCache redisCache;


    /**
     * 登录获取会话ID
     * 用户名/密码默认是：admin/admin123
     *
     * @return
     * @throws IOException
     */
    public String login(String username, String password, String baseUrl) {
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
        //设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        //特殊字符转义
        password = HttpUrlEscapeUtil.escape(password);
        // http://47.119.189.80:8888/login
        String url = baseUrl + "/login?username=" + username + "&password=" + password;
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            String code = jsonObject.getString("code");
//            String msg = jsonObject.getString("msg");
            if(String.valueOf(HttpStatus.ERROR).equals(code)){
                logger.error("获取照明token报错：",responseBody);
            }else{
                List<String> headers = response.headers("Set-Cookie");
                String jessionIdStr = headers.get(0);
                if(jessionIdStr != null && !"".equals(jessionIdStr)){
                    String[] array = jessionIdStr.split(";");
                    return array.length > 0 ? array[0] : "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("请求三晶照明token报错：",e.getMessage());
            return "";
        }

        return "";
    }

    @Override
    public int setBrightness(String deviceId, Integer bright) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String eqTunnelId = device.getEqTunnelId();
        String eqDirection = device.getEqDirection();
        Long externalSystemId = device.getExternalSystemId();
        String step = device.getExternalDeviceId();
        Assert.hasText(eqTunnelId, "未配置该设备所属隧道");
        Assert.hasText(eqDirection, "未配置该设备所属方向");
        Assert.notNull(externalSystemId, "未配置该设备关联的外部系统");
        // Assert.hasText(step, "未配置该设备关联的段号");
        Assert.hasText(step, "该设备暂时不可控！");

        //确定隧道洞编号
        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");
        String jessionId;
        try {
            String tokenKey = "control:sanJingLighttoken";
            jessionId = redisCache.getCacheObject(tokenKey);
            if(jessionId == null){
                jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
                redisCache.setCacheObject(tokenKey,jessionId);
                redisCache.expire(tokenKey,15*60);
            }
        } catch (Exception e) {
            logger.error("获取token异常，请联系管理员。");
            return 0;
        }
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        // 示例 "tunnelId=2&step=0&bright=98"
        String url = baseUrl + "/api/adjustBrightness";
        String content = "tunnelId=" + externalSystemTunnelId + "&step=" + step + "&bright=" + bright;
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", jessionId)
                .build();

        String responseBody = "";
        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
            logger.info("responseBody：{}",responseBody);
        } catch (IOException e) {
            logger.error("加强照明调光功能异常，请联系管理员。");
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }

    @Override
    public int lineControl(String deviceId, Integer openClose, Integer brightness) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);

        String eqTunnelId = device.getEqTunnelId();
        String eqDirection = device.getEqDirection();
        Long externalSystemId = device.getExternalSystemId();
        String step = device.getExternalDeviceId();
        Assert.hasText(eqTunnelId, "未配置该设备所属隧道");
        Assert.hasText(eqDirection, "未配置该设备所属方向");
        Assert.notNull(externalSystemId, "未配置该设备关联的外部系统");
        Assert.hasText(step, device.getEqName() + "未配置该设备关联的段号");

        //确定隧道洞编号
        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");


//        String jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        String jessionId;

        String tokenKey = "control:sanJingLighttoken";

        jessionId = redisCache.getCacheObject(tokenKey);

        if(jessionId == null){
            jessionId =  login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
            redisCache.setCacheObject(tokenKey,jessionId);
            redisCache.expire(tokenKey,15*60);
        }

        if(jessionId == null || "".equals(jessionId)){
            return 0;
        }
        //开关对应关系
        openClose = SanjingLightStateEnum.getValue(openClose);

        //开关
        int switchType = updateSwitch(jessionId, baseUrl, externalSystemTunnelId, step, openClose);
        //亮度
        int brightnessType = 1;
        //如果亮度有值并且控制状态不是关，就控制亮度
        if(brightness != null && !Objects.equals(SanjingLightStateEnum.CLOSE.getState(), openClose)){
            brightnessType = updateBrightness(jessionId, baseUrl, externalSystemTunnelId, step ,brightness);
        }
        return switchType==1 && brightnessType==1 ? 1 : 0;
    }


    public int lineControlAddLog(String deviceId, Integer openClose, Integer brightness) {
        SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);
        //推送照明指令
//        int resultStatus = lineControl(deviceId, openClose,brightness);
        int resultStatus = 1;
        // 如果控制成功
        if (resultStatus == 1) {
            //更新设备状态
            device.setEqStatus("1");
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            Integer deviceStatus = null;
            if( DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(device.getEqType())){
                deviceStatus = DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode();
            }else if( DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(device.getEqType())){
                deviceStatus = DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode();
            }
            //更新设备实时数据
            updateDeviceData(device, String.valueOf(openClose), deviceStatus);
        }

        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(device.getEqType());
        sdOperationLog.setTunnelId(device.getEqTunnelId());
        sdOperationLog.setEqId(device.getEqId());
        sdOperationLog.setOperationState(String.valueOf(openClose));
        sdOperationLog.setControlType(DeviceControlTypeEnum.AUTO_EXEC.getCode());
        sdOperationLog.setCreateTime(new Date());
        try {
            sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sdOperationLog.setState(String.valueOf(resultStatus));
        // 确定设备之前的开关状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(deviceId);


        if (String.valueOf(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode()).equals(device.getEqType())) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode()));
        } else if (String.valueOf(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode()).equals(device.getEqType())) {
            sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode()));
        }
        List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (brightness != null && null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
            sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
        }
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return resultStatus;
    }

    /**
     * 批量控制等亮度方法
     * @param deviceIds  eqId 设备ID
     * @param bright 亮度值
     * @param controlType 操作方式
     * @param operIp IP地址
     */
    @Override
    public void setBrightnessByList(List<String> deviceIds, Integer bright, String controlType, String operIp) {
        for (String deviceId:deviceIds ) {
            ThreadPool.executor.execute(()->{
                SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);
                int resultStatus = setBrightness(deviceId,bright);
                // 如果控制成功
                if (resultStatus == 1) {
                    // 更新设备在线状态
                    device.setEqStatus("1");
                    device.setEqStatusTime(new Date());
                    sdDevicesService.updateSdDevices(device);
                    //更新设备实时数据
                    sdDeviceDataService.updateDeviceData(device, String.valueOf(bright), Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode()));
                }
                //添加操作日志
                SdOperationLog sdOperationLog = new SdOperationLog();
                sdOperationLog.setEqTypeId(device.getEqType());
                sdOperationLog.setTunnelId(device.getEqTunnelId());
                sdOperationLog.setEqId(device.getEqId());
                sdOperationLog.setOperationState(String.valueOf(bright));
                sdOperationLog.setControlType(controlType);
                sdOperationLog.setCreateTime(new Date());
                sdOperationLog.setOperIp(operIp);
                sdOperationLog.setState(String.valueOf(resultStatus));
                // 确定设备之前亮度值
                SdDeviceData sdDeviceData = new SdDeviceData();
                sdDeviceData.setDeviceId(deviceId);
                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode()));
                List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
                if (null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
                    sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
                }
                sdOperationLogService.insertSdOperationLog(sdOperationLog);
            });
        }
    }

    /**
     * 控制灯开关方法
     * @param jessionId cookit
     * @param baseUrl 外部系统地址
     * @param externalSystemTunnelId  隧道洞编号
     * @param step 关联的段号
     * @param openClose 开关
     * @return
     */
    public int updateSwitch(String jessionId ,String baseUrl ,String externalSystemTunnelId, String step , Integer openClose) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
//        if (openClose == 2) {
//            openClose = 0;
//        }
        String url = baseUrl + "/api/lineControl?tunnelId=" + externalSystemTunnelId + "&step=" + step + "&openClose=" + openClose;
        // String url = lineControl + "?tunnelId=" + externalSystemTunnelId + "&step=" + step + "&openClose=" + openClose;
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Cookie", jessionId)
                .build();
        //调用开关返回参数
        String responseBody = "";
        //调用调节亮度返回参数
        String responseLuminanceBody = "";

        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }

    /**
     * 控制灯亮度方法
     * @param jessionId  cookit
     * @param baseUrl 外部系统地址
     * @param deviceId 隧道洞编号
     * @param step 关联的段号
     * @param bright 亮度
     * @return
     */
    public int updateBrightness(String jessionId ,String baseUrl ,String deviceId,String step , Integer bright) {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        // 示例 "tunnelId=2&step=0&bright=98"
        String url = baseUrl + "/api/adjustBrightness";
        String content = "tunnelId=" + deviceId + "&step=" + step + "&bright=" + bright;
        RequestBody body = RequestBody.create(mediaType, content);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cookie", jessionId)
                .build();

        String responseBody = "";
        try {
            Response response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }
        return responseBody.contains("发送成功") ? 1 : 0;
    }


    /**
     *
     * @param deviceIds         设备集合
     * @param oldLuminanceRange         原有亮度值
     * @param luminanceRange            亮度值
     * @param controlType       控制方式     3：手动 1：时间控制 2：光强控制
     * @param operIp            操作地址
     */
    public void setBrightnessByList(List<SdDevices> deviceIds, Integer oldLuminanceRange, Integer luminanceRange, String controlType, String operIp) {
        for (SdDevices device:deviceIds ) {
            int resultStatus = 0;
            try{
                resultStatus = setBrightness(device.getEqId(),luminanceRange);
            }catch (Exception e){
                resultStatus = 0;
            }
            // 如果控制成功
            if (resultStatus == 1) {
                // 更新设备在线状态
                device.setEqStatus("1");
                device.setEqStatusTime(new Date());
                sdDevicesService.updateSdDevices(device);
                //更新设备实时数据
                sdDeviceDataService.updateDeviceData(device, String.valueOf(luminanceRange), Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode()));
                redisCache.setCacheObject("control:"+device.getEqTunnelId()+"_"+device.getEqId()+"_LuminanceRange",luminanceRange);
            }else{
                redisCache.deleteObject("control:"+device.getEqTunnelId()+"_"+device.getEqId()+"_LuminanceRange");
            }
            //添加操作日志
            SdOperationLog sdOperationLog = new SdOperationLog();
            sdOperationLog.setEqTypeId(device.getEqType());
            sdOperationLog.setTunnelId(device.getEqTunnelId());
            sdOperationLog.setEqId(device.getEqId());
            sdOperationLog.setOperationState(String.valueOf(luminanceRange));
            sdOperationLog.setControlType(controlType);
            sdOperationLog.setCreateTime(new Date());
            try {
                sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            sdOperationLog.setState(String.valueOf(resultStatus));
            sdOperationLog.setBeforeState(device.getData());
            sdOperationLogService.insertSdOperationLog(sdOperationLog);
        }
    }


    /**
     * 单个设备调光
     * @param device
     * @param luminanceRange
     * @param controlType
     * @return
     */
    public int setBrightnessByDevice(SdDevices device,  Integer luminanceRange, String controlType) {
        int resultStatus;
        try{
            resultStatus = setBrightness(device.getEqId(),luminanceRange);
        }catch (Exception e){
            resultStatus = 0;
        }
        // 如果控制成功
        if (resultStatus == 1) {
            // 更新设备在线状态
            device.setEqStatus("1");
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            //更新设备实时数据
            sdDeviceDataService.updateDeviceData(device, String.valueOf(luminanceRange), Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode()));
            //更新redis缓存
            String redisLuminanceRangeKey = "control:"+device.getEqId()+"_LuminanceRange";
            redisCache.setCacheObject(redisLuminanceRangeKey,luminanceRange);
        }
        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(device.getEqType());
        sdOperationLog.setTunnelId(device.getEqTunnelId());
        sdOperationLog.setEqId(device.getEqId());
        sdOperationLog.setOperationState(String.valueOf(luminanceRange));
        sdOperationLog.setControlType(controlType);
        sdOperationLog.setCreateTime(new Date());
        try {
            sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sdOperationLog.setState(String.valueOf(resultStatus));
        sdOperationLog.setBeforeState(device.getData());
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return resultStatus;
    }


    /**
     * 单个设备调光
     * @param device
     * @param nowLuminanceRange    原有亮度
     * @param luminanceRange    当前亮度
     * @param controlType
     * @return
     */
    public int setBrightnessByDevice(SdDevices device,Integer nowLuminanceRange ,Integer luminanceRange, String controlType) {
        int resultStatus;
        try{
//            resultStatus = setBrightness(device.getEqId(),luminanceRange);
            resultStatus = 1;
        }catch (Exception e){
            e.printStackTrace();
            resultStatus = 0;
        }
        // 如果控制成功
        if (resultStatus == 1) {
            //设备类型
            Integer typeItem = null;
            if(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(device.getEqType())){
                typeItem = DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode();
            }else if(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(device.getEqType())){
                typeItem = DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode();
            }
            // 更新设备在线状态
            device.setEqStatus(DevicesStatusEnum.DEVICE_ON_LINE.getCode());
            device.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(device);
            //更新设备实时数据
            updateDeviceData(device, String.valueOf(luminanceRange), typeItem);
            //更新redis缓存
            String redisLuminanceRangeKey = "control:"+device.getEqId()+"_LuminanceRange";
            redisCache.setCacheObject(redisLuminanceRangeKey,luminanceRange);
        }
        //添加操作日志
        SdOperationLog sdOperationLog = new SdOperationLog();
        sdOperationLog.setEqTypeId(device.getEqType());
        sdOperationLog.setTunnelId(device.getEqTunnelId());
        sdOperationLog.setEqId(device.getEqId());
        sdOperationLog.setOperationState(String.valueOf(luminanceRange));
        sdOperationLog.setControlType(controlType);
        sdOperationLog.setCreateTime(new Date());
        try {
            sdOperationLog.setOperIp(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sdOperationLog.setState(String.valueOf(resultStatus));
        sdOperationLog.setBeforeState(nowLuminanceRange.toString());
        sdOperationLogService.insertSdOperationLog(sdOperationLog);
        return resultStatus;
    }

    public void updateDeviceData(SdDevices sdDevices, String value, Integer itemId) {
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        sdDeviceData.setItemId(Long.valueOf(itemId));
        List<SdDeviceData> deviceData = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
        if (deviceData.size() > 0) {
            SdDeviceData data = deviceData.get(0);
            data.setData(value);
            data.setUpdateTime(new Date());
            sdDeviceDataService.updateSdDeviceData(data);
        } else {
            sdDeviceData.setData(value);
            sdDeviceData.setCreateTime(new Date());
            sdDeviceDataService.insertSdDeviceData(sdDeviceData);
        }
    }

    /**
     * 获取token
     * @param username 用户名
     * @param password 密码
     * @param systemUrl 系统接口访问地址
     * @return
     */
    public String getCacheToken(String username,String password,String systemUrl){

        //token缓存key值
        String key = Constants.SANJING_LIGHT_TOKEN;
        //token有效时间15分钟
        Integer expireTime = 15;
        //获取缓存token
        String token = redisCache.getCacheObject(key);
        if(token == null || "".equals(token)){
            //缓存中获取不到token，重新从接口中获取，更新缓存
            token = login(username, password, systemUrl);
            redisCache.setCacheObject( key, token, expireTime, TimeUnit.MINUTES);
        }
        return token;
    }


    /**
     * 控制方法
     * @param map
     * @param sdDevices
     * @return
     */
    public AjaxResult controlLight(Map<String, Object> map, SdDevices sdDevices) {

        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //亮度
        String brightnessStr = Optional.ofNullable(map.get("brightness")).orElse("").toString();
        Integer brightness = Integer.valueOf(brightnessStr);
        //所属隧道
        String eqTunnelId = sdDevices.getEqTunnelId();
        //所属方向
        String eqDirection = sdDevices.getEqDirection();
        //设备关联的外部系统
        Long externalSystemId = sdDevices.getExternalSystemId();
        //段号
        String step = sdDevices.getExternalDeviceId();
        if(StringUtils.isEmpty(eqTunnelId)){
            return AjaxResult.error("未配置该设备所属隧道");
        }
        if(StringUtils.isEmpty(eqDirection)){
            return AjaxResult.error("未配置该设备所属方向");
        }
        if(externalSystemId == null){
            return AjaxResult.error("未配置该设备关联的外部系统");
        }
        if(StringUtils.isEmpty(step)){
            return AjaxResult.error(sdDevices.getEqName() + "未配置该设备关联的段号");
        }


        //确定隧道洞编号
        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
//        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");
        if(StringUtils.isEmpty(externalSystemTunnelId)){
            return AjaxResult.error("未配置该设备关联的隧道洞编号");
        }

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
//        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");
        if(StringUtils.isEmpty(baseUrl)){
            return AjaxResult.error("未配置该设备所属的外部系统地址");
        }

//        String jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        String jessionId;

        String tokenKey = "control:sanJingLighttoken";

        jessionId = redisCache.getCacheObject(tokenKey);

        if(jessionId == null || "".equals(jessionId)){
            jessionId =  login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
            redisCache.setCacheObject(tokenKey,jessionId);
            redisCache.expire(tokenKey,15*60);
        }

        if(jessionId == null || "".equals(jessionId)){
            return AjaxResult.error("三晶照明获取token失败",0);
        }
        //开关对应关系
        Integer openClose = SanjingLightStateEnum.getValue(Integer.valueOf(state));

        //开关
        int switchType = updateSwitch(jessionId, baseUrl, externalSystemTunnelId, step, openClose);
        //亮度
        int brightnessType = 1;
        //如果亮度有值并且控制状态不是关，就控制亮度
        if(!Objects.equals(SanjingLightStateEnum.CLOSE.getState(), openClose)){
            brightnessType = updateBrightness(jessionId, baseUrl, externalSystemTunnelId, step ,brightness);
        }
        int result =  switchType==1 && brightnessType==1 ? 1 : 0;
        if(result == 1){
            return AjaxResult.success(1);
        }else {
            return AjaxResult.error("控制失败",0);
        }
    }


    /**
     * 控制方法
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices){
        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启
            return commonControlService.excecuteAnalogControl(sdDevices,map);
        }
        Integer controlState = 0;

//        //控制设备之前获取设备状态
        //基本照明、加强照明多个设备类型数据项，暂时不存储控制前状态beforeState todo
//        String beforeState = commonControlService.selectBeforeState(sdDevices);

        AjaxResult ajaxResult = controlLight(map,sdDevices);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }

        commonControlService.addOperationLog(map,sdDevices,"",controlState);
        return AjaxResult.success(controlState);
    }


    /**
     * 设备控制
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map){

        int controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();

        String devId = map.get("devId").toString();
//        String state = map.get("state").toString();
//        String controlType = map.get("controlType").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        //控制照明设备
        if (!isopen) {
//            controlState = lightService.lineControl(devId, Integer.parseInt(state),brightness);
            //设备控制
            AjaxResult ajaxResult = controlLight(map,sdDevices);
            Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
            if( code == HttpStatus.SUCCESS){
                controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
            }
        } else {
            //模拟控制
            controlState = analogControl(map,sdDevices);
        }

        addOperationLog(map,sdDevices,controlState);

        return controlState;
    }


    /**
     * 模拟控制方法
     *
     * @param map
     * @param sdDevices
     * @return
     */
//    @Override
    public Integer analogControl(Map<String, Object> map, SdDevices sdDevices) {
        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //亮度
        String brightnessStr = Optional.ofNullable(map.get("brightness")).orElse("").toString();
        Integer brightness = Integer.valueOf(brightnessStr);

        //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
        sdDevices.setEqStatus("1");
        sdDevices.setEqStatusTime(new Date());
        sdDevicesService.updateSdDevices(sdDevices);
        SdDeviceTypeItem sdDeviceTypeItem = new SdDeviceTypeItem();
        sdDeviceTypeItem.setDeviceTypeId(sdDevices.getEqType());
        List<SdDeviceTypeItem> sdDeviceTypeItems = sdDeviceTypeItemService.selectSdDeviceTypeItemList(sdDeviceTypeItem);
        if (sdDeviceTypeItems.size() == 0) {
            throw new RuntimeException("当前设备没有设备类型数据项数据，请添加后重试！");
        }
        sdDeviceTypeItems.stream().forEach(item -> {
            if("brightness".equals(item.getItemCode()) &&
                    (DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().equals(sdDevices.getEqType()) ||
                            DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())
                    )
            ){
                sdDeviceDataService.updateDeviceData(sdDevices, brightness.toString(), item.getId());
            }
            if("state".equals(item.getItemCode())){
                sdDeviceDataService.updateDeviceData(sdDevices, state, item.getId());
            }
        });

        Integer controlState = 1;
        return controlState;
    }

    public void addOperationLog(Map<String, Object> map, SdDevices sdDevices,Integer controlState){

        String state = map.get("state").toString();
        String controlType = map.get("controlType").toString();

        //获取当前设备状态
        SdDeviceData sdDeviceData = new SdDeviceData();
        sdDeviceData.setDeviceId(sdDevices.getEqId());
        List<SdDeviceData> data = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);

        //添加操作记录
        SdOperationLog sdOperationLog = new SdOperationLog();

        //部份设备未接入，无法正确获取设备控制结果，默认失败
        sdOperationLog.setState("0");

        sdOperationLog.setEqTypeId(sdDevices.getEqType());
        sdOperationLog.setTunnelId(sdDevices.getEqTunnelId());
        sdOperationLog.setEqId(sdDevices.getEqId());
        sdOperationLog.setOperationState(state);
        sdOperationLog.setControlType(controlType);
        if (null != map.get("operIp")) {
            sdOperationLog.setOperIp(map.get("operIp").toString());
        }
        if (null != map.get("controlTime")) {
            sdOperationLog.setCreateTime(DateUtils.parseDate(map.get("controlTime")));
        }else{
            sdOperationLog.setCreateTime(new Date());
        }
        if (null != map.get("eventId")) {
            sdOperationLog.setEventId(map.get("eventId").toString());
        }

//        照明有多个设备数据项，获取列表中第一个可能会有bug
        if (data.size() > 0) {
            sdOperationLog.setBeforeState(data.get(0).getData());
        }

        Integer brightness = 0;
        // 加强照明，可设置照明亮度
        if(sdDevices.getEqType().longValue() == DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode().longValue()
                || sdDevices.getEqType().longValue() == DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().longValue()
        ){
            //亮度值
            if(map.get("brightness") != null){
                brightness = Integer.parseInt(map.get("brightness").toString());
                sdOperationLog.setDescription(brightness+"");
            }

            String operationStateStr = "1".equals(state) ?"开启":"关闭";
            operationStateStr += "，亮度："+brightness + "%";
            sdOperationLog.setOperationState(operationStateStr);
        }else{
            sdOperationLog.setOperationState(state);
        }

        sdOperationLog.setState(String.valueOf(controlState));

        sdOperationLogService.insertSdOperationLog(sdOperationLog);

    }




}
