package com.tunnel.platform.mapper.event;

import com.tunnel.platform.domain.event.SdReservePlanFile;

import java.util.List;

/**
 * 预案文件Mapper接口
 * 
 * @author gongfanfei
 * @date 2020-09-10
 */
public interface SdReservePlanFileMapper 
{
    /**
     * 查询预案文件
     * 
     * @param id 预案文件ID
     * @return 预案文件
     */
    public SdReservePlanFile selectSdReservePlanFileById(Long id);

    /**
     * 查询预案文件列表
     * 
     * @param sdReservePlanFile 预案文件
     * @return 预案文件集合
     */
    public List<SdReservePlanFile> selectSdReservePlanFileList(SdReservePlanFile sdReservePlanFile);

    /**
     * 新增预案文件
     * 
     * @param sdReservePlanFile 预案文件
     * @return 结果
     */
    public int insertSdReservePlanFile(SdReservePlanFile sdReservePlanFile);

    /**
     * 修改预案文件
     * 
     * @param sdReservePlanFile 预案文件
     * @return 结果
     */
    public int updateSdReservePlanFile(SdReservePlanFile sdReservePlanFile);

    /**
     * 删除预案文件
     * 
     * @param id 预案文件ID
     * @return 结果
     */
    public int deleteSdReservePlanFileById(Long id);

    /**
     * 批量删除预案文件
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdReservePlanFileByIds(Long[] ids);
    /**
     * 批量删除预案文件
     * 
     * @param planFileIds 需要删除的文件关联数据ID
     * @return 结果
     */
    public int deleteSdReservePlanFileByRlIds(String[] planFileIds);

    /**
     * 删除预案文件
     *
     * @param planFileId 需要删除的文件关联数据ID
     * @return 结果
     */
    public int deleteSdReservePlanFileByPlanFileId(String planFileId);
    
    /**
     * 批量新增预案文件
     * 
     * @param List<SdReservePlanFile> 预案文件
     * @return 结果
     */
    public int brachInsertSdReservePlanFile(List<SdReservePlanFile> list);

    
}