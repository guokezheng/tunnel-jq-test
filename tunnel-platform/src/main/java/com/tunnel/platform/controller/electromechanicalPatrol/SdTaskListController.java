package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.SysDeptTunnel;
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

import java.util.ArrayList;
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
    /*@GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sdTaskListService.selectSdTaskListById(id));
    }*/

    /**
     * 获取修改任务信息
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id){
        List<SdTaskList> taskList = sdTaskListService.getTaskInfoList(id);
        List<SdPatrolList> patrolLists = sdTaskListService.getUpdatePatrolLists(id);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.put("patrol", patrolLists);
        return Result.success(map);
    }

    /**
     * 新增巡查任务
     */
   /* @PreAuthorize("@ss.hasPermi('system:list:add')")*/
   /* @Log(title = "巡查任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTaskList sdTaskList, List<SdPatrolList>sdPatrolList)
    {
        return toAjax(sdTaskListService.insertSdTaskList(sdTaskList,sdPatrolList));
    }*/

    /**
     *
     * @param sdTaskList
     * @param
     * @return
     */

    @PostMapping("/addTask")
    public AjaxResult addTask(SdTaskList sdTaskList)
    {
        return toAjax(sdTaskListService.insertSdTaskList(sdTaskList));
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
     * 废止巡查任务
     *
     * @return
     */
    @GetMapping("/abolish")
    public AjaxResult abolish(String id)
    {
        System.out.println("jdhasdihjsoaidhjosaidjoas======="+id);
        return toAjax(sdTaskListService.abolishSdTaskList(id));
    }

    /**
     * 删除巡查任务
     */
    /*@PreAuthorize("@ss.hasPermi('system:list:remove')")*/
    @Log(title = "巡查任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String ids)
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
        //List<SysDept> depts = deptService.selectTunnelDeptList(deptId);
        List<SysDeptTunnel>deptTunnels = new ArrayList<>();

        List<SysDept> depts = deptService.selectTunnelDeptList(deptId);
        List<SdTunnels> tunnels = tunnelsService.selectTunnelList(deptId);
        if(depts!=null&&depts.size()>0){
            for(int i = 0;i<depts.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId((depts.get(i).getDeptId()).toString());
                deptTunnel.setDeptName(depts.get(i).getDeptName());
                deptTunnel.setParentId((depts.get(i).getParentId()).toString());
                deptTunnel.setParentName(depts.get(i).getParentName());
                deptTunnels.add(deptTunnel);
            }
        }
        if(tunnels!=null&&tunnels.size()>0){
            for(int i = 0;i<tunnels.size();i++){
                SysDeptTunnel deptTunnel = new SysDeptTunnel();
                deptTunnel.setDeptId(tunnels.get(i).getTunnelId());
                deptTunnel.setDeptName(tunnels.get(i).getTunnelName());
                deptTunnel.setParentId((tunnels.get(i).getDeptId()).toString());
                deptTunnel.setParentName(tunnels.get(i).getTunnelStationName());
                deptTunnels.add(deptTunnel);
            }
        }
        sdTaskListService.buildDeptTunnelTreeSelect(deptTunnels);
        return Result.success(sdTaskListService.buildDeptTunnelTreeSelect(deptTunnels));
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
     * 查询设备列表
     * @param tunnelId
     * @return
     */
    @GetMapping("/getDevicesList")
    public TableDataInfo getDevicesList(String tunnelId, String deviceType){
        startPage();
        List<SdDevices> devices = devicesService.getDevicesList(tunnelId,deviceType);
        return getDataTable(devices);
    }

    /**
     * 根据隧道、故障类型获取故障列表
     * @param tunnelId
     * @param faultLevel
     * @return
     */
    @GetMapping("/getFaultList")
    public TableDataInfo getFaultList(String tunnelId,String faultLevel){
        startPage();
        List<SdFaultList> faultList = sdFaultListService.getFaultList(tunnelId,faultLevel);
        return getDataTable(faultList);
    }

    /**
     * 任务详情
     * @param taskId
     * @return
     */
    @PostMapping("/getTaskInfoList")
    public Result getTaskInfoList(@RequestBody String taskId){
        List<SdTaskList> taskList = sdTaskListService.getTaskInfoList(taskId);
        List<SdPatrolList> patrolLists = sdTaskListService.getPatrolListsInfo(taskId);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.put("patrol", patrolLists);
        return Result.success(map);
    }

    /**
     * 查询班组列表
     */
    @ApiOperation("查询班组列表")
    @GetMapping("/getListBz")
    public TableDataInfo<List<SysDept>> list()
    {
        startPage();
        Long deptId = SecurityUtils.getDeptId();
        List<SysDept> list = sdTaskListService.selectTableBzDataInfo(deptId);
        return getDataTable(list);
    }

    /**
     * app  巡查任务列表
     * @param tunnelName
     * @param sdTaskList
     * @return
     */
    @GetMapping("/app/getTaskList")
    public Result getTaskList(String tunnelName,SdTaskList sdTaskList){
        List<SdTaskList> taskList = sdTaskListService.getTaskList(tunnelName,sdTaskList);
        if(taskList!=null&&taskList.size()>0){
            for(int i=0;i<taskList.size();i++){
                if(taskList.get(i).getId()!=null){
                    int num = sdTaskListService.countPatrolNum(taskList.get(i).getId());
                    taskList.get(i).setTotalNum(num);
                }
            }

        }
        return Result.success(taskList);
    }

    /**
     *  app 巡检任务基本信息
     * @param taskId
     * @return
     */
    @PostMapping("/app/getTaskInfo")
    public Result getTaskInfo(@RequestBody String taskId){
        List<SdTaskList> taskList = sdTaskListService.getTaskInfoList(taskId);
        return Result.success(taskList);
    }

    /**
     * app 端接收任务  task_status变为1（巡检中）
     * @param id
     * @return
     */
    @GetMapping("/app/accept")
    public AjaxResult accept(String id)
    {
        System.out.println("jdhasdihjsoaidhjosaidjoas======="+id);
        return toAjax(sdTaskListService.acceptSdTaskList(id));
    }

    /**
     * app端  巡查点清单
     * @param taskId
     * @return
     */
    @PostMapping("/app/getPatrolInfo")
    public Result getPatrolInfo(@RequestBody String taskId){
        List<SdPatrolList> patrolList = sdTaskListService.getPatrolInfo(taskId);
        return Result.success(patrolList);
    }

    /**
     * app端  获取任务现场情况
     * @param taskId
     * @return
     */
    public Result getTaskSiteCondition(@RequestBody String taskId){
        List<SdTaskList> patrolList = sdTaskListService.getTaskSiteCondition(taskId);
        return Result.success(patrolList);
    }








}
