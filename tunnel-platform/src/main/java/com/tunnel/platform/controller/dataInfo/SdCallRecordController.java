package com.tunnel.platform.controller.dataInfo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.dataInfo.SdCallRecord;
import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.platform.service.dataInfo.ISdCallRecordService;
import com.tunnel.platform.service.dataInfo.ISdDevicesService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.tunnel.platform.utils.util.InterfaceAccessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 紧急电话记录Controller
 * 
 * @author yanghousheng
 * @date 2020-11-10
 */
@RestController
@RequestMapping("/record")
public class SdCallRecordController extends BaseController
{
    @Autowired
    private ISdCallRecordService sdCallRecordService;
    @Autowired
    private ISdDevicesService sdDevicesService;

    /**
     * 查询紧急电话记录列表
     */
    @GetMapping("/list")
    public AjaxResult list(SdCallRecord sdCallRecord)
    {
    	// abc();
    	// List<Map<String,String>> echartsData = sdCallRecordService.selectSdCallRecordEcharts(sdCallRecord);
        startPage();
        List<SdCallRecord> tableList = sdCallRecordService.selectSdCallRecordList(sdCallRecord);
        Map<String,Object> map = new HashMap<String,Object>();
        // map.put("echartsData", echartsData);
        map.put("tableData", getDataTable(tableList));
        
        return AjaxResult.success(map);
    }
    
    public void abc(){
        	List<SdDevices> devLists = sdDevicesService.selectSdDevicesList(new SdDevices());
        	for (int i = 0; i < devLists.size(); i++) {
        		SdDevices sdDevices = devLists.get(i);
        		String stack = devLists.get(i).getPile();
        		//ZK59+459
        		if(stack.contains("ZK")){
        			int k = Integer.parseInt(stack.substring(2, 4));
        			int newK = k+189;
        			int point = Integer.parseInt(stack.substring(5, 8));
        			int newPoint = point + 66;
        			if(newPoint > 1000){
        				newK = newK + 1;
        			}
        			Integer newKInt =new Integer(newK);
    				String s1 =newKInt.toString();
    				Integer newPointInt =new Integer(newPoint);
    				String s2 =newPointInt.toString();
    				String s3 = "";
    				if(stack.length()>8){
    					s3 = stack.substring(0, 2) + s1 +"+" + s2 + stack.substring(8, stack.length());
    				}else{
    					s3 = stack.substring(0, 2) + s1 +"+" + s2;
    				}
    				sdDevices.setPile(s3);
    				System.out.println(s3);
    				sdDevicesService.updateSdDevices(sdDevices);
        		}else if(stack.contains("YK")){
        			String a = stack.substring(2,4);
        			int k = Integer.parseInt(a);
        			int newK = k+189;
        			int point = Integer.parseInt(stack.substring(5, 8));
        			int newPoint = point + 66;
        			if(newPoint > 1000){
        				newK = newK + 1;
        			}
        			Integer newKInt =new Integer(newK);
    				String s1 =newKInt.toString();
    				Integer newPointInt =new Integer(newPoint);
    				String s2 =newPointInt.toString();
    				String s3 = "";
    				if(stack.length()>8){
    					s3 = stack.substring(0, 2) + s1 +"+" + s2 + stack.substring(8, stack.length());
    				}else{
    					s3 = stack.substring(0, 2) + s1 +"+" + s2;
    				}
    				sdDevices.setPile(s3);
    				System.out.println(s3);
    				sdDevicesService.updateSdDevices(sdDevices);
        		}
    		}
    }

    /**
     * 导出紧急电话记录列表
     */
    /*@Log(title = "紧急电话记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SdCallRecord sdCallRecord) throws IOException
    {
        List<SdCallRecord> list = sdCallRecordService.selectSdCallRecordList(sdCallRecord);
        ExcelUtil<SdCallRecord> util = new ExcelUtil<SdCallRecord>(SdCallRecord.class);
        util.exportExcel(response, list, "record");
    }*/

    /**
     * 获取紧急电话记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sdCallRecordService.selectSdCallRecordById(id));
    }

    /**
     * 新增紧急电话记录
     */
    @Log(title = "紧急电话记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SdCallRecord sdCallRecord)
    {
        return toAjax(sdCallRecordService.insertSdCallRecord(sdCallRecord));
    }

    /**
     * 修改紧急电话记录
     */
    @Log(title = "紧急电话记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SdCallRecord sdCallRecord)
    {
        return toAjax(sdCallRecordService.updateSdCallRecord(sdCallRecord));
    }

    /**
     * 删除紧急电话记录
     */
    @Log(title = "紧急电话记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sdCallRecordService.deleteSdCallRecordByIds(ids));
    }
    
    /**
     * 获取位置信息列表
     */
    @GetMapping("/positionList")
    public AjaxResult positionList()
    {
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	try{
    		String token = getToken();
    		String positionUrl = "http://localhost:8980/api/platform/pathList";
    		String positionResult = InterfaceAccessUtil.doGet(positionUrl, token);
        	JSONObject positionResultObject = JSONObject.parseObject(positionResult);
        	JSONArray callArr = positionResultObject.getJSONObject("data").getJSONArray("items");
        	// 位置信息
        	for(int i=0;i<callArr.size();i++){
        		String pathId = callArr.getJSONObject(i).get("pathId").toString();
        		String name = callArr.getJSONObject(i).get("name").toString();
        		String pathNo = callArr.getJSONObject(i).get("pathNo").toString();
        		if("00".equals(pathNo)){
        			continue;
        		}
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("pathId", pathId);
        		map.put("name", name);
        		list.add(map);
        	}
        	
    	}catch(Exception e){
    		return AjaxResult.success(list);
    	}
    	return AjaxResult.success(list);
    }
    
    /**
     * 获取token
     * @return
     */
    public String getToken(){
    	String token = "";
    	try{
    		String loginUrl = "http://localhost:8980/api/platform/login";
        	String tokenResult = InterfaceAccessUtil.doPostParams(loginUrl, "", "");
        	token = JSONObject.parseObject(tokenResult).get("token").toString();
    	}catch(Exception e){
    		return "";
    	}
    	
		return token;
    }
    
    /**
     * 通话记录-定时执行
     * @param params
     */
    /*@Scheduled(fixedRate = 1000*60*60*24)*/
    public void callLog(String params)
    {
    	System.out.println("定时获取通话记录");
    	// 登录获取token
    	try{
    		String token = getToken();
        	// 获取通话记录列表
        	String callRecordUrl = "http://localhost:8980/api/platform/callRecordList";
        	// 查询昨日到今天的时间
        	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        	SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	String TimeStart = simpleDateFormat.format(new Date());
        	Calendar calendar = new GregorianCalendar();
        	calendar.setTime(new Date());
        	calendar.add(calendar.DATE,-1);
        	calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        	String TimeEnd = simpleDateFormat.format(simpleDateFormat.format(calendar.getTime()));
        	callRecordUrl += "?TimeStart=" + TimeStart + "&TimeEnd=" + TimeEnd + "&pageSize=" + 10000;
        	// GET的方式获取
        	String callRecordResult = InterfaceAccessUtil.doGet(callRecordUrl, token);
        	JSONObject callJsonObject = JSONObject.parseObject(callRecordResult);
        	JSONArray callArr = callJsonObject.getJSONObject("data").getJSONArray("items");
        	List<SdCallRecord> sdCallRecordList = new ArrayList<SdCallRecord>();
        	for(int i=0;i<callArr.size();i++){
        		SdCallRecord SdCallRecord = new SdCallRecord();
        		// 主叫号码
        		String cpn = callArr.getJSONObject(i).get("CPN").toString();
        		// 位置ID
        		JSONArray pathIdArr = callArr.getJSONObject(i).getJSONArray("pathId");
        		String pathId = "";
        		for(int j=0;i<pathIdArr.size();i++){
        			pathId = pathId + pathIdArr.get(j).toString() + ";";
        		}
        		// 电话内容
        		String recording = callArr.getJSONObject(i).get("recording").toString();
        		// 通话ID
        		String callRecordId = callArr.getJSONObject(i).get("callRecordId").toString();
        		// 大于或等于此开始时间
        		String timeStart = callArr.getJSONObject(i).get("TimeStart").toString();
        		Date start = simpleDateFormat.parse(timeStart);
        		// 通话类别
        		String Type = callArr.getJSONObject(i).get("Type").toString();
        		// 被叫号码
        		String CDPN = callArr.getJSONObject(i).get("CDPN").toString();
        		// 小于或等于此结束时间
        		String timeEnd = callArr.getJSONObject(i).get("TimeEnd").toString();
        		Date end = simpleDateFormat.parse(timeEnd);
        		SdCallRecord.setType(Type);
        		SdCallRecord.setTimeStart(simpleDateFormat1.format(start));
        		SdCallRecord.setTimeEnd(simpleDateFormat1.format(end));
        		SdCallRecord.setCpn(cpn);
        		SdCallRecord.setCdpn(CDPN);
        		SdCallRecord.setTelephoneContent(recording);
        		SdCallRecord.setCallId(callRecordId);
        		SdCallRecord.setPathId(pathId);
        		sdCallRecordList.add(SdCallRecord);
        	}
        	sdCallRecordService.insertSdCallRecordList(sdCallRecordList);
    	}catch(Exception e){
    		System.out.println(e);
    	}
    	
    	
    }
}
