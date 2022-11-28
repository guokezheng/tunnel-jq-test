package com.tunnel.platform.task;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.tunnel.platform.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("userDeptTask")
public class UserDeptTask {

    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 同步部门
     */
    public void syncDept() {
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
//                String jtDeptId = (String) map.get("id");
                String pid = (String) map.get("pid");
                String name = (String) map.get("name");
                String sort = (String) map.get("sort");

//                sysDept.setJtDeptId(jtDeptId);
//                sysDept.setJtPid(pid);
//                sysDept.setDeptName(name);
//                sysDept.setOrderNum(sort);
//
//                SysDept dept = deptMapper.selectJtDeptById(jtDeptId);
//                if (null!= dept) {
//                    // 如果存在就更新
//                    deptMapper.updateDept(sysDept);
//                }else {
//                    // 如果不存在就新增
//                    deptMapper.insertDept(sysDept);
//                }
            }
        }
    }

    /**
     * 同步用户
     */
    public void syncUser() {

    }

}

