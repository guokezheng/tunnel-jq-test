package com.tunnel.webthings.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.domain.dataInfo.SdStateStorage;
import com.tunnel.webthings.dao.SendMsgMapper;
import com.tunnel.webthings.domain.ActiveLuminousSigns;
import com.tunnel.webthings.domain.ConfluenceDevFaultWarn;
import com.tunnel.webthings.domain.Jqdict;
import com.tunnel.webthings.domain.RadarMsgTopic;
import com.tunnel.webthings.enums.MsgType;
import com.tunnel.webthings.service.SendMsgService;
import com.tunnel.webthings.vo.RadarMsgTopicVo;
import com.tunnel.webthings.vo.SdDevStatusVO;
import com.tunnel.webthings.vo.SdRadarMsgTopicVO;
import com.tunnel.webthings.vo.SendMsgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SendMsgServiceImpl implements SendMsgService {

    private static final Logger log = LoggerFactory.getLogger(SendMsgService.class);

    /**
     * 时间戳格式
     */
    private static final String sdf_pattern = "yyyy-MM-dd HH:mm:ss.SSS";

    @Value("${iot.url}")
    private String url;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SendMsgMapper msgMapper;


    /**
     * 发送指令数据
     */
    @Override
    public String sendDirect(String devNo,String devType) {
        //查询字典
        List<Jqdict> jqdict = msgMapper.selectDict();
        //模拟指令数据
        Map<String, Object> dev01Map = new HashMap<>();
        dev01Map.put("devNo", devNo); //设备编号，必填
        dev01Map.put("devType", devType); //设备类型，必填
        //指令数据，格式根据业务自定义开始
        dev01Map.put("directType", "ActiveLuminousSigns"); //指令A
        dev01Map.put("directTypeDesc", "主动发光标志数据描述"+RandomUtil.randomString(3)); //指令A描述
        dev01Map.put("random", RandomUtil.randomString(128)); //随机写入
        //指令数据，格式根据业务自定义结束
        dev01Map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
        //设备自定义字段
        ActiveLuminousSigns acls=new ActiveLuminousSigns();
        acls.setTaskType("130");
        acls.setBacklitSwitch("1");
        acls.setBacklightBrightness("5");
        acls.setReversibleLanes("1100");
        acls.setDroopCompensation("2");
        acls.setMeasuredCurrent("实测电流：数据来源 标准牌实时监测背光电流");
        acls.setDefaultCurrent("默认电流：标志牌安装后检测电流");
        dev01Map.put("expands", acls); //自定义扩展数据，选填
        dev01Map.put("user",SecurityUtils.getUsername());
        List<String> devNos = CollUtil.newArrayList(devNo);
        List<Map<String, Object>> devList = CollUtil.newArrayList(dev01Map);
        Map<String, Object> content = new HashMap<>();
        content.put("devList", devList);

        //调用接口
        String s = this.sendData(MsgType.msg31.getCode(), MsgType.msgId03.getCode(), devType, devNos, MsgType.msgUp.getCode(), content);
        return s;
    }

    @Override
    public AjaxResult sendEvent(SendMsgVO msgVO) {
        String devType = msgVO.getDevType();
        String[] devNos = msgVO.getDevNo().split(",");
        //发送事件数据
        Map<String, Object> eventmap = new HashMap<>();
        eventmap.put("devNo",devNos);
        eventmap.put("devType",devType);
        eventmap.put("directType","ConfluenceDevFaultWarn");
        eventmap.put("directTypeDesc","合流区预警设备故障告警"+RandomUtil.randomString(4));
        eventmap.put("random", RandomUtil.randomString(20)); //随机写入
        eventmap.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填

        //事件自定义字段
        ConfluenceDevFaultWarn confluenceDevFaultWarn = new ConfluenceDevFaultWarn();
        confluenceDevFaultWarn.setDevCode("devNo在String数组");
        confluenceDevFaultWarn.setDevType(Integer.parseInt(devType));
        confluenceDevFaultWarn.setDevName("预警设备");
        confluenceDevFaultWarn.setMsgType("消息类型：告警消息");
        confluenceDevFaultWarn.setDevTime(DateUtil.format(DateUtil.date(), sdf_pattern));
        confluenceDevFaultWarn.setEnvTriType(1);
        confluenceDevFaultWarn.setFaultCode("3");
        confluenceDevFaultWarn.setEnvLon("116.75199");
        confluenceDevFaultWarn.setEnvLat("36.55358");
        confluenceDevFaultWarn.setRemark("这是一个备注信息");
        eventmap.put("expands", confluenceDevFaultWarn); //自定义字段
        eventmap.put("user",SecurityUtils.getUsername());

        List<String> devNo = CollUtil.newArrayList(devNos);
        List<Map<String, Object>> devList = CollUtil.newArrayList(eventmap);
        Map<String, Object> content = new HashMap<>();
        content.put("devList", devList);

        String s = this.sendData(MsgType.msg31.getCode(), MsgType.msgId02.getCode(), devType, devNo, MsgType.msgUp.getCode(), content);
        return AjaxResult.success(s);
    }

    @Override
    public String sendDevStatus(RadarMsgTopicVo vo) {
        //设备编号
        String devNo = vo.getDevNo();
        //设备类型
        String devType = vo.getDevType();
        //时间戳
        String format = DateUtil.format(DateUtil.date(), sdf_pattern);
        //雷达消息数据
        Map<String, Object> map = new HashMap<>();
        map.put("devNo",devNo);
        map.put("devType",devType);
        map.put("directType","RadarMsgTopic");
        map.put("directTypeDesc","雷达消息数据"+RandomUtil.randomString(4));
        map.put("random", RandomUtil.randomString(20)); //随机写入
        map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
        //自定义字段
        RadarMsgTopic radarMsgTopic =new RadarMsgTopic();
        radarMsgTopic.setFuseId("这是融合ID");
        radarMsgTopic.setId(MsgType.msg31.getCode()+devNo+DateUtil.date()+"车道号"+RandomUtil.randomString(4));
        radarMsgTopic.setPtcType(1);
        radarMsgTopic.setLane(2);
        radarMsgTopic.setDirect(Integer.parseInt(MsgType.direct1.getCode()));
        radarMsgTopic.setVehL(3.45);
        radarMsgTopic.setVehW(1.62);
        radarMsgTopic.setVehH(1.25);
        radarMsgTopic.setVehPlate("鲁A88888");
        radarMsgTopic.setVehPlateColor("03");
        radarMsgTopic.setVehColor("6");
        radarMsgTopic.setVehBrand("五菱宏光");
        radarMsgTopic.setPtcLon(116.75199);
        radarMsgTopic.setPtcLat(36.55358);
        radarMsgTopic.setPtcEle(8848.88);
        radarMsgTopic.setAmapLon(116.75199);
        radarMsgTopic.setAmapLat(36.55358);
        radarMsgTopic.setPtcSpeed(120.5);
        radarMsgTopic.setPtcHeading(20.15);
        radarMsgTopic.setVehType(4);
        radarMsgTopic.setCreateTime(format);
        radarMsgTopic.setSpare(null);
        map.put("expands",radarMsgTopic);
        map.put("user",SecurityUtils.getUsername());

        List<String> devNos = CollUtil.newArrayList(devNo);
        List<Map<String, Object>> devList = CollUtil.newArrayList(map);
        Map<String, Object> content = new HashMap<>();
        content.put("devList", devList);
        String s = this.sendData(MsgType.msg31.getCode(), MsgType.msgId01.getCode(), devType, devNos, MsgType.msgUp.getCode(), content);
        return s;
    }

    @Override
    public AjaxResult storages(SdStateStorage sdStateStorage) {
        String devNo="G00030001A1990001";
        String devType="199";
        //时间戳
        String format = DateUtil.format(DateUtil.date(), sdf_pattern);
        //消息数据
        Map<String, Object> map = new HashMap<>();
        map.put("devNo",devNo);
        map.put("devType",devType);
        map.put("directType","车道指示器topic");
        map.put("directTypeDesc","车道指示器消息");
        map.put("timeStamp", DateUtil.format(DateUtil.date(), sdf_pattern)); //时间戳，必填
        //自定义数据
        SdStateStorage storage=new SdStateStorage();
        //状态
        storage.setState(sdStateStorage.getState());
        storage.setDeviceId(sdStateStorage.getDeviceId());
        storage.setTunnelId(sdStateStorage.getTunnelId());
        map.put("expands",storage);
        map.put("user",SecurityUtils.getUsername());
        //设备编号
        List<String> devNos = CollUtil.newArrayList(devNo);
        List<Map<String, Object>> devList = CollUtil.newArrayList(map);
        Map<String, Object> content = new HashMap<>();
        content.put("devList", devList);
        String s = this.sendData(MsgType.msg31.getCode(), MsgType.msgId02.getCode(), devType, devNos, MsgType.msgUp.getCode(), content);
        return AjaxResult.success(s);
    }

    /**
     * 发送数据
     *
     * @param msgType 消息类型
     * @param msgId 消息编码
     * @param devType 设备类型
     * @param devNos 设备编号
     * @param direct 数据传输
     * @param content 内容
     * @return
     */
    private String sendData(String msgType, String msgId, String devType, List<String> devNos, String direct, Map<String, Object> content) {
        JSONObject requestData = this.getBaseRequestData(msgType, msgId, devType, devNos, direct);
        requestData.put("content", content);
        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestData.toString(), requestHeaders);
        //执行请求
        log.info("参数 --> {}", requestData);
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("返回值 --> {}", responseEntity.getBody());
            return "发送成功--时间:"+DateUtil.format(DateUtil.date(), sdf_pattern);
        } catch (Exception e) {
            log.error("物联网关数据发送失败！{}", e.getMessage());
            return "发送失败--时间:"+DateUtil.format(DateUtil.date(), sdf_pattern);
        }
    }
    /**
     * 获取上报外层数据
     *
     * @param msgType
     * @param msgId
     * @param devType
     * @param devNos
     * @param direct
     * @return
     */

    private JSONObject getBaseRequestData(String msgType, String msgId, String devType, List<String> devNos, String direct) {
        JSONObject requestData = new JSONObject();
        String timeStamp = String.valueOf(DateUtil.current());
        //设备类型
        requestData.put("msgType", msgType);
        //消息编码
        requestData.put("msgId", msgId);
        //版本号
        requestData.put("msgVer", "1.0");
        //设备编号
        requestData.put("devNo", devNos);
        //设备类型
        requestData.put("devType", devType);
        //时间戳
        requestData.put("timeStamp", timeStamp);
        //消息方向
        requestData.put("direct", direct);
        //MD5
        String md5 = this.getMd5(timeStamp, devNos);
        requestData.put("md5", md5);
        return requestData;
    }
    /**
     * 获取MD5值
     *
     * @param timeStamp
     * @param devNos
     * @return
     */
    private String getMd5(String timeStamp, List<String> devNos) {
        String plaintext = timeStamp + this.getDevSecureKeys(devNos);
        return SecureUtil.md5(plaintext);
    }

    /**
     * 获取设备秘钥，目前和设备编号一致
     *
     * @param devNos
     * @return
     */
    private String getDevSecureKeys(List<String> devNos) {
        //TODO 目前设备秘钥和设备编号一致
        return StrUtil.join("", devNos);
    }

}
