package com.tunnel.business.service.event;


import com.tunnel.business.domain.event.SdReserveProcess;
import com.tunnel.business.domain.event.SdReserveProcessModel;

import java.util.List;
import java.util.Map;

/**
 * 预案流程节点Service接口
 *
 * @author ruoyi
 * @date 2022-09-02
 */
public interface ISdReserveProcessService {
    /**
     * 查询预案流程节点
     *
     * @param id 预案流程节点主键
     * @return 预案流程节点
     */
    SdReserveProcess selectSdReserveProcessById(Long id);

    /**
     * 查询预案流程节点列表
     *
     * @param sdReserveProcess 预案流程节点
     * @return 预案流程节点集合
     */
    List<SdReserveProcess> selectSdReserveProcessList(SdReserveProcess sdReserveProcess);

    /**
     * 新增预案流程节点
     *
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    int insertSdReserveProcess(SdReserveProcess sdReserveProcess);

    /**
     * 批量添加预案流程节点
     *
     * @param sdReserveProcesses
     * @return
     */
    int batchSdReserveProcessed(SdReserveProcessModel sdReserveProcesses);

    /**
     * 修改预案流程节点
     *
     * @param sdReserveProcess 预案流程节点
     * @return 结果
     */
    int updateSdReserveProcess(SdReserveProcess sdReserveProcess);

    /**
     * 批量删除预案流程节点
     *
     * @param ids 需要删除的预案流程节点主键集合
     * @return 结果
     */
    int deleteSdReserveProcessByIds(Long[] ids);

    /**
     * 删除预案流程节点信息
     *
     * @param id 预案流程节点主键
     * @return 结果
     */
    int deleteSdReserveProcessById(Long id);

    /**
     * 根据预案id删除预案流程节点信息
     *
     * @param id
     * @return
     */
    int deleteSdReserveProcessByPlanId(Long id);

    /**
     * 根据预案id查找预案流程节点信息
     *
     * @param RId
     * @return
     */
    List<SdReserveProcess> selectSdReserveProcessByRId(Long RId);

    /**
     * 查询预览展示
     *
     * @param reserveId
     * @return
     */
    List<Map> selectPreviewDisplay(Long reserveId);

    /**
     * 预案执行
     *
     * @param reserveId
     * @return
     */
    Integer planImplementa(String eventId, Long reserveId);
}
