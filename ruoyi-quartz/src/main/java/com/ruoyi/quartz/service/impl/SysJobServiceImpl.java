package com.ruoyi.quartz.service.impl;

import com.ruoyi.common.constant.ScheduleConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.job.TaskException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.mapper.SysJobMapper;
import com.ruoyi.quartz.service.ISysJobService;
import com.ruoyi.quartz.util.CronUtils;
import com.ruoyi.quartz.util.ScheduleUtils;
import com.tunnel.business.datacenter.util.CronUtil;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调度信息 服务层
 * 
 *
 */
@Service
public class SysJobServiceImpl implements ISysJobService
{
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SysJobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException
    {
        scheduler.clear();
        List<SysJob> jobList = jobMapper.selectJobAll();
        for (SysJob job : jobList)
        {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     * 
     * @param job 调度信息
     * @return
     */
    @Override
    public List<SysJob> selectJobList(SysJob job)
    {
        return jobMapper.selectJobList(job);
    }

    /**
     * 通过调度任务ID查询调度信息
     * 
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public SysJob selectJobById(Long jobId)
    {
        return jobMapper.selectJobById(jobId);
    }

    /**
     * 暂停任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(SysJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(SysJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(SysJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0)
        {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     * 
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException
    {
        for (Long jobId : jobIds)
        {
            SysJob job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    /**
     * 任务调度状态修改
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(SysJob job) throws SchedulerException
    {
        int rows = 0;
        String status = job.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status))
        {
            rows = resumeJob(job);
        }
        else if (ScheduleConstants.Status.PAUSE.getValue().equals(status))
        {
            rows = pauseJob(job);
        }
        return rows;
    }

    /**
     * 立即运行任务
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(SysJob job) throws SchedulerException
    {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        SysJob properties = selectJobById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
    }

    /**
     * 新增任务
     * 
     * @param job 调度信息 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertJob(SysJob job) throws SchedulerException, TaskException
    {
        //job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        //校验是否存在
        int count = jobMapper.checkJob(job);
        if(count > 0){
            return AjaxResult.error("定时任务名称已存在");
        }
        int rows = jobMapper.insertJob(job);
        if (rows > 0)
        {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return AjaxResult.success();
    }

    /**
     * 更新任务的时间表达式
     * 
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateJob(SysJob job) throws SchedulerException, TaskException
    {
        //校验是否存在
        int count = jobMapper.checkJob(job);
        if(count > 0){
            return AjaxResult.error("定时任务名称已存在");
        }
        SysJob properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0)
        {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return AjaxResult.success();
    }

    /**
     * 更新任务
     * 
     * @param job 任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException
    {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey))
        {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 校验cron表达式是否有效
     * 
     * @param cronExpression 表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression)
    {
        return CronUtils.isValid(cronExpression);
    }

    /**
     * 修改定时任务状态
     * @param job
     * @return
     */
    @Override
    public int updateState(SysJob job) {
        int result = -1;
        SdStrategy sdStrategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyByJobRelationId(job.getInvokeTarget());
        sdStrategy.setStrategyState(job.getStatus());
        result = SpringUtils.getBean(SdStrategyMapper.class).updateSdStrategyById(sdStrategy);
        if ("1".equals(sdStrategy.getStrategyType()) && result > -1){
            SysJob sysJob = new SysJob();
            sysJob.setInvokeTarget(job.getInvokeTarget());
            List<SysJob> sysJobs = jobMapper.selectJobList(sysJob);
            for (SysJob job1 : sysJobs) {
                job1.setStatus(job.getStatus());
                result = jobMapper.updateJob(job1);
            }
        }
        return result;
    }

    /**
     * 批量添加定时任务
     * @param maps
     * @return
     */
    @Override
    public int batchScheduledJob(List<Map> maps) throws SchedulerException {
        List<SysJob> jobs = new ArrayList<>();
        if (StringUtils.isNotEmpty(maps)) {
            for (Map<String, Object> map : maps) {
                String name = (String) map.get("name");
                String guid = (String) map.get("guid");
                String time = (String) map.get("time");
                SysJob job = new SysJob();
                // 定时任务名称
                job.setJobName(name);
                // 调用目标字符串
                job.setInvokeTarget("strategyTask.strategyParams('" + guid + "')");
                // corn表达式
                String cronDate = CronUtil.DateConvertCron(time);
                job.setCronExpression(cronDate);
                // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
                job.setMisfirePolicy("1");
                // 是否并发执行（0允许 1禁止）
                job.setConcurrent("0");
                // 状态（0正常 1暂停）
                job.setStatus("0");
                jobs.add(job);
            }
        }
        if (jobs.size() > 0){
            for (SysJob job : jobs) {
                List<SysJob> jobList = jobMapper.selectJobList(job);
                if (jobList.size() > 0) {
                    for (SysJob sysJob : jobList) {
                        deleteJob(sysJob);
                    }
                }
            }
        }
        int result = -1;
        if (jobs.size() > 0) {
            result = jobMapper.batchScheduledJob(jobs);
        }
        return result;
    }

}
