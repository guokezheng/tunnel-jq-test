package com.tunnel.platform.service.event.impl;

import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.event.SdStrategyRl;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.platform.mapper.event.SdStrategyRlMapper;
import com.tunnel.platform.service.event.ISdStrategyRlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 策略关联设备信息Service业务层处理
 * 
 * @author gongfanfei
 * @date 2020-08-31
 */
@Service
public class SdStrategyRlServiceImpl implements ISdStrategyRlService 
{
    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;
    @Autowired
    private SdEquipmentStateMapper sdEquipmentStateMapper;
    /**
     * 查询策略关联设备信息
     * 
     * @param id 策略关联设备信息ID
     * @return 策略关联设备信息
     */
    @Override
    public SdStrategyRl selectSdStrategyRlById(Long id)
    {
        return sdStrategyRlMapper.selectSdStrategyRlById(id);
    }
   /* List<SdStrategyRl> list = sdStrategyRlMapper.selectSdStrategyRlList(sdStrategyRl);
    for (int i = 0; i < list.size(); i++) {
    	SdEquipmentState sdEquipmentState = new SdEquipmentState();
    	sdEquipmentState.setStateTypeId(Long.parseLong(sdStrategyRl.getEqTypeId()));
    	List<SdEquipmentState> stateList = sdEquipmentStateMapper.selectSdEquipmentStateList(sdEquipmentState);
    	List stat = new ArrayList();
    	for (int k = 0; k < stateList.size(); k++) {
    		stat.add(stateList.get(k).get)
		}
    	
    	list.get(i).setStateList(stateList);
	}
    return null;*/
    /**
     * 查询策略关联设备信息列表
     * 
     * @param sdStrategyRl 策略关联设备信息
     * @return 策略关联设备信息
     */
    @Override
    public List<SdStrategyRl> selectSdStrategyRlList(SdStrategyRl sdStrategyRl)
    {
    	List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlList(sdStrategyRl);
    	for (int i = 0; i < rlList.size(); i++) {
    		SdEquipmentState sdEquipmentState = new SdEquipmentState();
    		sdEquipmentState.setStateTypeId(Long.parseLong(rlList.get(i).getEqTypeId()));
    		sdEquipmentState.setIsControl(1);
    		List<SdEquipmentState> stateList = sdEquipmentStateMapper.selectDropSdEquipmentStateList(sdEquipmentState);
    		rlList.get(i).setEqStateList(stateList);
    	}
        return rlList;
    }

    /**
     * 新增策略关联设备信息
     * 
     * @param sdStrategyRl 策略关联设备信息
     * @return 结果
     */
    @Override
    public int insertSdStrategyRl(SdStrategyRl sdStrategyRl)
    {
        return sdStrategyRlMapper.insertSdStrategyRl(sdStrategyRl);
    }

    /**
     * 修改策略关联设备信息
     * 
     * @param sdStrategyRl 策略关联设备信息
     * @return 结果
     */
    @Override
    public int updateSdStrategyRl(SdStrategyRl sdStrategyRl)
    {
        return sdStrategyRlMapper.updateSdStrategyRl(sdStrategyRl);
    }

    /**
     * 批量删除策略关联设备信息
     * 
     * @param ids 需要删除的策略关联设备信息ID
     * @return 结果
     */
    @Override
    public int deleteSdStrategyRlByIds(Long[] ids)
    {
        return sdStrategyRlMapper.deleteSdStrategyRlByIds(ids);
    }

    /**
     * 删除策略关联设备信息信息
     * 
     * @param id 策略关联设备信息ID
     * @return 结果
     */
    @Override
    public int deleteSdStrategyRlById(Long id)
    {
        return sdStrategyRlMapper.deleteSdStrategyRlById(id);
    }

    /**
     * 批量添加关联设备信息
     * 
     * @param list 策略关联设备信息集合
     * @return 结果
     */
	@Override
	public int batchInsertStrategyRl(List<SdStrategyRl> list) 
	{
		return sdStrategyRlMapper.batchInsertStrategyRl(list);
	}
	
	
}