package com.tunnel.webthings.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.tunnel.webthings.domain.DataSystemUsers;
import com.tunnel.webthings.service.RoleService;
import com.tunnel.webthings.vo.DataSystemUsersVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZHC
 * {@code @date} 2022/8/24 9:40
 * 数据中台系统用户的Service接口的实现
 */
@Service
public class RoleServiceImpl implements RoleService {

    public static final Logger log = LoggerFactory.getLogger(RoleService.class);

    @Value("${iot.token}")
    private String token;

    @Value("${iot.host}")
    private String host;

    @Value("${iot.roleList}")
    private String roleList;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据条件查询系统用户
     * @param vo 需要参数
     * @return 系统用户
     */
    @Override
    public List<DataSystemUsers> getRoleList(DataSystemUsersVO vo) {
        // 请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        // 设置Params
        //requestHeaders.setContentType();
        String token = getDataToken();
        requestHeaders.add("Authorization","Bearer "+token);
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit",vo.getLimit());
        map.put("page", vo.getPage());
        map.put("username", vo.getUsername());
        // 请求体
        HttpEntity<HashMap<String, Object>> httpEntity = new HttpEntity<>(requestHeaders);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(host).append(roleList).append("?limit=")
                .append(map.get("limit")).append("&page=").append(map.get("page")).append("&username=").append(map.get("username"));
        String url = stringBuffer.toString();
        // 发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);
        // 接收返回参数
        Map body = responseEntity.getBody();
        if (body.get("code").equals(0)) {
            JSON parse = JSONUtil.parse(body.get("data"));
            Map data = JSONUtil.toBean(parse.toString(), Map.class);
            JSON records = JSONUtil.parse(data.get("records"));
            List<DataSystemUsers> dataSystemUsers = JSONUtil.toList(records.toString(), DataSystemUsers.class);
            return dataSystemUsers;
        }
        return null;
    }

    /**
     *获取token
     */
    private String getDataToken() {
        RestTemplate restTemplate = new RestTemplate();
        Map map = restTemplate.postForObject(token, null,Map.class);
        String accessToken = map.get("access_token").toString();
        return  accessToken;
    }

}
