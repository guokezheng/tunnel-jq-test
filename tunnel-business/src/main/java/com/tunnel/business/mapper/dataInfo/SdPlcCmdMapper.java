package com.tunnel.business.mapper.dataInfo;


import com.tunnel.business.domain.dataInfo.SdPlcCmd;

import java.util.List;

/**
 * plc 报文Mapper接口
 * 
 * @author ruoyi
 * @date 2021-12-01
 */
public interface SdPlcCmdMapper 
{
    /**
     * 查询plc 报文
     * 
     * @param cmdId plc 报文ID
     * @return plc 报文
     */
    public SdPlcCmd selectSdPlcCmdById(Long cmdId);

    /**
     * 查询plc 报文列表
     * 
     * @param sdPlcCmd plc 报文
     * @return plc 报文集合
     */
    public List<SdPlcCmd> selectSdPlcCmdList(SdPlcCmd sdPlcCmd);

    /**
     * 新增plc 报文
     * 
     * @param sdPlcCmd plc 报文
     * @return 结果
     */
    public int insertSdPlcCmd(SdPlcCmd sdPlcCmd);

    /**
     * 修改plc 报文
     * 
     * @param sdPlcCmd plc 报文
     * @return 结果
     */
    public int updateSdPlcCmd(SdPlcCmd sdPlcCmd);

    /**
     * 删除plc 报文
     * 
     * @param cmdId plc 报文ID
     * @return 结果
     */
    public int deleteSdPlcCmdById(Long cmdId);

    /**
     * 批量删除plc 报文
     * 
     * @param cmdIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSdPlcCmdByIds(Long[] cmdIds);
}
