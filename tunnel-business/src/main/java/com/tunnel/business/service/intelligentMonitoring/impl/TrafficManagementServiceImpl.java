package com.tunnel.business.service.intelligentMonitoring.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagement;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementDTO;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementUserMessage;
import com.tunnel.business.mapper.intelligentMonitoring.TrafficManagementMapper;
import com.tunnel.business.service.intelligentMonitoring.ITrafficManagementService;
import com.tunnel.business.service.intelligentMonitoring.ITrafficManagementUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 特种车辆通行管理Service业务层处理
 *
 * @author ruoyi
 * @date 2021-12-01
 */
@Service
public class TrafficManagementServiceImpl implements ITrafficManagementService {
    @Autowired
    private TrafficManagementMapper trafficManagementMapper;

    @Autowired
    private ITrafficManagementUserMessageService trafficManagementUserMessageService;

    /**
     * 查询特种车辆通行管理
     *
     * @param id 特种车辆通行管理ID
     * @return 特种车辆通行管理
     */
    @Override
    public TrafficManagement selectTrafficManagementById(Long id) {
        return trafficManagementMapper.selectTrafficManagementById(id);
    }

    /**
     * 查询特种车辆通行管理列表
     *
     * @param trafficManagementDTO 特种车辆通行管理
     * @return 特种车辆通行管理
     */
    @Override
    public List<TrafficManagement> selectTrafficManagementList(TrafficManagementDTO trafficManagementDTO) {
        return trafficManagementMapper.selectTrafficManagementList(trafficManagementDTO);
    }

    /**
     * 新增特种车辆通行管理
     *
     * @param trafficManagement 特种车辆通行管理
     * @return 结果
     */
    @Override
    public int insertTrafficManagement(TrafficManagement trafficManagement) {
        trafficManagement.setCreateTime(DateUtils.getNowDate());
        return trafficManagementMapper.insertTrafficManagement(trafficManagement);
    }

    /**
     * 修改特种车辆通行管理
     *
     * @param trafficManagement 特种车辆通行管理
     * @return 结果
     */
    @Override
    public int updateTrafficManagement(TrafficManagement trafficManagement) {
        return trafficManagementMapper.updateTrafficManagement(trafficManagement);
    }

    /**
     * 批量删除特种车辆通行管理
     *
     * @param ids 需要删除的特种车辆通行管理ID
     * @return 结果
     */
    @Override
    public int deleteTrafficManagementByIds(Long[] ids) {
        return trafficManagementMapper.deleteTrafficManagementByIds(ids);
    }

    /**
     * 删除特种车辆通行管理信息
     *
     * @param id 特种车辆通行管理ID
     * @return 结果
     */
    @Override
    public int deleteTrafficManagementById(Long id) {
        return trafficManagementMapper.deleteTrafficManagementById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTrafficManagement(Long id, Long[] userIds) {
        for (Long userId : userIds) {
            TrafficManagementUserMessage trafficManagementUserMessage = new TrafficManagementUserMessage();
            trafficManagementUserMessage.setTrafficManagementId(id);
            trafficManagementUserMessage.setUserId(userId);
            trafficManagementUserMessageService.insertTrafficManagementUserMessage(trafficManagementUserMessage);
        }
    }
}
