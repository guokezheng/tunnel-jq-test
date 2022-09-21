package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.SdCrashRecovery;

import java.util.List;


/**
 * 应急恢复Mapper接口
 * 
 * @author why
 * @date 2022-02-22
 */
public interface SdCrashRecoveryMapper 
{
    /**
     * 查询应急恢复
     * 
     * @param id 应急恢复主键
     * @return 应急恢复
     */
    public SdCrashRecovery selectSdCrashRecoveryById(Long id);

    /**
     * 查询应急恢复列表
     * 
     * @param sdCrashRecovery 应急恢复
     * @return 应急恢复集合
     */
    public List<SdCrashRecovery> selectSdCrashRecoveryList(SdCrashRecovery sdCrashRecovery);

    /**
     * 新增应急恢复
     * 
     * @param sdCrashRecovery 应急恢复
     * @return 结果
     */
    public int insertSdCrashRecovery(SdCrashRecovery sdCrashRecovery);

    /**
     * 修改应急恢复
     * 
     * @param sdCrashRecovery 应急恢复
     * @return 结果
     */
    public int updateSdCrashRecovery(SdCrashRecovery sdCrashRecovery);

    /**
     * 删除应急恢复
     * 
     * @param id 应急恢复主键
     * @return 结果
     */
    public int deleteSdCrashRecoveryById(Long id);

    /**
     * 批量删除应急恢复
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdCrashRecoveryByIds(Long[] ids);

    public int controlRecovery(SdCrashRecovery sdCrashRecovery);
}
