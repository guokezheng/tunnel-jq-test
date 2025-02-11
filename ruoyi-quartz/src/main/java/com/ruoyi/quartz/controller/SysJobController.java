package com.ruoyi.quartz.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.service.ISysJobService;
import com.ruoyi.quartz.util.CronUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 调度任务信息操作处理
 *
 *
 */
@RestController
@RequestMapping("/monitor/job")
@Api(tags = "调度任务管理")
@ApiSupport(order = 99999)
public class SysJobController extends BaseController
{
    @Autowired
    private ISysJobService jobService;

    /**
     * 查询定时任务列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    @ApiOperation("查询定时任务列表")
    public TableDataInfo list(SysJob sysJob)
    {
        startPage();
        List<SysJob> list = jobService.selectJobList(sysJob);
        return getDataTable(list);
    }

    /**
     * 导出定时任务列表
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "定时任务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("导出定时任务列表")
    public AjaxResult export(SysJob sysJob)
    {
        List<SysJob> list = jobService.selectJobList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        return util.exportExcel(list, "定时任务");
    }

    /**
     * 获取定时任务详细信息
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{jobId}")
    @ApiOperation("获取定时任务详细信息")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId)
    {
        return AjaxResult.success(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:add')")
    @Log(title = "定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增定时任务")
    public AjaxResult add(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi://'调用");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_LDAP))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap://'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.HTTP, Constants.HTTPS }))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)//'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR))
        {
            return error("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        job.setCreateBy(getUsername());
        return jobService.insertJob(job);
    }

    /**
     * 修改定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:edit')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改定时任务")
    public AjaxResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(job.getCronExpression()))
        {
            return error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi://'调用");
        }
        else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_LDAP))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap://'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[] { Constants.HTTP, Constants.HTTPS }))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)//'调用");
        }
        else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), Constants.JOB_ERROR_STR))
        {
            return error("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        job.setUpdateBy(getUsername());
        return jobService.updateJob(job);
    }

    /**
     * 定时任务状态修改
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation("定时任务状态修改")
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    @ApiOperation("定时任务立即执行一次")
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException
    {
        jobService.run(job);
        return AjaxResult.success();
    }
    /**
     * 获取定时策略关联的Job信息
     *
     */
    @GetMapping(value = "/select/{id}")
    @ApiOperation("获取定时策略关联的Job信息")
    public AjaxResult getJobByRelationId(@PathVariable("id") String relationId)
    {
        SysJob job=new SysJob();
        job.setInvokeTarget(relationId);
        List<SysJob> sysJobs = jobService.selectJobList(job);
        if (sysJobs.size()>0){
            return AjaxResult.success(sysJobs.get(0));//
        }else {
            return AjaxResult.success("定时任务不存在");//
        }
    }
    /**
     * 删除定时任务
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{relationId}")
    @ApiOperation("删除定时任务")
    public AjaxResult remove(@PathVariable String relationId) throws SchedulerException, TaskException
    {
        SysJob job=new SysJob();
        /*job.setInvokeTarget(Long.valueOf(relationId));
        List<SysJob> sysJobs = jobService.selectJobList(job);
        for (SysJob rjob:sysJobs){
            jobService.deleteJob(rjob);
        }*/
        List<String> list = Arrays.asList(relationId.split(","));
        for(String sysJobId : list){
            job.setJobId(Long.valueOf(sysJobId));
            jobService.deleteJob(job);
        }
        return AjaxResult.success();
    }

    /**
     * 修改定时任务执行状态
     * @param job
     * @return
     */
    @PutMapping("/updateState")
    @ApiOperation("修改定时任务执行状态")
    public AjaxResult updateState(@RequestBody SysJob job) {
        return AjaxResult.success(jobService.updateState(job));
    }

    /**
     * 批量添加定时任务
     * @param maps
     * @return
     */
    @PostMapping("/batchScheduledJob")
    @ApiOperation("批量添加定时任务")
    public AjaxResult batchAddScheduledJob(@RequestBody List<Map> maps) throws SchedulerException {
        return AjaxResult.success(jobService.batchScheduledJob(maps));
    }
}
