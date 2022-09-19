package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.InductionlampControlStatusDetails;
import com.tunnel.business.mapper.dataInfo.InductionlampControlStatusDetailsMapper;
import com.tunnel.business.service.dataInfo.IInductionlampControlStatusDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备控制状态详情关联Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-30
 */
@Service
public class InductionlampControlStatusDetailsServiceImpl implements IInductionlampControlStatusDetailsService {
    @Autowired
    private InductionlampControlStatusDetailsMapper inductionlampControlStatusDetailsMapper;

    /**
     * 查询设备控制状态详情关联
     *
     * @param id 设备控制状态详情关联主键
     * @return 设备控制状态详情关联
     */
    @Override
    public InductionlampControlStatusDetails selectInductionlampControlStatusDetailsById(Long id) {
        return inductionlampControlStatusDetailsMapper.selectInductionlampControlStatusDetailsById(id);
    }

    /**
     * 查询设备控制状态详情关联列表
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 设备控制状态详情关联
     */
    @Override
    public List<InductionlampControlStatusDetails> selectInductionlampControlStatusDetailsList(InductionlampControlStatusDetails inductionlampControlStatusDetails) {
        return inductionlampControlStatusDetailsMapper.selectInductionlampControlStatusDetailsList(inductionlampControlStatusDetails);
    }

    /**
     * 新增设备控制状态详情关联
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 结果
     */
    @Override
    public int insertInductionlampControlStatusDetails(InductionlampControlStatusDetails inductionlampControlStatusDetails) {
        inductionlampControlStatusDetails.setCreateTime(DateUtils.getNowDate());
        return inductionlampControlStatusDetailsMapper.insertInductionlampControlStatusDetails(inductionlampControlStatusDetails);
    }

    /**
     * 修改设备控制状态详情关联
     *
     * @param inductionlampControlStatusDetails 设备控制状态详情关联
     * @return 结果
     */
    @Override
    public int updateInductionlampControlStatusDetails(InductionlampControlStatusDetails inductionlampControlStatusDetails) {
        inductionlampControlStatusDetails.setUpdateTime(DateUtils.getNowDate());
        return inductionlampControlStatusDetailsMapper.updateInductionlampControlStatusDetails(inductionlampControlStatusDetails);
    }

    /**
     * 批量删除设备控制状态详情关联
     *
     * @param ids 需要删除的设备控制状态详情关联主键
     * @return 结果
     */
    @Override
    public int deleteInductionlampControlStatusDetailsByIds(Long[] ids) {
        return inductionlampControlStatusDetailsMapper.deleteInductionlampControlStatusDetailsByIds(ids);
    }

    /**
     * 删除设备控制状态详情关联信息
     *
     * @param id 设备控制状态详情关联主键
     * @return 结果
     */
    @Override
    public int deleteInductionlampControlStatusDetailsById(Long id) {
        return inductionlampControlStatusDetailsMapper.deleteInductionlampControlStatusDetailsById(id);
    }
}
