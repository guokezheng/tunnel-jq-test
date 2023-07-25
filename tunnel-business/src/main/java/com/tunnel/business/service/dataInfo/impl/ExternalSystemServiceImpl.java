package com.tunnel.business.service.dataInfo.impl;

import com.ruoyi.common.utils.DateUtils;
import com.tunnel.business.domain.dataInfo.ExternalSystem;
import com.tunnel.business.domain.dataInfo.SdTunnels;
import com.tunnel.business.mapper.dataInfo.ExternalSystemMapper;
import com.tunnel.business.service.dataInfo.IExternalSystemService;
import com.tunnel.business.service.dataInfo.ISdTunnelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 外部系统Service业务层处理
 */
@Service
public class ExternalSystemServiceImpl implements IExternalSystemService
{
    @Autowired
    private ExternalSystemMapper externalSystemMapper;

    @Autowired
    private ISdTunnelsService sdTunnelsService;

    /**
     * 查询外部系统
     *
     * @param id 外部系统主键
     * @return 外部系统
     */
    @Override
    public ExternalSystem selectExternalSystemById(Long id)
    {
        return externalSystemMapper.selectExternalSystemById(id);
    }

    /**
     * 查询外部系统列表
     *
     * @param externalSystem 外部系统
     * @return 外部系统
     */
    @Override
    public List<ExternalSystem> selectExternalSystemList(ExternalSystem externalSystem)
    {
        List<ExternalSystem> externalSystemList = externalSystemMapper.selectExternalSystemList(externalSystem);
        List<SdTunnels> list = sdTunnelsService.selectAllSdTunnelsList1();
        if(externalSystemList!=null&&externalSystemList.size()>0&&list!=null&&list.size()>0){
            for(int i=0;i<externalSystemList.size();i++){
                if(externalSystemList.get(i).getTunnelId()!=null&&!"".equals(externalSystemList.get(i).getTunnelId())){
                    externalSystemList.get(i).setTunnel(externalSystemList.get(i).getTunnelId());
                    String tunnelId = externalSystemList.get(i).getTunnelId();
                    List result = Arrays.asList(tunnelId.split(","));
                    String tunnelName = "";
                    if(result!=null&&result.size()>0){
                        for(int j=0;j<result.size();j++){
                            for(int k=0;k<list.size();k++){
                                if(result.get(j).equals(list.get(k).getTunnelId())){
                                    tunnelName += list.get(k).getTunnelName()+"\n";

                                }
                            }
                        }
                    }
                    tunnelName = tunnelName.substring(0,tunnelName.length()-1);
                    externalSystemList.get(i).setTunnelId(tunnelName);
                }

            }

        }
        return externalSystemList;

    }


    /**
     * 新增外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    @Override
    public int insertExternalSystem(ExternalSystem externalSystem)
    {
        externalSystem.setCreateTime(DateUtils.getNowDate());
        return externalSystemMapper.insertExternalSystem(externalSystem);
    }

    /**
     * 修改外部系统
     *
     * @param externalSystem 外部系统
     * @return 结果
     */
    @Override
    public int updateExternalSystem(ExternalSystem externalSystem)
    {
        externalSystem.setUpdateTime(DateUtils.getNowDate());
        return externalSystemMapper.updateExternalSystem(externalSystem);
    }

    /**
     * 批量删除外部系统
     *
     * @param ids 需要删除的外部系统主键
     * @return 结果
     */
    @Override
    public int deleteExternalSystemByIds(Long[] ids)
    {
        return externalSystemMapper.deleteExternalSystemByIds(ids);
    }

    /**
     * 删除外部系统信息
     *
     * @param id 外部系统主键
     * @return 结果
     */
    @Override
    public int deleteExternalSystemById(Long id)
    {
        return externalSystemMapper.deleteExternalSystemById(id);
    }
}
