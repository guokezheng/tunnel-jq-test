package com.tunnel.platform.service.intelligentMonitoring;

import com.tunnel.platform.domain.intelligentMonitoring.TrafficManagementUserMessage;
import com.tunnel.platform.domain.intelligentMonitoring.TrafficManagementUserMessageDTO;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2021-12-03
 */
public interface ITrafficManagementUserMessageService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public TrafficManagementUserMessage selectTrafficManagementUserMessageById(Integer id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<TrafficManagementUserMessageDTO> selectTrafficManagementUserMessageList(TrafficManagementUserMessage trafficManagementUserMessage, Long userId);

    /**
     * 新增【请填写功能名称】
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 结果
     */
    public int insertTrafficManagementUserMessage(TrafficManagementUserMessage trafficManagementUserMessage);

    /**
     * 修改【请填写功能名称】
     *
     * @param trafficManagementUserMessage 【请填写功能名称】
     * @return 结果
     */
    public int updateTrafficManagementUserMessage(TrafficManagementUserMessage trafficManagementUserMessage);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTrafficManagementUserMessageByIds(Integer[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteTrafficManagementUserMessageById(Integer id);
}
