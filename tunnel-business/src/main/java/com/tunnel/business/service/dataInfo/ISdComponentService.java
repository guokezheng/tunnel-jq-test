package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdComponent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 设备档案管理Service接口
 *
 * @author yanghousheng
 * @date 2020-11-18
 */
public interface ISdComponentService {
    /**
     * 查询设备档案管理
     *
     * @param id 设备档案管理ID
     * @return 设备档案管理
     */
    SdComponent selectSdComponentById(Long id);

    /**
     * 查询设备档案管理列表
     *
     * @param sdComponent 设备档案管理
     * @return 设备档案管理集合
     */
    List<SdComponent> selectSdComponentList(SdComponent sdComponent);

    /**
     * 新增设备档案管理
     *
     * @param sdComponent 设备档案管理
     * @return 结果
     */
    int insertSdComponent(MultipartFile[] file, SdComponent sdComponent);

    /**
     * 修改设备档案管理
     *
     * @param sdComponent 设备档案管理
     * @return 结果
     */
    int updateSdComponent(MultipartFile[] file, SdComponent sdComponent, Long[] ids);

    /**
     * 批量删除设备档案管理
     *
     * @param ids 需要删除的设备档案管理ID
     * @return 结果
     */
    int deleteSdComponentByIds(String[] rlIds);

    /**
     * 删除设备档案管理信息
     *
     * @param id 设备档案管理ID
     * @return 结果
     */
    int deleteSdComponentById(Long[] id);
}
