package com.tunnel.business.service.event;

import com.ruoyi.common.core.domain.AjaxResult;
import com.tunnel.business.domain.event.SdReservePlan;
import com.tunnel.business.domain.event.SdStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 预案信息Service接口
 *
 * @author xuebi
 * @date 2020-09-10
 */
public interface ISdReservePlanService {
    /**
     * 查询预案信息
     *
     * @param id 预案信息ID
     * @return 预案信息
     */
    SdReservePlan selectSdReservePlanById(Long id);

    /**
     * 查询预案信息列表
     *
     * @param sdReservePlan 预案信息
     * @return 预案信息集合
     */
    List<SdReservePlan> selectSdReservePlanList(SdReservePlan sdReservePlan);

    /**
     * 新增预案信息
     *
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    int insertSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan);

    /**
     * 修改预案信息
     *
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    int updateSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan, Long[] ids);

    /**
     * 批量删除预案信息
     *
     * @param ids 需要删除的预案信息ID
     * @return 结果
     */
    int deleteSdReservePlanByIds(String[] rlIds);

    /**
     * 删除预案信息信息
     *
     * @param id 预案信息ID
     * @return 结果
     */
    int deleteSdReservePlanById(Long id);

    /**
     * 查询预案信息列表
     *
     * @param sdReservePlan 预案信息
     * @return 预案信息集合
     */
    List<SdStrategy> selectStrategyListByPlanId(Long id);

    /**
     * 查询预案类型
     *
     * @return
     */
    List<Map> selectPlanCategory();

    /**
     * 根据分区id查询预案
     *
     * @param sdReservePlan
     * @return
     */
    List<SdReservePlan> selectSdReservePlanBySubareaId(SdReservePlan sdReservePlan);

    /**
     * 根据隧道查询预案
     *
     * @param tunnelId
     * @return
     */
    List<Map> selectSdReservePlanByTunnelId(String tunnelId);

    /**
     * 查询事件相关预案
     *
     * @param sdReservePlan
     * @return
     */
    AjaxResult getReservePlanData(SdReservePlan sdReservePlan);
}