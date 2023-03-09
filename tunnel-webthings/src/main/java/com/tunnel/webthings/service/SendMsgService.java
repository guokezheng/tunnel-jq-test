package com.tunnel.webthings.service;


import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdStateStorage;
import com.tunnel.webthings.vo.RadarMsgTopicVo;


/**
 * @author dear_dzy
 */
public interface SendMsgService {

//    String sendDirect(String devNo,String devType);

//    AjaxResult sendEvent();

//    AjaxResult devicestatus(String devId);

//    AjaxResult devicesdata(String devId,String state);
//
//    String sendDevStatus(RadarMsgTopicVo vo);
//
//    AjaxResult storages(SdStateStorage sdStateStorage);

    int pushDevicesStatusToOtherSystem(SdDevices sdDevices, String role, String status);
}
