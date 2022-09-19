package com.tunnel.webthings.service;


import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.dataInfo.SdStateStorage;
import com.tunnel.webthings.vo.RadarMsgTopicVo;
import com.tunnel.webthings.vo.SendMsgVO;


/**
 * @author dear_dzy
 */
public interface SendMsgService {

    String sendDirect(String devNo,String devType);

    AjaxResult sendEvent(SendMsgVO msgVO);

    String sendDevStatus(RadarMsgTopicVo vo);

    AjaxResult storages(SdStateStorage sdStateStorage);
}
