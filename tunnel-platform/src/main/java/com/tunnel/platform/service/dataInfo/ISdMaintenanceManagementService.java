package com.tunnel.platform.service.dataInfo;

import java.io.IOException;
import java.util.List;
import com.tunnel.platform.domain.dataInfo.SdMaintenanceManagement;
import org.springframework.web.multipart.MultipartFile;

/**
 * 养护管理Service接口
 *
 * @author ruoyi
 * @date 2022-02-14
 */
public interface ISdMaintenanceManagementService
{
    /**
     * 查询养护管理
     *
     * @param id 养护管理主键
     * @return 养护管理
     */
    public SdMaintenanceManagement selectSdMaintenanceManagementById(Long id) throws IOException;

    /**
     * 查询养护管理列表
     *
     * @param sdMaintenanceManagement 养护管理
     * @return 养护管理集合
     */
    public List<SdMaintenanceManagement> selectSdMaintenanceManagementList(SdMaintenanceManagement sdMaintenanceManagement);

    /**
     * 新增养护管理
     *
     * @param sdMaintenanceManagement 养护管理
     * @return 结果
     */
    public int insertSdMaintenanceManagement(MultipartFile[] file, SdMaintenanceManagement sdMaintenanceManagement);

    /**
     * 修改养护管理
     *
     * @param sdMaintenanceManagement 养护管理
     * @return 结果
     */
    public int updateSdMaintenanceManagement(MultipartFile[] file, SdMaintenanceManagement sdMaintenanceManagement,Long[] removeIds);

    /**
     * 批量删除养护管理
     *
     * @param ids 需要删除的养护管理主键集合
     * @return 结果
     */
    public int deleteSdMaintenanceManagementByIds(Long[] ids);

    /**
     * 删除养护管理信息
     *
     * @param id 养护管理主键
     * @return 结果
     */
    public int deleteSdMaintenanceManagementById(Long id);

    public int add(SdMaintenanceManagement sdMaintenanceManagement);

    public int edit(SdMaintenanceManagement sdMaintenanceManagement);

    public String uploadPicture(MultipartFile[] file) throws IOException;
}
