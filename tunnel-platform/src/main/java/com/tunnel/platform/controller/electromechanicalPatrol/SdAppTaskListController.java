package com.tunnel.platform.controller.electromechanicalPatrol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.datacenter.domain.dataReport.TaskStatus;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.mapper.electromechanicalPatrol.SdPatrolListMapper;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTeamsListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 巡查任务Controller
 *
 * @author tjw
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/app/task/list")
public class SdAppTaskListController extends BaseController
{
    @Autowired
    private ISdTaskListService sdTaskListService;


    @Autowired
    private SdPatrolListMapper sdPatrolListMapper;


    @Autowired
    private ISdTeamsListService sdTeamsListService;


    /**
     * app端获取任务状态
     * @return
     */
    @PostMapping("/app/getTaskStatus")
    public Result getTaskStatus(){
        List<SdTaskList> taskList = sdTaskListService.getTaskStatus();
        return Result.success(taskList);
    }

    /**
     * app  巡查任务列表
     * @param
     * @param sdTaskList
     * @return
     */
    @PostMapping("/app/getTaskList")
    public TableDataInfo getTaskList(SdTaskList sdTaskList,Integer pageSize,Integer pageNum){
        String deptId = SecurityUtils.getDeptId();
         if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        String startTime = "";//开始时间
        String endTime = "";//结束时间
        if(sdTaskList.getTime()!=null&&!"".equals(sdTaskList.getTime())){//不为空
            //  0：最近1周；1：最近3周；2：最近1月；3：最近3月
           /* SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            endTime = format.format(date);//结束时间
            if("0".equals(sdTaskList.getTime())){//最近一周
                c.setTime(new Date());
                c.add(Calendar.DATE, - 7);
                Date d = c.getTime();
                startTime = format.format(d);
            }else if("1".equals(sdTaskList.getTime())){//最近3周
                c.setTime(new Date());
                c.add(Calendar.DATE, - 21);
                Date d = c.getTime();
                startTime = format.format(d);
            }else if("2".equals(sdTaskList.getTime())){//最近1月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -1);
                Date m = c.getTime();
                startTime = format.format(m);
            }else if("3".equals(sdTaskList.getTime())){//最近3月
                c.setTime(new Date());
                c.add(Calendar.MONTH, -3);
                Date m3 = c.getTime();
                startTime = format.format(m3);
            }else{*/
                String [] s= sdTaskList.getTime().split(",",0);
                startTime = s[0];
                endTime = s[1];
            //}
        }

        Long userId = SecurityUtils.getUserId();
        List<SdTaskList> taskList = new ArrayList<>();
        //先判断在不在用户班组
        String result = sdTeamsListService.existInTeams(userId);
        int count = 0;
        if(result!=null&&!"".equals(result)){//存在班组中
            deptId = result;
            count = sdTaskListService.getTaskCountListTeams(sdTaskList.getTaskStatus(),sdTaskList.getTaskName(),startTime,endTime,deptId,userId);
        }else{
            count = sdTaskListService.getTaskCountList(sdTaskList.getTaskStatus(),sdTaskList.getTaskName(),startTime,endTime,deptId,userId);
        }
        if(count>0){
            if(result!=null&&!"".equals(result)){//存在班组中
                deptId = result;
                taskList = sdTaskListService.getTaskListTeams(sdTaskList.getTaskStatus(),sdTaskList.getTaskName(),startTime,endTime,deptId,pageSize,pageNum,userId);
            }else{
                taskList = sdTaskListService.getTaskList(sdTaskList.getTaskStatus(),sdTaskList.getTaskName(),startTime,endTime,deptId,pageSize,pageNum,userId);
            }
            if(taskList!=null&&taskList.size()>0){
                for(int i=0;i<taskList.size();i++){
                    if(taskList.get(i).getId()!=null){
                        SdTaskList task = sdTaskListService.countPatrolNum(taskList.get(i).getId());
                        taskList.get(i).setTotalNum(task.getTotalNum());
                    }
                }

            }
            return new TableDataInfo(taskList,count);
        }


        return new TableDataInfo(null,0);
    }



    /**
     *  app 巡检任务基本信息
     * @param taskId
     * @return
     */
    @PostMapping("/app/getTaskInfo")
    public Result getTaskInfo(String taskId){
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
        Long userId  = SecurityUtils.getUserId();

        return toAjax(sdTaskListService.acceptSdTaskList(id,userId));
    }

    /**
     * app端  巡查点清单
     * @param taskId
     * @return
     */
    @PostMapping("/app/getPatrolInfo")
    public Result getPatrolInfo(String taskId){
        List<SdPatrolList> patrolList = sdTaskListService.getPatrolInfo(taskId);
        return Result.success(patrolList);
    }

    /**
     * app端  获取任务现场情况
     * @param taskId
     * @return
     */
    @PostMapping("/app/getTaskSiteCondition`")
    public Result getTaskSiteCondition(String taskId){
        String result = sdTaskListService.getTaskSiteCondition(taskId);
        return Result.success(result);
    }



    /**
     * app 端暂存本地  task_status 变为3 待回传：APP点击“暂存本地”；PC端不可见(暂不用)
     *     提交上报    task_status 变为2 已完结
     * @param  sdTaskList
     * @return
     */
    @GetMapping("/app/saveTask")
    public AjaxResult saveTask(SdTaskList sdTaskList)
    {
        return toAjax(sdTaskListService.saveLocal(sdTaskList));
    }

    /**
     * 巡查点检修情况保存
     * @param uploadPicture
     * @return
     */
    @PostMapping("/app/uploadPicture")
    public AjaxResult uploadPicture(@RequestParam(name = "file", required = false) MultipartFile[] file)
    {
        return AjaxResult.success(sdTaskListService.uploadPicture(file));
    }



    @PostMapping("/app/savePatrol")
    public AjaxResult savePatrolInfo(@RequestBody SdPatrolList sdPatrolList)
    {
        return toAjax(sdTaskListService.savePatrol(sdPatrolList));
    }

    /**
     * app端首页待处理任务单
     * @return
     */
    @PostMapping("/app/taskToDo")
    public Result taskToDo(HttpServletRequest request){
        String deptId = SecurityUtils.getDeptId();
        Long userId = SecurityUtils.getUserId();
        List<Map> taskList = new ArrayList<>();
        //先判断在不在用户班组
        String result = sdTeamsListService.existInTeams(userId);
        if(result!=null&&!"".equals(result)){//存在班组中
            deptId = result;
            taskList = sdTaskListService.getTaskToDoTeams(deptId,userId);
        }else{
            taskList = sdTaskListService.getTaskToDo(deptId,userId);
        }
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.put("num", taskList.size());
        return Result.success(map);
    }

    /**
     * app端查看现场情况
     * @param taskId
     * @return
     */
    @PostMapping("/app/getSiteInfo")
    public Result getSiteInfo(String taskId){
        List<SdTaskList> taskList = sdTaskListService.getSiteInfo(taskId);
        return Result.success(taskList);
    }

    /**
     * app端删除图片
     */
    @DeleteMapping("/app/{id}")
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(sdTaskListService.deleteSitePhoto(id));
    }


    /**
     * APP 巡检任务列表
     * @return
     */
    @GetMapping("/app/getTaskAllList")
    public Result getTaskAllList(){

        return Result.success(sdTaskListService.getTaskAllList());
    }
    /**
     * APP 同步巡检任务信息
     * @return
     */
    @PostMapping("/app/modifyTaskData")
    public Result modifyTaskData(@RequestBody String reqJson){

        JSONArray jsonArray = JSONArray.parseArray(reqJson);

        Map res = new HashMap();
        List<String>  success = new ArrayList<>();
        List<String>  error = new ArrayList<>();
        for(Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            Boolean  isEdit = jsonObject.getBoolean("isEdit");

            if(isEdit){
                SdTaskList sdTaskList = new SdTaskList();

                //更新状态
                String taskStatus = jsonObject.getString("taskStatus");
                //巡检编号
                String id = jsonObject.getString("id");

                sdTaskList.setId(id);
                if(taskStatus != null){
                    // 状态为 待回传
                    if(taskStatus == TaskStatus.DAIHUICHUAN.getCode()){
                        taskStatus = TaskStatus.YIWANJIE.getCode();
                    }
                    sdTaskList.setTaskStatus(taskStatus);
                }

                JSONObject zc = jsonObject.getJSONObject("zc");
                if(zc != null){
                    String siteDescription = zc.getString("siteDescription"); //巡检描述
                    Date taskEndtime = zc.getDate("taskEndtime"); // 选件完成时间
                    sdTaskList.setSiteDescription(siteDescription);
                    sdTaskList.setTaskEndtime(taskEndtime);
                }

                if(sdTaskListService.updateSdTaskList(sdTaskList) != 0){
                    success.add(id);
                }else{
                    error.add(id);
                }

                // 巡检设备信息
                JSONArray patrolInfoFormArray = jsonObject.getJSONArray("patrolInfoForm");

                for (Object plObj : patrolInfoFormArray){
                    JSONObject patrolJSON = (JSONObject) plObj;
                    SdPatrolList sdPatrolList = new SdPatrolList();
                    sdPatrolList.setId(patrolJSON.getString("id"));
                    sdPatrolList.setImpression(patrolJSON.getString("impression"));
                    sdPatrolList.setNetwork(patrolJSON.getString("network"));
                    sdPatrolList.setPower(patrolJSON.getString("power"));

               /*     @Excel(name = "设备状态")
                    private String eqStatus;

                    @Excel(name = "运行状态")
                    private String runStatus;*/


                    // ? 缺个巡查时间 xcTime
                    sdPatrolList.setEqStatus(patrolJSON.getString("eqStatus"));
                    sdPatrolList.setRunStatus(patrolJSON.getString("runStatus"));
                    sdPatrolList.setXcTime(patrolJSON.getDate("xcTime"));

                    sdPatrolList.setEqFaultCode(patrolJSON.getString("eqFaultCode"));
                    sdPatrolList.setEqFaultDescription(patrolJSON.getString("eqFaultDescription"));
                    sdPatrolList.setFaultClstatus(patrolJSON.getString("faultClstatus"));
                    sdPatrolList.setXcStatus("1");
                    sdPatrolList.setImgFileId(patrolJSON.getString("imgFileId"));

                    sdPatrolListMapper.updateSdPatrolList(sdPatrolList);
                }

            }

        }
        res.put("success",success);
        res.put("error",error);
        return Result.success(res);
    }





}
