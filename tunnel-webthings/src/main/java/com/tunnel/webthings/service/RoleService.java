package com.tunnel.webthings.service;

import com.tunnel.webthings.vo.DataSystemUsersVO;

/**
 * @author ZHC
 * {@code @date} 2022/8/24 9:38
 * 查询数据中台系统用户的Service接口
 */
public interface RoleService {

    /**
     * 查询符合条件的系统用户
     * @param vo 需要参数
     * @return 返回
     */
    Object getRoleList(DataSystemUsersVO vo);

}
