package com.tunnel.platform.controller.dataInfo;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.task.WorkbenchIncidentTask;
import com.tunnel.business.datacenter.domain.enumeration.PlatformAuthEnum;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.platform.controller.platformAuthApi.PlatformApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final Logger log = LoggerFactory.getLogger(SdTunnelsController.class);

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    /**
     * 平台
     */
    @Value("${authorize.name}")
    private String platformName;

    /**
     * 推送、接收controller
     */
    @Autowired
    private PlatformApiController platformApiController;

    /**
     * 查询隧道列表
     */
    @ApiOperation("查询隧道列表")
    @GetMapping("/list")
    public TableDataInfo<List<SdTunnels>> list(SdTunnels sdTunnels)
    {
        startPage();
        if (null == sdTunnels.getDeptId() || "".equals(sdTunnels.getDeptId())){
            String deptId = SecurityUtils.getDeptId();
            if (deptId == null) {
                throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
            }
            sdTunnels.setDeptId(deptId);
        }
        List<SdTunnels> list = sdTunnelsService.selectSdTunnelsList(sdTunnels);
        return getDataTable(list);
    }

    /**
     * 查询隧道列表
     */
    @ApiOperation("查询隧道列表")
    @GetMapping("/list1")
    public TableDataInfo<List<SdTunnels>> list1(SdTunnels sdTunnels)
    {
        startPage();
        if (null == sdTunnels.getDeptId() || "".equals(sdTunnels.getDeptId())){
            String deptId = SecurityUtils.getDeptId();
            if (deptId == null) {
                throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
            }
            sdTunnels.setDeptId(deptId);
        }
        List<SdTunnels> list = sdTunnelsService.selectSdTunnelsList1(sdTunnels);
        return getDataTable(list);
    }

    /**
     * 查询隧道列表
     */
    @ApiOperation("查询隧道列表")
    @GetMapping("/listDepId")
    public TableDataInfo<List<SdTunnels>> listDepId(SdTunnels sdTunnels)
    {
        startPage();
        if (null == sdTunnels.getDeptId() || "".equals(sdTunnels.getDeptId())){
            String deptId = SecurityUtils.getDeptId();
            if (deptId == null) {
                throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
            }
            sdTunnels.setDeptId(deptId);
        }
        List<SdTunnels> list = sdTunnelsService.selectTunnelsDeptIdList(sdTunnels);
        return getDataTable(list);
    }

    /**
     * 外部系统获取隧道下拉
     * @return
     */
    @ApiOperation("外部系统获取隧道下拉")
    @GetMapping("/listAll")
    public AjaxResult listAll() {
        List<SdTunnels> list = sdTunnelsService.selectAllSdTunnelsList();
        return AjaxResult.success(list);
    }

    /**
     * 外部系统获取隧道下拉
     * @return
     */
    @ApiOperation("外部系统获取隧道下拉")
    @GetMapping("/listAll1")
    public AjaxResult listAll1() {
        List<SdTunnels> list = sdTunnelsService.selectAllSdTunnelsList1();
        return AjaxResult.success(list);
    }



    /**
     * 获取隧道详细信息
     */
    @ApiOperation("获取隧道详细信息")
    // @ApiOperation("获取选中隧道详细信息")
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
        int i = sdTunnelsService.insertSdTunnels(sdTunnels);
        //管理站平台下推送数据
        pushData(sdTunnels,"add",i);
        return Result.toResult(i);
    }

    /**
     * 修改隧道
     */
    @ApiOperation("修改隧道信息")
    @Log(title = "隧道", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody SdTunnels sdTunnels)
    {
        log.info("------------------------");
        log.info(JSON.toJSONString(sdTunnels));
        int i = sdTunnelsService.updateSdTunnels(sdTunnels);
        //管理站平台下推送数据
        pushData(sdTunnels,"edit",i);
        return Result.toResult(i);
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
        int i = sdTunnelsService.deleteSdTunnelsByIds(tunnelIds);
        //管理站平台下推送数据
        if(PlatformAuthEnum.GLZ.getCode().equals(platformName) && i > 0){
            List<SdTunnels> sdTunnelsList = new ArrayList<>();
            SdTunnels sdTunnels = new SdTunnels();
            sdTunnels.setTunnelIds(Arrays.asList(tunnelIds));
            sdTunnels.setCreateTime(DateUtils.getNowDate());
            sdTunnelsList.add(sdTunnels);
            platformApiController.tunnelsPush(sdTunnelsList,"del");
        }
        return Result.toResult(i);
    }

    /**
     * 导出
     * @param sdTunnels
     * @return
     */
    @ApiOperation("导出")
    @Log(title = "隧道管理", businessType = BusinessType.EXPORT)
    @GetMapping("/exportTunnels")
    public AjaxResult exportTunnels(SdTunnels sdTunnels)
    {
        List<SdTunnels> list = sdTunnelsService.selectSdTunnelsList1(sdTunnels);
        ExcelUtil<SdTunnels> util = new ExcelUtil<SdTunnels>(SdTunnels.class);
        return util.exportExcel(list, "隧道管理");
    }

    /**
     * 查询隧道列表
     */
    @ApiOperation("查询隧道列表")
    // @ApiOperation("查询隧道分区列表")
    @GetMapping("/sublist")
    public TableDataInfo<List<SdTunnels>> sublist(SdTunnels sdTunnels)
    {
        List<SdTunnels> list = sdTunnelsService.selectSdTunnelsSubList(sdTunnels);
        return getDataTable(list);
    }

    /**
     * 查询部门列表
     * @param deptId
     * @return
     */
    @ApiOperation("查询部门列表")
    @GetMapping("/deptId")
    public Result<List<SdTunnels>> deptId(@PathParam("deptId") Long deptId)
    {
        return Result.success(sdTunnelsService.deptId(deptId));
    }

    /**
     * 同步数据
     * @param sdTunnels
     * @param pushType
     * @param count
     */
    @ApiOperation("同步数据")
    public void pushData(SdTunnels sdTunnels, String pushType, int count){
        if(PlatformAuthEnum.GLZ.getCode().equals(platformName) && count > 0){
            List<SdTunnels> sdTunnelsList = new ArrayList<>();
            sdTunnelsList.add(sdTunnels);
            platformApiController.tunnelsPush(sdTunnelsList,pushType);
        }
    }

    /**
     * 查询当前登录者所属
     * @return
     */
    @ApiOperation("查询当前登录者所属")
    @GetMapping("/getJlyTunnel")
    public AjaxResult getJlyTunnel(){
        return AjaxResult.success(sdTunnelsService.getJlyTunnel());
    }

    /**
     * 查询部门列表
     * @param ids
     * @return
     */
    @ApiOperation("查询部门列表")
    @GetMapping("/deptIdList")
    public Result<List<SdTunnels>> deptIdList(@RequestParam("ids")  String[] ids)
    {
        return Result.success(sdTunnelsService.deptIdList(ids));
    }
}
