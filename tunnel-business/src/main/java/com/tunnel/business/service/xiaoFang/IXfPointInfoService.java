package com.tunnel.business.service.xiaoFang;


import com.tunnel.business.domain.xiaoFang.XfPointInfo;

import java.util.List;

/**
 * 消防点位信息Service接口
 *
 * @author ruoyi
 * @date 2021-05-08
 */
public interface IXfPointInfoService {
    /**
     * 查询消防点位信息
     *
     * @param id 消防点位信息ID
     * @return 消防点位信息
     */
    XfPointInfo selectXfPointInfoById(Long id);

    /**
     * 查询消防点位信息列表
     *
     * @param xfPointInfo 消防点位信息
     * @return 消防点位信息集合
     */
    List<XfPointInfo> selectXfPointInfoList(XfPointInfo xfPointInfo);

}
