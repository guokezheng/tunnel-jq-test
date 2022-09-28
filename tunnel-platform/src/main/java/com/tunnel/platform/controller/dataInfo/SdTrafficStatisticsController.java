package com.tunnel.platform.controller.dataInfo;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.business.domain.dataInfo.SdDevices;
import com.tunnel.business.domain.dataInfo.SdSensorMessage;
import com.tunnel.business.domain.dataInfo.SdTrafficStatistics;
import com.tunnel.business.service.dataInfo.ISdDevicesService;
import com.tunnel.business.service.dataInfo.ISdSensorMessageService;
import com.tunnel.business.service.dataInfo.ISdTrafficStatisticsService;
import com.tunnel.business.utils.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 车流量信息Controller
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
@RestController
@RequestMapping("/statistics")
public class SdTrafficStatisticsController extends BaseController
{
    @Autowired
    private ISdTrafficStatisticsService sdTrafficStatisticsService;
    
    @Autowired
	private ISdDevicesService sdDevicesService;
    
    @Autowired
	 private RedisCache redisCache;
    
    @Autowired
    private ISdSensorMessageService sdSensorMessageService;
    
    /**
     * 查询车流量信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SdTrafficStatistics sdTrafficStatistics)
    {
        startPage();
        List<SdTrafficStatistics> list = sdTrafficStatisticsService.selectSdTrafficStatisticsList(sdTrafficStatistics);
        return getDataTable(list);
    }
    
    /**
     * 查询车流量信息列表
     */
    @GetMapping("/analysisData")
    public AjaxResult analysisData(String tunnelId,String holes)
    {
    	Map<String,List<SdTrafficStatistics>> data 
    		= sdTrafficStatisticsService.getAnalysisData(tunnelId,holes);
        return AjaxResult.success("成功", data);
    }
    
    /**
     * 按时间查询车流量信息列表
     */
    @GetMapping("/analysisDataByTIme")
    public AjaxResult analysisDataByTime(SdTrafficStatistics sdTrafficStatistics)
    {
    	List<SdTrafficStatistics> data
    		= sdTrafficStatisticsService.analysisDataByTime(sdTrafficStatistics);
        return AjaxResult.success("成功", data);
    }
    
    /**
     * 大屏查询车流量信息列表
     */
    @GetMapping("/bigscreenStatisticsList")
    public AjaxResult bigscreenStatisticsList(String tunnelId,String direction)
    {
    	SdTrafficStatistics sd =new SdTrafficStatistics();
    	sd.setTunnelId(tunnelId);
    	sd.setEqDirection(direction);
    	List<SdTrafficStatistics> list = sdTrafficStatisticsService.selectSdTrafficStatisticsToday(sd);
    	int sumNum1 = 0;
    	int sumNum2 = 0;
    	int sumNum3 = 0;
    	int sumNum4 = 0;
    	for(int i=0;i<list.size();i++){
    		SdTrafficStatistics SdTrafficStatistics = list.get(i);
    		long num = SdTrafficStatistics.getDwLightVehicle() + SdTrafficStatistics.getDwMidVehicle() +
    		SdTrafficStatistics.getDwHeavyVehicle();
    		Calendar date = Calendar.getInstance();
    		date.setTime(SdTrafficStatistics.getCreateTime());
    		// 获取time的时
    		int hour = date.get(Calendar.HOUR_OF_DAY);
    		if(hour > 0 && hour <= 6){
    			sumNum1 += num;
    		}
    		else if(hour > 6 && hour <= 12){
    			sumNum2 += num;
    		}
    		else if(hour > 12 && hour <= 18){
    			sumNum3 += num;
    		}
    		else if(hour > 18 && hour <= 24){
    			sumNum4 += num;
    		}
    		
    	}
    	int[] lcount = {sumNum1,sumNum2,sumNum3,sumNum4};
    	return AjaxResult.success("成功", lcount);
    }
    
    /**
     * CO等
     */
    @GetMapping("/bigscreenChartList")
    public AjaxResult bigscreenChartList(String tunnelId,String typeId)
    {
    	SdSensorMessage sdSensorMessage = new SdSensorMessage();
    	sdSensorMessage.setEqTunnelId(tunnelId);
    	sdSensorMessage.setEqType(typeId);
    	List<SdSensorMessage> list = sdSensorMessageService.selectSdSensorMessageNow(sdSensorMessage);
    	int[] acount = {0,0,0,0,0,0,0,0,0,0,0,0};
    	for(int i=0;i<list.size();i++){
    		// 时间
    		GregorianCalendar calendar = new GregorianCalendar();
    		calendar.setTime(list.get(i).getCreateTime());
    		int hour = calendar.get(Calendar.HOUR_OF_DAY);
    		// 数据
    		String value = list.get(i).getSensorValue();
    		if("".equals(value) || value == null){
    			value = "0";
    		}
    		if((hour > 0 || hour <= 2) && acount[0] == 0){
    			acount[0] = Integer.valueOf(value);
    		}else if((hour > 2 || hour <= 4) && acount[1] == 0){
    			acount[1] = Integer.valueOf(value);
    		}else if((hour > 4 || hour <= 6) && acount[2] == 0){
    			acount[2] = Integer.valueOf(value);
    		}else if((hour > 6 || hour <= 8) && acount[3] == 0){
    			acount[3] = Integer.valueOf(value);
    		}else if((hour > 8 || hour <= 10) && acount[4] == 0){
    			acount[4] = Integer.valueOf(value);
    		}else if((hour > 10 || hour <= 12) && acount[5] == 0){
    			acount[5] = Integer.valueOf(value);
    		}else if((hour > 12 || hour <= 14) && acount[6] == 0){
    			acount[6] = Integer.valueOf(value);
    		}else if((hour > 14 || hour <= 16) && acount[7] == 0){
    			acount[7] = Integer.valueOf(value);
    		}else if((hour > 16 || hour <= 18) && acount[8] == 0){
    			acount[8] = Integer.valueOf(value);
    		}else if((hour > 18 || hour <= 20) && acount[9] == 0){
    			acount[9] = Integer.valueOf(value);
    		}else if((hour > 20 || hour <= 22) && acount[10] == 0){
    			acount[10] = Integer.valueOf(value);
    		}else if((hour > 22 || hour <= 24) && acount[11] == 0){
    			acount[11] = Integer.valueOf(value);
    		}
    	}
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("type", typeId);
    	map.put("datas", acount);
    	return AjaxResult.success("成功", map);
    }
    /**
     * 大屏查询车流量信息列表
     */
    @GetMapping("/bigscreenCgqList")
    public AjaxResult CgqList(String tunnelId,String direction)
    {
    	redisCache = (RedisCache) SpringContextUtils.getBean(RedisCache.class);
    	SdDevices sdDevices = new SdDevices();
    	sdDevices.setEqTunnelId(tunnelId);
    	sdDevices.setEqDirection(direction);
    	List<SdDevices> list = sdDevicesService.selectSdDevicesList(sdDevices);
    	int sumNum1 = 0;
    	int sumNum2 = 0;
    	int sumNum3 = 0;
    	int sumNum4 = 0;
    	int sumNum5 = 0;
    	int sumNum6 = 0;
    	for(int i=0;i<list.size();i++){
    		SdDevices devices = list.get(i);
    		long type = 0;
    		try{
    			type = devices.getEqType();
			}catch(Exception e){
				type = 0;
			}
    		if(type == 14){
    			String id = devices.getEqId();
    			try{
    				sumNum1 = redisCache.getCacheObject(id);
    			}catch(Exception e){
    				sumNum1 = 0;
    			}
    			
    		}else if(type == 15){
    			String id = devices.getEqId();
    			try{
    				sumNum2 = redisCache.getCacheObject(id);
    			}catch(Exception e){
    				sumNum2 = 0;
    			}
    			
    		}else if(type == 16){
    			String id = devices.getEqId();
    			try{
    				sumNum3 = redisCache.getCacheObject(id);
    			}catch(Exception e){
    				sumNum3 = 0;
    			}
    		}else if(type == 13){
    			String id = devices.getEqId();
    			try{
    				sumNum4 = redisCache.getCacheObject(id);
    			}catch(Exception e){
    				sumNum4 = 0;
    			}
    		}else if(type == 5){
    			String id = devices.getEqId();
    			try{
    				sumNum5 = redisCache.getCacheObject(id);
    			}catch(Exception e){
    				sumNum5 = 0;
    			}
    		}else if(type == 6){
    			String id = devices.getEqId();
    			try{
    				sumNum6 = redisCache.getCacheObject(id);
    			}catch(Exception e){
    				sumNum6 = 0;
    			}
    		}
    		
    	}
    	int[] lcount = {sumNum1,sumNum2,sumNum3,sumNum4,sumNum5,sumNum6};
    	return AjaxResult.success("成功", lcount);
    }
    
    /**
     * 导出车流量信息列表
     */
    /*@PreAuthorize(hasPermi="system:statistics:export")
    @Log(title = "车流量信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdTrafficStatistics sdTrafficStatistics) throws IOException
    {
        List<SdTrafficStatistics> list = sdTrafficStatisticsService.selectSdTrafficStatisticsList(sdTrafficStatistics);
        ExcelUtil<SdTrafficStatistics> util = new ExcelUtil<SdTrafficStatistics>(SdTrafficStatistics.class);
        util.exportExcel(response, list, "statistics");
    }*/

    /**
     * 获取车流量信息详细信息
     */
    @GetMapping(value = "/{statisticsId}")
    public AjaxResult getInfo(@PathVariable("statisticsId") Long statisticsId)
    {
        return AjaxResult.success(sdTrafficStatisticsService.selectSdTrafficStatisticsById(statisticsId));
    }
    
    /**
     * 查询车流量信息列表
     */
  /*  @PreAuthorize(hasPermi="system:statistics:queryStatistics")
    @GetMapping("/queryStatistics")
    public Map<String, Object> queryStatistics(@PathVariable("beginTime") String beginTime,@PathVariable("endTime") String endTime)
    {
    	
        return sdTrafficStatisticsService.queryStatistics(beginTime,endTime);
    }*/

    /**
     * 新增车流量信息
     */
    @Log(title = "车流量信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdTrafficStatistics sdTrafficStatistics)
    {
        return toAjax(sdTrafficStatisticsService.insertSdTrafficStatistics(sdTrafficStatistics));
    }

    /**
     * 修改车流量信息
     */
    @Log(title = "车流量信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdTrafficStatistics sdTrafficStatistics)
    {
        return toAjax(sdTrafficStatisticsService.updateSdTrafficStatistics(sdTrafficStatistics));
    }

    /**
     * 删除车流量信息
     */
    @Log(title = "车流量信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{statisticsIds}")
    public AjaxResult remove(@PathVariable Long[] statisticsIds)
    {
        return toAjax(sdTrafficStatisticsService.deleteSdTrafficStatisticsByIds(statisticsIds));
    }

	/**
	 * 获取运营APP首页需要的车流量数据
	 */
	@PostMapping("/getTrafficFlowMsgToApp")
	public AjaxResult getTrafficFlowMsgToApp()
	{
		return AjaxResult.success(sdTrafficStatisticsService.getTrafficFlowMsgToApp());
	}
    
}
