package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 个人信息 业务处理
 * 
 *  *
 */
@RestController
@RequestMapping("/app/system/user/profile")
public class SysAppProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Resource
    Environment environment;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile(HttpServletRequest request)
    {
        String ip = request.getRemoteHost();
        String port = environment.getProperty("server.port");
        LoginUser loginUser = getLoginUser();
        //头像地址拼接
        String avatar = "http://"+ip+":"+port+"/app"+loginUser.getUser().getAvatar();
        loginUser.getUser().setAvatar(avatar);
        SysUser user = loginUser.getUser();
        AjaxResult ajax = AjaxResult.success(user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(loginUser.getUsername()));
        ajax.put("postGroup", userService.selectUserPostGroup(loginUser.getUsername()));
        return ajax;
    }


}
