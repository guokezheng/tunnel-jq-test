package com.tunnel.platform.service.xiaoFang.impl;

import com.tunnel.platform.domain.xiaoFang.XfPointInfo;
import com.tunnel.platform.mapper.xiaoFang.XfPointInfoMapper;
import com.tunnel.platform.service.xiaoFang.IXfPointInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消防点位信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2021-05-08
 */
@Service
public class XfPointInfoServiceImpl implements IXfPointInfoService
{
    @Autowired
    private XfPointInfoMapper xfPointInfoMapper;

    /**
     * 查询消防点位信息
     * 
     * @param id 消防点位信息ID
     * @return 消防点位信息
     */
    @Override
    public XfPointInfo selectXfPointInfoById(Long id)
    {
        return xfPointInfoMapper.selectXfPointInfoById(id);
    }

    /**
     * 查询消防点位信息列表
     * 
     * @param xfPointInfo 消防点位信息
     * @return 消防点位信息
     */
    @Override
    public List<XfPointInfo> selectXfPointInfoList(XfPointInfo xfPointInfo)
    {
        return xfPointInfoMapper.selectXfPointInfoList(xfPointInfo);
    }
}
