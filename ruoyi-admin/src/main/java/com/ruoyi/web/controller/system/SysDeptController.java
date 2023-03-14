package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.TreeDeptSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 部门信息
 * <p>
 * *
 */
@RestController
@RequestMapping("/system/dept")
@Api(tags = "部门管理")
public class SysDeptController extends BaseController {
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
//    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @ApiOperation("获取部门列表")
    public Result list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Result.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    @ApiOperation("查询部门列表（排除节点）")
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = false, dataType = "String", paramType = "path", dataTypeClass = Long.class)
    public Result excludeChild(@PathVariable(value = "deptId", required = false) String deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().equals(deptId)
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return Result.success(depts);
    }

    /**
     * 根据部门编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    @ApiOperation("根据部门编号获取详细信息")
    @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "path", dataTypeClass = Long.class)
    public Result<SysDept> getInfo(@PathVariable String deptId) {
        deptService.checkDeptDataScope(deptId);
        return Result.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    @ApiOperation("获取部门下拉树列表")
    public Result treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        List<TreeDeptSelect> treeDept = deptService.buildDeptTreeSelect(depts);
        return Result.success(treeDept);
    }

    @GetMapping("/getTreeByDeptId")
    @ApiOperation("获取部门及其祖先树")
    public Result getTreeByDeptId(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }


        List<TreeDeptSelect> treeDept = null;
        boolean hasChildren = deptService.hasChildByDeptId(deptId);
        if (hasChildren) {
            treeDept = deptService.buildDeptTreeSelect(depts);
        }else{
            List<SysDept> target = new ArrayList<>();
            getTreeListByDeptId(depts, deptId, target);
            System.out.println(target);
            treeDept = deptService.buildDeptTreeSelect(target);
        }

        return Result.success(treeDept);
    }

    public List<SysDept> getTreeListByDeptId(List<SysDept> source, String deptId, List<SysDept> target) {
        for (SysDept sysDept : source) {
            if (deptId.equals(sysDept.getDeptId())) {
                target.add(sysDept);
                String parentId = sysDept.getParentId();

                if (org.apache.commons.lang3.StringUtils.isNotBlank(parentId)) {
                    getTreeListByDeptId(source, parentId, target);
                }
            }
        }

        return target;
    }


    /**
     * 获取部门下拉树列表(不包括 dept_id=YG1及其子孙部门)
     */
    @GetMapping("/treeselectExcYG1")
    @ApiOperation("获取部门下拉树列表")
    public Result treeselectExcYG1(SysDept dept) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        List<SysDept> source = deptService.listDeptExcYG1(dept);

        List<SysDept> target = new ArrayList<>();

        List<TreeDeptSelect> treeDept = null;
        boolean hasChildren = deptService.hasChildByDeptId(deptId);
        if (hasChildren) {
            target = deptService.selectChildrenIncludeSelfById(deptId);
        }else{
            getTreeListByDeptId(source, deptId, target);
        }

        return Result.success(deptService.buildDeptTreeSelect(target));


        /*List<SysDept> deptList = deptService.listDeptExcYG1(dept);

        System.out.println("22222222222222222222222222222" + deptService.buildDeptTreeSelect(deptList).size());
        return Result.success(deptService.buildDeptTreeSelect(deptList));*/
    }


    /**
     *
     * 获取管养部门下拉树列表(包括 dept_id=YG1及其子孙部门)
     * @param dept
     * @return
     */
    @GetMapping("/treeSelectYG1")
    @ApiOperation("应急人员获取部门下拉树列表")
    public Result treeSelectYG1(SysDept dept) {
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        List<SysDept> source = deptService.listDeptYG1(dept);

        List<SysDept> target = new ArrayList<>();

        List<TreeDeptSelect> treeDept = null;
        boolean hasChildren = deptService.hasChildByDeptId(deptId);
        if (hasChildren) {
            target = deptService.selectChildrenIncludeSelfById(deptId);
        }else{
            getTreeListByDeptId(source, deptId, target);
        }

        return Result.success(deptService.buildDeptTreeSelect(target));


    }


    @GetMapping("/treeselectYG1")
    @ApiOperation("获取部门下拉树列表")
    public Result treeselectYG1(SysDept dept) {
        List<SysDept> deptList = deptService.treeselectYG1(dept);
        System.out.println("22222222222222222222222222222" + deptService.buildDeptTreeSelect(deptList).size());
        return Result.success(deptService.buildDeptTreeSelect(deptList));
    }


    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @ApiOperation("加载对应角色部门列表树")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * 新增部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增部门")
    public Result add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return Result.error("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(getUsername());
        return Result.toResult(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改部门")
    public Result edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return Result.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return Result.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return Result.error("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(getUsername());
        return Result.toResult(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    @ApiOperation("删除部门")
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = true, dataType = "String", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable String deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return Result.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return Result.error("部门存在用户,不允许删除");
        }
        return Result.toResult(deptService.deleteDeptById(deptId));
    }

    /**
     * 获取部门用户树
     * lhd
     */
    @Log(title = "获取部门用户树")
    @GetMapping("/treeDeptUser")
    @ApiOperation("获取部门用户树")
    public Result treeDeptUser() {
        return Result.success(deptService.treeDeptUser());
    }

    /**
     * 根据id查询事件
     *
     * @param
     * @return
     */
    @GetMapping("/toll")
    public AjaxResult tollById() {
        return AjaxResult.success(deptService.tollById());
    }
}
