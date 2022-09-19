package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.InductionlampControlStatusDetails;

import java.util.List;

/**
 * 设备控制状态详情关联Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-30
 */
public interface InductionlampControlStatusDetailsMapper
{
    /**
     * 查询设备控制状态详情关联
     *
     * @param id 设备控制状态详情关联主键
     * @return 设备控制状态详情关联
     */
    public InductionlampControlStatusDetails selectInductionlampControlStatusDetailsById(Long id);

    /**
     * 查询设备控制状态详情关联列表
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 设备控制状态详情关联集合
     */
    public List<InductionlampControlStatusDetails> selectInductionlampControlStatusDetailsList(InductionlampControlStatusDetails inductionlampControlStatusDetails);

    /**
     * 新增设备控制状态详情关联
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 结果
     */
    public int insertInductionlampControlStatusDetails(InductionlampControlStatusDetails inductionlampControlStatusDetails);

    /**
     * 修改设备控制状态详情关联
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 结果
     */
    public int updateInductionlampControlStatusDetails(InductionlampControlStatusDetails inductionlampControlStatusDetails);

    /**
     * 删除设备控制状态详情关联
     *
     * @param id 设备控制状态详情关联主键
     * @return 结果
     */
    public int deleteInductionlampControlStatusDetailsById(Long id);

    /**
     * 批量删除设备控制状态详情关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteInductionlampControlStatusDetailsByIds(Long[] ids);
}
