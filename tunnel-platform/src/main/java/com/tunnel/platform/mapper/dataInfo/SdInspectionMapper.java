package com.tunnel.platform.mapper.dataInfo;


import com.tunnel.platform.domain.dataInfo.SdInspection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡视记录Mapper接口
 *
 * @author liubaokui
 * @date 2021-05-12
 */
public interface SdInspectionMapper
{
    /**
     * 查询巡视记录
     *
     * @param inspectionId 巡视记录ID
     * @return 巡视记录
     */
    public SdInspection selectSdInspectionById(Long inspectionId);

    /**
     * 查询巡视记录列表
     *
     * @param sdInspection 巡视记录
     * @return 巡视记录集合
     */
    public List<SdInspection> selectSdInspectionList(SdInspection sdInspection);

    /**
     * 新增巡视记录
     *
     * @param sdInspection 巡视记录
     * @return 结果
     */
    public int insertSdInspection(SdInspection sdInspection);

    /**
     * 修改巡视记录
     *
     * @param sdInspection 巡视记录
     * @return 结果
     */
    public int updateSdInspection(SdInspection sdInspection);

    /**
     * 删除巡视记录
     *
     * @param inspectionId 巡视记录ID
     * @return 结果
     */
    public int deleteSdInspectionById(Long inspectionId);

    /**
     * 批量删除巡视记录
     *
     * @param inspectionIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdInspectionByIds(Long[] inspectionIds);
}
