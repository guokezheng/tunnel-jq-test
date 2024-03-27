package com.tunnel.platform.controller.sso;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.tunnel.platform.config.OpmConfig;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * describe: 外部系统登录系统接口
 *
 * @author zs
 * @date 2023/1/13
 */
@Api(tags="外部系统登录系统接口")
@RestController
@RequestMapping("/api")
@ApiSupport(order = 16)
public class ExternalTokenController {


    @Autowired
    private OpmConfig opmConfig;

    @Resource
    private AuthenticationManager authenticationManager;


    @Autowired
    private TokenService tokenService;


    private static final Logger log = LoggerFactory.getLogger(ExternalTokenController.class);

    /**
     * 获取token
     * @param appId 访问标识
     * @return 结果
     */
    @ApiOperation("外部系统登录系统接口")
    @PostMapping("/getToken")
    @ApiResponses({
            @ApiResponse(code=200,message="请求成功"),
            @ApiResponse(code=500,message="系统内部错误")
    })
    public AjaxResult login(@ApiParam(value = "访问标识", required = true) @RequestParam String appId) {
        AjaxResult ajax = AjaxResult.success();
        //验证访问标识
        if(!opmConfig.appId.equals(appId)){
            ajax.put(AjaxResult.MSG_TAG,"访问标识不正确");
            return ajax;
        }
        String username = opmConfig.loginName;

        // 用户验证
        Authentication authentication = null;
        try {
            //PreAuthenticatedAuthenticationToken不对密码进行校验,密码可以为空；如果用户名不对应，报错提示登录用户不存在；
            //authenticate方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new PreAuthenticatedAuthenticationToken(username,""));
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            // 生成token
            String token = tokenService.createToken(loginUser);
            ajax.put(Constants.TOKEN, token);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                log.error("外部系统获取token方法调用：用户名错误",e.getMessage());
            } else {
                log.error("外部系统获取token方法调用报错：",e.getMessage());
            }
            ajax.put(AjaxResult.MSG_TAG,"系统内部错误");
            ajax.put(AjaxResult.CODE_TAG, HttpStatus.ERROR);
        }

        return ajax;
    }
}
