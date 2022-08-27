package com.zc.service.impl;

import com.zc.domain.informationCenterDto.*;
import com.zc.service.InformationDicService;
import com.zc.utils.RtErrorHandler;

import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


@Service
public class InformationDicServiceImpl implements InformationDicService {

    /**
     * 查询信息分中心字典
     * @param opmaSubCenterDto
     * @return
     * @throws URISyntaxException
     */
    @Override
    public Object queryInformatDic(OpmaSubCenterDto opmaSubCenterDto) throws URISyntaxException {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmsplitsubcenterri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer"+token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("opmaCliqueId", opmaSubCenterDto.getOpmaCliqueId());
        requestBody.put("opmaCliqueName", opmaSubCenterDto.getOpmaCliqueName());
        requestBody.put("opmaXxsubcenterId", opmaSubCenterDto.getOpmaXxsubcenterId());
        requestBody.put("opmaXxsubcenterName", opmaSubCenterDto.getOpmaXxsubcenterName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询分中心（拆账）
     * @param splitSubCenterDto
     * @return
     */
    @Override
    public Object querySplitSubCenterInformation(SplitSubCenterDto splitSubCenterDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmsplitsubcenterri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("splitSectionId", splitSubCenterDto.getSplitSectionId());
        requestBody.put("splitSectionName", splitSubCenterDto.getSplitSectionName());
        requestBody.put("splitSubcenterId", splitSubCenterDto.getSplitSubcenterId());
        requestBody.put("splitSubcenterName", splitSubCenterDto.getSplitSubcenterName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询国标公路字典
     * @param roadDto
     * @return
     */
    @Override
    public Object queryRoadInformation(RoadDto roadDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmroadri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("roadId", roadDto.getRoadId());
        requestBody.put("roadName", roadDto.getRoadName());
        requestBody.put("tecLevel", roadDto.getTecLevel());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 分页查询收费站字典
     * @param tollStatDto
     * @return
     */
    @Override
    public Object queryTollStartInformation(TollStatDto tollStatDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmtollstationri/page";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("limit", tollStatDto.getLimit());
        requestBody.put("opmaCliqueId", tollStatDto.getOpmaCliqueId());
        requestBody.put("opmaCliqueName", tollStatDto.getOpmaCliqueName());
        requestBody.put("order", tollStatDto.getOrder());
        requestBody.put("orderField", tollStatDto.getOrderField());
        requestBody.put("page", tollStatDto.getPage());
        requestBody.put("regionId", tollStatDto.getRegionId());
        requestBody.put("regionName", tollStatDto.getRegionName());
        requestBody.put("roadId", tollStatDto.getRoadId());
        requestBody.put("roadName", tollStatDto.getRoadName());
        requestBody.put("tollStationFullName", tollStatDto.getTollStationFullName());
        requestBody.put("tollStationHex", tollStatDto.getTollStationHex());
        requestBody.put("tollStationId", tollStatDto.getTollStationId());
        requestBody.put("tollStationName", tollStatDto.getTollStationName());
        requestBody.put("tollStationProvinceId", tollStatDto.getTollStationProvinceId());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询收费路段字典
     * @param tollSectionDto
     * @return
     */
    @Override
    public Object queryTollSectionInformation(TollSectionDto tollSectionDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmtollsectionri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("roadId", tollSectionDto.getRoadId());
        requestBody.put("roadName", tollSectionDto.getRoadName());
        requestBody.put("tollSectionId", tollSectionDto.getTollSectionId());
        requestBody.put("tollSectionName", tollSectionDto.getTollSectionName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询管养公司编码信息
     * @param opmaManagerDto
     * @return
     */
    @Override
    public Object queryOpmaManagerInformation(OpmaManagerDto opmaManagerDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmopmamanagercropri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("opmaCliqueId", opmaManagerDto.getOpmaCliqueId());
        requestBody.put("opmaCliqueName", opmaManagerDto.getOpmaCliqueName());
        requestBody.put("opmaManagerCropId", opmaManagerDto.getOpmaManagerCropId());
        requestBody.put("opmaManagerCropName", opmaManagerDto.getOpmaManagerCropName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询管养单位字典
     * @param managerCompanyDto
     * @return
     */
    @Override
    public Object queryManagerCompanyInformation(ManagerCompanyDto managerCompanyDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmopmamanagerri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("opmaCliqueId", managerCompanyDto.getOpmaCliqueId());
        requestBody.put("opmaCliqueName", managerCompanyDto.getOpmaCliqueName());
        requestBody.put("opmaManagerId", managerCompanyDto.getOpmaManagerId());
        requestBody.put("opmaManagerName", managerCompanyDto.getOpmaManagerName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询管辖路段（拆账）
     * @param splitSectionDto
     * @return
     */
    @Override
    public Object querySplitSectionInformation(SplitSectionDto splitSectionDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmsplitsectionri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("splitManagerId", splitSectionDto.getSplitManagerId());
        requestBody.put("splitManagerName", splitSectionDto.getSplitManagerName());
        requestBody.put("splitSectionId", splitSectionDto.getSplitSectionId());
        requestBody.put("splitSectionName", splitSectionDto.getSplitSectionName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询运营管理单位（拆账）表
     * @param managerDto
     * @return
     */
    @Override
    public Object querySplitManagerInformation(SplitManagerDto managerDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmsplitmanagerri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("splitManagerId", managerDto.getSplitManagerId());
        requestBody.put("splitManagerName", managerDto.getSplitManagerName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 查询集团公司表信息
     * @param opmaCliqueDto
     * @return
     */
    @Override
    public Object queryOpmaCliqueInformation(OpmaCliqueDto opmaCliqueDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmopmacliqueri/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("opmaCliqueId", opmaCliqueDto.getOpmaCliqueId());
        requestBody.put("opmaCliqueName",  opmaCliqueDto.getOpmaCliqueName());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 节点信息查询列表
     * @param nodeDto
     * @return
     */
    @Override
    public Object queryNodeInformation(NodeDto nodeDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmnodeinfo/list";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("distance", nodeDto.getDistance());
        requestBody.put("id", nodeDto.getId());
        requestBody.put("lat", nodeDto.getLat());
        requestBody.put("lng", nodeDto.getLng());
        requestBody.put("managerId", nodeDto.getManagerId());
        requestBody.put("nodeType", nodeDto.getNodeType());
        requestBody.put("opmaRoadId", nodeDto.getOpmaRoadId());
        requestBody.put("opmaSectionId", nodeDto.getOpmaSectionId());
        requestBody.put("regionId", nodeDto.getRegionId());


        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    /**
     * 相邻门架查询
     * @param adjacentGantryDto
     * @return
     */
    @Override
    public Object queryAdjacentGantry(AdjacentGantryDto adjacentGantryDto) {

        String url = "http://10.200.1.93:31006/masterdata/dimmdmnodeinfo/round_gantry";

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.add("Authorization", "Bearer" + token);
        headers.setContentType(type);
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", adjacentGantryDto.getId());

        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new RtErrorHandler());

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        Map body = exchange.getBody();
        Object data = body.get("data");
        if (data != null) {
            return data;
        }
        return "未查到数据";
    }

    //获取token
    public static String  getToken() {
        String url = "http://10.200.1.93:31006/auth/client/token?appId=jq_master_data&clientSecret=qz_opm";
        RestTemplate restTemplate = new RestTemplate();
        Map map = restTemplate.postForObject(url, null,Map.class);
        String accessToken = map.get("access_token").toString();
        return  accessToken;
    }
}
