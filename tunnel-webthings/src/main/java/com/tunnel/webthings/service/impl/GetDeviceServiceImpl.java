package com.tunnel.webthings.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.utils.StringUtils;
import com.tunnel.webthings.dao.TunnelIotDeviceMapper;
import com.tunnel.webthings.domain.DeviceParameter;
import com.tunnel.webthings.service.GetDeviceService;
import com.tunnel.webthings.service.SendMsgService;
import com.tunnel.webthings.vo.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dzy
 * @date 2022/8/22 17:20
 */
@Service
public class GetDeviceServiceImpl implements GetDeviceService {

    @Value("${iot.token}")
    private String token;

    @Value("${iot.host}")
    private String host;

    @Value("${iot.deviceList}")
    private String deviceList;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TunnelIotDeviceMapper deviceMapper;

    private static final Logger log = LoggerFactory.getLogger(SendMsgService.class);

    @Override
    public List<ResponseVO> getDevList(DeviceParameter dev) {
        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        String token = getDataToken();
        requestHeaders.add("Authorization", "Bearer " + token);
        HashMap<String, Object> map = new HashMap<>();
        map.put("devCategory", dev.getDevCategory());
        map.put("devNo", dev.getDevNo());
        map.put("devType", dev.getDevType());
        map.put("opmaCliqueId", dev.getOpmaCliqueId());
        map.put("opmaManagerCropId", dev.getOpmaManagerCropId());
        map.put("opmaManagerId", dev.getOpmaManagerId());
        map.put("roadId", dev.getRoadId());
        map.put("stationId", dev.getStationId());
        //请求体
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(map, requestHeaders);
        //发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(host + deviceList, requestEntity, Map.class);
        HashMap<String, Object> hashMap = (HashMap<String, Object>) responseEntity.getBody();
        log.info("返回值-->{}", hashMap);
        JSON parse = JSONUtil.parse(hashMap.get("data"));
        List<ResponseVO> list = JSONUtil.toList(parse.toString(), ResponseVO.class);
        return list;
    }

    /**
     * 同步中台数据
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyData() {
        int number = deviceMapper.selectCount();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (number > 0) {
            deviceMapper.deleteDevice();
            //查询数据(参数非必填)
            //插入数据库
            DeviceParameter deviceParameter = new DeviceParameter();
            deviceParameter.setDevType("001");
            List<ResponseVO> devList = getDevList(deviceParameter);
            devList.forEach(f -> {
                if (StringUtils.isNotEmpty(f.getDevType())) {
                    f.setdType(Integer.parseInt(f.getDevType()));
                }
                f.setrDirection(f.getRoadDirection() + "");
                f.setuLife(f.getUseLife() + "");
                f.setuStatus(f.getUseStatus() + "");
                try {
                    if (StringUtils.isNotEmpty(f.getDeliveryTime())) {
                        f.setDyTime(sdf.parse(f.getDeliveryTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getWarrantyEndTime())) {
                        f.setwEndTime(sdf.parse(f.getWarrantyEndTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getInstallTime())) {
                        f.setInTime(sdf.parse(f.getInstallTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getPortStatusTime())) {
                        f.setpStatusTime(sdf.parse(f.getPortStatusTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getGatewayNetstatusTime())) {
                        f.setgNetstatusTime(sdf.parse(f.getGatewayNetstatusTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getDevStatusTime())) {
                        f.setdStatusTime(sdf.parse(f.getDevStatusTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            deviceMapper.insertDevice(devList);
        } else {
            //查询数据(参数非必填)
            //插入数据库
            DeviceParameter deviceParameter = new DeviceParameter();
            deviceParameter.setDevType("001");
            List<ResponseVO> devList = getDevList(deviceParameter);
            devList.forEach(f -> {
                if (StringUtils.isNotEmpty(f.getDevType())) {
                    f.setdType(Integer.parseInt(f.getDevType()));
                }
                f.setrDirection(f.getRoadDirection() + "");
                f.setuLife(f.getUseLife() + "");
                f.setuStatus(f.getUseStatus() + "");
                try {
                    if (StringUtils.isNotEmpty(f.getDeliveryTime())) {
                        f.setDyTime(sdf.parse(f.getDeliveryTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getWarrantyEndTime())) {
                        f.setwEndTime(sdf.parse(f.getWarrantyEndTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getInstallTime())) {
                        f.setInTime(sdf.parse(f.getInstallTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getPortStatusTime())) {
                        f.setpStatusTime(sdf.parse(f.getPortStatusTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getGatewayNetstatusTime())) {
                        f.setgNetstatusTime(sdf.parse(f.getGatewayNetstatusTime()));
                    }
                    if (StringUtils.isNotEmpty(f.getDevStatusTime())) {
                        f.setdStatusTime(sdf.parse(f.getDevStatusTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            deviceMapper.insertDevice(devList);
        }
    }

    /**
     * 获取token
     */
    private String getDataToken() {
        RestTemplate restTemplate = new RestTemplate();
        Map map = restTemplate.postForObject(token, null, Map.class);
        String accessToken = map.get("access_token").toString();
        return accessToken;
    }
}
