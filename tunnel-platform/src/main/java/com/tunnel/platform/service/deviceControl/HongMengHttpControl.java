package com.tunnel.platform.service.deviceControl;

import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.datacenter.domain.enumeration.OperationLogEnum;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.logRecord.SdOperationLog;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.logRecord.ISdOperationLogService;
import com.tunnel.business.strategy.service.CommonControlService;
import com.tunnel.deal.generalcontrol.GeneralControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * describe: 鸿蒙控制器控制-杭山东
 *
 * @author zs
 * @date 2023/4/4
 */
@Component
public class HongMengHttpControl implements GeneralControlBean {


    @Autowired
    private HongMengDevService hongMengDevService;

    @Autowired
    private CommonControlService commonControlService;

    @Autowired
    private ISdDevicesService sdDevicesService;


    @Autowired
    private ISdOperationLogService sdOperationLogService;


    /**
     * 设备控制+操作日志
     * @param map
     * @return
     */
    public AjaxResult control(Map<String, Object> map) {
//        //设备ID
//        String devId = Optional.ofNullable(map.get("devId")).orElse("").toString();
//
//        //设备状态
//        String state = Optional.ofNullable(map.get("state")).orElse("").toString();
        //设备控制
        Map<String, String> hongMap = hongMengDevService.hongMengHttpControl(map);
        Integer code = Integer.valueOf(hongMap.get("code"));
        String msg = hongMap.get("msg");
        if(code == HttpStatus.SUCCESS){
            return AjaxResult.success(1);
        }else {
            return AjaxResult.error(msg,0);
        }
    }

    @Override
    public AjaxResult control(Map<String, Object> map, SdDevices sdDevices) {
        boolean isopen = commonControlService.queryAnalogControlConfig();
        if (isopen) {
            //设备模拟控制开启
            return commonControlService.excecuteAnalogControl(sdDevices,map);
        }
        return control(map);
    }


    /**
     * 设备控制+模拟控制
     * @param map
     * @return
     */
    @Override
    public Integer controlDevices(Map<String, Object> map){
        int controlState = 0;

        boolean isopen = commonControlService.queryAnalogControlConfig();

        String devId = map.get("devId").toString();
//        String state = map.get("state").toString();

        SdDevices sdDevices = sdDevicesService.selectSdDevicesById(devId);

        if (!isopen) {
            //设备控制，并生成操作日志
            AjaxResult ajaxResult = control(map);
            Integer code = Integer.valueOf(String.valueOf(ajaxResult.get("code")));
            if( code == HttpStatus.SUCCESS){
                controlState = Integer.valueOf(OperationLogEnum.STATE_SUCCESS.getCode());
            }
        } else {
            //设备模拟控制开启，直接变更设备状态为在线并展示对应运行状态
            controlState = commonControlService.analogControl(map,sdDevices);

            //生成日志
            SdOperationLog sdOperationLog = commonControlService.getOperationLog(map,sdDevices,controlState);

            sdOperationLogService.insertSdOperationLog(sdOperationLog);
        }

        return controlState;
    }



}
