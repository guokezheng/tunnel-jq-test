package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.business.datacenter.domain.enumeration.*;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdDeviceDataService;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ITunnelAssociationService;
import com.tunnel.deal.light.enums.SanjingLightStateEnum;
import com.tunnel.deal.light.impl.SanJingLight;
import com.zc.common.core.httpclient.OkHttpClientUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 三晶照明定时任务
 */
@Component("lightTask")
public class LightTask {

    private static final Logger log = LoggerFactory.getLogger(LightTask.class);

    @Autowired
    private SanJingLight sanJingLight;
    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private IExternalSystemService externalSystemService;
    @Autowired
    private ISdDeviceDataService sdDeviceDataService;
    @Autowired
    private ITunnelAssociationService tunnelAssociationService;


    /**
     * 同步照明设备数据
     */
    public void syncDeviceData() {
        List<ExternalSystem> systemList = getExternalSystem();
        if(systemList == null || systemList.size() == 0){
            return;
        }

        for (ExternalSystem system : systemList) {

            String tunnelIdSystem = system.getTunnelId();
            Long systemId = system.getId();

            String username = system.getUsername();
            String password = system.getPassword();
            String systemUrl = system.getSystemUrl();
            //获取cookie
            String jessionId = sanJingLight.getCacheToken(username,password,systemUrl);
            if(StringUtils.isEmpty(jessionId)){
                log.error("照明定时任务报错：jessionId为空,jessionId=",jessionId);
                return;
            }
//        多个隧道执行照明定时任务
            String[] tunnelIdArray = tunnelIdSystem.split(",");
            for(String tunnelId : tunnelIdArray){
                // 上行对应的外部系统隧道洞ID
                String rightTunnelId = tunnelAssociationService.getExternalSystemTunnelId(tunnelId, TunnelDirectionEnum.UP_DIRECTION.getCode(), systemId);
                // 下行对应的外部系统隧道洞ID
                String leftTunnelId = tunnelAssociationService.getExternalSystemTunnelId(tunnelId, TunnelDirectionEnum.DOWN_DIRECTION.getCode(), systemId);

                if(rightTunnelId == null || "".equals(rightTunnelId)){
                    continue;
                }
                if(leftTunnelId == null || "".equals(leftTunnelId)){
                    continue;
                }

                //同步右洞亮度,潍坊方向
                getLatestDeviceData(tunnelId, systemUrl, rightTunnelId, jessionId, TunnelDirectionEnum.UP_DIRECTION.getCode());

                //同步左洞亮度，济南方向
                getLatestDeviceData(tunnelId, systemUrl, leftTunnelId, jessionId, TunnelDirectionEnum.DOWN_DIRECTION.getCode());

                //获取开关状态
                getTunnelSwitchStatus(tunnelId, systemUrl, leftTunnelId,rightTunnelId, jessionId);
            }
        }

    }

    /**
     * 获取开关状态
     * @param eqTunnelId 隧道ID
     * @param systemUrl 照明系统地址
     * @param leftTunnelId 照明系统左洞隧道ID
     * @param rightTunnelId 照明系统右洞隧道ID
     * @param jessionId jessionId
     */
    public void getTunnelSwitchStatus(String eqTunnelId, String systemUrl, String leftTunnelId,String rightTunnelId, String jessionId){

        List<String> typeList = new ArrayList<>();
        //加强照明类型ID
        String typeId = String.valueOf(DevicesTypeEnum.JIA_QIANG_ZHAO_MING.getCode());
        typeList.add(typeId);
        //基本照明类型ID
        typeId = String.valueOf(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode());
        typeList.add(typeId);

        //根据隧道ID、设备类型获取设备列表
       List<SdDevices> devicesList = sdDevicesService.selectDeviceList(eqTunnelId,typeList);
       for(SdDevices devices : devicesList){
           String step = devices.getExternalDeviceId();
           String direction = devices.getEqDirection();
           String eSystemTunnelId = leftTunnelId;
           if(TunnelDirectionEnum.UP_DIRECTION.getCode().equals(direction)){
            eSystemTunnelId = rightTunnelId;
           }
          JSONObject jsonObject = getSwitchStatus(systemUrl,eSystemTunnelId,jessionId,step);
          updateSwitchStatus(devices,jsonObject);
       }

    }

    /**
     * 获取开关状态
     * @param systemUrl
     * @param eSystemTunnelId
     * @param jessionId
     * @param step
     */
    public JSONObject getSwitchStatus(String systemUrl, String eSystemTunnelId, String jessionId, String step){
        StringBuffer urlStr = new StringBuffer(systemUrl);
        urlStr.append("/api/getSwitchStatus?tunnelId=").append(eSystemTunnelId).append("&step=").append(step);
        String url = urlStr.toString();

        return okHttpGetWithCookie(url,jessionId);
    }


    /**
     * 更新开关状态实时数据
     * @param sdDevices 设备信息
     * @param data 实时数据
     */
    public void updateSwitchStatus(SdDevices sdDevices, JSONObject data){
        //加强照明开关状态
        Integer itemId = DevicesTypeItemEnum.JQ_LIGHT_OPENCLOSE.getCode();
        if(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
            //基本照明
            itemId = DevicesTypeItemEnum.JI_BEN_ZHAO_MING_OPENCLOSE.getCode();
        }
        String openStatus = "opened";
        //关
        Integer realStatus = SanjingLightStateEnum.CLOSE.getCode();
        //开关实时状态
        if(data != null && data.getString("switch") != null){
            String switchStatus = data.getString("switch");
            if(openStatus.equals(switchStatus)){
                //开
                realStatus =  SanjingLightStateEnum.OPEN.getCode();
            }
            //修改设备状态为在线
            sdDevices.setEqStatus("1");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            //修改实时数据
            sdDeviceDataService.updateDeviceData(sdDevices, String.valueOf(realStatus), Long.valueOf(itemId));
        }else{
            sdDevices.setEqStatus("2");
            sdDevices.setEqStatusTime(new Date());
            sdDevicesService.updateSdDevices(sdDevices);
            log.error("updateSwitchStatus--数据问题--data="+data);
            System.out.println("updateSwitchStatus--数据问题--data="+data);
        }

    }


    /**
     * 同步照明亮度值
     * @param eqTunnelId 隧道ID
     * @param systemUrl 照明系统地址
     * @param eSystemTunnelId 照明系统隧道ID
     * @param jessionId jessionId
     * @param direction 方向
     */
    public void getLatestDeviceData(String eqTunnelId, String systemUrl, String eSystemTunnelId, String jessionId,String direction) {
        String url = systemUrl + "/api/getLatestDeviceData?tunnelId=" + eSystemTunnelId;
        JSONObject data = okHttpGetWithCookie(url,jessionId);
        //将获取到的照明实时数据更新到设备实时记录表中
        getLightDeviceData(data,eqTunnelId,direction);
    }

    /**
     * 将获取到的照明实时数据更新到设备实时记录表中
     * @param data 照明实时数据
     * @param eqTunnelId 隧道ID
     * @param direction 隧道方向
     */
    private void getLightDeviceData(JSONObject data, String eqTunnelId, String direction){
        SdDevices sdDevices = new SdDevices();
        //隧道Id
        sdDevices.setEqTunnelId(eqTunnelId);
        //隧道方向
        sdDevices.setEqDirection(direction);

        for (TunnelStepEnum tunnelStepEnum : TunnelStepEnum.values()) {
            String name = tunnelStepEnum.getName();
            Integer brightness = data.getInteger(name);

            if (null != brightness) {
                //设备类型
                sdDevices.setEqType(TunnelStepEnum.getEqType(name));
                //段号
                sdDevices.setExternalDeviceId(TunnelStepEnum.getValue(name));
                SdDevices device = sdDevicesService.getLight(sdDevices);
                if (device != null) {

                    Integer itemId = DevicesTypeItemEnum.JQ_LIGHT_BRIGHNESS.getCode();
                    if(DevicesTypeEnum.JI_BEN_ZHAO_MING.getCode().equals(sdDevices.getEqType())){
                        //基本照明
                        itemId = DevicesTypeItemEnum.JB_LIGHT_BRIGHNESS.getCode();
                    }
                    sdDeviceDataService.updateDeviceData(device, String.valueOf(brightness), Long.valueOf(itemId));
                }
            }
        }
    }

    /**
     * 获取外部系统
     * @return
     */
    public List<ExternalSystem> getExternalSystem() {
        ExternalSystem system = new ExternalSystem();
        //三晶照明
        system.setBrandId(DevicesBrandEnum.SAN_JING.getCode());
        List<ExternalSystem> list = externalSystemService.queryExternalSystemList(system);
        /*if (list.size() > 1) {
            String brandName = DevicesBrandEnum.SAN_JING.getName();
            throw new RuntimeException("存在多个" + brandName + "品牌的外部系统！");
        }*/

        if(list != null && list.size() > 0){
            List<ExternalSystem> resultList = new ArrayList<>();
            for (ExternalSystem externalSystem : list) {
                String tunnelId = externalSystem.getTunnelId();
                String systemName = externalSystem.getSystemName();
                String username = externalSystem.getUsername();
                String password = externalSystem.getPassword();
                String systemUrl = externalSystem.getSystemUrl();
                if (StringUtils.isEmpty(tunnelId)){
                    System.out.println(systemName + "未配置所属隧道");
                    continue;
                }
                if (StringUtils.isEmpty(username)){
                    System.out.println(systemName + "未配置登录用户名");
                    continue;
                }
                if (StringUtils.isEmpty(password)){
                    System.out.println(systemName + "未配置登录密码");
                    continue;
                }
                if (StringUtils.isEmpty(systemUrl)){
                    System.out.println(systemName + "未配置系统地址");
                    continue;
                }

                resultList.add(externalSystem);
            }
            return resultList;
        }

        return null;
    }


    /**
     * OkHttp get请求方法
     * @param url url
     * @param cookie cookie
     * @return
     */
    public JSONObject okHttpGetWithCookie(String url, String cookie){
        JSONObject data = null;
        OkHttpClient client = OkHttpClientUtil.client;
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .readTimeout(5, TimeUnit.SECONDS)
//                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Cookie", cookie)
                .build();
        Response response = null;
        try {
             response = client.newCall(request).execute();
            String result = null;
            if (response.body() != null) {
                result = response.body().string();
                if (result.contains("操作成功")) {
                    data = JSONObject.parseObject(result).getJSONObject("data");
//                    return data;
                }
            }
            response.close();

        } catch (IOException e) {
            log.error("照明控制请求报错：",e.getMessage());
            System.err.println("照明控制请求报错："+e.getMessage());
            e.printStackTrace();
        }finally {
            if(response != null){
                response.close();
            }
        }
        return data;
    }

}
