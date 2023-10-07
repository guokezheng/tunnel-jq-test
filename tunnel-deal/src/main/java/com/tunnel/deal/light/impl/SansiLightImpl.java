package com.tunnel.deal.light.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
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
import com.tunnel.deal.guidancelamp.protocol.StringUtil;
import com.tunnel.deal.light.Light;
import com.tunnel.deal.light.enums.SanjingLightStateEnum;
import com.zc.common.core.ThreadPool.ThreadPool;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class SansiLightImpl implements Light , GeneralControlBean {

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
    public RedisTemplate redisTemplate;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISdDeviceTypeItemService sdDeviceTypeItemService;

    private static final Logger logger = LoggerFactory.getLogger(SanJingLight.class);

    @Autowired
    private ISdDevicesService devicesService;


    /**
     * 登录获取会话ID
     * 用户名/密码默认是：admin/admin123
     *
     * @return
     * @throws IOException
     */
    public String login(String username, String password, String baseUrl) {
        Response response = null;
        try {
            byte[] textByte = password.getBytes("UTF-8");
            String encode = new BASE64Encoder().encode(textByte);
            String data = "{\"username\":\"" + username + "\",\"password\":\"" + encode + "\"}";
            OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //强行返回true 即验证成功
                    return true;
                }
            }).build();
//            OkHttpClient client = new OkHttpClient().newBuilder()
//                    .hostnameVerifier((hostname, session) -> true)
//                    .build();
            MediaType mediaType=MediaType.parse("application/json");
            RequestBody body=RequestBody.create(mediaType, data);
            Request request=new Request.Builder().url(baseUrl+"api/core/users/login").post(body).build();
            response=client.newCall(request).execute();
            JSONObject jo = JSONObject.parseObject(  response.body().string());
            return  jo.getString("id");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }finally{
            if(response != null){
                response.close();
            }
        }
    }

    /**
     * 三思调光方法
     * @param deviceId
     * @param bright
     * @return
     */
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
//        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
//        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");

        //获取三思Cookie
        String jessionId = loginRedis(externalSystem, baseUrl);
        return updateSwitch(jessionId, baseUrl, step, bright);
    }

    /**
     * 照明 灯开关 控制
     * @param deviceId
     * @param openClose
     * @return
     */
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
        Assert.hasText(step, "未配置该设备关联的段号");

        //确定隧道洞编号
        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");
        //获取三思Cookie
        String jessionId = loginRedis(externalSystem, baseUrl);
        //灯开关
        int switchType = updateSwitch(jessionId, baseUrl, baseUrl, openClose);
        //亮度
        int brightnessType = 0;
        //2表示关
        if(openClose!=2){
            brightnessType = updateBrightness(jessionId, baseUrl, step, openClose);
        }
        return switchType==1 && brightnessType==1 ? 1 : 0;
    }

    /**
     * token获取设置默认过期时间1天
     * @param externalSystem
     * @param baseUrl
     * @return
     */
    public String loginRedis( ExternalSystem externalSystem ,String baseUrl){
        String jessionId = "";
        String sanSiToken = (String)redisTemplate.opsForValue().get("sanSiToken");
        if(StringUtils.isNotEmpty(sanSiToken)){
            jessionId = sanSiToken;
        }else{
            jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
            if(StringUtils.isNotEmpty(jessionId)){
                //默认设置两小时
                redisTemplate.opsForValue().set("sanSiToken",jessionId,1, TimeUnit.DAYS);
            }
        }
        return jessionId;
    }
    /**
     * 控制灯开关方法
     * @param jessionId cookit
     * @param baseUrl 外部系统地址
     * @param step 关联的段号
     * @param openClose 开关
     * @return
     */
    public int updateSwitch(String jessionId ,String baseUrl , String step , Integer openClose) {
        //灯开关
        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //强行返回true 即验证成功
                return true;
            }
        }).build();
        MediaType mediaType = MediaType.parse("application/json");
        //传入的请求参数
        if (openClose == 2) {
            openClose = 0;
        }
        String data = "{\"param\":" + openClose + "}";
        RequestBody body = RequestBody.create(mediaType, data);


        String url = baseUrl + "api/core/assetGroups/"+step+"/action/device-sensor:switch-status";

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", jessionId)
                .build();

        String responseBody = "";
        Response response = null;
        try {
            response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }finally{
            if(response != null){
                response.close();
            }
        }
        System.out.print("控制回调"+responseBody.toString());
        return responseBody.contains("pipelineId") ? 1 : 0;
    }

    /**
     * 控制灯亮度方法
     * @param jessionId  cookit
     * @param baseUrl 外部系统地址
     * @param step 关联的段号
     * @param bright 亮度
     * @return
     */
    public int updateBrightness(String jessionId ,String baseUrl , String step , Integer bright) {

        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //强行返回true 即验证成功
                return true;
            }
        }).build();
        MediaType mediaType = MediaType.parse("application/json");
        //传入的请求参数
        String data = "{\"param\":" + bright + "}";
        RequestBody body = RequestBody.create(mediaType, data);
        String url = baseUrl + "api/core/assetGroups/"+step+"/action/device-sensor:dim-level";

        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization", jessionId)
                .build();

        String responseBody = "";
        Response response = null;
        try {
            response = client.newCall(request).execute();
            //包含“发送成功"就可以
            responseBody = response.body().string();
        } catch (IOException e) {
            return 0;
        }finally{
            if(response != null){
                response.close();
            }
        }
        return responseBody.contains("pipelineId") ? 1 : 0;
    }

//    /**
//     * 批量控制等亮度方法
//     * @param deviceIds  eqId 设备ID
//     * @param bright 亮度值
//     * @param controlType 操作方式
//     * @param operIp IP地址
//     */
//    @Override
//    public void setBrightnessByList(List<String> deviceIds, Integer bright, String controlType, String operIp) {
//        for (String deviceId:deviceIds ) {
//            ThreadPool.executor.execute(()->{
//                SdDevices device = sdDevicesService.selectSdDevicesById(deviceId);
//                int resultStatus = setBrightness(deviceId,bright);
//                // 如果控制成功
//                if (resultStatus == 1) {
//                    // 更新设备在线状态
//                    device.setEqStatus("1");
//                    device.setEqStatusTime(new Date());
//                    sdDevicesService.updateSdDevices(device);
//                    //更新设备实时数据
//                   sdDeviceDataService.updateDeviceData(device, String.valueOf(bright), Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode()));
//                }
//                //添加操作日志
//                SdOperationLog sdOperationLog = new SdOperationLog();
//                sdOperationLog.setEqTypeId(device.getEqType());
//                sdOperationLog.setTunnelId(device.getEqTunnelId());
//                sdOperationLog.setEqId(device.getEqId());
//                sdOperationLog.setOperationState(String.valueOf(bright));
//                sdOperationLog.setControlType(controlType);
//                sdOperationLog.setCreateTime(new Date());
//                sdOperationLog.setOperIp(operIp);
//                sdOperationLog.setState(String.valueOf(resultStatus));
//                // 确定设备之前亮度值
//                SdDeviceData sdDeviceData = new SdDeviceData();
//                sdDeviceData.setDeviceId(deviceId);
//                sdDeviceData.setItemId(Long.valueOf(DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode()));
//                List<SdDeviceData> sdDeviceDataList = sdDeviceDataService.selectSdDeviceDataList(sdDeviceData);
//                if (null != sdDeviceDataList && sdDeviceDataList.size() > 0) {
//                    sdOperationLog.setBeforeState(sdDeviceDataList.get(0).getData());
//                }
//                sdOperationLogService.insertSdOperationLog(sdOperationLog);
//            });
//        }
//    }

    /**
     * 控制方法
     * @param map
     * @param sdDevices
     * @return
     */
    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
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
        return ajaxResult;
    }
    /**
     * 设备控制+模拟控制--其他模块调用的统一控制方法
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {
        int controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();
        //根据字典中配置的设备模拟控制值进行模拟状态展示
        String devId = map.get("devId").toString();
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        //控制照明设备
        if (!isopen) {
            //设备控制
            AjaxResult ajaxResult = controlLight(map,sdDevices);
            Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
            if( code == HttpStatus.SUCCESS){
                controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
            }
            commonControlService.addOperationLog(map,sdDevices,"",controlState);
        } else {
            //模拟控制
            controlState = analogControl(map,sdDevices);
        }


        return controlState;
    }
    /**
     * 模拟控制方法
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


        //确定隧道洞编号  不确定虚报也需要
//        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
//        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");
//        if(StringUtils.isEmpty(externalSystemTunnelId)){
//            return AjaxResult.error("未配置该设备关联的隧道洞编号");
//        }
        //查询外部系统信息
        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
//        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");
        if(StringUtils.isEmpty(baseUrl)){
            return AjaxResult.error("未配置该设备所属的外部系统地址");
        }

//        String jessionId = login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
        String jessionId;

        String tokenKey = "sanSiToken";

        jessionId = redisCache.getCacheObject(tokenKey);

        if(jessionId == null || "".equals(jessionId)){
            jessionId =  login(externalSystem.getUsername(), externalSystem.getPassword(), baseUrl);
            redisCache.setCacheObject(tokenKey,jessionId);
            redisCache.expire(tokenKey,15*60);
        }

        if(jessionId == null || "".equals(jessionId)){
            return AjaxResult.error("三思照明获取token失败",0);
        }
        //开关对应关系 三晶的
//        Integer openClose = SanjingLightStateEnum.getValue(Integer.valueOf(state));、

        //开关
        int switchType = 1;
        //亮度
        int brightnessType = 1;
        //开关对应关系 Integer openClose = Integer.valueOf(state);
        Integer openClose = SanjingLightStateEnum.getValue(Integer.valueOf(state));

        //如果实时状态和控制状态都为开，不控制开关，只控制亮度
        Integer switchStatus = Integer.valueOf(state);
        Integer currentSwitchStatus = getLightSwitchStatus(sdDevices);

        if(switchStatus.equals(currentSwitchStatus) && Objects.equals(SanjingLightStateEnum.OPEN.getState(), openClose)){
//            brightnessType = updateBrightness(jessionId, baseUrl, externalSystemTunnelId, step ,brightness);
            brightnessType = updateBrightness(jessionId, baseUrl, step ,brightness);
        }else{

            //三晶照明：已关灯不能调光，请先开灯
            //如果是控制状态为关，先调光亮度为0，再关灯
            if(Objects.equals(SanjingLightStateEnum.CLOSE.getState(), openClose)){
                if(SanjingLightStateEnum.OPEN.getCode().equals(currentSwitchStatus)){
                    //如果实际状态为开，控制亮度值（如果实际状态为关，控制亮度值返回失败）
                    //如果控制状态是关，下发亮度值为0
                    brightness = 0;
//                    brightnessType = updateBrightness(jessionId, baseUrl, externalSystemTunnelId, step ,brightness);
                    brightnessType = updateBrightness(jessionId, baseUrl, step ,brightness);
                }

                //控制开关
//                switchType = updateSwitch(jessionId, baseUrl, externalSystemTunnelId, step, openClose);
                switchType = updateSwitch(jessionId, baseUrl, step, Integer.valueOf(state));
            }else{
                //先开灯，再调光
                //控制开关
//                switchType = updateSwitch(jessionId, baseUrl, externalSystemTunnelId, step, openClose);
//                brightnessType = updateBrightness(jessionId, baseUrl, externalSystemTunnelId, step ,brightness);
                switchType = updateSwitch(jessionId, baseUrl, step, Integer.valueOf(state));
                brightnessType = updateBrightness(jessionId, baseUrl, step ,brightness);
            }
        }


        //开关
//        int switchType = updateSwitch(jessionId, baseUrl, step, Integer.valueOf(state));
        //说明控制成功
        if(switchType==1&&brightnessType==1){
            //将获取到的照明实时数据更新到设备实时记录表中
            getLightDeviceData(sdDevices,brightnessStr,eqDirection,state);
        }

        logger.error("照明开关控制打印信息:switchType="+switchType+",brightnessType="+brightnessType);
        System.out.println("照明开关控制打印信息:switchType="+switchType+",brightnessType="+brightnessType);
        int result =  switchType==1 && brightnessType==1 ? 1 : 0;
        logger.error("照明开关控制打印信息:result="+result);
        System.out.println("照明开关控制打印信息:result="+result);
        if(result == 1){
            return AjaxResult.success(1);
        }else {
            return AjaxResult.error("控制失败",0);
        }
    }
    /**
     * 将获取到的照明实时数据更新到设备实时记录表中
     * @param sdDevices 设备数据
     * @param brightnessStr 照明亮度
     * @param direction 隧道方向
     */
    private void getLightDeviceData(SdDevices sdDevices, String brightnessStr, String direction, String state){
        //设备上线
        sdDevices.setEqStatus("1");
        devicesService.updateSdDevices(sdDevices);
        //加强照明开关亮度
        Integer itemId = DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode();
        Integer itemSwitchId = DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode();
        if(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
            //基本照明亮度
            itemId = DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode();
            //基本照明开光
            itemSwitchId = DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode();
        }


        sdDeviceDataService.updateDeviceData(sdDevices, state, Long.valueOf(itemSwitchId));
        sdDeviceDataService.updateDeviceData(sdDevices, brightnessStr, Long.valueOf(itemId));
//        for (TunnelStepEnum tunnelStepEnum : TunnelStepEnum.values()) {
//            String name = tunnelStepEnum.getName();
//            Integer brightness = data.getInteger(name);
//
//            if (null != brightness) {
//                //设备类型
//                sdDevices.setEqType(TunnelStepEnum.getEqType(name));
//                //段号
//                sdDevices.setExternalDeviceId(TunnelStepEnum.getValue(name));
//                SdDevices device = sdDevicesService.getLight(sdDevices);
//                if (device != null) {
//
//                    Integer itemId = DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode();
//                    if(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
//                        //基本照明
//                        itemId = DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode();
//                    }
//                    sdDeviceDataService.updateDeviceData(device, String.valueOf(brightness), Long.valueOf(itemId));
//                }
//            }
//        }
    }
    /**
     * 获取开关实时状态
     * @param sdDevices
     * @return
     */
    public Integer getLightSwitchStatus(SdDevices sdDevices){
        Integer switchStatus = null;
        String eqId = sdDevices.getEqId();
        Long eqType = sdDevices.getEqType();
        Long itemCode = getItemCode(eqType);
        SdDeviceData deviceData = new SdDeviceData();
        deviceData.setDeviceId(eqId);
        deviceData.setItemId(itemCode);
        List<SdDeviceData> deviceDataList = sdDeviceDataService.selectSdDeviceDataList(deviceData);
        if (!deviceDataList.isEmpty()) {
            switchStatus = Integer.valueOf(deviceDataList.get(0).getData());
        }
        return switchStatus;
    }

    /**
     * 获取设备类型数据项
     * @param eqType
     * @return
     */
    private Long getItemCode(Long eqType){
        //状态设备类型数据项
        long statusItemCode;
        if(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(eqType)){
            //基本照明
            statusItemCode = DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode();
        }else{
            //加强照明
            statusItemCode = DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode();
        }
        return statusItemCode;
    }
    /**
     * 设备调光
     * @param device
     * @param nowLuminanceRange    原有亮度
     * @param luminanceRange    当前亮度
     * @param controlType
     * @return
     */
    public int setBrightnessByDevice(SdDevices device,Integer nowLuminanceRange ,Integer luminanceRange, String controlType) {
        int resultStatus;
        try{
            resultStatus = setBrightness(device.getEqId(),luminanceRange);
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
}
