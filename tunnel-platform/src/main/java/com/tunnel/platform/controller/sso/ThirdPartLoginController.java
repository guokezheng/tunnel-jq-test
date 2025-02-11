package com.tunnel.platform.controller.sso;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.sso.ThirdPartLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 第三方登录控制器
 */
@Api(tags = "单点登录")
@RestController
@RequestMapping("/thirdPart")
@ApiSupport(order = 16)
public class ThirdPartLoginController {

    @Autowired
    private ThirdPartLoginService thirdPartLoginService;


    /**
     * 单点登录方法--测试环境
     *
     * @param username 第三方用户名
     * @return 结果
     */
    @GetMapping("/testLogin")
    @ApiOperation(value = "单点登录方法")
    public AjaxResult testLogin(@RequestParam String username) {

        return thirdPartLoginService.testLogin(username);
    }

    /**
     * 单点登录方法--正式环境
     *
     * @param loginInfo 登录code
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation(value = "单点登录方法")
    public AjaxResult login(@RequestBody Map loginInfo) {

        String code = Optional.ofNullable(loginInfo.get("code")).orElse("").toString();
        return thirdPartLoginService.login(code);
    }


}
