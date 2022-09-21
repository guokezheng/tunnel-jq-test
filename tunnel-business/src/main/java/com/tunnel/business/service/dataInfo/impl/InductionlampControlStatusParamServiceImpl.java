package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.InductionlampControlStatusParam;
import com.tunnel.business.mapper.dataInfo.InductionlampControlStatusParamMapper;
import com.tunnel.business.service.dataInfo.IInductionlampControlStatusParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备控制状态参数Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-30
 */
@Service
public class InductionlampControlStatusParamServiceImpl implements IInductionlampControlStatusParamService {
    @Autowired
    private InductionlampControlStatusParamMapper inductionlampControlStatusParamMapper;

    /**
     * 查询设备控制状态参数
     *
     * @param id 设备控制状态参数主键
     * @return 设备控制状态参数
     */
    @Override
    public InductionlampControlStatusParam selectInductionlampControlStatusParamById(Long id) {
        return inductionlampControlStatusParamMapper.selectInductionlampControlStatusParamById(id);
    }

    /**
     * 查询设备控制状态参数列表
     *
     * @param inductionlampControlStatusParam 设备控制状态参数
     * @return 设备控制状态参数
     */
    @Override
    public List<InductionlampControlStatusParam> selectInductionlampControlStatusParamList(InductionlampControlStatusParam inductionlampControlStatusParam) {
        return inductionlampControlStatusParamMapper.selectInductionlampControlStatusParamList(inductionlampControlStatusParam);
    }

    /**
     * 新增设备控制状态参数
     *
     * @param inductionlampControlStatusParam 设备控制状态参数
     * @return 结果
     */
    @Override
    public int insertInductionlampControlStatusParam(InductionlampControlStatusParam inductionlampControlStatusParam) {
        inductionlampControlStatusParam.setCreateTime(DateUtils.getNowDate());
        return inductionlampControlStatusParamMapper.insertInductionlampControlStatusParam(inductionlampControlStatusParam);
    }

    /**
     * 修改设备控制状态参数
     *
     * @param inductionlampControlStatusParam 设备控制状态参数
     * @return 结果
     */
    @Override
    public int updateInductionlampControlStatusParam(InductionlampControlStatusParam inductionlampControlStatusParam) {
        inductionlampControlStatusParam.setUpdateTime(DateUtils.getNowDate());
        return inductionlampControlStatusParamMapper.updateInductionlampControlStatusParam(inductionlampControlStatusParam);
    }

    /**
     * 批量删除设备控制状态参数
     *
     * @param ids 需要删除的设备控制状态参数主键
     * @return 结果
     */
    @Override
    public int deleteInductionlampControlStatusParamByIds(Long[] ids) {
        return inductionlampControlStatusParamMapper.deleteInductionlampControlStatusParamByIds(ids);
    }

    /**
     * 删除设备控制状态参数信息
     *
     * @param id 设备控制状态参数主键
     * @return 结果
     */
    @Override
    public int deleteInductionlampControlStatusParamById(Long id) {
        return inductionlampControlStatusParamMapper.deleteInductionlampControlStatusParamById(id);
    }
}
