package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdReservePlan;

import java.util.List;

/**
 * 预案信息Mapper接口
 *
 * @author gongfanfei
 * @date 2020-09-10
 */
public interface SdReservePlanMapper
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
    public int insertSdReservePlan(SdReservePlan sdReservePlan);

    /**
     * 修改预案信息
     *
     * @param sdReservePlan 预案信息
     * @return 结果
     */
    public int updateSdReservePlan(SdReservePlan sdReservePlan);

    /**
     * 删除预案信息
     *
     * @param id 预案信息ID
     * @return 结果
     */
    public int deleteSdReservePlanById(Long id);

    /**
     * 批量删除预案信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdReservePlanByIds(Long[] ids);
    /**
     * 批量删除预案信息
     *
     * @param planFileIds 需要删除的数据文件关联ID
     * @return 结果
     */
    public int deleteSdReservePlanByRlIds(String[] planFileIds);

    public List<SdReservePlan> checkIfSingleReservePlan(SdReservePlan sdReservePlan);
}
