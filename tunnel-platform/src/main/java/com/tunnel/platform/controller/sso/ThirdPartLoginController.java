package com.tunnel.platform.controller.sso;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.platform.service.sso.ThirdPartLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 第三方登录控制器
 */
@RestController
@RequestMapping("/thirdPart")
public class ThirdPartLoginController {

   @Autowired
   private ThirdPartLoginService thirdPartLoginService;



   /**
    * 单点登录方法
    *
    * @param username 第三方用户名
    * @return 结果
    */
   @GetMapping("/login")
   public AjaxResult login(@RequestParam String username) {
      return thirdPartLoginService.login(username);
   }




}
