package com.tunnel.platform.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.platform.config.GLZInfoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SdOptDeviceService {

    @Autowired
    private ISdDevicesService sdDevicesService;
    @Autowired
    private ISdTunnelsService sdTunnelsService;

    /**
     * 批量控制设备方法参数不能为空，否则直接返回0（控制失败）
     * 控制设备必传参数：devId（设备ID）、state（变更的状态）
     * 控制诱导灯：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）
     * 控制疏散标志：devId（设备ID）、state（变更的状态）、brightness（亮度）、frequency（频率）、fireMark（设备地址标号，正常情况下为255,0为关灯）
     * 控制方式： controlType(控制方式   0：手动 1：时间控制 2：光强控制 3:预案控制)
     */
    public Integer optSingleDevice(Map<String, Object> params) {
        SdDevices sdDevices = sdDevicesService.selectSdDevicesById((String) params.get("devId"));
        SdTunnels tunnel = sdTunnelsService.selectSdTunnelsById(sdDevices.getEqTunnelId());

        //设备所属管理站host
        String host = getGlzHost(String.valueOf(tunnel.getDeptId()));
        //接口地址
        String url = host + "/workspace/commonControl";
        //String url = "http://localhost:8000/workspace/commonControl";
        String jsonObject = JSONObject.toJSONString(params);
        String result = HttpUtil.post(url, jsonObject);
        return Integer.parseInt(result);
    }

    public String getGlzHost(String deptId) {
        //文祖北
        String wzbDeptId = GLZInfoConfig.getWzbDeptId();
        //太河
        String thDeptId = GLZInfoConfig.getThDeptId();
        //庙子
        String mzDeptId = GLZInfoConfig.getMzDeptId();
        //九龙峪
        String jlyDeptId = GLZInfoConfig.getJlyDeptId();
        //仰天山
        String ytsDeptId = GLZInfoConfig.getYtsDeptId();

        String host = "";
        if (deptId.equals(wzbDeptId)) {
            host = GLZInfoConfig.getWzbHost();
        } else if (deptId.equals(thDeptId)) {
            host = GLZInfoConfig.getThHost();
        } else if (deptId.equals(mzDeptId)) {
            host = GLZInfoConfig.getMzHost();
        } else if (deptId.equals(jlyDeptId)) {
            host = GLZInfoConfig.getJlyHost();
        } else if (deptId.equals(ytsDeptId)) {
            host = GLZInfoConfig.getYtsHost();
        }
        return host;
    }


}
