package com.tunnel.platform.service.trafficOperationControl.trafficOperationStatus.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.tunnel.platform.domain.trafficOperationControl.trafficOperationStatus.SdTrafficFlowState;
import com.tunnel.platform.mapper.trafficOperationControl.trafficOperationStatus.SdTrafficFlowStateMapper;
import com.tunnel.platform.service.trafficOperationControl.trafficOperationStatus.ISdTrafficFlowStateService;
import com.tunnel.platform.utils.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【交通流状态记录】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-02-14
 */
@Service
public class SdTrafficFlowStateServiceImpl implements ISdTrafficFlowStateService
{
    @Autowired
    private SdTrafficFlowStateMapper sdTrafficFlowStateMapper;

    /**
     * 查询【交通流状态记录】
     *
     * @param id 【交通流状态记录】主键
     * @return 【交通流状态记录】
     */
    @Override
    public SdTrafficFlowState selectSdTrafficFlowStateById(Long id)
    {
        return sdTrafficFlowStateMapper.selectSdTrafficFlowStateById(id);
    }

    /**
     * 查询【交通流状态记录】列表
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 【交通流状态记录】
     */
    @Override
    public List<SdTrafficFlowState> selectSdTrafficFlowStateList(SdTrafficFlowState sdTrafficFlowState)
    {
        Long deptId = SecurityUtils.getDeptId();
        sdTrafficFlowState.getParams().put("deptId",deptId);
        return sdTrafficFlowStateMapper.selectSdTrafficFlowStateList(sdTrafficFlowState);
    }

    /**
     * 新增【交通流状态记录】
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 结果
     */
    @Override
    public int insertSdTrafficFlowState(SdTrafficFlowState sdTrafficFlowState)
    {
        sdTrafficFlowState.setCreateTime(DateUtils.getNowDate());
        return sdTrafficFlowStateMapper.insertSdTrafficFlowState(sdTrafficFlowState);
    }

    /**
     * 修改【交通流状态记录】
     *
     * @param sdTrafficFlowState 【交通流状态记录】
     * @return 结果
     */
    @Override
    public int updateSdTrafficFlowState(SdTrafficFlowState sdTrafficFlowState)
    {
        sdTrafficFlowState.setUpdateTime(DateUtils.getNowDate());
        return sdTrafficFlowStateMapper.updateSdTrafficFlowState(sdTrafficFlowState);
    }

    /**
     * 批量删除【交通流状态记录】
     *
     * @param ids 需要删除的【交通流状态记录】主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficFlowStateByIds(Long[] ids)
    {
        return sdTrafficFlowStateMapper.deleteSdTrafficFlowStateByIds(ids);
    }

    /**
     * 删除【交通流状态记录】信息
     *
     * @param id 【交通流状态记录】主键
     * @return 结果
     */
    @Override
    public int deleteSdTrafficFlowStateById(Long id)
    {
        return sdTrafficFlowStateMapper.deleteSdTrafficFlowStateById(id);
    }

    /**
     * 展示各隧道最近24小时内各个种类车型的车流量和平均速度的趋势图
     *
     * @param tunnelId 隧道id
     * @return
     */
    @Override
    public JSONObject getChartData(String tunnelId) {
        Long deptId = SecurityUtils.getDeptId();
        List<SdTrafficFlowState> list = sdTrafficFlowStateMapper.getChartData(tunnelId, deptId);
        List<String> timeList = new ArrayList<>();
        List<String> averageVelocityList = new ArrayList<>();
        List<String> carList = new ArrayList<>();
        List<String> trunkList = new ArrayList<>();
        List<String> specialCarList = new ArrayList<>();

        String strDateFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        if(list != null && list.size() > 0){
            list.forEach(flowState -> {
                String time = sdf.format(flowState.getStatisticTime());
                timeList.add(time);
                averageVelocityList.add(flowState.getAverageVelocity());
                carList.add(flowState.getCarNum());
                trunkList.add(flowState.getTrunkNum());
                specialCarList.add(flowState.getSpecialCarNum());
            });
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timeList",timeList);
        jsonObject.put("averageVelocityList",averageVelocityList);
        jsonObject.put("carList",carList);
        jsonObject.put("trunkList",trunkList);
        jsonObject.put("specialCarList",specialCarList);
        return jsonObject;
    }
}
