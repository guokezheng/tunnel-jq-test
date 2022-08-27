package com.tunnel.platform.service.dataInfo.impl;

import com.alibaba.fastjson.JSONObject;
import com.tunnel.platform.domain.dataInfo.SdPushCall;
import com.tunnel.platform.mapper.dataInfo.SdPushCallMapper;
import com.tunnel.platform.service.dataInfo.ISdPushCallService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 紧急电话推送记录Service业务层处理
 * 
 * @author wangx
 * @date 2021-03-15
 */
@Service
public class SdPushCallServiceImpl implements ISdPushCallService 
{
    @Autowired
    private SdPushCallMapper sdPushCallMapper;

    /**
     * 查询紧急电话推送记录
     * 
     * @param id 紧急电话推送记录ID
     * @return 紧急电话推送记录
     */
    @Override
    public SdPushCall selectSdPushCallById(Long id)
    {
        return sdPushCallMapper.selectSdPushCallById(id);
    }

    /**
     * 查询紧急电话推送记录列表
     * 
     * @param sdPushCall 紧急电话推送记录
     * @return 紧急电话推送记录
     */
    @Override
    public List<SdPushCall> selectSdPushCallList(SdPushCall sdPushCall)
    {
    	List<SdPushCall> list = sdPushCallMapper.selectSdPushCallList(sdPushCall);
    	for(int i=0;i<list.size();i++){
    		// 获取接收数据
    		String reserveData = list.get(i).getReserveData();
    		if(getJson(reserveData)){
    			String returnData = "";
    			JSONObject jsonObj = JSONObject.parseObject(reserveData);
    			if(reserveData.contains("ext")){
    				returnData += "主叫编号：" + jsonObj.getJSONObject("ext").get("id").toString() + "   ";
    			}
    			if(reserveData.contains("attribute")){
    				String contents = "";
    				switch(jsonObj.get("attribute").toString()){
    					case "ALERT":
    						contents = "呼叫";
    						break;
    					case "ANSWERED":
    						contents = "应答";
    						break;
    					case "BYE":
    						contents = "通话释放";
    						break;
    					case "BUSY":
    						contents = "通话忙碌";
    						break;
    					case "IDLE":
    						contents = "空闲";
    						break;
    				}
    				returnData += "状态：" + contents + "   ";
    			}
    			if(reserveData.contains("visitor")){
    				String from = jsonObj.getJSONObject("visitor").get("from").toString();
    				String to = jsonObj.getJSONObject("visitor").get("to").toString();
    				returnData += "内容：" + from + "呼叫" + to + "   ";;
    			}
    			if(reserveData.contains("outer")){
    				String to = jsonObj.getJSONObject("outer").get("to").toString();
    				returnData += "被呼叫方：" + to + "   ";;
    			}
    			list.get(i).setReserveData(returnData);
    		}else{
    			continue;
    		}
    	}
        return list;
    }
    
    /**
     * 判断是否为json格式
     * @param content
     * @return
     */
    public boolean getJson(String content){
    	try {
            JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 新增紧急电话推送记录
     * 
     * @param sdPushCall 紧急电话推送记录
     * @return 结果
     */
    @Override
    public int insertSdPushCall(SdPushCall sdPushCall)
    {
        sdPushCall.setCreateTime(DateUtils.getNowDate());
        return sdPushCallMapper.insertSdPushCall(sdPushCall);
    }

    /**
     * 修改紧急电话推送记录
     * 
     * @param sdPushCall 紧急电话推送记录
     * @return 结果
     */
    @Override
    public int updateSdPushCall(SdPushCall sdPushCall)
    {
        sdPushCall.setUpdateTime(DateUtils.getNowDate());
        return sdPushCallMapper.updateSdPushCall(sdPushCall);
    }

    /**
     * 批量删除紧急电话推送记录
     * 
     * @param ids 需要删除的紧急电话推送记录ID
     * @return 结果
     */
    @Override
    public int deleteSdPushCallByIds(Long[] ids)
    {
        return sdPushCallMapper.deleteSdPushCallByIds(ids);
    }

    /**
     * 删除紧急电话推送记录信息
     * 
     * @param id 紧急电话推送记录ID
     * @return 结果
     */
    @Override
    public int deleteSdPushCallById(Long id)
    {
        return sdPushCallMapper.deleteSdPushCallById(id);
    }
}
