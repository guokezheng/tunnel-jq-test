package com.tunnel.webthings.controller;

import com.ruoyi.common.core.page.Result;
import com.tunnel.webthings.service.RoleService;
import com.tunnel.webthings.service.TunnelIotDeviceService;
import com.tunnel.webthings.vo.DataSystemUsersVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZHC
 * @date 2022/8/24 9:37
 * 数据中台系统用户
 */
@RestController
@RequestMapping("/data/sys/users")
public class RoleController {

    @Autowired
    RoleService roleService;

    @Autowired
    TunnelIotDeviceService iotDeviceService;

    /**
     * 查询符合用户名的系统用户
     * @param vo 接收分页和用户名
     * @return 返回
     */
    @PostMapping("/page")
    public Result getListByUserName(DataSystemUsersVO vo) {
        return Result.success(roleService.getRoleList(vo));
    }

    /**
     * 可以修改本地库的图片路径
     */
    @PostMapping("/aaa")
    public void getUrl(){
        iotDeviceService.method();
    }


}
