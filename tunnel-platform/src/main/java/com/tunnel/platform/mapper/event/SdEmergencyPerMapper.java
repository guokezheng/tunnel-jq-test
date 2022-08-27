package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdEmergencyPer;

import java.util.List;
/**
 * 应急人员信息Mapper接口
 * 
 * @author ruoyi
 * @date 2021-05-12
 */
public interface SdEmergencyPerMapper 
{
    /**
     * 查询应急人员信息
     * 
     * @param id 应急人员信息ID
     * @return 应急人员信息
     */
    public SdEmergencyPer selectSdEmergencyPerById(Long id);

    /**
     * 查询应急人员信息列表
     * 
     * @param sdEmergencyPer 应急人员信息
     * @return 应急人员信息集合
     */
    public List<SdEmergencyPer> selectSdEmergencyPerList(SdEmergencyPer sdEmergencyPer);

    /**
     * 新增应急人员信息
     * 
     * @param sdEmergencyPer 应急人员信息
     * @return 结果
     */
    public int insertSdEmergencyPer(SdEmergencyPer sdEmergencyPer);

    /**
     * 修改应急人员信息
     * 
     * @param sdEmergencyPer 应急人员信息
     * @return 结果
     */
    public int updateSdEmergencyPer(SdEmergencyPer sdEmergencyPer);

    /**
     * 删除应急人员信息
     * 
     * @param id 应急人员信息ID
     * @return 结果
     */
    public int deleteSdEmergencyPerById(Long id);

    /**
     * 批量删除应急人员信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdEmergencyPerByIds(Long[] ids);

    public List<SdEmergencyPer> selectPersonList(SdEmergencyPer sdEmergencyPer);
}
