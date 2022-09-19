package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdComponent;

import java.util.List;

/**
 * 设备档案管理Mapper接口
 * 
 * @author yanghousheng
 * @date 2020-11-18
 */
public interface SdComponentMapper 
{
    /**
     * 查询设备档案管理
     * 
     * @param id 设备档案管理ID
     * @return 设备档案管理
     */
    public SdComponent selectSdComponentById(Long id);

    /**
     * 查询设备档案管理列表
     * 
     * @param sdComponent 设备档案管理
     * @return 设备档案管理集合
     */
    public List<SdComponent> selectSdComponentList(SdComponent sdComponent);

    /**
     * 新增设备档案管理
     * 
     * @param sdComponent 设备档案管理
     * @return 结果
     */
    public int insertSdComponent(SdComponent sdComponent);

    /**
     * 修改设备档案管理
     * 
     * @param sdComponent 设备档案管理
     * @return 结果
     */
    public int updateSdComponent(SdComponent sdComponent);

    /**
     * 删除设备档案管理
     * 
     * @param id 设备档案管理ID
     * @return 结果
     */
    public int deleteSdComponentById(Long id);

    /**
     * 批量删除设备档案管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdComponentByIds(Long[] ids);
    /**
     * 通过文件id删除设备档案
     * @param fileIds
     * @return
     */
	public int deleteSdComponentByrlIds(String[] fileIds);
}
