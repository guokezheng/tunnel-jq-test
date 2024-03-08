package com.tunnel.business.mapper.dataInfo;

import com.tunnel.business.domain.dataInfo.ExternalSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 外部系统Mapper接口
 *
 * @author ruoyi
 * @date 2022-11-30
 */
public interface ExternalSystemMapper
{
    /**
     * 查询外部系统
     *
     * @param id 外部系统主键
     * @return 外部系统
     */
    public ExternalSystem selectExternalSystemById(Long id);


    /**
     * 查询外部系统列表
     *
     * @param externalSystem 外部系统
     * @return 外部系统集合
     */
    public List<ExternalSystem> selectExternalSystemList(ExternalSystem externalSystem);



    /**
     * 新增外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    public int insertExternalSystem(ExternalSystem externalSystem);

    /**
     * 修改外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    public int updateExternalSystem(ExternalSystem externalSystem);

    /**
     * 删除外部系统
     *
     * @param id 外部系统主键
     * @return 结果
     */
    public int deleteExternalSystemById(Long id);

    /**
     * 批量删除外部系统
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExternalSystemByIds(Long[] ids);

    /**
     *  查询需要检测网络的外部系统
     * @return 外部系统集合
     */
    List<ExternalSystem> selectExternalSystemCheckList();

    /**
     * 修改网络状态
     * @param id
     * @param networkStatus
     */
    void updateExternalSystemNetWorkStatus(@Param("id") Long id, @Param("networkStatus") String networkStatus);
}
