package com.tunnel.business.service.event;


import com.tunnel.business.domain.event.SdPlanType;

import java.util.List;

/**
 * 预案类型Service接口
 *
 * @author gongfanfei
 * @date 2020-08-25
 */
public interface ISdPlanTypeService {
    /**
     * 查询预案类型
     *
     * @param id 预案类型ID
     * @return 预案类型
     */
    SdPlanType selectSdPlanTypeById(Long id);

    /**
     * 查询预案类型列表
     *
     * @param sdPlanType 预案类型
     * @return 预案类型集合
     */
    List<SdPlanType> selectSdPlanTypeList(SdPlanType sdPlanType);

    /**
     * 新增预案类型
     *
     * @param sdPlanType 预案类型
     * @return 结果
     */
    int insertSdPlanType(SdPlanType sdPlanType);

    /**
     * 修改预案类型
     *
     * @param sdPlanType 预案类型
     * @return 结果
     */
    int updateSdPlanType(SdPlanType sdPlanType);

    /**
     * 批量删除预案类型
     *
     * @param ids 需要删除的预案类型ID
     * @return 结果
     */
    int deleteSdPlanTypeByIds(Long[] ids);

    /**
     * 删除预案类型信息
     *
     * @param id 预案类型ID
     * @return 结果
     */
    int deleteSdPlanTypeById(Long id);
}