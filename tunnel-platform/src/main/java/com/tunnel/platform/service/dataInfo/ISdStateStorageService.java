package com.tunnel.platform.service.dataInfo;

import com.tunnel.platform.domain.dataInfo.SdStateStorage;

import java.util.List;

/**
 * 隧道数据存储表Service接口
 * 
 * @author 刘方堃
 * @date 2022-01-07
 */
public interface ISdStateStorageService 
{
    /**
     * 查询隧道数据存储表
     * 
     * @param id 隧道数据存储表主键
     * @return 隧道数据存储表
     */
    public SdStateStorage selectSdStateStorageById(Long id);
    
    public SdStateStorage selectSdStateStorage(String deviceId);

    /**
     * 查询隧道数据存储表列表
     * 
     * @param sdStateStorage 隧道数据存储表
     * @return 隧道数据存储表集合
     */
    public List<SdStateStorage> selectSdStateStorageList(SdStateStorage sdStateStorage);

    /**
     * 新增隧道数据存储表
     * 
     * @param sdStateStorage 隧道数据存储表
     * @return 结果
     */
    public int insertSdStateStorage(SdStateStorage sdStateStorage);

    /**
     * 修改隧道数据存储表
     * 
     * @param sdStateStorage 隧道数据存储表
     * @return 结果
     */
    public int updateSdStateStorage(SdStateStorage sdStateStorage);

    /**
     * 批量删除隧道数据存储表
     * 
     * @param ids 需要删除的隧道数据存储表主键集合
     * @return 结果
     */
    public int deleteSdStateStorageByIds(Long[] ids);

    /**
     * 删除隧道数据存储表信息
     * 
     * @param id 隧道数据存储表主键
     * @return 结果
     */
    public int deleteSdStateStorageById(Long id);
}
