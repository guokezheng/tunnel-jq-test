package com.tunnel.platform.service.dataInfo;

import java.util.List;
import com.tunnel.platform.domain.dataInfo.InductionlampControlStatusParam;

/**
 * 设备控制状态参数Service接口
 *
 * @author ruoyi
 * @date 2022-08-30
 */
public interface IInductionlampControlStatusParamService
{
    /**
     * 查询设备控制状态参数
     *
     * @param id 设备控制状态参数主键
     * @return 设备控制状态参数
     */
    public InductionlampControlStatusParam selectInductionlampControlStatusParamById(Long id);

    /**
     * 查询设备控制状态参数列表
     *
     * @param inductionlampControlStatusParam 设备控制状态参数
     * @return 设备控制状态参数集合
     */
    public List<InductionlampControlStatusParam> selectInductionlampControlStatusParamList(InductionlampControlStatusParam inductionlampControlStatusParam);

    /**
     * 新增设备控制状态参数
     *
     * @param inductionlampControlStatusParam 设备控制状态参数
     * @return 结果
     */
    public int insertInductionlampControlStatusParam(InductionlampControlStatusParam inductionlampControlStatusParam);

    /**
     * 修改设备控制状态参数
     *
     * @param inductionlampControlStatusParam 设备控制状态参数
     * @return 结果
     */
    public int updateInductionlampControlStatusParam(InductionlampControlStatusParam inductionlampControlStatusParam);

    /**
     * 批量删除设备控制状态参数
     *
     * @param ids 需要删除的设备控制状态参数主键集合
     * @return 结果
     */
    public int deleteInductionlampControlStatusParamByIds(Long[] ids);

    /**
     * 删除设备控制状态参数信息
     *
     * @param id 设备控制状态参数主键
     * @return 结果
     */
    public int deleteInductionlampControlStatusParamById(Long id);
}
