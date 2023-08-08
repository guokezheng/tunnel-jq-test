package com.ruoyi.system.service;

import com.ruoyi.system.domain.SdAppVersion;

import java.util.List;

/**
 * app版本管理Service接口
 * 
 * @author guoz
 * @date 2023-08-08
 */
public interface ISdAppVersionService 
{
    /**
     * 查询app版本管理
     * 
     * @param id app版本管理主键
     * @return app版本管理
     */
    public SdAppVersion selectSdAppVersionById(Long id);

    /**
     * 查询app版本管理列表
     * 
     * @param sdAppVersion app版本管理
     * @return app版本管理集合
     */
    public List<SdAppVersion> selectSdAppVersionList(SdAppVersion sdAppVersion);

    /**
     * 新增app版本管理
     * 
     * @param sdAppVersion app版本管理
     * @return 结果
     */
    public int insertSdAppVersion(SdAppVersion sdAppVersion);

    /**
     * 修改app版本管理
     * 
     * @param sdAppVersion app版本管理
     * @return 结果
     */
    public int updateSdAppVersion(SdAppVersion sdAppVersion);

    /**
     * 批量删除app版本管理
     * 
     * @param ids 需要删除的app版本管理主键集合
     * @return 结果
     */
    public int deleteSdAppVersionByIds(Long[] ids);

    /**
     * 删除app版本管理信息
     * 
     * @param id app版本管理主键
     * @return 结果
     */
    public int deleteSdAppVersionById(Long id);

    List getVersionList(String editionType, String versionType, String editionNumber);
}
