package com.tunnel.platform.service.event;

import com.tunnel.platform.domain.event.SdReservePlan;
import com.tunnel.platform.domain.event.SdStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 预案信息Service接口
 * 
 * @author xuebi
 * @date 2020-09-10
 */
public interface ISdReservePlanService 
{
    /**
     * 查询预案信息
     * 
     * @param id 预案信息ID
     * @return 预案信息
     */
    public SdReservePlan selectSdReservePlanById(Long id);

    /**
     * 查询预案信息列表
     * 
     * @param sdReservePlan 预案信息
     * @return 预案信息集合
     */
    public List<SdReservePlan> selectSdReservePlanList(SdReservePlan sdReservePlan);

    /**
     * 新增预案信息
     * 
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    public int insertSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan);

    /**
     * 修改预案信息
     * 
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    public int updateSdReservePlan(MultipartFile[] file, SdReservePlan sdReservePlan, Long[] ids);

    /**
     * 批量删除预案信息
     * 
     * @param ids 需要删除的预案信息ID
     * @return 结果
     */
    public int deleteSdReservePlanByIds(String[] rlIds);

    /**
     * 删除预案信息信息
     * 
     * @param id 预案信息ID
     * @return 结果
     */
    public int deleteSdReservePlanById(Long id);
    /**
     * 查询预案信息列表
     * 
     * @param sdReservePlan 预案信息
     * @return 预案信息集合
     */
    public List<SdStrategy> selectStrategyListByPlanId(Long id);

    /**
     * 查询预案类型
     * @return
     */
    public List<Map> selectPlanCategory();
}