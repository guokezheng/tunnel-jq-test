package com.tunnel.deal.light.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeItemEnum;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import sun.misc.BASE64Encoder;

import java.io.IOException;
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
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType=MediaType.parse("application/json");
            RequestBody body=RequestBody.create(mediaType, data);
            Request request=new Request.Builder().url(baseUrl+"/api/core/users/login").method("POST",body).addHeader("Content-Type","application/json")
                    .addHeader("Accept","application/json").build();
            response=client.newCall(request).execute();

        } catch (IOException e) {
            e.printStackTrace();
            return "99";
        }finally{
            if(response != null){
                response.close();
            }
        }
        JSONObject jo = JSONObject.parseObject(  response.body().toString());
        return  jo.getJSONObject("data").getString("id");
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
        String externalSystemTunnelId = tunnelAssociationService.getExternalSystemTunnelId(eqTunnelId, eqDirection, externalSystemId);
        Assert.hasText(externalSystemTunnelId, "未配置该设备关联的隧道洞编号");

        ExternalSystem externalSystem = externalSystemService.selectExternalSystemById(externalSystemId);
        String baseUrl = externalSystem.getSystemUrl();
        Assert.hasText(baseUrl, "未配置该设备所属的外部系统地址");

        //获取三思Cookie
        String jessionId = loginRedis(externalSystem, baseUrl);
        return updateBrightness(jessionId, baseUrl, baseUrl, 1);
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
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        //传入的请求参数
        String data = "{\"param\":\"" + openClose + "\"}";
        RequestBody body = RequestBody.create(mediaType, data);

        if (openClose == 2) {
            openClose = 0;
        }
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

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        //传入的请求参数
        String data = "{\"param\":\"" + bright + "\"}";
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

        Integer openClose = Integer.valueOf(state);
        //开关
        int switchType = updateSwitch(jessionId, baseUrl, step, Integer.valueOf(state));

        //三晶
        //亮度
//        int brightnessType = 1;
        //如果亮度有值并且控制状态不是关，就控制亮度
//        if(!Objects.equals(SanjingLightStateEnum.CLOSE.getState(), openClose)){
//            brightnessType = updateBrightness(jessionId, baseUrl, step ,brightness);
//        }

        //亮度
        int brightnessType = 0;
        //2表示关
        if(openClose!=2){
            brightnessType = updateBrightness(jessionId, baseUrl, step, openClose);
        }
        int result =  switchType==1 && brightnessType==1 ? 1 : 0;
        if(result == 1){
            return AjaxResult.success(1);
        }else {
            return AjaxResult.error("控制失败",0);
        }
    }
}
