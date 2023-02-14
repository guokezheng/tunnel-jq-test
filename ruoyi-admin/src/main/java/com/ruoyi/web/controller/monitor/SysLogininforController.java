package com.ruoyi.web.controller.monitor;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.core.domain.SysDeptTunnel;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysLogininfor;
import com.ruoyi.system.service.ISysLogininforService;

/**
 * 系统访问记录
 *
 *  *
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController extends BaseController
{
    @Autowired
    private ISysLogininforService logininforService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdDevicesService sdDevicesService;

    @Autowired
    private ISdTaskListService sdTaskListService;

//    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor)
    {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @Log(title = "登录日志", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    @GetMapping("/export")
    public AjaxResult export(SysLogininfor logininfor)
    {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        return util.exportExcel(list, "登录日志");
    }

//    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

//    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        logininforService.cleanLogininfor();
        return AjaxResult.success();
    }


    @PostMapping("/getDeviceTreeselect")
    @ApiOperation("获取隧道树形结构")
    public Result getDeviceTreeselect(@RequestBody String type)
    {
        String eqtype = "";
        if(type !=null&&!"".equals(type)) {
            if ("1".equals(type)) {
                eqtype = "19";
            }else if ("2".equals(type)) {
                eqtype = "17";
            }else if ("3".equals(type)) {
                eqtype = "18";
            }else if ("4".equals(type)) {
                eqtype = "5";
            }
        }
        String deptId = String.valueOf(SecurityUtils.getDeptId());
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        List<SysDeptTunnel>tunnelsDevices = new ArrayList<>();
        List<SdTunnels> tunnels = tunnelsService.selectTunnelLineList(deptId);
        List<SdDevices>devices = sdDevicesService.selectDevicesLineList(deptId,eqtype);

        if(tunnels!=null&&tunnels.size()>0){
            for(int i = 0;i<tunnels.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId(tunnels.get(i).getTunnelId());
                deptTunnel.setDeptName(tunnels.get(i).getTunnelName());
                deptTunnel.setParentId((tunnels.get(i).getDeptId()).toString());
                deptTunnel.setParentName(tunnels.get(i).getTunnelStationName());
                tunnelsDevices.add(deptTunnel);
            }
        }
        if(devices!=null&&devices.size()>0){
            for(int i = 0;i<devices.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId(devices.get(i).getEqId());
                deptTunnel.setDeptName(devices.get(i).getEqName());
                deptTunnel.setParentId(devices.get(i).getEqTunnelId());
                deptTunnel.setParentName(devices.get(i).getTunnel());
                tunnelsDevices.add(deptTunnel);
            }
        }
        sdTaskListService.buildDeptTunnelTreeSelect(tunnelsDevices);
        return Result.success(sdTaskListService.buildDeptTunnelTreeSelect(tunnelsDevices));
    }

}
