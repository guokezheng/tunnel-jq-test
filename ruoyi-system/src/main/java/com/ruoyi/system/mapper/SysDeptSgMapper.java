package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysDeptSg;

import java.util.List;

/**
 * 机构Mapper接口
 * 
 * @author ruoyi
 * @date 2022-12-02
 */
public interface SysDeptSgMapper 
{
    /**
     * 查询机构
     * 
     * @param id 机构主键
     * @return 机构
     */
    public SysDeptSg selectSysDeptSgById(String id);

    /**
     * 查询机构列表
     * 
     * @param sysDeptSg 机构
     * @return 机构集合
     */
    public List<SysDeptSg> selectSysDeptSgList(SysDeptSg sysDeptSg);

    /**
     * 新增机构
     * 
     * @param sysDeptSg 机构
     * @return 结果
     */
    public int insertSysDeptSg(SysDeptSg sysDeptSg);

    /**
     * 修改机构
     * 
     * @param sysDeptSg 机构
     * @return 结果
     */
    public int updateSysDeptSg(SysDeptSg sysDeptSg);

    /**
     * 删除机构
     * 
     * @param id 机构主键
     * @return 结果
     */
    public int deleteSysDeptSgById(String id);

    /**
     * 批量删除机构
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysDeptSgByIds(String[] ids);
}
