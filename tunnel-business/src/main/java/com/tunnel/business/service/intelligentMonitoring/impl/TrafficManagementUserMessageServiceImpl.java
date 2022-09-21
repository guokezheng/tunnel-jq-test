package com.tunnel.business.service.intelligentMonitoring.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementUserMessage;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementUserMessageDTO;
import com.tunnel.business.mapper.intelligentMonitoring.TrafficManagementUserMessageMapper;
import com.tunnel.business.service.intelligentMonitoring.ITrafficManagementUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2021-12-03
 */
@Service
public class TrafficManagementUserMessageServiceImpl implements ITrafficManagementUserMessageService {
    @Autowired
    private TrafficManagementUserMessageMapper trafficManagementUserMessageMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public TrafficManagementUserMessage selectTrafficManagementUserMessageById(Integer id) {
        return trafficManagementUserMessageMapper.selectTrafficManagementUserMessageById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TrafficManagementUserMessageDTO> selectTrafficManagementUserMessageList(TrafficManagementUserMessage trafficManagementUserMessage, Long userId) {
        return trafficManagementUserMessageMapper.selectTrafficManagementUserMessageList(trafficManagementUserMessage, userId);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertTrafficManagementUserMessage(TrafficManagementUserMessage trafficManagementUserMessage) {
        trafficManagementUserMessage.setCreateTime(DateUtils.getNowDate());
        return trafficManagementUserMessageMapper.insertTrafficManagementUserMessage(trafficManagementUserMessage);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateTrafficManagementUserMessage(TrafficManagementUserMessage trafficManagementUserMessage) {
        return trafficManagementUserMessageMapper.updateTrafficManagementUserMessage(trafficManagementUserMessage);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTrafficManagementUserMessageByIds(Integer[] ids) {
        return trafficManagementUserMessageMapper.deleteTrafficManagementUserMessageByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteTrafficManagementUserMessageById(Integer id) {
        return trafficManagementUserMessageMapper.deleteTrafficManagementUserMessageById(id);
    }
}
