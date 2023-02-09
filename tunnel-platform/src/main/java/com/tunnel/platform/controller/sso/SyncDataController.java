package com.tunnel.platform.controller.sso;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDeptSg;
import com.ruoyi.system.mapper.SysDeptSgMapper;
import com.tunnel.platform.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于同步集团数据库数据的控制器
 * 暂时不对外开放
 */
// @RestController
// @RequestMapping(value = "/syncData")
public class SyncDataController {

    @Autowired
    private SysDeptSgMapper deptSgMapper;

    /**
     * 同步部门
     */
    // @GetMapping(value = "/syncDept")
    public AjaxResult syncDept() {
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
            SysDeptSg sysDeptSg = new SysDeptSg();
            for (Map<String, Object> map : deptList) {
                String id = (String) map.get("id");
                String pid = (String) map.get("pid");
                // String pids = (String) map.get("pids");
                String code = (String) map.get("code");
                String pCode = (String) map.get("pcode");
                String name = (String) map.get("name");
                String type = (String) map.get("type");
                Long sort = (Long) map.get("sort");
                String spell = (String) map.get("spell");
                Integer hasLeaf = (Integer) map.get("hasLeaf");
                // String property = (String) map.get("property");
                String fullName = (String) map.get("fullName");

                sysDeptSg.setId(id);
                sysDeptSg.setPid(pid);
                // sysDeptSg.setPids(pids);
                sysDeptSg.setCode(code);
                sysDeptSg.setpCode(pCode);
                sysDeptSg.setName(name);
                sysDeptSg.setType(type);
                sysDeptSg.setSort(sort);
                sysDeptSg.setSpell(spell);
                sysDeptSg.setHasLeaf(hasLeaf);
                // sysDeptSg.setProperty(property);
                sysDeptSg.setFullName(fullName);

                SysDeptSg deptSg = deptSgMapper.selectSysDeptSgById(id);
                if (null != deptSg) {
                    // 如果存在就更新
                    deptSgMapper.updateSysDeptSg(deptSg);
                } else {
                    // 如果不存在就新增
                    deptSgMapper.insertSysDeptSg(deptSg);
                }
            }
        }

        return AjaxResult.success("部门数据同步完成");
    }
}
