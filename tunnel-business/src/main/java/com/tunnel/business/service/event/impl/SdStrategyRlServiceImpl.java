package com.tunnel.business.service.event.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.tunnel.business.datacenter.domain.enumeration.DevicesTypeEnum;
import com.tunnel.business.domain.dataInfo.SdEquipmentState;
import com.tunnel.business.domain.dataInfo.SdEquipmentStateIconFile;
import com.tunnel.business.domain.event.SdJoinPlanStrategy;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyRl;
import com.tunnel.business.domain.informationBoard.IotBoardTemplateContent;
import com.tunnel.business.mapper.dataInfo.SdEquipmentIconFileMapper;
import com.tunnel.business.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.business.mapper.event.SdJoinPlanStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyMapper;
import com.tunnel.business.mapper.event.SdStrategyRlMapper;
import com.tunnel.business.mapper.informationBoard.IotBoardTemplateContentMapper;
import com.tunnel.business.service.event.ISdStrategyRlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 策略关联设备信息Service业务层处理
 *
 * @author gongfanfei
 * @date 2020-08-31
 */
@Service
public class SdStrategyRlServiceImpl implements ISdStrategyRlService {
    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;
    @Autowired
    private SdEquipmentStateMapper sdEquipmentStateMapper;

    @Autowired
    private SdEquipmentIconFileMapper sdEquipmentIconFileMapper;

    @Autowired
    private SdJoinPlanStrategyMapper planStrategyMapper;

    @Autowired
    private IotBoardTemplateContentMapper contentMapper;

    /**
     * 查询策略关联设备信息
     *
     * @param id 策略关联设备信息ID
     * @return 策略关联设备信息
     */
    @Override
    public SdStrategyRl selectSdStrategyRlById(Long id) {
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
    public List<SdStrategyRl> selectSdStrategyRlList(SdStrategyRl sdStrategyRl) {
        List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlList(sdStrategyRl);
       /* SdStrategy strategy = SpringUtils.getBean(SdStrategyMapper.class).selectSdStrategyById(sdStrategyRl.getStrategyId());
        if(strategy.getStrategyType().equals("3")){
            rlList = rlList.stream().filter(s-> StrUtil.isNotEmpty(s.getEndState())).collect(Collectors.toList());
        }*/
        for (int i = 0; i < rlList.size(); i++) {
            SdEquipmentState sdEquipmentState = new SdEquipmentState();
            sdEquipmentState.setStateTypeId(Long.parseLong(rlList.get(i).getEqTypeId()));
            sdEquipmentState.setIsControl(1);
            List<SdEquipmentState> stateList = sdEquipmentStateMapper.selectDropSdEquipmentStateList(sdEquipmentState);
            rlList.get(i).setEqStateList(stateList);

            // 获取情报板历史数据
            if(DevicesTypeEnum.VMS.getCode() == Long.parseLong(rlList.get(i).getEqTypeId()) || DevicesTypeEnum.MEN_JIA_VMS.getCode() == Long.parseLong(rlList.get(i).getEqTypeId())){
                SdJoinPlanStrategy planStrategy = new SdJoinPlanStrategy();
                planStrategy.setCurrentId(rlList.get(i).getId());
                planStrategy.setTemplateId(rlList.get(i).getState());
                planStrategy.setType("1");
                List<SdJoinPlanStrategy> list = planStrategyMapper.selectSdJoinPlanStrategyList(planStrategy);
                IotBoardTemplateContent content = new IotBoardTemplateContent();
                content.setTemplateId(rlList.get(i).getState());
                List<IotBoardTemplateContent> contentList = contentMapper.selectSdVmsTemplateContentList(content);
                rlList.get(i).setContent(list.size() == 0 ? contentList.size() == 0 ? "" : contentList.get(0).getContent() : list.get(0).getContent());
            }

        }
        return rlList;
    }

    /**
     * 根据策略id查询策略关联设备信息
     *
     * @param strategyId
     * @return
     */
    @Override
    public List<SdStrategyRl> selectSdStrategyRlListByStrategyId(Long strategyId) {
        List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlByStrategyId(strategyId);
        for (int i = 0; i < rlList.size(); i++) {
            SdEquipmentState sdEquipmentState = new SdEquipmentState();
            sdEquipmentState.setStateTypeId(Long.parseLong(rlList.get(i).getEqTypeId()));
            sdEquipmentState.setIsControl(1);
            List<SdEquipmentState> stateList = sdEquipmentStateMapper.selectDropSdEquipmentStateList(sdEquipmentState);
            for (SdEquipmentState state : stateList) {
                if (state.getIconFileId() != null && !"".equals(state.getIconFileId()) && !"null".equals(state.getIconFileId())) {
                    if (!"-1".equals(state.getIconFileId())) {
                        SdEquipmentStateIconFile sdEquipmentStateIconFile = new SdEquipmentStateIconFile();
                        sdEquipmentStateIconFile.setStateIconId(state.getIconFileId());
                        state.setiFileList(sdEquipmentIconFileMapper.selectStateIconFileList(sdEquipmentStateIconFile));
                    }
                }
            }
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
    public int insertSdStrategyRl(SdStrategyRl sdStrategyRl) {
        return sdStrategyRlMapper.insertSdStrategyRl(sdStrategyRl);
    }

    /**
     * 修改策略关联设备信息
     *
     * @param sdStrategyRl 策略关联设备信息
     * @return 结果
     */
    @Override
    public int updateSdStrategyRl(SdStrategyRl sdStrategyRl) {
        return sdStrategyRlMapper.updateSdStrategyRl(sdStrategyRl);
    }

    /**
     * 批量删除策略关联设备信息
     *
     * @param ids 需要删除的策略关联设备信息ID
     * @return 结果
     */
    @Override
    public int deleteSdStrategyRlByIds(Long[] ids) {
        return sdStrategyRlMapper.deleteSdStrategyRlByIds(ids);
    }

    /**
     * 删除策略关联设备信息信息
     *
     * @param id 策略关联设备信息ID
     * @return 结果
     */
    @Override
    public int deleteSdStrategyRlById(Long id) {
        return sdStrategyRlMapper.deleteSdStrategyRlById(id);
    }

    /**
     * 批量添加关联设备信息
     *
     * @param list 策略关联设备信息集合
     * @return 结果
     */
    @Override
    public int batchInsertStrategyRl(List<SdStrategyRl> list) {
        return sdStrategyRlMapper.batchInsertStrategyRl(list);
    }


}