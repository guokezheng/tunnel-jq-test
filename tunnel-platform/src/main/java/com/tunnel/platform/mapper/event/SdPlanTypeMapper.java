package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdPlanType;

import java.util.List;

/**
 * 预案类型Mapper接口
 * 
 * @author gongfanfei
 * @date 2020-08-25
 */
public interface SdPlanTypeMapper 
{
    /**
     * 查询预案类型
     * 
     * @param id 预案类型ID
     * @return 预案类型
     */
    public SdPlanType selectSdPlanTypeById(Long id);

    /**
     * 查询预案类型列表
     * 
     * @param sdPlanType 预案类型
     * @return 预案类型集合
     */
    public List<SdPlanType> selectSdPlanTypeList(SdPlanType sdPlanType);

    /**
     * 新增预案类型
     * 
     * @param sdPlanType 预案类型
     * @return 结果
     */
    public int insertSdPlanType(SdPlanType sdPlanType);

    /**
     * 修改预案类型
     * 
     * @param sdPlanType 预案类型
     * @return 结果
     */
    public int updateSdPlanType(SdPlanType sdPlanType);

    /**
     * 删除预案类型
     * 
     * @param id 预案类型ID
     * @return 结果
     */
    public int deleteSdPlanTypeById(Long id);

    /**
     * 批量删除预案类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdPlanTypeByIds(Long[] ids);
}