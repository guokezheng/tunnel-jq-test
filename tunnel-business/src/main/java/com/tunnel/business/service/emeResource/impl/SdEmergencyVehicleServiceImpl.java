package com.tunnel.business.service.emeResource.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.emeResource.SdEmergencyVehicle;
import com.tunnel.business.mapper.emeResource.SdEmergencyVehicleMapper;
import com.tunnel.business.service.emeResource.ISdEmergencyVehicleService;
import com.tunnel.business.utils.sso.AuthUtil;
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

    @Value("${sso.emergencyVehicle.edit}")
    private String updateUrl;

    @Value("${sso.emergencyVehicle.syn}")
    private String synUrl;

    @Value("${sso.emergencyVehicle.detail}")
    private String detailUrl;


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
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String deptId = user.getDeptId();
        sdEmergencyVehicle.setOrgName(deptId);
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

    @Override
    public String synVehicleData() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String deptId = user.getDeptId();
        //获取token
        String token = AuthUtil.getGeneralToken();
        if(token == ""){
            return "";
        }
        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Authorization", "Bearer "+ token);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestHeaders);
        try {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(5 * 1000);
            httpRequestFactory.setConnectTimeout(5 * 1000);
            httpRequestFactory.setReadTimeout(5 * 1000);
            RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
            //调取第三方接口获取车辆数据
            ResponseEntity<String> exchange = restTemplate.exchange(synUrl.concat("?current=1&size=2000&deptId="+deptId), HttpMethod.GET, requestEntity, String.class);
            //解析车辆数据
            synVehicleDataList(exchange.getBody(),deptId);
            log.info("返回值 --> {}", exchange.getBody());
            return JSONObject.toJSONString(exchange.getBody());
        } catch (Exception e) {
            log.error("应急车辆同步失败！{}", e.getMessage());
            return "";
        }
    }

    @Override
    public SdEmergencyVehicle getVehicleDetails(String plateNumber) {
        //调用第三方接口查询详情
        SdEmergencyVehicle sdEmergencyVehicle = detailsVehicle(plateNumber);
        //如果查询不到则查询数据库
        List<SdEmergencyVehicle> list = new ArrayList<>();
        if(sdEmergencyVehicle == null){
            sdEmergencyVehicle.setPlateNumber(plateNumber);
            list = sdEmergencyVehicleMapper.selectSdEmergencyVehicleList(sdEmergencyVehicle);
            if(list.size() > 0){
                sdEmergencyVehicle = list.get(0);
            }
        }
        return sdEmergencyVehicle;
    }

    /**
     * 解析车辆数据
     * @param vehicleData
     * @return
     */
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void synVehicleDataList(String vehicleData, String deptId){
        //删除应急车辆旧数据
        /*SdEmergencyVehicle sdEmergencyVehicle1 = new SdEmergencyVehicle();
        sdEmergencyVehicle1.setOrgName(deptId);
        List<Long> collect = sdEmergencyVehicleMapper.selectSdEmergencyVehicleList(sdEmergencyVehicle1).stream().map(SdEmergencyVehicle::getId).collect(Collectors.toList());
        if(collect.size() > 0){
            Long[] longs = collect.toArray(new Long[collect.size()]);
            sdEmergencyVehicleMapper.deleteSdEmergencyVehicleByIds(longs);
        }*/
        //解析数据
        String data = JSONObject.parseObject(vehicleData).getString("data");
        String records = JSONObject.parseObject(data).getString("records");
        JSONArray objects = JSONObject.parseArray(records);
        //查询数据库所有车辆数据
        List<SdEmergencyVehicle> emergencyVehicles = sdEmergencyVehicleMapper.selectSdEmergencyVehicleList(new SdEmergencyVehicle());
        //车辆数据集合(新增)
        List<SdEmergencyVehicle> addList = new ArrayList<>();
        List<SdEmergencyVehicle> editList = new ArrayList<>();
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
            sdEmergencyVehicle.setOwnerName(object.getString("ownerName") == null ? null : object.getString("ownerName"));
            sdEmergencyVehicle.setVehicleModel(object.getString("brandName") == null ? null : object.getString("brandName"));
            sdEmergencyVehicle.setEtcStateDesc(object.getString("etcStateDesc") == null ? null : object.getString("etcStateDesc"));
            sdEmergencyVehicle.setEtcTypeDesc(object.getString("etcTypeDesc") == null ? null : object.getString("etcTypeDesc"));
            sdEmergencyVehicle.setCarAge(object.getString("carAge") == null ? null : object.getString("carAge"));
            sdEmergencyVehicle.setMileage(object.getString("mileage") == null ? null : object.getString("mileage"));
            //判断是否存在于数据库
            int number = 0;
            for(SdEmergencyVehicle item : emergencyVehicles){
                if(item.getId().toString().equals(sdEmergencyVehicle.getId().toString())){
                    number = 1;
                    break;
                }
            }
            if(number == 0){
                addList.add(sdEmergencyVehicle);
            }else {
                editList.add(sdEmergencyVehicle);
            }
            if(sdEmergencyVehicle.getOrgId() == null || sdEmergencyVehicle.getOrgId() == ""){
                System.out.println("");
            }
        }
        //新增
        if(addList.size() > 0){
            sdEmergencyVehicleMapper.insertSdEmergencyVehicles(addList);
        }
        //修改
        if(editList.size() > 0){
            sdEmergencyVehicleMapper.updateSdEmergencyVehicles(editList);
        }
    }

    /**
     * 修改第三方车辆状态
     */
    public int updateVehicle(SdEmergencyVehicle sdEmergencyVehicle){
        //获取token
        String token = AuthUtil.getGeneralToken();

        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Authorization", "Bearer " + token);
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

        //获取token
        String token = AuthUtil.getGeneralToken();

        //请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        //设置JSON格式数据
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.add("Authorization", "Bearer " + token);
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
            if(data != null){
                JSONObject object = JSONObject.parseObject(data);
                sdEmergencyVehicle.setOrgId(object.getString("deptId"));
                sdEmergencyVehicle.setOwnerName(object.getString("ownerName"));
                sdEmergencyVehicle.setvPlace(object.getString("locationName"));
                sdEmergencyVehicle.setPlateNumber(object.getString("carNum"));
                sdEmergencyVehicle.setVehicleModel((object.getString("brandName") == null || "".equals(object.getString("brandName")) ? "" : object.getString("brandName")).concat(object.getString("model")));
                sdEmergencyVehicle.setEtcStateDesc(object.getString("etcStateDesc"));
                sdEmergencyVehicle.setEtcTypeDesc(object.getString("etcTypeDesc"));
                sdEmergencyVehicle.setCarAge(object.getString("carAge"));
                sdEmergencyVehicle.setMileage(object.getString("mileage"));
                sdEmergencyVehicle.setvType(object.getString("ptypeName"));
                sdEmergencyVehicle.setUseStatus(object.getString("usageStateDesc"));
                sdEmergencyVehicle.setAccState(object.getString("accState"));
            }else {
                return null;
            }
            log.info("返回值 --> {}", exchange.getBody());
        } catch (Exception e) {
            log.error("应急车辆获取详情报错！{}", e.getMessage());
            return null;
        }
        return sdEmergencyVehicle;
    }
}
