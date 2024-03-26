package com.tunnel.platform.controller.platformAuthApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.page.Result;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.platformAuthApi.PlatformApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ApiOperation("设备管理推送接口")
    public void devicesPush(List<SdDevices> sdDevicesList, String pushType, String userName){
        if(sdDevicesList.size() > 0){
            platformApiService.devicesPush(sdDevicesList, pushType, userName);
        }
    }

    /**
     * 隧道管理推送接口
     *
     * @param sdTunnelsList 隧道集合(推送)
     * @return
     */
    @ApiOperation("隧道管理推送接口")
    public void tunnelsPush(List<SdTunnels> sdTunnelsList, String pushType){
        if(sdTunnelsList.size() > 0){
            platformApiService.tunnelsPush(sdTunnelsList, pushType);
        }
    }

    /**
     * 设备接收接口
     * @param deviceData 设备集合(接收)
     * @return
     */
    @PostMapping(value = "/devicesAccept")
    @ApiOperation("设备接收接口")
    public Result devicesAccept(String deviceData){
        //把map拆分
        JSONObject object = JSONObject.parseObject(deviceData);
        //获取到数据集合
        JSONArray sdTunnelsList = JSONObject.parseArray(object.getJSONArray("sdDevicesList").toString());
        //获取到推送类型
        String pushType = object.get("pushType").toString();
        //将集合转为list
        List<SdDevices> sdDevicesList = JSONObject.parseArray(sdTunnelsList.toString(), SdDevices.class);

        //判断推送类型：add(新增)、edit(修改)、del(删除)、import(导入)
        int count = 0;
        if("add".equals(pushType)){
            count = platformApiService.insertSdDevices(sdDevicesList);
        }else if("edit".equals(pushType)){
            count = platformApiService.updateSdDevices(sdDevicesList);
        }else if("del".equals(pushType)){
            count = platformApiService.deleteSdDevices(sdDevicesList);
        }else {
            String userName = object.get("userName").toString();
            count = platformApiService.importSdDevices(sdDevicesList,userName);
        }
        return Result.toResult(count);
    }

    /**
     * 隧道接收接口
     * @param tunnelData 隧道集合(接收)
     * @return
     */
    @PostMapping(value = "/tunnelsAccept")
    @ApiOperation("隧道接收接口")
    public Result tunnelsAccept(String tunnelData){
        //把map拆分
        JSONObject object = JSONObject.parseObject(tunnelData);
        //获取到数据集合
        JSONArray sdTunnelsList = JSONObject.parseArray(object.getJSONArray("sdTunnelsList").toString());
        //获取到推送类型
        String pushType = object.get("pushType").toString();
        //将集合转为list
        List<SdTunnels> sdTunnelsList1 = JSONObject.parseArray(sdTunnelsList.toString(), SdTunnels.class);

        //判断推送类型：add(新增)、edit(修改)、del(删除)
        int count = 0;
        if("add".equals(pushType)){
            count = platformApiService.insertSdTunnels(sdTunnelsList1);
        }else if("edit".equals(pushType)){
            count = platformApiService.updateSdTunnels(sdTunnelsList1);
        }else if("del".equals(pushType)){
            count = platformApiService.deleteSdTunnelsByIds(sdTunnelsList1);
        }
        return Result.toResult(count);
    }
}
