package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.InductionlampControlStatusDetails;

import java.util.List;

/**
 * 设备控制状态详情关联Service接口
 *
 * @author ruoyi
 * @date 2022-08-30
 */
public interface IInductionlampControlStatusDetailsService {
    /**
     * 查询设备控制状态详情关联
     *
     * @param id 设备控制状态详情关联主键
     * @return 设备控制状态详情关联
     */
    InductionlampControlStatusDetails selectInductionlampControlStatusDetailsById(Long id);

    /**
     * 查询设备控制状态详情关联列表
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 设备控制状态详情关联集合
     */
    List<InductionlampControlStatusDetails> selectInductionlampControlStatusDetailsList(InductionlampControlStatusDetails inductionlampControlStatusDetails);

    /**
     * 新增设备控制状态详情关联
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 结果
     */
    int insertInductionlampControlStatusDetails(InductionlampControlStatusDetails inductionlampControlStatusDetails);

    /**
     * 修改设备控制状态详情关联
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 结果
     */
    int updateInductionlampControlStatusDetails(InductionlampControlStatusDetails inductionlampControlStatusDetails);

    /**
     * 批量删除设备控制状态详情关联
     *
     * @param ids 需要删除的设备控制状态详情关联主键集合
     * @return 结果
     */
    int deleteInductionlampControlStatusDetailsByIds(Long[] ids);

    /**
     * 删除设备控制状态详情关联信息
     *
     * @param id 设备控制状态详情关联主键
     * @return 结果
     */
    int deleteInductionlampControlStatusDetailsById(Long id);
}
