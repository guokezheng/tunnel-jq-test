package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdPushCall;

import java.util.List;

/**
 * 紧急电话推送记录Service接口
 * 
 * @author wangx
 * @date 2021-03-15
 */
public interface ISdPushCallService 
{
    /**
     * 查询紧急电话推送记录
     * 
     * @param id 紧急电话推送记录ID
     * @return 紧急电话推送记录
     */
    public SdPushCall selectSdPushCallById(Long id);

    /**
     * 查询紧急电话推送记录列表
     * 
     * @param sdPushCall 紧急电话推送记录
     * @return 紧急电话推送记录集合
     */
    public List<SdPushCall> selectSdPushCallList(SdPushCall sdPushCall);

    /**
     * 新增紧急电话推送记录
     * 
     * @param sdPushCall 紧急电话推送记录
     * @return 结果
     */
    public int insertSdPushCall(SdPushCall sdPushCall);

    /**
     * 修改紧急电话推送记录
     * 
     * @param sdPushCall 紧急电话推送记录
     * @return 结果
     */
    public int updateSdPushCall(SdPushCall sdPushCall);

    /**
     * 批量删除紧急电话推送记录
     * 
     * @param ids 需要删除的紧急电话推送记录ID
     * @return 结果
     */
    public int deleteSdPushCallByIds(Long[] ids);

    /**
     * 删除紧急电话推送记录信息
     * 
     * @param id 紧急电话推送记录ID
     * @return 结果
     */
    public int deleteSdPushCallById(Long id);
}
