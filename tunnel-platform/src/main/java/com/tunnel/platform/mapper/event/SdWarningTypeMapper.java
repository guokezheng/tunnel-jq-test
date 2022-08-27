package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdWarningType;

import java.util.List;

/**
 * 预警类型Mapper接口
 *
 * @author gongfanfei
 * @date 2020-12-21
 */
public interface SdWarningTypeMapper
{
    /**
     * 查询预警类型
     *
     * @param id 预警类型ID
     * @return 预警类型
     */
    public SdWarningType selectSdWarningTypeById(Long id);

    /**
     * 查询预警类型列表
     *
     * @param sdWarningType 预警类型
     * @return 预警类型集合
     */
    public List<SdWarningType> selectSdWarningTypeList(SdWarningType sdWarningType);

    /**
     * 按预警类型名称统计数量
     *
     * @param typeName
     * @return
     */
    int countSdWarningTypeByTypeName(String typeName);

    /**
     * 新增预警类型
     *
     * @param sdWarningType 预警类型
     * @return 结果
     */
    public int insertSdWarningType(SdWarningType sdWarningType);

    /**
     * 修改预警类型
     *
     * @param sdWarningType 预警类型
     * @return 结果
     */
    public int updateSdWarningType(SdWarningType sdWarningType);

    /**
     * 删除预警类型
     *
     * @param id 预警类型ID
     * @return 结果
     */
    public int deleteSdWarningTypeById(Long id);

    /**
     * 批量删除预警类型
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdWarningTypeByIds(Long[] ids);

}
