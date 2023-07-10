package com.tunnel.deal.mca;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import com.tunnel.deal.mca.service.McaDataParse;
import com.tunnel.deal.mca.service.McaService;
import com.tunnel.deal.tcp.client.general.TcpClientGeneralBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * describe: MCA控制类
 *
 * @author zs
 * @date 2023/4/4
 */
@Component
public class McaControl implements GeneralControlBean, TcpClientGeneralBean {

    @Autowired
    private McaService mcaService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdOperationLogService sdOperationLogService;

    @Autowired
    private McaDataParse mcaDataParse;

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        String deviceId = sdDevices.getEqId();

        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启
            return commonControlService.excecuteAnalogControl(sdDevices,map);
        }

        Integer controlState = 0;

        //控制设备之前获取设备状态
        String beforeState = commonControlService.selectBeforeState(sdDevices);


        //设备状态
        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        AjaxResult ajaxResult = mcaService.control(deviceId,state);
        Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
        if( code == HttpStatus.SUCCESS){
            controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
        }
        //操作日志
        commonControlService.addOperationLog(map,sdDevices,beforeState,controlState);

        return AjaxResult.success(controlState);
    }

    /**
     * 设备控制
     *
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map) {
        int controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();

        String deviceId = map.get("devId").toString();
        String state = map.get("state").toString();

        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(deviceId);

        if (!isopen) {
            //连接设备进行控制
            AjaxResult ajaxResult = mcaService.control(deviceId,state);
            Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
            if( code == HttpStatus.SUCCESS){
                controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
            }
        } else {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);
        }

        SdOperationLog sdOperationLog = commonControlService.getOperationLog(map,sdDevices,controlState);
        sdOperationLogService.insertSdOperationLog(sdOperationLog);

        return controlState;
    }


    /**
     * 解析读取的数据
     * @param ip 网关设备IP
     * @param deviceId 网关设备ID
     * @param msg 读取的数据
     */
    @Override
    public void handleReadData(String ip, String deviceId, String msg) {
        mcaDataParse.readDataParse(ip,deviceId,msg);
    }
}
