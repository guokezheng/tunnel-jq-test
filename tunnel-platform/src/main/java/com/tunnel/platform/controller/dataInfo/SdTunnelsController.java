package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;

/**
 * 隧道Controller
 *
 * @author zhangweitian
 * @date 2020-08-27
 */
@RestController
@RequestMapping("/tunnels")
@Api(tags = "隧道管理")
public class SdTunnelsController extends BaseController
{
    @Autowired
    private ISdTunnelsService sdTunnelsService;

    /**
     * 查询隧道列表
     */
    @ApiOperation("查询隧道列表")
    @GetMapping("/list")
    public TableDataInfo<List<SdTunnels>> list(SdTunnels sdTunnels)
    {
        startPage();
        if (null == sdTunnels.getDeptId() || "".equals(sdTunnels.getDeptId())){
            Long deptId = SecurityUtils.getDeptId();
            sdTunnels.setDeptId(deptId);
        }
        List<SdTunnels> list = sdTunnelsService.selectSdTunnelsList(sdTunnels);
        return getDataTable(list);
    }

    /**
     * 获取隧道详细信息
     */
    @ApiOperation("获取选中隧道详细信息")
    @ApiImplicitParam(name = "tunnelId", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    @GetMapping(value = "/{tunnelId}")
    public Result<SdTunnels> getInfo(@PathVariable("tunnelId") String tunnelId)
    {
        return Result.success(sdTunnelsService.selectSdTunnelsById(tunnelId));
    }

    /**
     * 新增隧道
     */
    @ApiOperation("新增隧道")
    @Log(title = "隧道", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody SdTunnels sdTunnels)
    {
        return Result.toResult(sdTunnelsService.insertSdTunnels(sdTunnels));
    }

    /**
     * 修改隧道
     */
    @ApiOperation("修改隧道信息")
    @Log(title = "隧道", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody SdTunnels sdTunnels)
    {
        return Result.toResult(sdTunnelsService.updateSdTunnels(sdTunnels));
    }

    /**
     * 删除隧道
     */
    @ApiOperation("删除选中隧道")
    @ApiImplicitParam(name = "tunnelIds", value = "隧道ID", required = true, dataType = "String", paramType = "path", dataTypeClass = String.class)
    @Log(title = "隧道", businessType = BusinessType.DELETE)
	@DeleteMapping("/{tunnelIds}")
    public Result remove(@PathVariable String[] tunnelIds)
    {
        return Result.toResult(sdTunnelsService.deleteSdTunnelsByIds(tunnelIds));
    }

    /**
     * 查询隧道列表
     */
    @ApiOperation("查询隧道分区列表")
    @GetMapping("/sublist")
    public TableDataInfo<List<SdTunnels>> sublist(SdTunnels sdTunnels)
    {
        List<SdTunnels> list = sdTunnelsService.selectSdTunnelsSubList(sdTunnels);
        return getDataTable(list);
    }


    @GetMapping("/deptId")
    public Result<List<SdTunnels>> deptId(@PathParam("deptId") Long deptId)
    {
        return Result.success(sdTunnelsService.deptId(deptId));
    }

}
