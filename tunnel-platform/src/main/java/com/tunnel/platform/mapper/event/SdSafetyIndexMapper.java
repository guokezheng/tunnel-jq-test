package com.tunnel.platform.mapper.event;

import java.util.List;
import com.tunnel.platform.domain.event.SdSafetyIndex;

/**
 * 安全指数Mapper接口
 * 
 * @author ruoyi
 * @date 2021-12-27
 */
public interface SdSafetyIndexMapper 
{
    /**
     * 查询安全指数
     * 
     * @param id 安全指数主键
     * @return 安全指数
     */
    public SdSafetyIndex selectSdSafetyIndexById(Long id);

    /**
     * 查询安全指数列表
     * 
     * @param sdSafetyIndex 安全指数
     * @return 安全指数集合
     */
    public List<SdSafetyIndex> selectSdSafetyIndexList(SdSafetyIndex sdSafetyIndex);

    /**
     * 新增安全指数
     * 
     * @param sdSafetyIndex 安全指数
     * @return 结果
     */
    public int insertSdSafetyIndex(SdSafetyIndex sdSafetyIndex);

    /**
     * 修改安全指数
     * 
     * @param sdSafetyIndex 安全指数
     * @return 结果
     */
    public int updateSdSafetyIndex(SdSafetyIndex sdSafetyIndex);

    /**
     * 删除安全指数
     * 
     * @param id 安全指数主键
     * @return 结果
     */
    public int deleteSdSafetyIndexById(Long id);

    /**
     * 批量删除安全指数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSdSafetyIndexByIds(Long[] ids);
}
