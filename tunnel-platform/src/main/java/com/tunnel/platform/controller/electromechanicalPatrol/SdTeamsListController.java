package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.tunnel.business.domain.electromechanicalPatrol.SdTeamsList;
import com.tunnel.business.service.electromechanicalPatrol.ISdTeamsListService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班组Controller
 *
 * @author tjw
 * @date 2023-03-10
 */
@RestController
@RequestMapping("/teams/list")
public class SdTeamsListController extends BaseController
{
    @Autowired
    private ISdTeamsListService sdTeamsListService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysUserService userService;

    @GetMapping("/list")
    public TableDataInfo list(SdTeamsList sdTeamsList)
    {
        startPage();
        List<SdTeamsList> list = sdTeamsListService.selectSdTeamsList(sdTeamsList);
        return getDataTable(list);
    }


    /**
     * 新增班组
     */
    @Log(title = "班组管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增班组")
    public AjaxResult add(@Validated @RequestBody SdTeamsList sdTeamsList) {
        if (UserConstants.NOT_UNIQUE.equals(sdTeamsListService.checkTeamsNameUnique(sdTeamsList))) {
            return AjaxResult.error("新增菜单'" + sdTeamsList.getDeptName() + "'失败，班组名称已存在");
        }
        sdTeamsList.setCreateBy(getUsername());
        return toAjax(sdTeamsListService.insertTeams(sdTeamsList));

    }

    /**
     * 修改班组
     */
    @Log(title = "班组管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改班组")
    public Result edit(@Validated @RequestBody SdTeamsList sdTeamsList) {
        sdTeamsList.setUpdateBy(getUsername());
        return Result.toResult(sdTeamsListService.updateTeams(sdTeamsList));
    }


    /**
     * 根据班组编号获取详细信息
     */
    @GetMapping(value = "/{deptId}")
    @ApiOperation("根据班组编号获取详细信息")
    @ApiImplicitParam(name = "deptId", value = "班组id", required = true, dataType = "String", paramType = "path", dataTypeClass = Long.class)
    public Result<SdTeamsList> getInfo(@PathVariable String deptId) {
        return Result.success(sdTeamsListService.selectTeamsById(deptId));
    }


    @Log(title = "班组管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTeamsList sdTeamsList)
    {
        List<SdTeamsList> list = sdTeamsListService.selectSdTeamsList(sdTeamsList);
        ExcelUtil<SdTeamsList> util = new ExcelUtil<SdTeamsList>(SdTeamsList.class);
        return util.exportExcel(list, "班组数据");
    }

    /**
     * 删除班组
     */
    @Log(title = "班组管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    @ApiOperation("删除班组")
    @ApiImplicitParam(name = "deptId", value = "班组ID", required = true, dataType = "String", paramType = "path", dataTypeClass = Long.class)
    public Result remove(@PathVariable String deptId) {
        if (deptService.checkDeptExistUser(deptId)) {
            return Result.error("班组存在用户,不允许删除");
        }
        return Result.toResult(sdTeamsListService.deleteTeamsById(deptId));
    }

    /**
     * 查询班组已包含的用户
     */
    @GetMapping("/teamsUserList")
    @ApiOperation("查询班组已包含的用户")
    public TableDataInfo<List<SysUser>> teamsUserList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.teamsUserList(user);
        return getDataTable(list);
    }

    /**
     * 查询班组未包含的用户
     */
    @GetMapping("/unTeamsUserList")
    @ApiOperation("查询班组已包含的用户")
    public TableDataInfo<List<SysUser>> unTeamsUserList(SysUser user)
    {
        startPage();
        String depts = SecurityUtils.getDeptId();
        if (depts == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        List<SysUser> list = userService.unTeamsUserList(user.getUserName(),user.getPhonenumber(),user.getDeptId(),depts);
        return getDataTable(list);
    }


    /**
     * 批量选择用户授权
     */
    @Log(title = "班组管理", businessType = BusinessType.GRANT)
    @PutMapping("/teamsUserSelectAll")
    @ApiOperation("批量选择用户授权")
    public Result teamsUserSelectAll(String deptId, Long[] userIds)
    {
        return Result.toResult(sdTeamsListService.teamsUserSelectAll(deptId, userIds));
    }


    /**
     * 班组中取消用户
     * @param user
     * @return
     */
    @Log(title = "班组管理", businessType = BusinessType.GRANT)
    @PutMapping("/deleteTeamsUserCancel")
    @ApiOperation("取消授权用户")
    public Result cancelAuthUser(@RequestBody SysUser user)
    {
        return Result.toResult(userService.deleteTeamsUserCancel(user));
    }

    /**
     * 批量取消班组中的用户
     */
    @Log(title = "班组管理", businessType = BusinessType.GRANT)
    @PutMapping("/deleteTeamsUserCancelAll")
    @ApiOperation("批量取消授权用户")
    public Result cancelAuthUserAll(String deptId, Long[] userIds)
    {
        return Result.toResult(userService.deleteTeamsUserCancelAll(deptId, userIds));
    }


}
