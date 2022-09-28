package com.tunnel.business.service.dataInfo;


import com.tunnel.business.domain.dataInfo.SdPlcCmd;

import java.util.List;

/**
 * plc 报文Service接口
 *
 * @author ruoyi
 * @date 2021-12-01
 */
public interface ISdPlcCmdService {
    /**
     * 查询plc 报文
     *
     * @param cmdId plc 报文ID
     * @return plc 报文
     */
    SdPlcCmd selectSdPlcCmdById(Long cmdId);

    /**
     * 查询plc 报文列表
     *
     * @param sdPlcCmd plc 报文
     * @return plc 报文集合
     */
    List<SdPlcCmd> selectSdPlcCmdList(SdPlcCmd sdPlcCmd);

    /**
     * 新增plc 报文
     *
     * @param sdPlcCmd plc 报文
     * @return 结果
     */
    int insertSdPlcCmd(SdPlcCmd sdPlcCmd);

    /**
     * 修改plc 报文
     *
     * @param sdPlcCmd plc 报文
     * @return 结果
     */
    int updateSdPlcCmd(SdPlcCmd sdPlcCmd);

    /**
     * 批量删除plc 报文
     *
     * @param cmdIds 需要删除的plc 报文ID
     * @return 结果
     */
    int deleteSdPlcCmdByIds(Long[] cmdIds);

    /**
     * 删除plc 报文信息
     *
     * @param cmdId plc 报文ID
     * @return 结果
     */
    int deleteSdPlcCmdById(Long cmdId);
}
