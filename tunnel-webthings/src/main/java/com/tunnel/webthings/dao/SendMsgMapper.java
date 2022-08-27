package com.tunnel.webthings.dao;


import com.tunnel.platform.domain.dataInfo.SdDevices;
import com.tunnel.webthings.domain.Jqdict;

import java.util.List;

/**
 * 字典表Mapper
 * @author dear_dzy
 */

public interface SendMsgMapper {

    /**
     * 查找字典表
     * @return
     */
    List<Jqdict> selectDict();

}
