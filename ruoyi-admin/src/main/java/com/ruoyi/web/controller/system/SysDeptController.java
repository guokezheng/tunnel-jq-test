package com.ruoyi.web.controller.system;

import java.util.Iterator;
import java.util.List;

import com.ruoyi.common.core.page.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 部门信息
 *
 *  *
 */
@RestController
@RequestMapping("/system/dept")
@Api(tags = "部门管理")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    /**
     * 获取部门列表
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    @ApiOperation("获取部门列表")
    public Result list(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Result.success(depts);
    }

    /**
     * 查询部门列表（排除节点）
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    @ApiOperation("查询部门列表（排除节点）")
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = false, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext())
        {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
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
    @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result<SysDept> getInfo(@PathVariable Long deptId)
    {
        deptService.checkDeptDataScope(deptId);
        return Result.success(deptService.selectDeptById(deptId));
    }

    /**
     * 获取部门下拉树列表
     */
    @GetMapping("/treeselect")
    @ApiOperation("获取部门下拉树列表")
    public Result treeselect(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return Result.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * 加载对应角色部门列表树
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    @ApiOperation("加载对应角色部门列表树")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId)
    {
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
    public Result add(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
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
    public Result edit(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return Result.error("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return Result.error("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0)
        {
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
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = true, dataType = "Long", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return Result.error("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
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
    public Result treeDeptUser()
    {
        return Result.success(deptService.treeDeptUser());
    }
}
