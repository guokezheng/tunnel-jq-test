package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysDeptService;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.domain.electromechanicalPatrol.SdFaultList;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 巡查任务Controller
 * 
 * @author tjw
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/task/list")
public class SdTaskListController extends BaseController
{
    @Autowired
    private ISdTaskListService sdTaskListService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISdTunnelsService tunnelsService;

    @Autowired
    private ISdDevicesService devicesService;

    @Autowired
    private ISdFaultListService sdFaultListService;


    /**
     * 查询巡查任务列表
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(SdTaskList sdTaskList)
    {
        treeselect();
        startPage();
        List<SdTaskList> list = sdTaskListService.selectSdTaskListList(sdTaskList);
        return getDataTable(list);
    }

    /**
     * 导出巡查任务列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:export')")*/
    @Log(title = "巡查任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SdTaskList sdTaskList)
    {
        List<SdTaskList> list = sdTaskListService.selectSdTaskListList(sdTaskList);
        ExcelUtil<SdTaskList> util = new ExcelUtil<SdTaskList>(SdTaskList.class);
        return util.exportExcel(list, "巡查任务数据");
    }

    /**
     * 获取巡查任务详细信息
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sdTaskListService.selectSdTaskListById(id));
    }

    /**
     * 新增巡查任务
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:add')")*/
    @Log(title = "巡查任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTaskList sdTaskList, List<SdPatrolList>sdPatrolList)
    {
        return toAjax(sdTaskListService.insertSdTaskList(sdTaskList,sdPatrolList));
    }

    /**
     * 修改巡查任务
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:edit')")*/
    @Log(title = "巡查任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTaskList sdTaskList,List<SdPatrolList>sdPatrolList)
    {
        return toAjax(sdTaskListService.updateSdTaskList(sdTaskList,sdPatrolList));
    }

    /**
     * 删除巡查任务
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:remove')")*/
    @Log(title = "巡查任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sdTaskListService.deleteSdTaskListByIds(ids));
    }

    /**
     * 获取单位隧道所树形结构
     * @return
     */
    @GetMapping("/treeselect")
    @ApiOperation("获取隧道树形结构")
    public Result treeselect()
    {
        String deptId = String.valueOf(SecurityUtils.getDeptId());
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        List<SysDept> depts = deptService.selectTunnelDeptList(deptId);
        /*List<SdTunnels> tunnels = tunnelsService.selectTunnelsList(deptId);
        if(tunnels!=null&&tunnels.size()>0){
            for(int i = 0;i<tunnels.size();i++){
                SysDept dept = new SysDept();
                dept.setDeptId(tunnels.get(i).getDeptId()+'0'+i);
                dept.setParentId(tunnels.get(i).getDeptId());
                dept.setDeptName(tunnels.get(i).getTunnelName());
                dept.setAncestors(tunnels.get(i).getTunnelId());
                depts.add(dept);
            }
        }*/
        return Result.success(deptService.buildDeptTreeSelect(depts));
    }


    /**
     * 获取隧道列表
     * @return
     */
    @PostMapping("/getTunnelList")
    public Result getTunnelList(@RequestBody String deptId){
        List<SdTunnels> tunnels = tunnelsService.selectTunnelsList(deptId);
        return Result.success(tunnels);
    }


    /**
     * 根据隧道查询设备类型
     * @param tunnelId
     * @return
     */
    @PostMapping("/getDevicesTypeList")
    public Result getDevicesTypeList(@RequestBody String tunnelId){
        List<SdDevices> devicesType = devicesService.selectDevicesTypeList(tunnelId);
        return Result.success(devicesType);
    }


    /**
     * 查询设备列表
     * @param tunnelId
     * @return
     */
    @PostMapping("/getDevicesList")
    public Result getDevicesList(@RequestBody String tunnelId,String deviceType){
        List<SdDevices> devices = devicesService.getDevicesList(tunnelId,deviceType);
        return Result.success(devices);
    }

    /**
     * 根据隧道、故障类型获取故障列表
     * @param tunnelId
     * @param faultLevel
     * @return
     */
    @PostMapping("/getFaultList")
    public Result getFaultList(@RequestBody String tunnelId,String faultLevel){
        List<SdFaultList> faultList = sdFaultListService.getFaultList(tunnelId,faultLevel);
        return Result.success(faultList);
    }

    @PostMapping("/getTaskInfoList")
    public Result getTaskInfoList(@RequestBody String taskId){
        List<SdTaskList> taskList = sdTaskListService.getTaskInfoList(taskId);
        List<SdPatrolList> patrolLists = sdTaskListService.getPatrolListsInfo(taskId);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.put("patrol", patrolLists);
        return Result.success(map);
    }


}
