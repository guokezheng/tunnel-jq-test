package com.tunnel.platform.controller.platformAuthApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.platformAuthApi.PlatformApiService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 二级平台推送Controller
 *
 * @author zhai
 * @date 2020-10-25
 */
@RestController
@RequestMapping("/platform/api")
@Api(tags = "推送Controller")
public class PlatformApiController {

    @Autowired
    private PlatformApiService platformApiService;

    /**
     * 设备管理推送接口
     *
     * @param sdDevicesList 设备集合(推送)
     * @return
     */
    public Result devicesPush(List<SdDevices> sdDevicesList){
        if(sdDevicesList.size() > 0){
            return Result.toResult(platformApiService.devicesPush(sdDevicesList));
        }else {
            return Result.error();
        }
    }

    /**
     * 设备接收接口
     * @param requestEntity 设备集合(接收)
     * @return
     */
    @PostMapping(value = "/devicesAccept")
    public Result devicesAccept(HttpEntity<String> requestEntity){
        String deviceData = requestEntity.getBody();
        //把String转为list
        JSONArray jsonArray = JSONObject.parseArray(deviceData);
        String data = jsonArray.toJSONString();
        //String data = JSONObject.toJSONString(jsonArray, SerializerFeature.WriteClassName);
        List<SdDevices> sdDevicesList = JSONObject.parseArray(data, SdDevices.class);
        //获取第一条数据
        SdDevices sdDevices = sdDevicesList.get(0);
        //判断推送类型：add(新增)、edit(修改)、del(删除)、import(导入)
        int count = 0;
        if("add".equals(sdDevices.getPushType())){
            count = platformApiService.insertSdDevices(sdDevicesList);
        }else if("edit".equals(sdDevices.getPushType())){
            count = platformApiService.updateSdDevices(sdDevicesList);
        }else if("del".equals(sdDevices.getPushType())){
            count = platformApiService.deleteSdDevices(sdDevicesList);
        }else {
            count = platformApiService.importSdDevices(sdDevicesList);
        }
        System.out.println("数据" + deviceData);
        return Result.toResult(count);
    }
}
