package com.tunnel.platform.controller.electromechanicalPatrol;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Result;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.business.domain.electromechanicalPatrol.SdPatrolList;
import com.tunnel.business.domain.electromechanicalPatrol.SdTaskList;
import com.tunnel.business.service.electromechanicalPatrol.ISdFaultListService;
import com.tunnel.business.service.electromechanicalPatrol.ISdTaskListService;
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
    private ISdFaultListService sdFaultListService;


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
    public Result getTaskList(SdTaskList sdTaskList){
        String deptId = SecurityUtils.getDeptId();
        if (deptId == null) {
            throw new RuntimeException("当前账号没有配置所属部门，请联系管理员进行配置！");
        }
        String startTime = "";//开始时间
        String endTime = "";//结束时间
        if(sdTaskList.getTime()!=null&&!"".equals(sdTaskList.getTime())){//不为空
            //  0：最近1周；1：最近3周；2：最近1月；3：最近3月
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
            }else{

                String [] s= sdTaskList.getTime().split(",",0);
                startTime = s[0];
                endTime = s[1];
            }
        }


        List<SdTaskList> taskList = sdTaskListService.getTaskList(sdTaskList.getTaskStatus(),sdTaskList.getTaskName(),startTime,endTime,deptId);
        if(taskList!=null&&taskList.size()>0){
            for(int i=0;i<taskList.size();i++){
                if(taskList.get(i).getId()!=null){
                    SdTaskList task = sdTaskListService.countPatrolNum(taskList.get(i).getId());
                    taskList.get(i).setTotalNum(task.getTotalNum());
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
     * app 端暂存本地  task_status 变为3 待回传：APP点击“暂存本地”；PC端不可见
     *     提交上报    task_status 变为2 已完结
     *     接收任务    task_status 从“待巡检”改为“巡检中”
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
     * @param sdPatrolList
     * @return
     */
    @GetMapping("/app/savePatrol")
    public AjaxResult savePatrol(@RequestParam(name = "file", required = false) MultipartFile[] file, SdPatrolList sdPatrolList)
    {
        return toAjax(sdTaskListService.savePatrol(file,sdPatrolList));
    }

    /**
     * app端首页待处理任务单
     * @return
     */
    @PostMapping("/app/taskToDo")
    public Result taskToDo(HttpServletRequest request){
        String token  = request.getHeader("Authorization");
        String deptId = SecurityUtils.getDeptId();
        //String deptId = "YG1";
        List<SdTaskList> taskList = sdTaskListService.getTaskToDo(deptId);
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("task",taskList);
        map.put("num", taskList.size());
        return Result.success(map);
    }



}
