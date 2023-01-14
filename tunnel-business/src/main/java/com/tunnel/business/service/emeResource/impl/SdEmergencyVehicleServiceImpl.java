package com.tunnel.business.service.emeResource.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.emeResource.SdEmergencyOrg;
import com.tunnel.business.domain.emeResource.SdEmergencyVehicle;
import com.tunnel.business.mapper.emeResource.SdEmergencyVehicleMapper;
import com.tunnel.business.service.emeResource.ISdEmergencyVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应急车辆Service业务层处理
 *
 * @author dzy
 * @date 2022-08-09
 */
@Service
public class SdEmergencyVehicleServiceImpl implements ISdEmergencyVehicleService {

    private static final Logger log = LoggerFactory.getLogger(SdEmergencyVehicleServiceImpl.class);

    @Autowired
    private SdEmergencyVehicleMapper sdEmergencyVehicleMapper;

    @Value("${url.edit}")
    private String updateUrl;

    @Value("${url.syn}")
    private String synUrl;

    @Value("${url.detail}")
    private String detailUrl;

//    @Autowired
//    private RestTemplate restTemplate;

    /**
     * 查询应急车辆
     *
     * @param id 应急车辆主键
     * @return 应急车辆
     */
    @Override
    public SdEmergencyVehicle selectSdEmergencyVehicleById(Long id) {
        return sdEmergencyVehicleMapper.selectSdEmergencyVehicleById(id);
    }

    /**
     * 查询应急车辆列表
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 应急车辆
     */
    @Override
    public List<SdEmergencyVehicle> selectSdEmergencyVehicleList(SdEmergencyVehicle sdEmergencyVehicle) {
        List<SdEmergencyVehicle> emergencyVehicles = sdEmergencyVehicleMapper.selectSdEmergencyVehicleList(sdEmergencyVehicle);
        return emergencyVehicles;
    }

    /**
     * 新增应急车辆
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    @Override
    public int insertSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle) {
        sdEmergencyVehicle.setCreateTime(DateUtils.getNowDate());
        return sdEmergencyVehicleMapper.insertSdEmergencyVehicle(sdEmergencyVehicle);
    }

    /**
     * 修改应急车辆
     *
     * @param sdEmergencyVehicle 应急车辆
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public AjaxResult updateSdEmergencyVehicle(SdEmergencyVehicle sdEmergencyVehicle) {
        sdEmergencyVehicle.setUpdateTime(DateUtils.getNowDate());
        int code = updateVehicle(sdEmergencyVehicle);
        if(code == 200){
            return AjaxResult.success(sdEmergencyVehicleMapper.updateSdEmergencyVehicle(sdEmergencyVehicle));
        }else {
            return AjaxResult.error("应急车辆修改失败");
        }
    }

    /**
     * 批量删除应急车辆
     *
     * @param ids 需要删除的应急车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyVehicleByIds(Long[] ids) {
        return sdEmergencyVehicleMapper.deleteSdEmergencyVehicleByIds(ids);
    }

    /**
     * 删除应急车辆信息
     *
     * @param id 应急车辆主键
     * @return 结果
     */
    @Override
    public int deleteSdEmergencyVehicleById(Long id) {
        return sdEmergencyVehicleMapper.deleteSdEmergencyVehicleById(id);
    }

    @Override
    public List<Map<String, Object>> getOrg() {
        return sdEmergencyVehicleMapper.getOrg();
    }

    public String synVehicleData() {
        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Authorization", "Bearer fe446153-a911-40d4-ad3f-afe771e2318b");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        try {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(5 * 1000);
            httpRequestFactory.setConnectTimeout(5 * 1000);
            httpRequestFactory.setReadTimeout(5 * 1000);
            RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
            //调取第三方接口获取车辆数据
            ResponseEntity<String> exchange = restTemplate.exchange(synUrl.concat("?current=1&size=2000"), HttpMethod.GET, requestEntity, String.class);
            //解析车辆数据
            synVehicleDataList(exchange.getBody());
            log.info("返回值 --> {}", exchange.getBody());
            return JSONObject.toJSONString(exchange.getBody());
        } catch (Exception e) {
            log.error("应急车辆同步失败！{}", e.getMessage());
            return JSONObject.toJSONString(e.getMessage());
        }
    }

    @Override
    public SdEmergencyVehicle getVehicleDetails(String plateNumber) {
        SdEmergencyVehicle sdEmergencyVehicle = detailsVehicle(plateNumber);
        return sdEmergencyVehicle;
    }

    /**
     * 解析车辆数据
     * @param vehicleData
     * @return
     */
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void synVehicleDataList(String vehicleData){
        //删除应急车辆旧数据
        List<Long> collect = sdEmergencyVehicleMapper.selectSdEmergencyVehicleList(new SdEmergencyVehicle()).stream().map(SdEmergencyVehicle::getId).collect(Collectors.toList());
        if(collect.size() > 0){
            Long[] longs = collect.toArray(new Long[collect.size()]);
            sdEmergencyVehicleMapper.deleteSdEmergencyVehicleByIds(longs);
        }
        //解析数据
        String data = JSONObject.parseObject(vehicleData).getString("data");
        String records = JSONObject.parseObject(data).getString("records");
        JSONArray objects = JSONObject.parseArray(records);
        //车辆数据集合
        List<SdEmergencyVehicle> list = new ArrayList<>();
        for(int i = 0; i < objects.size(); i++){
            SdEmergencyVehicle sdEmergencyVehicle = new SdEmergencyVehicle();
            JSONObject object = JSONObject.parseObject(objects.get(i).toString());
            sdEmergencyVehicle.setOrgId(object.getString("deptId"));
            sdEmergencyVehicle.setPlateNumber(object.getString("carNum"));
            sdEmergencyVehicle.setvType(object.getString("ptypeName"));
            sdEmergencyVehicle.setvPlace(object.getString("locationName"));
            sdEmergencyVehicle.setUseStatus(object.getString("usageStateDesc"));
            sdEmergencyVehicle.setId(Long.valueOf(object.getString("id")));
            sdEmergencyVehicle.setAccState(object.getString("accState"));
            list.add(sdEmergencyVehicle);
        }
        sdEmergencyVehicleMapper.insertSdEmergencyVehicles(list);
    }

    /**
     * 修改第三方车辆状态
     */
    public int updateVehicle(SdEmergencyVehicle sdEmergencyVehicle){
        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Authorization", "Bearer fe446153-a911-40d4-ad3f-afe771e2318b");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        try {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(5 * 1000);
            httpRequestFactory.setConnectTimeout(5 * 1000);
            httpRequestFactory.setReadTimeout(5 * 1000);
            RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
            //拼接url
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(updateUrl)
                    .queryParam("carNum",sdEmergencyVehicle.getPlateNumber())
                    .queryParam("state",Integer.valueOf(sdEmergencyVehicle.getAccState()));
            //调取第三方接口获取车辆数据
            ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
            String code = JSONObject.parseObject(exchange.getBody()).getString("code");
            log.info("返回值 --> {}", exchange.getBody());
            return Integer.valueOf(code);
        } catch (Exception e) {
            log.error("应急车辆修改失败！{}", e.getMessage());
            return 500;
        }
    }

    /**
     * 查询车辆详情
     */
    public SdEmergencyVehicle detailsVehicle(String plateNumber){
        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Authorization", "Bearer fe446153-a911-40d4-ad3f-afe771e2318b");
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        SdEmergencyVehicle sdEmergencyVehicle = new SdEmergencyVehicle();
        try {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(5 * 1000);
            httpRequestFactory.setConnectTimeout(5 * 1000);
            httpRequestFactory.setReadTimeout(5 * 1000);
            RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
            //拼接url
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(detailUrl)
                    .queryParam("carNum",plateNumber);
            //调取第三方接口获取车辆数据
            ResponseEntity<String> exchange = restTemplate.exchange(builder.build().toUri(), HttpMethod.GET, requestEntity, String.class);
            String data = JSONObject.parseObject(exchange.getBody()).getString("data");
            JSONObject object = JSONObject.parseObject(data);

            sdEmergencyVehicle.setOrgId(object.getString("deptId"));
            sdEmergencyVehicle.setOwnerName(object.getString("ownerName"));
            sdEmergencyVehicle.setvPlace(object.getString("locationName"));
            sdEmergencyVehicle.setPlateNumber(object.getString("carNum"));
            sdEmergencyVehicle.setVehicleModel((object.getString("brandName") == null || object.getString("brandName") =="" ? "" : object.getString("brandName")).concat(object.getString("model")));
            sdEmergencyVehicle.setEtcStateDesc(object.getString("etcStateDesc"));
            sdEmergencyVehicle.setEtcTypeDesc(object.getString("etcTypeDesc"));
            sdEmergencyVehicle.setCarAge(object.getString("carAge"));
            sdEmergencyVehicle.setMileage(object.getString("mileage"));
            sdEmergencyVehicle.setvType(object.getString("ptypeName"));
            sdEmergencyVehicle.setUseStatus(object.getString("usageStateDesc"));
            sdEmergencyVehicle.setAccState(object.getString("accState"));
            log.info("返回值 --> {}", exchange.getBody());
        } catch (Exception e) {
            log.error("应急车辆修改失败！{}", e.getMessage());
        }
        return sdEmergencyVehicle;
    }
}
