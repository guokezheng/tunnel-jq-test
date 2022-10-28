package com.tunnel.platform.service.sso;

import cn.hutool.json.JSONUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class ThirdPartLoginService {

    @Value("${sso.authCode}")
    private String authCodeUrl;

    @Value("${sso.token}")
    private String tokenUrl;

    @Value("${sso.userInfo}")
    private String userInfoUrl;

    @Value("${sso.appId}")
    private String appId;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    public String getAuthCode(String username) {
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        String url = authCodeUrl + "?username=" + username;
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);

        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        String authCode = null;
        if (body.get("code").equals(0)) {
            authCode = (String) body.get("data");
        }
        return authCode;
    }


    public String getToken(String authCode, String appId) {
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        String url = tokenUrl + "?code=" + authCode + "&app_id=" + appId;
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);

        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        String token = null;
        if (body.get("code").equals(0)) {
            token = (String) body.get("access_token");
        }
        return token;
    }

    public Map<String, Object> getUserInfo(String token) {
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(userInfoUrl, httpEntity, Map.class);

        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        Map<String, Object> userInfo = null;
        if (body.get("code").equals(0)) {
            userInfo = JSONUtil.toBean(JSONUtil.parse(body.get("data")).toString(), Map.class);
        }
        return userInfo;
    }

    public AjaxResult login(String username) {
        //获取授权码
        String authCode = getAuthCode(username);

        AjaxResult ajaxResult = null;
        if (null != getAuthCode(username)) {
            //获取token
            String token = getToken(authCode, appId);
            if (null != token) {
                //获取用户信息
                Map<String, Object> userInfo = getUserInfo(token);
                //通过解析token得到的第三方系统用户名称
                String thirdUserName = (String) userInfo.get("username");
                //查询己方系统中是否存在此用户
                SysUser sysUser = sysUserService.selectUserByUserName(thirdUserName);
                if (null != sysUser) {
                    Authentication authentication = null;
                    try {
                        authentication = authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(username, sysUser.getPassword()));
                    } catch (Exception e) {
                        if (e instanceof BadCredentialsException) {
                            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                            throw new UserPasswordNotMatchException();
                        } else {
                            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                            throw new ServiceException(e.getMessage());
                        }
                    }
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
                    LoginUser loginUser = (LoginUser) authentication.getPrincipal();

                    ajaxResult = AjaxResult.success().put(Constants.TOKEN, tokenService.createToken(loginUser));
                } else {
                    ajaxResult = AjaxResult.error("系统中无此用户！");
                }

            }
        }
        return ajaxResult;
    }
}
