package com.tunnel.platform.controller.sso;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.tunnel.platform.service.sso.ThirdPartLoginService;
import com.tunnel.platform.util.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 第三方登录控制器
 */
@Api(tags = "单点登录")
@RestController
@RequestMapping("/thirdPart")
public class ThirdPartLoginController {

    @Autowired
    private ThirdPartLoginService thirdPartLoginService;

    @Autowired
    private AuthUtil authUtil;


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

    @Autowired
    private SysDeptMapper deptMapper;

    @PostMapping(value = "/getTreeAll")
    public AjaxResult getDeptTree(String id) {
        // List<Map<String, Object>> deptTree = thirdPartLoginService.getDeptTree("5b7a963a-6363-4ca9-b691-b534835c4bcb", id);
        // thirdPartLoginService.metho();

        // 获取授权码
        String authCode = AuthUtil.getAuthCode("admin");
        // 获取token
        String token = AuthUtil.getToken(authCode);
        // 获取部门树
        List<Map<String, Object>> deptTree = AuthUtil.getDeptTree(token, null);
        // 部门树转List
        List<Map<String, Object>> deptList = new ArrayList<>();
        AuthUtil.treeToList(deptTree, deptList);

        if (deptList.size() > 0) {
            SysDept sysDept = new SysDept();
            for (Map<String, Object> map : deptList) {
                String jtDeptId = (String) map.get("id");
                String pid = (String) map.get("pid");
                String name = (String) map.get("name");
                String sort = (String) map.get("sort");


                sysDept.setDeptName(name);
                sysDept.setOrderNum(sort);

//                SysDept dept = deptMapper.selectJtDeptById(jtDeptId);
//                if (null!= dept) {
//                    deptMapper.updateDept(sysDept);
//                }else {
//                    deptMapper.insertDept(sysDept);
//                }
            }
        }

        return AjaxResult.success();
    }

    @PostMapping(value = "/getUserListByCompanyAndDept")
    public AjaxResult getUserListByCompanyAndDept(String companyName, String deptName) {
        List<Map<String, Object>> userList = thirdPartLoginService.getUsersByDept(null, companyName, deptName);
        return AjaxResult.success(userList);
    }

}
