package com.tunnel.platform.service.dataInfo;

import java.io.IOException;
import java.util.List;

import com.tunnel.platform.domain.dataInfo.SdCrashRecovery;
import org.springframework.web.multipart.MultipartFile;

/**
 * 应急恢复Service接口
 * 
 * @author why
 * @date 2022-02-22
 */
public interface ISdCrashRecoveryService 
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
    public int insertSdCrashRecovery(MultipartFile[] file, SdCrashRecovery sdCrashRecovery) throws IOException;

    /**
     * 修改应急恢复
     * 
     * @param sdCrashRecovery 应急恢复
     * @return 结果
     */
    public int updateSdCrashRecovery(MultipartFile[] file, SdCrashRecovery sdCrashRecovery) throws IOException;

    /**
     * 批量删除应急恢复
     * 
     * @param ids 需要删除的应急恢复主键集合
     * @return 结果
     */
    public int deleteSdCrashRecoveryByIds(Long[] ids);

    /**
     * 删除应急恢复信息
     * 
     * @param id 应急恢复主键
     * @return 结果
     */
    public int deleteSdCrashRecoveryById(Long id);

    public int controlRecovery(SdCrashRecovery sdCrashRecovery);
}
