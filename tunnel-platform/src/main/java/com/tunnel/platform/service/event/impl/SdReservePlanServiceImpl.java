package com.tunnel.platform.service.event.impl;

import com.ruoyi.common.utils.StringUtils;
import com.tunnel.platform.domain.dataInfo.SdDeviceTypeItem;
import com.tunnel.platform.domain.dataInfo.SdEquipmentState;
import com.tunnel.platform.domain.dataInfo.SdEquipmentType;
import com.tunnel.platform.domain.dataInfo.SdTunnels;
import com.tunnel.platform.domain.event.*;
import com.tunnel.platform.mapper.dataInfo.SdDeviceTypeItemMapper;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentStateMapper;
import com.tunnel.platform.mapper.dataInfo.SdEquipmentTypeMapper;
import com.tunnel.platform.mapper.dataInfo.SdTunnelsMapper;
import com.tunnel.platform.mapper.event.*;
import com.tunnel.platform.service.event.ISdReservePlanService;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.utils.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 预案信息Service业务层处理
 *
 * @author xuebi
 * @date 2020-09-10
 */
@Service
public class SdReservePlanServiceImpl implements ISdReservePlanService
{

	private static final Logger logger = LoggerFactory.getLogger(SdReservePlanServiceImpl.class);

    @Autowired
    private SdReservePlanMapper sdReservePlanMapper;
    @Autowired
    private SdReservePlanFileMapper sdReservePlanFileMapper;
    @Autowired
    private SdStrategyMapper sdStrategyMapper;
    @Autowired
    private SdStrategyRlMapper sdStrategyRlMapper;
    @Autowired
    private SdEquipmentStateMapper sdEquipmentStateMapper;
    @Autowired
    private SdEquipmentTypeMapper sdEquipmentTypeMapper;

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

	@Autowired
	private SdTunnelSubareaMapper sdTunnelSubareaMapper;

	@Autowired
	private SdTunnelsMapper sdTunnelsMapper;

	@Autowired
	private SdReserveProcessMapper sdReserveProcessMapper;

    /**
     * 查询预案信息
     *
     * @param id 预案信息ID
     * @return 预案信息
     */
    @Override
    public SdReservePlan selectSdReservePlanById(Long id)
    {
    	SdReservePlan plan = sdReservePlanMapper.selectSdReservePlanById(id);
    	if(plan.getPlanFileId()!=null){
    		SdReservePlanFile sdReservePlanFile = new SdReservePlanFile();
    		sdReservePlanFile.setPlanFileId(plan.getPlanFileId());
    		plan.setpFileList(sdReservePlanFileMapper.selectSdReservePlanFileList(sdReservePlanFile));
    	}
		Long subareaId = plan.getSubareaId();
		SdTunnelSubarea sdTunnelSubarea = sdTunnelSubareaMapper.selectSdTunnelSubareaBySId(subareaId);
		plan.setSdTunnelSubarea(sdTunnelSubarea);
		SdTunnels sdTunnels = sdTunnelsMapper.selectSdTunnelsById(sdTunnelSubarea.getTunnelId());
		plan.setSdTunnels(sdTunnels);
		List<String> strategyNames = new ArrayList<>();
    	if(!"-1".equals(plan.getStrategyId()) && plan.getStrategyId()!=null){
			String[] strategyAyy = plan.getStrategyId().split("；");
			String things = "";
			int index = 0;
			for (String s : strategyAyy) {
				if (s == null || s.equals("")){
					continue;
				}
				index++;
				SdStrategy sds = sdStrategyMapper.selectSdStrategyById(Long.parseLong(s));
				if (sds == null){
					logger.error("策略未找到！");
					continue;
				}
				things = things + index + "、" + sds.getStrategyName();
				strategyNames.add(things);
			}

    	}
		plan.setStrategyNames(strategyNames);
		return plan;
    }

    /**
     * 查询预案信息列表
     *
     * @param sdReservePlan 预案信息
     * @return 预案信息
     */
    @Override
    public List<SdReservePlan> selectSdReservePlanList(SdReservePlan sdReservePlan)
    {
		List<SdReservePlan> list =  sdReservePlanMapper.selectSdReservePlanList(sdReservePlan);
		for (int i = 0; i < list.size(); i++) {
			List<String> strategyNames = new ArrayList<>();
			Long subareaId = list.get(i).getSubareaId();
			SdTunnelSubarea sdTunnelSubarea = sdTunnelSubareaMapper.selectSdTunnelSubareaBySId(subareaId);
			list.get(i).setSdTunnelSubarea(sdTunnelSubarea);
			if (StringUtils.isNotEmpty(list.get(i).getStrategyId())) {
				String[] strategys = list.get(i).getStrategyId().split(",");
				int index = 0;
				for (String strategy : strategys) {
					String things = "";
					if (strategy != null && !"".equals(strategy)) {
						if (!"-1".equals(strategy)) {
							if (strategy == null || strategy.equals("")) {
								continue;
							}
							index++;
							SdStrategy sds = sdStrategyMapper.selectSdStrategyById(Long.parseLong(strategy));
							if (sds == null) {
								logger.error("策略未找到！");
								continue;
							}
							//things = things + sds.getStrategyName();
							things = things + index + "、" + sds.getStrategyName();
							strategyNames.add(things);
						}
					}
				}
			}
			list.get(i).setStrategyNames(strategyNames);
		}
        return list;
    }

    /**
     * 新增预案信息
     *
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    @Override
    public int insertSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan) {
		List<SdReservePlan> planList = sdReservePlanMapper.checkIfSingleReservePlan(sdReservePlan);
		if (planList.size() > 0) {
			throw new RuntimeException("当前预案已经存在，请勿重复添加！");
		}
		int result = -1;
    	List<SdReservePlanFile> list = new ArrayList<SdReservePlanFile>();
    	try {
    		if("#^#".equals(sdReservePlan.getPlanDescription())){
    			sdReservePlan.setPlanDescription(null);
    		}
			//创建时间
	    	sdReservePlan.setCreateTime(DateUtils.getNowDate());
			//设置当前创建人
	        sdReservePlan.setCreateBy(SecurityUtils.getUsername());
			//生成guid
			String guid = UUIDUtil.getRandom32BeginTimePK();
	        if(file.length > 0){
				//文件关联ID
				sdReservePlan.setPlanFileId(guid);
	        	for (int i = 0; i < file.length; i++) {
	        		// 从缓存中获取文件存储路径
	        		String fileServerPath = RuoYiConfig.getUploadPath();
	        		// 原图文件名
	        		String filename = file[i].getOriginalFilename();
	        		// 原图扩展名
	        		String extendName = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
	        		// 新的全名
	        		String fileName = extendName;
	        		// 加路径全名
	        		File dir = new File(fileServerPath + "/" + fileName);
	        		File filepath = new File(fileServerPath);

	        		SdReservePlanFile planFile = new SdReservePlanFile();
	        		planFile.setPlanFileId(guid);
	        		planFile.setUrl(fileServerPath + "/" + fileName);
	        		planFile.setFileName(fileName);
	        		planFile.setCreateBy(SecurityUtils.getUsername());
	        		planFile.setCreateTime(DateUtils.getNowDate());
	        		list.add(planFile);

	        		if (!filepath.exists()) {
	        			filepath.mkdirs();
	        		} else {
	        		}
	        		file[i].transferTo(dir);
	        	}
				result = sdReservePlanMapper.insertSdReservePlan(sdReservePlan);
				if(result>-1){
					result = sdReservePlanFileMapper.brachInsertSdReservePlanFile(list);
				}
	        }else{
	        	sdReservePlan.setPlanFileId(null);//文件关联ID
	        	result = sdReservePlanMapper.insertSdReservePlan(sdReservePlan);
	        }

	    } catch (IOException e) {
			e.printStackTrace();
			return 0;
	    }
        /*int result = sdReservePlanMapper.insertSdReservePlan(sdReservePlan);
        if(result>-1){
        	result = sdReservePlanFileMapper.brachInsertSdReservePlanFile(list);
        }*/
		return result;
    }

    /**
     * 修改预案信息
     *
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    @Override
    public int updateSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan, Long[] ids)
    {
		List<SdReservePlan> planList = sdReservePlanMapper.checkIfSingleReservePlan(sdReservePlan);
		if (planList.size() > 0 && ids == null) {
			throw new RuntimeException("当前预案修改内容已经存在，请勿重复添加！");
		}
    	int result = 0;
		sdReservePlan.setUpdateBy(SecurityUtils.getUsername());
        sdReservePlan.setUpdateTime(DateUtils.getNowDate());
//        return sdReservePlanMapper.updateSdReservePlan(sdReservePlan);
        List<SdReservePlanFile> list = new ArrayList<SdReservePlanFile>();
    	try {
    		if("-1".equals(sdReservePlan.getStrategyId())){
    			sdReservePlan.setStrategyId(null);
    		}
    		if("#^#".equals(sdReservePlan.getPlanDescription())){
    			sdReservePlan.setPlanDescription(null);
    		}
	    	sdReservePlan.setUpdateTime(DateUtils.getNowDate());//创建时间
	        sdReservePlan.setUpdateBy(SecurityUtils.getUsername());//设置当前创建人
	        String guid = sdReservePlan.getPlanFileId();//关联ID--guid
	        sdReservePlan.setPlanFileId(guid);//文件关联ID

	        if(file != null && file.length > 0){
	        	for (int i = 0; i < file.length; i++) {
	        		// 从缓存中获取文件存储路径
	        		String fileServerPath =  RuoYiConfig.getUploadPath();//Global.getUploadPath()
	        		// 原图文件名
	        		String filename = file[i].getOriginalFilename();
	        		// 原图扩展名
	        		String extendName = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
	        		// 新的全名
	        		String fileName = extendName;
	        		// 加路径全名
	        		File dir = new File(fileServerPath + "/" + fileName);
	        		File filepath = new File(fileServerPath);

	        		SdReservePlanFile planFile = new SdReservePlanFile();
	        		planFile.setPlanFileId(guid);
	        		planFile.setUrl(fileServerPath + "/" + fileName);
	        		planFile.setFileName(fileName);
	        		planFile.setCreateBy(SecurityUtils.getUsername());
	        		planFile.setCreateTime(DateUtils.getNowDate());
	        		list.add(planFile);

	        		if (!filepath.exists()) {
	        			filepath.mkdirs();
	        		} else {
	        		}
	        		file[i].transferTo(dir);
	        	}
	        	result = sdReservePlanFileMapper.brachInsertSdReservePlanFile(list);
	        } else {
				logger.info("当前文件信息为空或文件没有发生改动");
			}
	        if(ids.length > 0){
	    		result = sdReservePlanFileMapper.deleteSdReservePlanFileByIds(ids);//ids 为要删除的sd_reserve_plan_file id数组
	    	}
	        if(result>= 0){
	        	result = sdReservePlanMapper.updateSdReservePlan(sdReservePlan);
	        }
	    } catch (IOException e) {
			e.printStackTrace();
			return 0;
	    }
		return result;
    }

    /**
     * 批量删除预案信息
     *
     * @param rlIds 需要删除的预案信息ID
     * @return 结果
     */
    @Override
    public int deleteSdReservePlanByIds(String[] rlIds)
    {
    	int result = sdReservePlanMapper.deleteSdReservePlanByRlIds(rlIds);
    	if(result >= 0){
    		result = sdReservePlanFileMapper.deleteSdReservePlanFileByRlIds(rlIds);
    	}
    	return result;
    }

    /**
     * 删除预案信息信息
     *
     * @param id 预案信息ID
     * @return 结果
     */
    @Override
    public int deleteSdReservePlanById(Long id)
    {
		int result = -1;
		SdReservePlan sdReservePlan = sdReservePlanMapper.selectSdReservePlanById(id);
		if (sdReservePlan != null) {
			result = sdReservePlanMapper.deleteSdReservePlanById(id);
			if (result > -1) {
				result = sdReserveProcessMapper.deleteSdReserveProcessByPlanId(sdReservePlan.getId());
				result = sdReservePlanFileMapper.deleteSdReservePlanFileByPlanFileId(sdReservePlan.getPlanFileId());
			}
		}
		return result;
    }

    /**
     * 根据预案id，查询策略信息列表
     *
     * @param id 预案信息
     * @return 预案信息
     */
    @Override
    public List<SdStrategy> selectStrategyListByPlanId(Long id)
    {
    	List<SdStrategy> list = new ArrayList<SdStrategy>();
		SdReservePlan plan = sdReservePlanMapper.selectSdReservePlanById(id);
		String strategyIds = plan.getStrategyId();
		if(!"-1".equals(strategyIds)){
			String[] strategyIdAyy = strategyIds.split("；");
			for (String s : strategyIdAyy) {
				SdStrategy sds = sdStrategyMapper.selectSdStrategyById(Long.parseLong(s));
				list.add(sds);
			}
		}
    	for (int i = 0; i < list.size(); i++) {
    		List<String> sList = new ArrayList<String>();
    		SdStrategyRl rl = new SdStrategyRl();
    		String strategyType = list.get(i).getStrategyType();
    		if("1".equals(strategyType)){
    			sList.add(list.get(i).getStrategyInfo());
    		}
    		List<SdStrategyRl> rlList = sdStrategyRlMapper.selectSdStrategyRlList(rl);
    		//策略关联表信息
    		for (int j = 0; j < rlList.size(); j++) {
    			SdEquipmentType typeobject = sdEquipmentTypeMapper.selectSdEquipmentTypeById(Long.parseLong(rlList.get(j).getEqTypeId()));
    			String typeName = typeobject.getTypeName();//设备类型名称
    			SdEquipmentState stateobject = sdEquipmentStateMapper.selectSdEquipmentStateById(Long.parseLong(rlList.get(j).getState()));
    			String stateName = stateobject.getStateName();//设备状态名称
    			sList.add(typeName+"控制执行："+stateName+"；");
    		}

    		list.get(i).setSlist(sList);
		}
        return list;
    }

	/**
	 * 查询预案类型
	 * @return
	 */
	@Override
	public List<Map> selectPlanCategory() {
		return sdReservePlanMapper.selectPlanCategory();
	}

	/**
	 * 根据分区id查询预案
	 * @param sdReservePlan
	 * @return
	 */
	@Override
	public List<SdReservePlan> selectSdReservePlanBySubareaId(SdReservePlan sdReservePlan) {
		sdReservePlan.setCategory("1");
		List<SdReservePlan> sdReservePlans = sdReservePlanMapper.selectSdReservePlanBySubareaId(sdReservePlan);
		return sdReservePlans;
	}
}
