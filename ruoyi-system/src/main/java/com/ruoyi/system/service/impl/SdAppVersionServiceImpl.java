package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SdAppVersion;
import com.ruoyi.system.mapper.SdAppVersionMapper;
import com.ruoyi.system.service.ISdAppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * app版本管理Service业务层处理
 * 
 * @author guoz
 * @date 2023-08-08
 */
@Service
public class SdAppVersionServiceImpl implements ISdAppVersionService
{
    @Autowired
    private SdAppVersionMapper sdAppVersionMapper;

    /**
     * 查询app版本管理
     * 
     * @param id app版本管理主键
     * @return app版本管理
     */
    @Override
    public SdAppVersion selectSdAppVersionById(Long id)
    {
        return sdAppVersionMapper.selectSdAppVersionById(id);
    }

    /**
     * 查询app版本管理列表
     * 
     * @param sdAppVersion app版本管理
     * @return app版本管理
     */
    @Override
    public List<SdAppVersion> selectSdAppVersionList(SdAppVersion sdAppVersion)
    {
        return sdAppVersionMapper.selectSdAppVersionList(sdAppVersion);
    }

    /**
     * 新增app版本管理
     * 
     * @param sdAppVersion app版本管理
     * @return 结果
     */
    @Override
    public int insertSdAppVersion(SdAppVersion sdAppVersion)
    {

        if(sdAppVersion.getEditionIssue() == 1){
            sdAppVersionMapper.updateEditionIssueAllFalse(sdAppVersion);
        }

        return sdAppVersionMapper.insertSdAppVersion(sdAppVersion);
    }

    /**
     * 修改app版本管理
     * 
     * @param sdAppVersion app版本管理
     * @return 结果
     */
    @Override
    public int updateSdAppVersion(SdAppVersion sdAppVersion)
    {
        if(sdAppVersion.getEditionIssue() == 1){
            sdAppVersionMapper.updateEditionIssueAllFalse(sdAppVersion);
        }
        return sdAppVersionMapper.updateSdAppVersion(sdAppVersion);
    }

    /**
     * 批量删除app版本管理
     * 
     * @param ids 需要删除的app版本管理主键
     * @return 结果
     */
    @Override
    public int deleteSdAppVersionByIds(Long[] ids)
    {
        return sdAppVersionMapper.deleteSdAppVersionByIds(ids);
    }

    /**
     * 删除app版本管理信息
     * 
     * @param id app版本管理主键
     * @return 结果
     */
    @Override
    public int deleteSdAppVersionById(Long id)
    {
        return sdAppVersionMapper.deleteSdAppVersionById(id);
    }


    @Override
    public List getVersionList(String editionType, String versionType, String editionNumber) {
        return  sdAppVersionMapper.getVersionList(editionType,versionType,editionNumber);
    }
}
