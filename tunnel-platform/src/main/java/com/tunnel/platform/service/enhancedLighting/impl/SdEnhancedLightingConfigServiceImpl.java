package com.tunnel.platform.service.enhancedLighting.impl;


import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.quartz.domain.SysJob;
import com.ruoyi.quartz.service.ISysJobService;
import com.tunnel.business.domain.enhancedLighting.SdEnhancedLightingConfig;
import com.tunnel.business.mapper.enhancedLighting.SdEnhancedLightingConfigMapper;
import com.tunnel.platform.service.enhancedLighting.ISdEnhancedLightingConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ruoyi
 * @date 2023-03-24
 */
@Service
public class SdEnhancedLightingConfigServiceImpl implements ISdEnhancedLightingConfigService
{

    @Autowired
    private SdEnhancedLightingConfigMapper sdEnhancedLightingConfigMapper;

    @Autowired
    private   ISysJobService sysJobService;

//    @Autowired
//    private SysJobServiceImpl sysJobService;

    /**
     * Redis缓存工具类
     * */
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 查询【加强照明配置】
     *
     * @param id 【加强照明配置】主键
     * @return 【加强照明配置】
     */
    @Override
    public SdEnhancedLightingConfig selectSdEnhancedLightingConfigById(Long id)
    {
        return sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigById(id);
    }

    /**
     * 查询【加强照明配置】列表
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 【加强照明配置】
     */
    @Override
    public List<SdEnhancedLightingConfig> selectSdEnhancedLightingConfigList(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        return sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigList(sdEnhancedLightingConfig);
    }

    @Override
    public SdEnhancedLightingConfig selectSdEnhancedLightingConfigListByParam(SdEnhancedLightingConfig sdEnhancedLightingConfig) {
        return sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigListByParam(sdEnhancedLightingConfig);
    }

    /**
     * 新增【加强照明配置】
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 结果
     */
    @Override
    public SdEnhancedLightingConfig insertSdEnhancedLightingConfig(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        SdEnhancedLightingConfig sdEnhancedLightingConfig1 = new SdEnhancedLightingConfig();
        sdEnhancedLightingConfig1.setTunnelId(sdEnhancedLightingConfig.getTunnelId());
        List<SdEnhancedLightingConfig> sdEnhancedLightingConfigs = sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigList(sdEnhancedLightingConfig1);
        if(sdEnhancedLightingConfigs.size()>0){
            throw new RuntimeException("该隧道已存在，不可重复新增！");
        }
        //创建定时任务
        jobCreate(sdEnhancedLightingConfig);
        sdEnhancedLightingConfigMapper.insertSdEnhancedLightingConfig(sdEnhancedLightingConfig);
        return sdEnhancedLightingConfig;
    }

    /**
     * 修改【加强照明配置】
     *
     * @param sdEnhancedLightingConfig 【加强照明配置】
     * @return 结果
     */
    @Override
    public int updateSdEnhancedLightingConfig(SdEnhancedLightingConfig sdEnhancedLightingConfig)
    {
        //修改定时任务
        jobCreate(sdEnhancedLightingConfig);
        return sdEnhancedLightingConfigMapper.updateSdEnhancedLightingConfig(sdEnhancedLightingConfig);
    }

    /**
     * 批量删除【加强照明配置】
     *
     * @param ids 需要删除的【加强照明配置】主键
     * @return 结果
     */
    @Override
    public int deleteSdEnhancedLightingConfigByIds(Long[] ids)
    {
        try{
            for (int i = 0; i < ids.length; i++) {
                SdEnhancedLightingConfig sdEnhancedLightingConfig1 = new SdEnhancedLightingConfig();
                sdEnhancedLightingConfig1.setId(ids[i]);
                List<SdEnhancedLightingConfig> sdEnhancedLightingConfigs = sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigList(sdEnhancedLightingConfig1);
                if(sdEnhancedLightingConfigs.size()>0){
                    SysJob jobData = new SysJob();
                    jobData.setJobName("车来灯亮关灯-" + sdEnhancedLightingConfigs.get(0).getTunnelId());
                    List<SysJob> sysJobs = sysJobService.selectJobList(jobData);
                    SysJob job = null;
                    if(sysJobs.size()>0){
                        sysJobService.deleteJob(sysJobs.get(0));
                    }
                }
            }

        }catch (Exception e){
        }
        return sdEnhancedLightingConfigMapper.deleteSdEnhancedLightingConfigByIds(ids);
    }

    /**
     * 删除【加强照明配置】信息
     *
     * @param id 【加强照明配置】主键
     * @return 结果
     */
    @Override
    public int deleteSdEnhancedLightingConfigById(Long id)
    {
        try{
            SdEnhancedLightingConfig sdEnhancedLightingConfig1 = new SdEnhancedLightingConfig();
            sdEnhancedLightingConfig1.setId(id);
            List<SdEnhancedLightingConfig> sdEnhancedLightingConfigs = sdEnhancedLightingConfigMapper.selectSdEnhancedLightingConfigList(sdEnhancedLightingConfig1);
            if(sdEnhancedLightingConfigs.size()>0){
                SysJob jobData = new SysJob();
                jobData.setJobName("车来灯亮关灯-" + sdEnhancedLightingConfigs.get(0).getTunnelId());
                List<SysJob> sysJobs = sysJobService.selectJobList(jobData);
                if(sysJobs.size()>0){
                    sysJobService.deleteJob(sysJobs.get(0));
                }
            }
        }catch (Exception e){
        }
        return sdEnhancedLightingConfigMapper.deleteSdEnhancedLightingConfigById(id);
    }




    /**
     *
     * @param nowTrafficFlow    当前车流量
     * @param maxTrafficFlow     最大车流量
     * @param maxLuminanceRange     最大调光区间值
     * @param minLuminanceRange     最小调光区间值
     * @param luminanceRange        当前时间段调光值
     * @return
     */
    @Override
    public int getLuminanceByParam(Integer nowTrafficFlow, Integer maxTrafficFlow, Integer maxLuminanceRange, Integer minLuminanceRange, Integer luminanceRange) {
        if(nowTrafficFlow >= maxTrafficFlow ){
            //当前车流量大于现在车流量
            return luminanceRange+maxLuminanceRange;
        }else{
            Integer regionLuminanceRange = maxLuminanceRange - minLuminanceRange;
            //计算公式  (当前车流量/最大车流量)*亮度区间值
            BigDecimal nowTrafficFlowBig = new BigDecimal(nowTrafficFlow);
            BigDecimal maxTrafficFlowBig = new BigDecimal(maxTrafficFlow);
            BigDecimal regionLuminanceRangeBig = new BigDecimal(regionLuminanceRange);
            nowTrafficFlowBig = nowTrafficFlowBig.divide(maxTrafficFlowBig, 2,BigDecimal.ROUND_HALF_UP).multiply(regionLuminanceRangeBig);
            return luminanceRange + (nowTrafficFlowBig.intValue()/5)*5;
        }
    }

    public void jobCreate(SdEnhancedLightingConfig sdEnhancedLightingConfig){
        try{
            SysJob jobData = new SysJob();
            jobData.setJobName("车来灯亮关灯-" + sdEnhancedLightingConfig.getTunnelId());
            List<SysJob> sysJobs = sysJobService.selectJobList(jobData);
            SysJob job = null;
            if(sysJobs.size()>0){
                job = sysJobs.get(0);
            }else{
                job = new SysJob();
            }
            // 定时任务名称
            job.setJobName("车来灯亮关灯-" + sdEnhancedLightingConfig.getTunnelId());
            // 调用目标字符串
            job.setInvokeTarget("lightFixedTimeTask.dimmingTask('" + sdEnhancedLightingConfig.getTunnelId() + "')");
            job.setCronExpression("0/10 * * * * ?");
            // 计划执行错误策略（1立即执行 2执行一次 3放弃执行）
            job.setMisfirePolicy("1");
            // 是否并发执行（0允许 1禁止）
            job.setConcurrent("0");
            //定时任务分组
            job.setJobGroup("SYSTEM");
            if(null != sdEnhancedLightingConfig.getIsStatus() && sdEnhancedLightingConfig.getIsStatus()==0){
                job.setStatus("1");
            }else{
                // 0=正常,1=暂停
                 job.setStatus("0");
            }
            if(job.getJobId()!=null){
                sysJobService.updateJob(job);
            }else{
                sysJobService.insertJob(job);
            }
        }catch (Exception e){
        }
    }
}
