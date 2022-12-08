package com.tunnel.platform.service.sso;

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
import com.tunnel.platform.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThirdPartLoginService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;


    public List<Map<String, Object>> getUsersByDept(String token, String companyName, String deptName) {
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", "Bearer " + token);
        //设置请求体
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        //发送请求

        // String url = userListUrl;
        String url = "http://localhost:8000/Robot/getUsersByDept";

        url = UriComponentsBuilder.fromUriString(url).queryParam("companyName", companyName).queryParam("deptName", deptName).build().toString();

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, httpEntity, Map.class);
        HashMap<String, Object> body = (HashMap<String, Object>) responseEntity.getBody();
        List<Map<String, Object>> list = null;
        if (body.get("code").equals(200)) {
            list = (List<Map<String, Object>>) body.get("data");
        }
        return list;
    }


    public AjaxResult login(String username) {
        //获取授权码
        String authCode = AuthUtil.getAuthCode(username);

        AjaxResult ajaxResult = null;
        if (null != authCode) {
            //获取token
            String token = AuthUtil.getToken(authCode);
            if (null != token) {
                //获取用户信息
                Map<String, Object> userInfo = AuthUtil.getUserInfo(token);
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


    public void metho() {
        AuthUtil.getToken("12121");




    }


}
