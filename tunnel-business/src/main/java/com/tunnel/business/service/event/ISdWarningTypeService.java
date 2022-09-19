package com.tunnel.business.service.event;


import com.tunnel.business.domain.event.SdWarningType;

import java.util.List;

/**
 * 预警类型Service接口
 *
 * @author gongfanfei
 * @date 2020-12-21
 */
public interface ISdWarningTypeService {
    /**
     * 查询预警类型
     *
     * @param id 预警类型ID
     * @return 预警类型
     */
    SdWarningType selectSdWarningTypeById(Long id);

    /**
     * 查询预警类型列表
     *
     * @param sdWarningType 预警类型
     * @return 预警类型集合
     */
    List<SdWarningType> selectSdWarningTypeList(SdWarningType sdWarningType);

    /**
     * 判断预警类型是否唯一
     *
     * @param typeName
     * @return
     */
    boolean checkSdWarningTypeNameUnique(String typeName);

    /**
     * 新增预警类型
     *
     * @param sdWarningType 预警类型
     * @return 结果
     */
    int insertSdWarningType(SdWarningType sdWarningType);

    /**
     * 修改预警类型
     *
     * @param sdWarningType 预警类型
     * @return 结果
     */
    int updateSdWarningType(SdWarningType sdWarningType);

    /**
     * 批量删除预警类型
     *
     * @param ids 需要删除的预警类型ID
     * @return 结果
     */
    int deleteSdWarningTypeByIds(Long[] ids);

    /**
     * 删除预警类型信息
     *
     * @param id 预警类型ID
     * @return 结果
     */
    int deleteSdWarningTypeById(Long id);
}
