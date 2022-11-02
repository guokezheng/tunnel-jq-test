package com.tunnel.business.service.dataInfo;

import com.tunnel.business.domain.dataInfo.SdEnvironmentConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 隧道环境配置Service接口
 *
 * @author 刘方堃
 * @date 2021-12-13
 */
public interface ISdEnvironmentConfigurationService {
    /**
     * 查询隧道环境配置
     *
     * @param id 隧道环境配置主键
     * @return 隧道环境配置
     */
    SdEnvironmentConfiguration selectSdEnvironmentConfigurationById(Long id);

    /**
     * 查询隧道环境配置列表
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 隧道环境配置集合
     */
    List<SdEnvironmentConfiguration> selectSdEnvironmentConfigurationList(SdEnvironmentConfiguration sdEnvironmentConfiguration);/**

     * 导出隧道环境配置列表
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 隧道环境配置集合
     */

    /**
     * 新增隧道环境配置
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 结果
     */
    int insertSdEnvironmentConfiguration(MultipartFile[] file, SdEnvironmentConfiguration sdEnvironmentConfiguration);

    /**
     * 修改隧道环境配置
     *
     * @param sdEnvironmentConfiguration 隧道环境配置
     * @return 结果
     */
    int updateSdEnvironmentConfiguration(MultipartFile[] file, SdEnvironmentConfiguration sdEnvironmentConfiguration, Long[] removeIds);

    /**
     * 批量删除隧道环境配置
     *
     * @param ids 需要删除的隧道环境配置主键集合
     * @return 结果
     */
    int deleteSdEnvironmentConfigurationByIds(Long[] ids);

    /**
     * 删除隧道环境配置信息
     *
     * @param id 隧道环境配置主键
     * @return 结果
     */
    int deleteSdEnvironmentConfigurationById(Long id);

    /**
     * 导出查询
     * @param sdEnvironmentConfiguration
     * @return
     */
    List<SdEnvironmentConfiguration> selectSdEnvironmentConfigurationList_exp(SdEnvironmentConfiguration sdEnvironmentConfiguration);


}
