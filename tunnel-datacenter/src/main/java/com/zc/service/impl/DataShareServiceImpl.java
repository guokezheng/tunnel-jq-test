package com.zc.service.impl;

import com.zc.domain.dataShareDto.*;
import com.zc.entity.*;
import com.zc.service.DataShareService;
import com.zc.utils.RtErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class DataShareServiceImpl implements DataShareService {

    /**
     * 气象设备环境数据查询
     * @param meteorologicalEquipmentDto
     * @return
     */
    @Override
    public Object queryMeteorologicalEquipment(MeteorologicalEquipmentDto meteorologicalEquipmentDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/dev_G21_tr_data";
        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", meteorologicalEquipmentDto.getSysId());
        requestBody.put("service_code", meteorologicalEquipmentDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<Meteorological>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<Meteorological>>() {
        });
        List<Meteorological> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";


    }

    /**
     * 能见度设备数据查询
     * @param visibilityDeviceDto
     * @return
     */
    @Override
    public Object queryVisibilityDevice(VisibilityDeviceDto visibilityDeviceDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/dev_G21_tr_datavisibility";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", visibilityDeviceDto.getSysId());
        requestBody.put("service_code", visibilityDeviceDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<Visibility>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<Visibility>>() {
        });
        List<Visibility> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    /**
     * 桥梁明细查询
     * @param bridgeDataDto
     * @return
     */
    @Override
    public Object queryBridgeData(BridgeDataDto bridgeDataDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/br_G21_tr_databridge";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", bridgeDataDto.getSysId());
        requestBody.put("service_code", bridgeDataDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<BridgeData>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<BridgeData>>() {
        });
        List<BridgeData> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    /**
     * 枢纽立交信息查询
     * @param hingeInterchangeDto
     * @return
     */
    @Override
    public Object queryHingeInterchange(HingeInterchangeDto hingeInterchangeDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/rd_G21_tr_datainterchange";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", hingeInterchangeDto.getSysId());
        requestBody.put("service_code", hingeInterchangeDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<JunctionInterchange>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<JunctionInterchange>>() {
        });
        List<JunctionInterchange> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    /**
     * 服务区详情查询
     * @param serviceAreaDto
     * @return
     */
    @Override
    public Object queryServiceArea(ServiceAreaDto serviceAreaDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/sa_G21_tr_dataservicearea";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", serviceAreaDto.getSysId());
        requestBody.put("service_code", serviceAreaDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<ServiceArea>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<ServiceArea>>() {
        });
        List<ServiceArea> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    /**
     * 收费车道信息查询
     * @param chargeVehicleLaneDto
     * @return
     */
    @Override
    public Object queryChargeVehicleLane(ChargeVehicleLaneDto chargeVehicleLaneDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/ts_G21_tr_datalane";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", chargeVehicleLaneDto.getSysId());
        requestBody.put("service_code", chargeVehicleLaneDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<TollLane>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<TollLane>>() {
        });
        List<TollLane> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    /**
     * 收费站车流量信息
     * @param tollStatTrafficFlowDto
     * @return
     */
    @Override
    public Object queryTollStatTrafficFlow(TollStatTrafficFlowDto tollStatTrafficFlowDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/ts_a_tr_regionGantryTrafficflowAndFeeSumByModel";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", tollStatTrafficFlowDto.getSysId());
        requestBody.put("service_code", tollStatTrafficFlowDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<TollStatCarFlow>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<TollStatCarFlow>>() {
        });
        List<TollStatCarFlow> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    /**
     * 微波车检器周期过车数据查询
     * @param microwaveCarDetectorDto
     * @return
     */
    @Override
    public Object queryMicrowaveCarDetector(MicrowaveCarDetectorDto microwaveCarDetectorDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/dev_G21_tr_datavehicle";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", microwaveCarDetectorDto.getSysId());
        requestBody.put("service_code", microwaveCarDetectorDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<MicrowaveCarDetector>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<MicrowaveCarDetector>>() {
        });
        List<MicrowaveCarDetector> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    /**
     * 微波车检器周期过车数据查询2
     * @param microwaveDataDto
     * @return
     */
    @Override
    public Object queryMicrowaveData(MicrowaveDataDto microwaveDataDto) {

        String url = "http://10.166.139.16:8080/api/dc/query/dev_G21_tr_dataroarddetector";

        HttpHeaders headers = new HttpHeaders();
        String dataToken = getDataToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + dataToken);
        headers.setContentType(type);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("sysid", microwaveDataDto.getSysId());
        requestBody.put("service_code", microwaveDataDto.getServiceCode());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<List<MicrowaveData>> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<MicrowaveData>>() {
        });
        List<MicrowaveData> body = exchange.getBody();
        if(body != null) {
            return body;
        }
        return "未查到数据";
    }

    //获取token
    public static String  getDataToken() {
        String url = "http://10.166.139.16:8080/auth/client/token?appId=sdgs_it_hs_jiqing&clientSecret=sdgs_it_hs_jiqing";
        RestTemplate restTemplate = new RestTemplate();
        Map map = restTemplate.postForObject(url, null,Map.class);
        String accessToken = map.get("access_token").toString();
        return  accessToken;
    }
}
