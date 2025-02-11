package com.tunnel.platform.service.event;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.event.SdStrategy;
import com.tunnel.business.domain.event.SdStrategyModel;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

/**
 * 控制策略Service接口
 *
 * @author gongfanfei
 * @date 2020-08-27
 */
public interface ISdStrategyService {
    /**
     * 查询控制策略
     *
     * @param id 控制策略ID
     * @return 控制策略
     */
    SdStrategy selectSdStrategyById(Long id);

    /**
     * 工作台分时控制抽屉
     * @return
     */
    List<Map> getTimeSharingInfo(String tunnelId);

    /**
     * 工作台分时控制抽屉修改控制时间
     * @param strategyId
     * @param controlTime
     * @return
     */
    int updateControlTime(Long strategyId,String controlTime);

    /**
     * 控制策略状态开关
     * @param strategyId
     * @param change
     * @return
     */
    int strategySwitch(Long strategyId,String change);

    List<Map> workTriggerInfo(String tunnelId);

    /**
     * 查询控制策略
     *
     * @param jobRelationId 控制策略ID
     * @return 控制策略
     */
    SdStrategy selectSdStrategyByJobRelationId(String jobRelationId);


    /**
     * 查询控制策略列表
     *
     * @param sdStrategy 控制策略
     * @return 控制策略集合
     */
    List<SdStrategy> selectSdStrategyList(SdStrategy sdStrategy);

    /**
     * 新增控制策略
     *
     * @param sdStrategy 控制策略
     * @return 结果
     */
    int insertSdStrategy(SdStrategy sdStrategy);

    /**
     * 修改控制策略
     *
     * @param sdStrategy 控制策略
     * @return 结果
     */
    int updateSdStrategy(SdStrategy sdStrategy);

    /**
     * 批量删除控制策略
     *
     * @param ids 需要删除的控制策略ID
     * @return 结果
     */
    int deleteSdStrategyByIds(Long[] ids);

    /**
     * 删除控制策略信息
     *
     * @param id 控制策略ID
     * @return 结果
     */
    int deleteSdStrategyById(Long id);

    /**
     * 批量删除控制策略信息
     *
     * @param ids
     * @return
     */
    int deleteSdStrategyByStrategyIds(Long[] ids);

    /**
     * 新增控制策略
     * ---对应批量新增关联信息子表
     *
     * @param SdStrategyModel 控制策略
     * @return 结果
     */
    int addStrategysInfo(SdStrategyModel model);

    /**
     * 修改控制策略
     *
     * @param SdStrategyModel 控制策略
     * @return 结果
     */
    int updateSdStrategyInfo(SdStrategyModel model);

    /**
     * 手动控制策略
     *
     * @param id 控制策略
     * @return 结果
     */
    void handleStrategy(Long id) throws UnknownHostException;

    /**
     * 手动控制策略一键处理
     *
     * @param ids 控制策略
     * @return 结果
     */
    void handleStrategyByIds(Long[] ids, Long warId);

    /**
     * 手动控制策略一键处理  返回原来的状态
     *
     * @param ids 控制策略
     * @return 结果
     */
    void handleStrategyRollBack(Long warId);

    AjaxResult implementPlan(String planId,Long eventId);

    int implementProcess(Long processId,Long eventId);

    int implementDisposalStrategy(Long strategyId,Long eventId);

    int implementDisposalStrategyRl(Long rlId,Long eventId);

    /**
     * 查询预警事件触发策略
     * @param strategy
     * @return
     */
    AjaxResult getStrategyData(SdStrategy strategy);

    /**
     * 查询所有定时策略详情信息
     *
     * @param sdStrategy 控制策略
     * @return 控制策略集合
     */
    List<SdStrategy> getSdStrategyAll(SdStrategy sdStrategy);

    /**
     * 手动策略执行
     *
     * @param model 控制策略
     * @return 结果
     */
    int manualControl(SdStrategyModel model);

}