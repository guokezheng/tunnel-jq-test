package com.tunnel.business.service.intelligentMonitoring;


import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementUserMessage;
import com.tunnel.business.domain.intelligentMonitoring.TrafficManagementUserMessageDTO;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2021-12-03
 */
public interface ITrafficManagementUserMessageService {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    TrafficManagementUserMessage selectTrafficManagementUserMessageById(Integer id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<TrafficManagementUserMessageDTO> selectTrafficManagementUserMessageList(TrafficManagementUserMessage trafficManagementUserMessage, Long userId);

    /**
     * 新增【请填写功能名称】
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 结果
     */
    int insertTrafficManagementUserMessage(TrafficManagementUserMessage trafficManagementUserMessage);

    /**
     * 修改【请填写功能名称】
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 结果
     */
    int updateTrafficManagementUserMessage(TrafficManagementUserMessage trafficManagementUserMessage);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    int deleteTrafficManagementUserMessageByIds(Integer[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    int deleteTrafficManagementUserMessageById(Integer id);
}
